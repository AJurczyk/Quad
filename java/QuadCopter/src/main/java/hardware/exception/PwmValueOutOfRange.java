package hardware.exception;

/**
 * @author aleksander.jurczyk@gmail.com on 30.12.15.
 */
public class PwmValueOutOfRange extends Exception {
    public PwmValueOutOfRange(String message) {
        super(message);
    }
}
