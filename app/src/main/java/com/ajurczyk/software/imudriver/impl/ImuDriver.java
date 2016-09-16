package com.ajurczyk.software.imudriver.impl;

import com.ajurczyk.hardware.gyroacc.impl.AccGyroData;
import com.ajurczyk.software.imudriver.IImuDriver;
import com.ajurczyk.software.imudriver.IImuFilteredReader;
import com.ajurczyk.software.imudriver.IImuReaderListener;
import com.ajurczyk.software.imudriver.exception.CalibrationManagerException;
import com.ajurczyk.software.imudriver.exception.ImuFilteredReaderException;
import com.ajurczyk.time.IClock;
import com.ajurczyk.time.impl.SystemClock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * @author aleksander.jurczyk@seedlabs.io
 */
public class ImuDriver implements IImuDriver, Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImuDriver.class);
    private static final int DT_MS = 20;

    /**
     * Factor used by complementary filter.
     */
    private static final float ACC_FACTOR = 0.02f;

    @Autowired
    private IImuReaderListener listener;

    private CalibrationManager calibrationMgr;
    private IClock clock = new SystemClock();
    private Thread runner;
    private IImuFilteredReader filteredReader;
    private PositionAngle positionAngle;

    protected static int getDtMs() {
        return DT_MS;
    }

    public void setCalibrationMgr(CalibrationManager calibrationMgr) {
        this.calibrationMgr = calibrationMgr;
    }

    public void setListener(IImuReaderListener listener) {
        this.listener = listener;
    }

    protected void setClock(IClock clock) {
        this.clock = clock;
    }

    public void setFilteredReader(IImuFilteredReader filteredReader) {
        this.filteredReader = filteredReader;
    }

    public void setPositionAngle(PositionAngle positionAngle) {
        this.positionAngle = positionAngle;
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
        if (runner == null) {
            return;
        }
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
        LOGGER.debug("Start reading gyro.");

        getInitPosition();

        try {
            calibrationMgr.calibrate();
            while (true) {
                mainReader();
            }
        } catch (ImuFilteredReaderException | InterruptedException | CalibrationManagerException e) {
            LOGGER.error(e.toString());
        } finally {
            LOGGER.debug("Reading gyro finished.");
        }
    }

    protected void getInitPosition() {
        LOGGER.debug("Reading current position using accelerometer.");
        positionAngle = new PositionAngle();//TODO get init position from acc reading only
    }

    protected void mainReader() throws ImuFilteredReaderException, InterruptedException {
        final long startTime = clock.getMiliseconds();
        final AccGyroData cleanReading = filteredReader.getFilteredReading();
        reCalcAngle(cleanReading);
        waitForNextIteration(clock.getMiliseconds() - startTime);
    }

    private void waitForNextIteration(long delay) throws InterruptedException {
        if (delay < DT_MS) {
            Thread.sleep(DT_MS - delay);
        } else {
            LOGGER.warn("Gyro math took longer than dt(" + DT_MS + "ms): " + delay + "ms.");
        }
    }

    /**
     * Calculates angle with using of complementary filter.
     *
     * @param cleanReading current reading from IMU
     */
    private void reCalcAngle(AccGyroData cleanReading) {
        //TODO: atan2 has its angle limitations which should be checked

        final float accXangle = (float)Math.toDegrees(Math.atan2(cleanReading.getAccY(), cleanReading.getAccZ()));
        final float accYangle = (float)Math.toDegrees(Math.atan2(cleanReading.getAccX(), cleanReading.getAccZ()));

        final float gyroXangle = positionAngle.getAngleX() + cleanReading.getGyroX() * (DT_MS / 1000f);
        final float gyroYangle = positionAngle.getAngleY() + cleanReading.getGyroY() * (DT_MS / 1000f);

        final float gyroFactor = 1f - ACC_FACTOR;

        positionAngle.setAngleX(gyroFactor * gyroXangle + ACC_FACTOR * accXangle);
        positionAngle.setAngleY(gyroFactor * gyroYangle + ACC_FACTOR * accYangle);
        listener.angleReceived(positionAngle);
    }
}
