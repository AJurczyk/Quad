package com.ajurczyk.software.flightcontroller;

/**
 * @author aleksander.jurczyk@gmail.com on 07.09.16.
 */
public interface IFlightControllerListener {
    /**
     * Received when power of the motor has changed.
     *
     * @param power percent power value.
     */
    void motorPowerChanged(float power);

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