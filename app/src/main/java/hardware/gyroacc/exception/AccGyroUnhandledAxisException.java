package hardware.gyroacc.exception;

/**
 * @author aleksander.jurczyk@gmail.com on 31.01.16.
 */
public class AccGyroUnhandledAxisException extends Exception {
    public AccGyroUnhandledAxisException(String message, Throwable cause) {
        super(message, cause);
    }

    public AccGyroUnhandledAxisException(String message) {
        super(message);
    }
}
