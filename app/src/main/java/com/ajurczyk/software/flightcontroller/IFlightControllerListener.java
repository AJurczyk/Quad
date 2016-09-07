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
    void motorPowerChanged(int power);
}
