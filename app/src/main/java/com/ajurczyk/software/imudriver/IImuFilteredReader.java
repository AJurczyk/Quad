package com.ajurczyk.software.imudriver;

import com.ajurczyk.hardware.gyroacc.impl.AccGyroData;
import com.ajurczyk.properties.PropertyNotFoundException;
import com.ajurczyk.software.imudriver.exception.ImuFilteredReaderException;

import java.io.IOException;

/**
 * Provides filtering of a given signal.
 *
 * @author aleksander.jurczyk@gmail.com on 13.06.16.
 */
public interface IImuFilteredReader {

    /**
     * Get raw reading without filtering.
     *
     * @return raw reading
     */
    AccGyroData readRaw() throws ImuFilteredReaderException;

    /**
     * Get filtered reading.
     *
     * @return filtered reading
     */
    AccGyroData readClean() throws ImuFilteredReaderException;

    /**
     * Clear previous readings.
     */
    void clear();

    /**
     * Reload compensation parameters from the compensation file.
     */
    void reloadCompensation() throws IOException, PropertyNotFoundException, ImuFilteredReaderException;

    /**
     * Enable/disable gyro compensation.
     *
     * @param state enable/disable
     */
    void enableGyroCompensation(boolean state);
}
