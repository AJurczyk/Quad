package com.ajurczyk.hardware.motor.impl;

import com.ajurczyk.hardware.motor.IMotor;
import com.ajurczyk.hardware.pwm.exceptions.PercentValRangeException;
import com.ajurczyk.hardware.pwm.exceptions.PwmValRangeException;
import com.ajurczyk.software.imudriver.IImuReaderListener;
import com.ajurczyk.software.imudriver.impl.ImuDriverSimulator;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author aleksander.jurczyk@gmail.com on 06.09.16.
 */
public class MotorSimulator implements IMotor {

    private ImuDriverSimulator imuDriverSimulator;

    public void setImuDriverSimulator(ImuDriverSimulator imuDriverSimulator) {
        this.imuDriverSimulator = imuDriverSimulator;
    }

    @Autowired
    private IImuReaderListener listener;
    float power = 0;

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
        imuDriverSimulator.setAngleSpeed(power);
    }
}
