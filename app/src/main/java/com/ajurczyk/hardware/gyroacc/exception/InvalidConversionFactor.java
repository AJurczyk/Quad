package com.ajurczyk.hardware.gyroacc.exception;

/**
 * @author aleksander.jurczyk@seedlabs.io
 */

public class InvalidConversionFactor extends Exception {
    public InvalidConversionFactor(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidConversionFactor(String message) {
        super(message);
    }
}
