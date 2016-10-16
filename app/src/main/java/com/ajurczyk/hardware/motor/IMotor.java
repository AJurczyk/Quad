package com.ajurczyk.hardware.motor;

import com.ajurczyk.hardware.motor.exception.MotorException;

/**
 * @author aleksander.jurczyk@gmail.com on 13.01.16.
 */
public interface IMotor {

    /**
     * Stop the motor.
     */
    void stop() throws MotorException;

    /**
     * Set maximum allowed rpm percent.
     *
     * @param rpmPrcnLimit max allowed rpm percent
     */
    void setRpmPrcnLimit(float rpmPrcnLimit);

    /**
     * Get maximum allowed rpm percent.
     * @return max allowed rpm percent.
     */
    float getRpmPrcntLimit();

    /**
     * Set motor rpm to a percent of max rpm.
     *
     * @param percent percent to set
     * @throws MotorException invalid value to set
     */
    void setRpmPrcnt(float percent) throws MotorException;

    /**
     * Get Current rpm percent.
     * @return current rpm percent
     */
    float getRpmPrcnt();

    /**
     * Sets motor thrust to a percent of max thrust.
     *
     * @param thrustPercent percent to set
     * @throws MotorException invalid value to set

     */
    void setThrustPercent(float thrustPercent) throws MotorException;

    /**
     * Get current thrust percent.
     *
     * @return thrust in percent of max thrust
     */
    float getThrustPercent();

    /**
     * Set motor thrust to a value in Newtons.
     *
     * @param thrustInNewtons desired Newton value
     * @throws MotorException invalid value to set
     */
    void setThrustNewtons(float thrustInNewtons) throws MotorException;

    /**
     * Get current thrust in Newtons.
     *
     * @return thrust in Newtons
     */
    float getThrustNewtons();

    /**
     * Set file with thrust characteristic.
     *
     * @param path path to file
     */
    void setThrustFile(String path);

    /**
     * Initialize motor.
     */
    void init() throws MotorException;
}