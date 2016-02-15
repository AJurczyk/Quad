package hardware.gyroacc.exception;

/**
 * @author aleksander.jurczyk@seedlabs.io
 */

public class invalidConversionFactor extends Exception {
    public invalidConversionFactor(String message, Throwable cause) {
        super(message, cause);
    }

    public invalidConversionFactor(String message) {
        super(message);
    }
}
