package hardware.gyroacc.exception;

/**
 * @author aleksander.jurczyk@gmail.com on 31.01.16.
 */
public class AccGyroIncorrectAxisException extends Exception {
    public AccGyroIncorrectAxisException(String message, Throwable cause) {
        super(message, cause);
    }

    public AccGyroIncorrectAxisException(String message) {
        super(message);
    }
}
