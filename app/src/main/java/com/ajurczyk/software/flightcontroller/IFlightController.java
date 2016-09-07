package com.ajurczyk.software.flightcontroller;

/**
 * @author aleksander.jurczyk@gmail.com on 07.09.16.
 */
public interface IFlightController {

    void start();

    void stop();

    void setDesiredAngle(double desiredAngle);
}
