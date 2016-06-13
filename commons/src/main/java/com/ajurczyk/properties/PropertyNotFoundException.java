package com.ajurczyk.properties;

/**
 * Throw when property is not found in .properties file.
 *
 * @author kasper.rybak@seedlabs.io
 */
public class PropertyNotFoundException extends Exception {

    /**
     * Constructor with message.
     *
     * @param message exception message
     */
    public PropertyNotFoundException(String message) {
        super(message);
    }
}
