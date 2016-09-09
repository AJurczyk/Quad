package com.ajurczyk.software.flightcontroller.impl;

import com.ajurczyk.hardware.motor.IMotor;
import com.ajurczyk.hardware.pwm.exceptions.PercentValRangeException;
import com.ajurczyk.hardware.pwm.exceptions.PwmValRangeException;
import com.ajurczyk.software.flightcontroller.IFlightController;
import com.ajurczyk.software.flightcontroller.IFlightControllerListener;
import com.ajurczyk.software.imudriver.IImuDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.TimeUnit;

/**
 * @author aleksander.jurczyk@gmail.com on 07.09.16.
 */
public class FlightController implements IFlightController, Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(FlightController.class);

    private IMotor motor;

    private IImuDriver imuDriver;

    @Autowired
    private IFlightControllerListener listener;

    private Thread runner;

    private int dt = 20;

    private double desiredAngle = 0;

    public void setDesiredAngle(double desiredAngle) {
        this.desiredAngle = desiredAngle;
    }

    public void setMotor(IMotor motor) {
        this.motor = motor;
    }

    public void setImuDriver(IImuDriver imuDriver) {
        this.imuDriver = imuDriver;
    }

    public void setListener(IFlightControllerListener listener) {
        this.listener = listener;
    }

    public void setDt(int dt) {
        this.dt = dt;
    }

    @Override
    public void start() {
        runner = new Thread(this);
        runner.start();
    }

    @Override
    public void stop() {
        imuDriver.stopWorking();
        if (runner == null) {
            return;
        }
        try {
            runner.interrupt();
            runner.join();
        } catch (InterruptedException e) {
            LOGGER.debug("Waiting for FlightController to end was interrupted.", e);
        }
    }

    @Override
    public void run() {
        LOGGER.debug("Start flight controller.");

        imuDriver.startWorking();
        try {
            while (true) {
                mainLoop();
            }
        } catch (InterruptedException e) {
            try {
                motor.stop();
            } catch (PwmValRangeException | PercentValRangeException e1) {
                LOGGER.error(e.toString());
                //TODO unable to stop motor. Cut off the power.
            }
            LOGGER.error(e.toString());
        } finally {
            LOGGER.debug("Reading gyro finished.");
        }
    }

    private void mainLoop() throws InterruptedException {
        final long startTime = System.currentTimeMillis();
        final double currentAngle = imuDriver.getAngle().getAngleX();
        listener.angleReceived(currentAngle);
        final int currentPower = motor.getPower();

        final double regulation = getRegulation(desiredAngle, currentAngle, currentPower);
        listener.regulationSignalReceived(regulation);

        waitForNextIteration(System.currentTimeMillis() - startTime);

        try {
            final int motorPower = currentPower + (int) regulation;
            motor.setPower(currentPower + (int) regulation);
            listener.motorPowerChanged(motorPower);
        } catch (PwmValRangeException | PercentValRangeException e) {
            LOGGER.debug("Unable to set power on motor.");
            //TODO do something
        }
    }

    private double getRegulation(double desiredAngle, double currentAngle, int currentPower) {
        return 0;
    }

    private void waitForNextIteration(long delay) throws InterruptedException {
        if (delay < dt) {
            TimeUnit.MILLISECONDS.sleep(dt - delay);
        } else {
            LOGGER.warn("FlightController math took longer than dt(" + dt + "ms): " + delay + "ms.");
        }
    }
}
