package com.ajurczyk.hardware.motor.impl;

import com.ajurczyk.hardware.motor.IMotor;
import com.ajurczyk.hardware.pwm.exceptions.PercentValRangeException;
import com.ajurczyk.hardware.pwm.exceptions.PwmValRangeException;

/**
 * @author aleksander.jurczyk@gmail.com on 06.09.16.
 */
public class MotorSimulator implements IMotor {

    private static final float MIN_POWER = 0f;
    private float powerLimit = 100f;
    private float power;
    private float maxThrust;

    @Override
    public float getMaxThrust() {
        return maxThrust;
    }

    public void setMaxThrust(float maxThrust) {
        this.maxThrust = maxThrust;
    }

    @Override
    public void setPowerLimit(float powerLimit) {
        this.powerLimit = powerLimit;
    }

    @Override
    public void stop() throws PwmValRangeException, PercentValRangeException {
        setPower(0);
    }

    @Override
    public float getPower() {
        return power;
    }

    @Override
    public void setPower(float power) throws PwmValRangeException, PercentValRangeException {
        if (power > powerLimit) {
            this.power = powerLimit;
        } else if (power < MIN_POWER) {
            this.power = MIN_POWER;
        } else {
            this.power = power;
        }
    }
}
