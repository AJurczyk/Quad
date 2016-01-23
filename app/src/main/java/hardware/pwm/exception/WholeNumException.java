package hardware.pwm.exception;

/**
 * @author aleksander.jurczyk@gmail.com on 09.01.16.
 */
public class WholeNumException extends Exception {
    public WholeNumException(String message) {
        super(message);
    }

    public WholeNumException(String message, Throwable cause) {
        super(message, cause);
    }
}
