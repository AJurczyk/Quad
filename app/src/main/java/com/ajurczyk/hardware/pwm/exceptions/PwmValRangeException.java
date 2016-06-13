package com.ajurczyk.hardware.pwm.exceptions;

/**
 * @author aleksander.jurczyk@gmail.com on 30.12.15.
 */
public class PwmValRangeException extends Exception {
    public PwmValRangeException(String message) {
        super(message);
    }

    public PwmValRangeException(String message, Throwable cause) {
        super(message, cause);
    }
}
