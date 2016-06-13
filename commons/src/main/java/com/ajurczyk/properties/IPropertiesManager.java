package com.ajurczyk.properties;

import java.io.IOException;
import java.util.Set;

/**
 * @author aleksander.jurczyk@gmail.com on 13.06.16.
 */
public interface IPropertiesManager {

    /**
     * Get property with given name.
     *
     * @param key key name
     * @return found property
     * @throws PropertyNotFoundException when key does not exist
     */
    String getProperty(String key) throws PropertyNotFoundException;

    /**
     * Get all properties names.
     *
     * @return set of all properties names
     */
    Set<String> getPropertiesNames();

    /**
     * Saves property to file.
     *
     * @param key key name
     * @param value property value
     */
    void saveProperty(String key, String value) throws IOException;
}
