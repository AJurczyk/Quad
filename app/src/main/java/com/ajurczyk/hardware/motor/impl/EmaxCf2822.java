package com.ajurczyk.hardware.motor.impl;

import com.ajurczyk.hardware.motor.IMotor;
import com.ajurczyk.hardware.pwm.IPwmController;
import com.ajurczyk.hardware.pwm.exceptions.PercentValRangeException;
import com.ajurczyk.hardware.pwm.exceptions.PwmValRangeException;
import com.ajurczyk.hardware.pwm.exceptions.WholeNumException;
import org.slf4j.LoggerFactory;

/**
 * @author aleksander.jurczyk@gmail.com on 13.01.16.
 */
public class EmaxCf2822 implements IMotor {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(EmaxCf2822.class);

    private static final int PWM_MIN_MS = 1;
    private static final int PWM_MAX_MS = 2;
    private static final int PWM_PERIOD_MS = 5;
    private final IPwmController pwm;
    private float powerPercent;
    private float powerLimit = 100f;

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

    /**
     * Returns pwm duty cycle in ms for 0% throttle.
     *
     * @return PWM_MIN_MS const
     */
    public static int getPwmMinMs() {
        return PWM_MIN_MS;
    }

    /**
     * Returns pwm duty cycle in ms for 100% throttle.
     *
     * @return PWM_MAX_MS const
     */
    public static int getPwmMaxMs() {
        return PWM_MAX_MS;
    }

    public static int getPwmPeriodMs() {
        return PWM_PERIOD_MS;
    }

    @Override
    public void setPowerLimit(float powerLimit) {
        this.powerLimit = powerLimit;
    }

    private float calcPwmPercent(float percentValue) throws PwmValRangeException {
        final float pwmValue = PWM_MIN_MS + ((float) (PWM_MAX_MS - PWM_MIN_MS)) / 100 * percentValue;
        if (pwmValue < PWM_MIN_MS || pwmValue > PWM_MAX_MS) {
            throw new PwmValRangeException("Calculated pwm value " + pwmValue
                + " is out of EMAX CF2822 range.");
        }
        return pwmValue;
    }

    @Override
    public void stop() throws PwmValRangeException, PercentValRangeException {
        setPower(0);
    }

    @Override
    public float getPower() {
        return powerPercent;
    }

    @Override
    public void setPower(float power) throws PwmValRangeException {
        float powerToSet = power;
        LOGGER.debug("[EMAX] Set " + power + "%");
        if (power > powerLimit) {
            LOGGER.warn("Tried to set not allowed power: " + power + ". PowerLimit=" + powerLimit);
            powerToSet = powerLimit;
        } else if (power < 0) {
            LOGGER.warn("Tried to set not allowed power: " + power + ". Power must be greater than zero.");
            powerToSet = 0;
        }
        pwm.setDuty(calcPwmPercent(powerToSet));
        this.powerPercent = powerToSet;
    }
}
