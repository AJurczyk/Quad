package com.ajurczyk.hardware.motor;

import com.ajurczyk.hardware.motor.exception.MotorException;
import com.ajurczyk.hardware.pwm.exceptions.PercentValRangeException;
import com.ajurczyk.hardware.pwm.exceptions.PwmValRangeException;

/**
 * @author aleksander.jurczyk@gmail.com on 13.01.16.
 */
public interface IMotor {

    /**
     * Stop the motor.
     */
    void stop() throws MotorException;

    /**
     * Set maximum allowed rpm as percent of max rpm.
     *
     * @param rpmPrcnLimit max allowed rpm percent
     */
    void setRpmPrcnLimit(float rpmPrcnLimit);

    /**
     * Returns max motor thrust.
     *
     * @return max motor thrust.
     */
    float getMaxThrust();

    /**
     * Set motor rpm to a percent of max rpm.
     *
     * @param percent percent to set
     */
    void setRpmPrcnt(float percent);

    /**
     * Get current thrust percent.
     *
     * @return thrust in percent of max thrust
     */
    float getThrustPercent();

    /**
     * Sets motor thrust to a percent of max thrust.
     *
     * @param thrustPercent percent to set
     */
    void setThrustPercent(float thrustPercent);

    /**
     * Get current thrust in Newtons.
     *
     * @return thrust in Newtons
     */
    float getThrustNewtons();

    /**
     * Set motor thrust to a value in Newtons.
     *
     * @param thrustInNewtons desired Newton value
     */
    void setThrustNewtons(float thrustInNewtons);
}