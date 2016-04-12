package software.imudriver;

import hardware.gyroacc.impl.AccGyroData;

/**
 * @author aleksander.jurczyk@seedlabs.io
 */
public interface IImuReadingListener {

    /**
     * Receives data from accGyro.
     *
     * @param data received data
     */
    void ReadingReceived(AccGyroData data);
}
