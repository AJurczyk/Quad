package com.ajurczyk.software.imudriver.impl;

import com.ajurczyk.hardware.gyroacc.impl.AccGyroData;
import com.ajurczyk.software.imudriver.ICalibrationManager;

/**
 * @author aleksander.jurczyk@gmail.com on 13.06.16.
 */
public class CalibrationManager implements ICalibrationManager {

    private static final int CALI_PROBES = 100;

    @Override
    public void calibrate() {
//        double meanGyroX = 0;
//        double meanGyroY = 0;
//        double meanGyroZ = 0;
//
//        for (int i = 0; i < CALI_PROBES; i++) {
//            final AccGyroData reading = applyMedianFilter(gyroAcc.readAll());
//
//            listeners.forEach(listener -> listener.readingReceived(reading));
//            listeners.forEach(listener -> listener.readingReceived(reading));
//
//            meanGyroX += reading.getGyroX();
//            meanGyroY += reading.getGyroY();
//            meanGyroZ += reading.getGyroZ();
//            Thread.sleep(DT_MS);
//        }
//        meanGyroX /= CALI_PROBES;
//        meanGyroY /= CALI_PROBES;
//        meanGyroZ /= CALI_PROBES;
//
//        compensation = new AccGyroData(0, 0, 0, meanGyroX, meanGyroY, meanGyroZ);
    }
}
