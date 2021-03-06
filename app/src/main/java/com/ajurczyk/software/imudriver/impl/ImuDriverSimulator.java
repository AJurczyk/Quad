package com.ajurczyk.software.imudriver.impl;

import com.ajurczyk.hardware.gyroacc.impl.AccGyroData;
import com.ajurczyk.hardware.motor.IMotor;
import com.ajurczyk.hardware.pwm.exceptions.PercentValRangeException;
import com.ajurczyk.hardware.pwm.exceptions.PwmValRangeException;
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
//    private static final float MAX_GYRO_ANGLE = 89f;
        private static final float MAX_GYRO_ANGLE = 60f;
    private static final float MIN_GYRO_ANGLE = -40f;
//    private static final float MIN_GYRO_ANGLE = -89f;

    private static final float RADIUS = 0.5f;
    private static final float GRAVITY_CONST = 9.81f;
    private static float mass;
    private float initialAngle = MIN_GYRO_ANGLE;
    private float initialMotorPower = 0;
    private PositionAngle positionAngle = new PositionAngle();
    private float angleSpeed;
    private Thread runner;
    @Autowired
    private IImuReaderListener listener;
    private IMotor motor;

    public static void setMass(float mass) {
        ImuDriverSimulator.mass = mass;
    }

    public void setInitialAngle(float initialAngle) {
        this.initialAngle = initialAngle;
    }

    public void setInitialMotorPower(float initialMotorPower) {
        this.initialMotorPower = initialMotorPower;
    }

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
        positionAngle = new PositionAngle(0, initialAngle, 0);
        try {
            motor.setPower(initialMotorPower);
        } catch (PwmValRangeException | PercentValRangeException e) {
            e.printStackTrace();
        }
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
        if (positionAngle.getAngleY() >= MAX_GYRO_ANGLE) {
            angleSpeed = 0;
        } else if (positionAngle.getAngleY() <= MIN_GYRO_ANGLE) {
            angleSpeed = 0;
        }
        angleSpeed = angleSpeed + calculateAngularAcceleration() * DT_MS;

        float gyroYangle = positionAngle.getAngleY() + angleSpeed * (DT_MS / 1000f);
        if (gyroYangle > MAX_GYRO_ANGLE) {
            gyroYangle = MAX_GYRO_ANGLE;
        } else if (gyroYangle < MIN_GYRO_ANGLE) {
            gyroYangle = MIN_GYRO_ANGLE;
        }
        positionAngle.setAngleY(gyroYangle);
        listener.rawReadingReceived(new AccGyroData(0, 0, 0, 0, angleSpeed, 0));
        listener.cleanReadingReceived(new AccGyroData(0, 0, 0, 0, angleSpeed, 0));
        listener.angleReceived(positionAngle);
    }

    @SuppressWarnings("PMD.AvoidFinalLocalVariable")
    private float calculateAngularAcceleration() {
        final float alfa = positionAngle.getAngleY();
        final float motorForce = motor.getPower() / 100 * motor.getMaxThrust();

        return (float) ((motorForce / mass - Math.cos(Math.toRadians(alfa)) * GRAVITY_CONST) * RADIUS);
    }
}
