package com.ajurczyk.hardware.motor.impl;

import com.ajurczyk.hardware.motor.IMotor;
import com.ajurczyk.hardware.pwm.exceptions.PercentValRangeException;
import com.ajurczyk.hardware.pwm.exceptions.PwmValRangeException;
import com.ajurczyk.software.imudriver.impl.ImuDriverSimulator;

/**
 * @author aleksander.jurczyk@gmail.com on 06.09.16.
 */
public class MotorSimulator implements IMotor {

    private float power;

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
        if (power > 100) {
            this.power = 100;
        } else if (power < 0) {
            this.power = 0;
        } else {
            this.power = power;
        }
    }
}
