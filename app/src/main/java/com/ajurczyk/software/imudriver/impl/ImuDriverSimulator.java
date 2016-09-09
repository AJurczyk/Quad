package com.ajurczyk.software.imudriver.impl;

import com.ajurczyk.hardware.gyroacc.impl.AccGyroData;
import com.ajurczyk.software.imudriver.IImuDriver;
import com.ajurczyk.software.imudriver.IImuReaderListener;
import com.ajurczyk.software.imudriver.exception.ImuFilteredReaderException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.TimeUnit;

/**
 * @author aleksander.jurczyk@gmail.com on 06.09.16.
 */
public class ImuDriverSimulator implements IImuDriver, Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(ImuDriverSimulator.class);
    private static final int DT_MS = 20;
    PositionAngle positionAngle = new PositionAngle(0, 0, 0);
    double angleSpeed = 0;
    private Thread runner;

    @Autowired
    private IImuReaderListener listener;

    public void setListener(IImuReaderListener listener) {
        this.listener = listener;
    }

    public PositionAngle getPositionAngle() {
        return positionAngle;
    }

    public void setAngleSpeed(double angleSpeed) {
        this.angleSpeed = angleSpeed;
    }

    @Override
    public PositionAngle getAngle() {
        return positionAngle;
    }

    @Override
    public void startWorking() {
        positionAngle = new PositionAngle(0, 0, 0);
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
        return false;
    }

    @Override
    public void run() {
        LOGGER.debug("Start reading gyro.");

        try {
            while (true) {
                mainReader();
            }
        } catch (ImuFilteredReaderException | InterruptedException e) {
            LOGGER.error(e.toString());
        } finally {
            LOGGER.debug("Reading gyro finished.");
        }
    }

    private void mainReader() throws ImuFilteredReaderException, InterruptedException {
        reCalcAngle();
        TimeUnit.MILLISECONDS.sleep(DT_MS);
    }

    /**
     * Calculates angle.
     */
    private void reCalcAngle() {
        double gyroXangle = positionAngle.getAngleX() + angleSpeed * (DT_MS / 1000d);
        if (gyroXangle > 180) {
            gyroXangle -= 360;
        } else if (gyroXangle < -180) {
            gyroXangle += 360;
        }
        positionAngle.setAngleX(gyroXangle);
        listener.rawReadingReceived(new AccGyroData(0, 0, 0, angleSpeed, 0, 0));
        listener.cleanReadingReceived(new AccGyroData(0, 0, 0, angleSpeed, 0, 0));
        listener.angleReceived(positionAngle);
    }

    public void setPositionAngle(PositionAngle positionAngle) {
        this.positionAngle = positionAngle;
    }
}
