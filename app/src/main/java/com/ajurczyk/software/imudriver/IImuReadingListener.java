package com.ajurczyk.software.imudriver;

import com.ajurczyk.hardware.gyroacc.impl.AccGyroData;

/**
 * @author aleksander.jurczyk@seedlabs.io
 */
public interface IImuReadingListener {

    /**
     * Receives data from accGyro.
     *
     * @param data received data
     */
    void readingReceived(AccGyroData data);
}
