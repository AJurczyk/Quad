package utils;

import utils.exceptions.InvalidArgException;

/**
 * @author aleksander.jurczyk@seedlabs.io
 */
public final class ByteArrayUtils {

    /**
     * Hidden constructor.
     */
    private ByteArrayUtils() {

    }

    /**
     * Converts 2-byte array into short.
     *
     * @param array array to be converted
     * @return result of conversion
     */
    public static short castToShort(byte[] array) throws InvalidArgException {
        if (array.length > 2) {
            throw new InvalidArgException("Invalid array size. Cannot be longer than 2 bytes");
        }
        return (short) ((array[0] << 8) | (array[1] & 0xFF));
    }
}
