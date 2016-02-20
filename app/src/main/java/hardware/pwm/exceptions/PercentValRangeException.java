package hardware.pwm.exceptions;

/**
 * @author aleksander.jurczyk@gmail.com on 13.01.16.
 */
public class PercentValRangeException extends Exception {
    public PercentValRangeException(String message) {
        super(message);
    }

    public PercentValRangeException(String message, Throwable cause) {
        super(message, cause);
    }
}
