package hardware.i2c.exception;

/**
 * @author aleksander.jurczyk@gmail.com on 30.01.16.
 */
public class I2cDeviceNotInitializedException extends Exception {
    public I2cDeviceNotInitializedException(String message, Throwable cause) {
        super(message, cause);
    }

    public I2cDeviceNotInitializedException(String message) {
        super(message);
    }
}
