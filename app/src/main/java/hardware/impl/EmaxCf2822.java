package hardware.impl;

import hardware.IMotor;
import hardware.IPwmController;
import hardware.exception.PrcValueOutOfRange;
import hardware.exception.PwmValueOutOfRange;
import hardware.exception.WholeNumberException;

/**
 * @author aleksander.jurczyk@gmail.com on 13.01.16.
 */
public class EmaxCf2822 implements IMotor {

    private final static int PWM_MIN_MS = 1;
    private final static int PWM_MAX_MS = 2;
    private final static int PWM_PERIOD_MS = 5;
    int percent;
    IPwmController pwm;

    public EmaxCf2822(IPwmController pwm) throws WholeNumberException, PwmValueOutOfRange {
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

    private float calcPwmPercent(int value) throws PwmValueOutOfRange {
        float pwmValue = PWM_MIN_MS + (float) (PWM_MAX_MS - PWM_MIN_MS) / 100 * value ;
        if (pwmValue < PWM_MIN_MS || pwmValue > PWM_MAX_MS) {
            throw new PwmValueOutOfRange("Calculated pwm value " + pwmValue
                    + " is out of EMAX CF2822 range");
        }
        return pwmValue;
    }

    @Override
    public void stop() throws PwmValueOutOfRange, PrcValueOutOfRange {
        setPercent(0);
    }

    @Override
    public int getPercent() {
        return percent;
    }

    @Override
    public void setPercent(int percent) throws PrcValueOutOfRange {
        System.out.println("[EMAX] Set "+percent+"%");
        if (percent < 0 || percent > 100) {
            throw new PrcValueOutOfRange("Invalid percentage value: " + percent + "%");
        }
        this.percent = percent;
        try {
            pwm.setDuty(calcPwmPercent(percent));
        } catch (PwmValueOutOfRange pwmValueOutOfRange) {
            throw new PrcValueOutOfRange(pwmValueOutOfRange.getMessage());
        }
    }
}
