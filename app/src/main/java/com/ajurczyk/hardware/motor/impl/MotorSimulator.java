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

    private ImuDriverSimulator imuDriverSimu;

    public void setImuDriverSimu(ImuDriverSimulator imuDriverSimu) {
        this.imuDriverSimu = imuDriverSimu;
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
        this.power = power;
        imuDriverSimu.setAngleSpeed(power - 50);
    }
}
