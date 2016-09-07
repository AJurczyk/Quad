package com.ajurczyk.software.imudriver;

import com.ajurczyk.hardware.gyroacc.impl.AccGyroData;
import com.ajurczyk.software.imudriver.impl.PositionAngle;

/**
 * @author aleksander.jurczyk@seedlabs.io
 */
public interface IImuReaderListener {

    /**
     * Received filtered data.
     *
     * @param data filtered reading
     */
    void cleanReadingReceived(AccGyroData data);

    /**
     * Received raw data.
     *
     * @param data raw reading
     */
    void rawReadingReceived(AccGyroData data);

    /**
     * Received info about current angle.
     *
     * @param angle current angle
     */
    void angleReceived(PositionAngle angle);
}
