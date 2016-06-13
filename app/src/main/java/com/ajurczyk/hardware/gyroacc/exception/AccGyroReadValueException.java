package com.ajurczyk.hardware.gyroacc.exception;

/**
 * @author aleksander.jurczyk@gmail.com on 30.01.16.
 */
public class AccGyroReadValueException extends Exception{
    public AccGyroReadValueException(String message, Throwable cause) {
        super(message, cause);
    }

    public AccGyroReadValueException(String message) {
        super(message);
    }
}
