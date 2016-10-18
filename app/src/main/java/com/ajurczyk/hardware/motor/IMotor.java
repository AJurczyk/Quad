package com.ajurczyk.hardware.motor;

import com.ajurczyk.hardware.motor.exception.MotorException;

/**
 * @author aleksander.jurczyk@gmail.com on 13.01.16.
 */
public interface IMotor {

    /**
     * Initialize motor.
     */
    void init() throws MotorException;

    /**
     * Stop the motor.
     */
    void stop() throws MotorException;

    /**
     * Set maximum allowed rpm percent.
     *
     * @param rpmPrcnLimit max allowed rpm percent
     */
    void setRpmLimit(float rpmPrcnLimit);

    /**
     * Get maximum allowed rpm percent.
     * @return max allowed rpm percent.
     */
    float getRpmLimit();

    /**
     * Set motor rpm to a percent of max rpm.
     *
     * @param percent percent to set
     *
     * @throws MotorException invalid value to set
     */
    void setRpm(float percent) throws MotorException;

    /**
     * Get Current rpm percent.
     * @return current rpm percent
     */
    float getCurrentRpmm();

    /**
     * Sets motor thrust to a percent of max thrust.
     *
     * @param thrustPrcnt percent to set
     * @throws MotorException invalid value to set

     */
    void setThrust(float thrustPrcnt) throws MotorException;

    /**
     * Get current thrust percent.
     *
     * @return thrust in percent of max thrust
     */
    float getCurrentThrust();

    /**
     * Set file with thrust characteristic.
     *
     * @param path path to file
     */
    void setThrustMapFile(String path);

    /**
     * Get maximum motor thrust.
     *
     * @return max motor thrust based on config file
     */
    float getMaxThrustInNewtons();
}