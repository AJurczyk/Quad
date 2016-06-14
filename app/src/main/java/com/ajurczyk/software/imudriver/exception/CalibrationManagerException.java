package com.ajurczyk.software.imudriver.exception;

/**
 * @author aleksander.jurczyk@gmail.com on 14.06.16.
 */
public class CalibrationManagerException extends Exception {
    public CalibrationManagerException(String message, Throwable cause) {
        super(message, cause);
    }

    public CalibrationManagerException(String message) {
        super(message);
    }
}
