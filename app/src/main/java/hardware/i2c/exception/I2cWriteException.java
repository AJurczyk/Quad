package hardware.i2c.exception;

/**
 * @author aleksander.jurczyk@gmail.com on 30.01.16.
 */
public class I2cWriteException extends Exception {

    public I2cWriteException(String message, Throwable cause) {
        super(message, cause);
    }

    public I2cWriteException(String message) {
        super(message);
    }
}
