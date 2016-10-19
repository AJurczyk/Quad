package com.ajurczyk.software.flightcontroller;

/**
 * @author aleksander.jurczyk@gmail.com on 07.09.16.
 */
public interface IFlightControllerListener {
    /**
     * Received when power of the motor has changed.
     *
     * @param thrustPrcnt percent power value.
     */
    void motorThrustChanged(float thrustPrcnt);

    /**
     * Received regulation signal for motor power.
     *
     * @param regulation regulation value
     */
    void regulationSignalReceived(double regulation);

    /**
     * Received when flight controller reads angle.
     *
     * @param angle angle in degrees
     */
    void angleReceived(double angle);
}