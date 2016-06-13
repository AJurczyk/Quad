package com.ajurczyk.hardware.i2c.exception;

/**
 * @author aleksander.jurczyk@gmail.com on 30.01.16.
 */
public class I2cInitException extends Exception {
    public I2cInitException(String message, Throwable cause) {
        super(message, cause);
    }

    public I2cInitException(String message) {
        super(message);
    }
}
