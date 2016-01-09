package hardware.impl;

import com.pi4j.wiringpi.Gpio;
import hardware.IGpioControl;
import hardware.exception.PwmValueOutOfRange;
import hardware.IPwm;

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

    public HardwarePwm(int pin, int periodMs) throws PwmValueOutOfRange {
        this(new pi4jGpio(),pin,periodMs);
    }

    public HardwarePwm(IGpioControl controller,int pin, int periodMs) throws PwmValueOutOfRange {
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

        int pwmValue = Math.round((range / periodMs) * msValue);
        gpioControl.pwmWrite(pin, pwmValue);
        duty = msValue;
    }

    @Override
    public void setPeriodMs(int periodMs) throws PwmValueOutOfRange {
        this.periodMs = periodMs;
        range = calcRange(periodMs);
        int clock = calcClock(periodMs, range);
        gpioControl.pwmSetClock(clock);
        gpioControl.pwmSetRange(range);
    }

    private void initPwmPin(int pin)
    {
        this.pin = pin;
        gpioControl.pinMode(pin, Gpio.PWM_OUTPUT);
        gpioControl.pwmSetMode(Gpio.PWM_MODE_MS);
    }

    private int calcClock(int periodMs, int range) throws PwmValueOutOfRange {

        int clock = Math.round((((float)periodMs / 1000) * BASE_PI_FREQ) / range);
        if (clock < MIN_CLOCK || clock > MAX_CLOCK) {
            throw new PwmValueOutOfRange("Calculated " + clock + "clock is out of allowed range " +
                    MIN_CLOCK + "-" + MAX_CLOCK);
        }
        return clock;
    }

    private int calcRange(int period) throws PwmValueOutOfRange {
        int range = period * 100;
        if (range < 2 || range > MAX_RANGE) {
            throw new PwmValueOutOfRange("Calculated " + range +
                    " range is out of allowed range 2 - " + MAX_RANGE);
        }
        return range;
    }
}