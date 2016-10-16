package com.ajurczyk.hardware.motor.impl;

import com.ajurczyk.hardware.motor.IMotor;

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
    public void setRpmPrcnLimit(float rpmPrcnLimit) {
        this.powerLimit = rpmPrcnLimit;
    }

    @Override
    public void stop() {
        setPower(0);
    }

    @Override
    public float getPower() {
        return power;
    }

    @Override
    public void setPower(float power) {
        if (power > powerLimit) {
            this.power = powerLimit;
        } else if (power < MIN_POWER) {
            this.power = MIN_POWER;
        } else {
            this.power = power;
        }
    }
}
