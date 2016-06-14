package com.ajurczyk.software.imudriver;

import com.ajurczyk.software.imudriver.exception.CalibrationManagerException;
import com.ajurczyk.software.imudriver.exception.ImuFilteredReaderException;

/**
 * Class used for gyroscope calibration.
 *
 * @author aleksander.jurczyk@gmail.com on 13.06.16.
 */
public interface ICalibrationManager {

    /**
     * Perform calibration.
     */
    void calibrate() throws InterruptedException, ImuFilteredReaderException, CalibrationManagerException;
}
