package com.ajurczyk.software.flightcontroller;

/**
 * PID regulator.
 *
 * @author aleksander.jurczyk@gmail.com on 09.09.16.
 */
public interface IPid {

    void setP(float value);

    void setI(float value);

    void setD(float value);

    double getRegulation(double desiredValue, double CurrentValue);
}
