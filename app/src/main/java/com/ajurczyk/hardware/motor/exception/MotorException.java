package com.ajurczyk.hardware.motor.exception;

/**
 * Exception thrown by motor.
 *
 * @author aleksander.jurczyk@gmail.com on 16.10.16.
 */
public class MotorException extends Exception {
    public MotorException(String message, Throwable cause) {
        super(message, cause);
    }

    public MotorException(String message) {
        super(message);
    }
}
