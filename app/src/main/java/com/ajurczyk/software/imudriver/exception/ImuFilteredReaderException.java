package com.ajurczyk.software.imudriver.exception;

/**
 * @author aleksander.jurczyk@gmail.com on 13.06.16.
 */
public class ImuFilteredReaderException extends Exception {
    public ImuFilteredReaderException(String message) {
        super(message);
    }

    public ImuFilteredReaderException(String message, Throwable cause) {
        super(message, cause);
    }
}
