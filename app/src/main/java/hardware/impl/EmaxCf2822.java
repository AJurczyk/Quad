package hardware.impl;

import hardware.IMotor;
import hardware.IPwmController;
import hardware.exception.PercentValRangeException;
import hardware.exception.PwmValRangeException;
import hardware.exception.WholeNumException;

/**
 * @author aleksander.jurczyk@gmail.com on 13.01.16.
 */
public class EmaxCf2822 implements IMotor {

    private static final int PWM_MIN_MS = 1;
    private static final int PWM_MAX_MS = 2;
    private static final int PWM_PERIOD_MS = 5;
    private final IPwmController pwm;
    private int percent;

    /**
     * Constructor that inits pwm parameters.
     *
     * @param pwm controller to control pwm pin (eg.Pi4JGpio)
     * @throws WholeNumException    thrown if period caused error in pwm calculations
     * @throws PwmValRangeException thrown if period caused error in pwm calculations
     */
    public EmaxCf2822(IPwmController pwm) throws WholeNumException, PwmValRangeException {
        this.pwm = pwm;
        pwm.setPeriodMs(PWM_PERIOD_MS);
        pwm.setDuty(PWM_MAX_MS);
    }

    public static int getPwmMinMs() {
        return PWM_MIN_MS;
    }

    public static int getPwmMaxMs() {
        return PWM_MAX_MS;
    }

    public static int getPwmPeriodMs() {
        return PWM_PERIOD_MS;
    }

    private float calcPwmPercent(int value) throws PwmValRangeException {
        final float pwmValue = PWM_MIN_MS + (float) (PWM_MAX_MS - PWM_MIN_MS) / 100 * value;
        if (pwmValue < PWM_MIN_MS || pwmValue > PWM_MAX_MS) {
            throw new PwmValRangeException("Calculated pwm value " + pwmValue
                    + " is out of EMAX CF2822 range");
        }
        return pwmValue;
    }

    @Override
    public void stop() throws PwmValRangeException, PercentValRangeException {
        setPercent(0);
    }

    @Override
    public int getPercent() {
        return percent;
    }

    @Override
    public void setPercent(int percent) throws PercentValRangeException, PwmValRangeException {
        System.out.println("[EMAX] Set " + percent + "%");
        if (percent < 0 || percent > 100) {
            throw new PercentValRangeException("Invalid percentage value: " + percent + "%");
        }
        this.percent = percent;
        pwm.setDuty(calcPwmPercent(percent));
    }
}
