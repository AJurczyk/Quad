package hardware.pwm.impl;

import com.pi4j.wiringpi.Gpio;
import hardware.exception.PwmValRangeException;
import hardware.exception.WholeNumException;
import hardware.pwm.IGpioController;
import hardware.pwm.IPwmController;
import org.slf4j.LoggerFactory;

/**
 * @author aleksander.jurczyk@gmail.com on 26.12.15.
 */
public class RPi2HardwarePwm implements IPwmController {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(RPi2HardwarePwm.class);


    private static final int BASE_PI_FREQ = 19200000;//The Raspberry Pi PWM clock base frequency of 19.2 MHz.
    // private static final int MAX_RANGE = 4096;
    private static final int MIN_CLOCK = 2;
    private static final int MAX_CLOCK = 4095;
    private static final int MAX_PERIOD = 877;

    private int pin;
    private float duty;
    private int periodMs;
    private IGpioController gpioControl;
    private int pwmValueFor001;

    /**
     * Create and init new Raspberry Hardware Pwm pin using Pi4JGpio.
     *
     * @param pin      gpio pin, don't remember how counted. But 1 is for sure hardware pwm
     * @param periodMs period cycle in miliseconds
     * @throws PwmValRangeException some pwm value out of allowed range (due to period)
     * @throws WholeNumException    some pwm value is not an integer (due to period)
     */
    public RPi2HardwarePwm(int pin, int periodMs) throws PwmValRangeException, WholeNumException {
        this(new Pi4jGpio(), pin, periodMs);
    }

    /**
     * Constructor for unit tests. Create and init new Raspberry Hardware Pwm pin.
     *
     * @param controller Gpio controller to control hardware PWM
     * @param pin        gpio pin, don't remember how counted. But 1 is for sure hardware pwm
     * @param periodMs   period cycle in miliseconds
     * @throws PwmValRangeException some pwm value out of allowed range (due to period)
     * @throws WholeNumException    some pwm value is not an integer (due to period)
     */
    public RPi2HardwarePwm(IGpioController controller, int pin, int periodMs) throws PwmValRangeException,
            WholeNumException {
        this.gpioControl = controller;
        if (periodMs > MAX_PERIOD) {
            throw new PwmValRangeException("Period " + periodMs + "is higher than allowed " + MAX_PERIOD);
        }
        initPwmPin(pin);
        setPeriodMs(periodMs);
    }

    @Override
    public float getFrequency() {
        return (1 / periodMs) * 1000;
    }

    @Override
    public int getPeriodMs() {
        return periodMs;
    }

    @Override
    public void setPeriodMs(int periodMs) throws PwmValRangeException, WholeNumException {
        this.periodMs = periodMs;
        final int range = calcRange(periodMs);
        final int clock = calcClock(periodMs, range);
        pwmValueFor001 = calcPwmValueFor001((float) range, (float) periodMs);

        LOGGER.debug("[PWM " + pin + "] Period: " + periodMs + "[ms]" + ", clock: " + clock + ", range: " + range);
        gpioControl.pwmWrite(pin, 0);
        gpioControl.pwmSetClock(clock);
        gpioControl.pwmSetRange(range);
    }

    @Override
    public float getDuty() {
        return duty;
    }

    @Override
    public void setDuty(float msValue) throws PwmValRangeException {
        LOGGER.debug("[PWM " + pin + "] set duty: " + msValue + "[ms]");
        if (msValue < 0 || msValue > periodMs) {
            throw new PwmValRangeException("pwm value " + msValue
                    + "ms must be lower than period " + periodMs
                    + "ms and higher than 0");
        }
        final int pwmValue = (int) ((msValue * 100) * pwmValueFor001);
        LOGGER.debug("[PWM " + pin + "] write: " + pwmValue);
        gpioControl.pwmWrite(pin, pwmValue);
        duty = msValue;
    }

    private void initPwmPin(int pin) {
        this.pin = pin;
        gpioControl.pinMode(pin, Gpio.PWM_OUTPUT);
        gpioControl.pwmSetMode(Gpio.PWM_MODE_MS);
    }

    private int calcClock(int periodMs, int range) throws PwmValRangeException, WholeNumException {

        final float clock = ((float) periodMs / 1000) * BASE_PI_FREQ / range;
        if (clock < MIN_CLOCK || clock > MAX_CLOCK) {
            throw new PwmValRangeException("Calculated " + clock + "clock is out of allowed range "
                    + MIN_CLOCK + "-" + MAX_CLOCK);
        }
        if (!isWholeNumber(clock)) {
            throw new WholeNumException("clock is not whole number: c=" + clock);
        }
        return (int) clock;
    }

    //todo move it to commons
    private Boolean isWholeNumber(float value) {
        return value % 1 == 0;

    }

    private int calcPwmValueFor001(float range, float periodMs) throws WholeNumException {
        final float setna = (float) range / (periodMs * 100);
        if (!isWholeNumber(setna)) {
            throw new WholeNumException("pwm value for 0.01ms is not a whole number.");
        }
        return (int) setna;
    }

    @SuppressWarnings("PMD.UnusedFormalParameter")
    private int calcRange(int period) throws WholeNumException {
        return 4000;
        //int range = period * 100;
        //if (range < 2 || range > MAX_RANGE) {
        //throw new PwmValRangeException("Calculated " + range +
        //" range is out of allowed range 2 - " + MAX_RANGE);
        //}
        //return range;
        //todo to implement. c must be a whole number, and r/100T must bee too
    }
}