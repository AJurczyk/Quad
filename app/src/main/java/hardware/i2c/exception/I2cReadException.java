package hardware.i2c.exception;

/**
 * @author aleksander.jurczyk@gmail.com on 30.01.16.
 */
public class I2cReadException extends Exception {
    public I2cReadException(String message, Throwable cause) {
        super(message, cause);
    }

    public I2cReadException(String message) {
        super(message);
    }
}
