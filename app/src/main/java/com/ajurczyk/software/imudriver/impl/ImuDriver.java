package com.ajurczyk.software.imudriver.impl;

import com.ajurczyk.hardware.gyroacc.impl.AccGyroData;
import com.ajurczyk.software.imudriver.IImuDriver;
import com.ajurczyk.software.imudriver.IImuFilteredReader;
import com.ajurczyk.software.imudriver.IImuReaderListener;
import com.ajurczyk.software.imudriver.exception.ImuFilteredReaderException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * @author aleksander.jurczyk@seedlabs.io
 */
public class ImuDriver implements IImuDriver, Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImuDriver.class);
    private static final int DT_MS = 20;

    private final List<IImuReaderListener> listeners = new ArrayList<>();

    private Thread runner;

    @Autowired
    private IImuFilteredReader filteredReader;

    private PositionAngle positionAngle;

    public void registerListener(IImuReaderListener listener) {
        listeners.add(listener);
    }

    @Override
    public PositionAngle getAngle() {
        return positionAngle;
    }

    @Override
    public void startWorking() {
        runner = new Thread(this);
        runner.start();
    }

    @Override
    public void stopWorking() {
        try {
            runner.interrupt();
            runner.join();
        } catch (InterruptedException e) {
            LOGGER.debug("Waiting for gyroscope to end was interrupted.", e);
        }
    }

    @Override
    public boolean isWorking() {
        return runner.isAlive();
    }

    @Override
    public void run() {
        long startTime;
        AccGyroData cleanReading = new AccGyroData();

        LOGGER.debug("Reading current position using accelerometer.");
        positionAngle = new PositionAngle();//TODO get init position from acc reading only

        LOGGER.debug("Start reading gyro.");

        try {
            while (true) {
                startTime = System.currentTimeMillis();
                cleanReading = filteredReader.readClean();

                reCalcAngle(cleanReading);

                waitForNextIteration(System.currentTimeMillis() - startTime);
            }

        } catch (ImuFilteredReaderException | InterruptedException e) {
            LOGGER.error(e.toString());
        } finally {
            LOGGER.debug("Reading gyro finished.");
        }
    }

    private void waitForNextIteration(long delay) throws InterruptedException {
        if (delay < DT_MS) {
            Thread.sleep(DT_MS - delay);
        } else {
            LOGGER.warn("Gyro math took longer than dt(" + DT_MS + "ms): " + delay + "ms.");
        }
    }

    private void reCalcAngle(AccGyroData cleanReading) {
        positionAngle.setAngleX(positionAngle.getAngleX() + cleanReading.getGyroX() * (DT_MS / 1000d));
        positionAngle.setAngleY(positionAngle.getAngleY() + cleanReading.getGyroY() * (DT_MS / 1000d));
        positionAngle.setAngleZ(positionAngle.getAngleZ() + cleanReading.getGyroZ() * (DT_MS / 1000d));
    }
}