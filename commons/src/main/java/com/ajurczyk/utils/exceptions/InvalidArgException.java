package com.ajurczyk.utils.exceptions;

/**
 * Thrown when method receives invalid arguments.
 *
 * @author aleksander.jurczyk@seedlabs.io
 */
public class InvalidArgException extends Exception {

    public InvalidArgException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidArgException(String message) {
        super(message);
    }
}
