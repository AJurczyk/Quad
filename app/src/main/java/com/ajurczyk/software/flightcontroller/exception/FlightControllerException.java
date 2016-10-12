package com.ajurczyk.software.flightcontroller.exception;

/**
 * @author aleksander.jurczyk@gmail.com on 02.10.16.
 */
public class FlightControllerException extends Exception {
    public FlightControllerException(String message, Throwable cause) {
        super(message, cause);
    }

    public FlightControllerException(String message) {
        super(message);
    }
}
