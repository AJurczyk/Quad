package com.ajurczyk.software.imudriver;

import com.ajurczyk.hardware.gyroacc.impl.AccGyroData;
import com.ajurczyk.software.imudriver.exception.ImuFilteredReaderException;

/**
 * Provides filtering of a given signal.
 *
 * @author aleksander.jurczyk@gmail.com on 13.06.16.
 */
public interface IImuFilteredReader {


    /**
     * Get filtered reading.
     *
     * @return filtered reading
     */
    AccGyroData getFilteredReading() throws ImuFilteredReaderException;

    /**
     * Clear previous readings.
     */
    void clear();

    /**
     * Reload compensation parameters from the compensation file.
     */
    void reloadCompensation() throws ImuFilteredReaderException;

    /**
     * Enable/disable gyro compensation.
     *
     * @param state enable/disable
     */
    void enableGyroCompensation(boolean state);
}
