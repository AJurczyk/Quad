package com.ajurczyk.software.flightcontroller;

import com.ajurczyk.software.flightcontroller.exception.FlightControllerException;
import com.ajurczyk.software.regulators.IRegulator;

/**
 * @author aleksander.jurczyk@gmail.com on 07.09.16.
 */
public interface IFlightController {

    void start() throws FlightControllerException;

    void stop();

    void setDesiredAngle(float desiredAngle);

    IRegulator getRegulator();
}
