package com.ajurczyk.hardware.motor;

import com.ajurczyk.hardware.pwm.exceptions.PercentValRangeException;
import com.ajurczyk.hardware.pwm.exceptions.PwmValRangeException;

/**
 * @author aleksander.jurczyk@gmail.com on 13.01.16.
 */
public interface IMotor {

    void stop() throws PwmValRangeException, PercentValRangeException;

    void setPower(float power) throws PwmValRangeException, PercentValRangeException;

    float getPower();
}
