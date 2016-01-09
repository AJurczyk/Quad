package hardware.impl;

import com.pi4j.wiringpi.Gpio;
import com.sun.org.apache.xpath.internal.operations.Bool;
import hardware.IGpioControl;
import hardware.exception.PwmValueOutOfRange;
import hardware.IPwm;
import hardware.exception.WholeNumberException;

/**
 * @author aleksander.jurczyk@gmail.com on 26.12.15.
 */
public class HardwarePwm extends AbstractPwm implements IPwm {

    private static final int BASE_PI_FREQ = 19200000;//The Raspberry Pi PWM clock base frequency of 19.2 MHz.
    private static final int MAX_RANGE = 4096;
    private static final int MIN_CLOCK = 2;
    private static final int MAX_CLOCK = 4095;
    private static final int MAX_PERIOD = 877;

    private int pin;
    private int range;
    private IGpioControl gpioControl;
    private int pwmValueFor001;

    public HardwarePwm(int pin, int periodMs) throws PwmValueOutOfRange, WholeNumberException {
        this(new pi4jGpio(), pin, periodMs);
    }

    public HardwarePwm(IGpioControl controller, int pin, int periodMs) throws PwmValueOutOfRange,
            WholeNumberException {
        this.gpioControl = controller;
        if (periodMs > MAX_PERIOD) {
            throw new PwmValueOutOfRange("Period " + periodMs + "is higher than allowed " + MAX_PERIOD);
        }
        initPwmPin(pin);
        setPeriodMs(periodMs);
    }

    @Override
    public void setDuty(float msValue) throws PwmValueOutOfRange {
        if (msValue < 0 || msValue > periodMs) {
            throw new PwmValueOutOfRange("pwm value " + msValue +
                    "ms must be lower than period " + periodMs +
                    "ms and higher than 0");
        }
        int pwmValue = (int) ((msValue * 100) * pwmValueFor001);
        gpioControl.pwmWrite(pin, pwmValue);
        duty = msValue;
    }

    @Override
    public void setPeriodMs(int periodMs) throws PwmValueOutOfRange, WholeNumberException {
        this.periodMs = periodMs;
        range = calcRange(periodMs);
        int clock = calcClock(periodMs, range);
        pwmValueFor001 = calcPwmValueFor001((float) range, (float) periodMs);

        gpioControl.pwmSetClock(clock);
        gpioControl.pwmSetRange(range);
    }

    private void initPwmPin(int pin) {
        this.pin = pin;
        gpioControl.pinMode(pin, Gpio.PWM_OUTPUT);
        gpioControl.pwmSetMode(Gpio.PWM_MODE_MS);
    }

    private int calcClock(int periodMs, int range) throws PwmValueOutOfRange, WholeNumberException {

        float clock = ((float) periodMs / 1000) * BASE_PI_FREQ / range;
        if (clock < MIN_CLOCK || clock > MAX_CLOCK) {
            throw new PwmValueOutOfRange("Calculated " + clock + "clock is out of allowed range " +
                    MIN_CLOCK + "-" + MAX_CLOCK);
        }
        if (!isWholeNumber(clock)) {
            throw new WholeNumberException("clock is not whole number: c=" + clock);
        }
        return (int) clock;
    }

    Boolean isWholeNumber(float value) {
        return (value % 1 == 0);

    }

    private int calcPwmValueFor001(float range, float periodMs) throws WholeNumberException {
        float setna = (float) range / (periodMs * 100);
        if (!isWholeNumber(setna)) {
            throw new WholeNumberException("pwm value for 0.01ms is not a whole number.");
        }
        return (int) setna;
    }

    private int calcRange(int period) throws WholeNumberException {
        return 4000;
//        int range = period * 100;
//        if (range < 2 || range > MAX_RANGE) {
//            throw new PwmValueOutOfRange("Calculated " + range +
//                    " range is out of allowed range 2 - " + MAX_RANGE);
//        }
//        return range;
        //todo to implement. c must be a whole number, and r/100T must bee too
    }
}