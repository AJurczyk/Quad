package com.ajurczyk.software.imudriver.impl;

import com.ajurczyk.hardware.gyroacc.impl.AccGyroData;
import com.ajurczyk.hardware.motor.IMotor;
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
    private static final float MAX_GYRO_ANGLE = 60f;
    private static final float MIN_GYRO_ANGLE = -40f;
    private PositionAngle positionAngle = new PositionAngle(0, 0, 0);
    private float angleSpeed;

    private Thread runner;
    @Autowired
    private IImuReaderListener listener;

    private IMotor motor;

    public void setMotor(IMotor motor) {
        this.motor = motor;
    }

    public void setListener(IImuReaderListener listener) {
        this.listener = listener;
    }

    public PositionAngle getPositionAngle() {
        return positionAngle;
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
        positionAngle = new PositionAngle(0, 0, 0);
        angleSpeed = 0;
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
        if (positionAngle.getAngleX() >= MAX_GYRO_ANGLE) {
            angleSpeed = 0;
        } else if (positionAngle.getAngleX() <= MIN_GYRO_ANGLE) {
            angleSpeed = 0;
        }
        angleSpeed = angleSpeed + calculateAngularAcceleration() * DT_MS;

        float gyroXangle = positionAngle.getAngleX() + angleSpeed * (DT_MS / 1000f);
        if (gyroXangle > MAX_GYRO_ANGLE) {
            gyroXangle = MAX_GYRO_ANGLE;
        } else if (gyroXangle < MIN_GYRO_ANGLE) {
            gyroXangle = MIN_GYRO_ANGLE;
        }
        positionAngle.setAngleX(gyroXangle);
        listener.rawReadingReceived(new AccGyroData(0, 0, 0, angleSpeed, 0, 0));
        listener.cleanReadingReceived(new AccGyroData(0, 0, 0, angleSpeed, 0, 0));
        listener.angleReceived(positionAngle);
    }

    private float calculateAngularAcceleration() {
        final float radius = 0.5f;
        final float maxFs = 2f;
        final float mass = 0.1f;
        final float gFactor = 10f;
        final float alfa = positionAngle.getAngleX();
        final float Fs = motor.getPower() * maxFs / 100;

        float toReturn = (float) ((Fs / mass - Math.cos(alfa * 0.01745) * gFactor) * radius);
        return toReturn;
    }
}
