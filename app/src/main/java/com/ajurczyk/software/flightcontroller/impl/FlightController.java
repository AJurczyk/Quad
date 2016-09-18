package com.ajurczyk.software.flightcontroller.impl;

import com.ajurczyk.hardware.motor.IMotor;
import com.ajurczyk.hardware.pwm.exceptions.PercentValRangeException;
import com.ajurczyk.hardware.pwm.exceptions.PwmValRangeException;
import com.ajurczyk.software.flightcontroller.IFlightController;
import com.ajurczyk.software.flightcontroller.IFlightControllerListener;
import com.ajurczyk.software.imudriver.IImuDriver;
import com.ajurczyk.software.regulators.IRegulator;
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

    private IRegulator regulator;
    private Thread runner;
    private int interval = 20;
    private float desiredAngle;

    public IRegulator getRegulator() {
        return regulator;
    }

    public void setRegulator(IRegulator regulator) {
        this.regulator = regulator;
    }

    public void setDesiredAngle(float desiredAngle) {
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

    public void setInterval(int interval) {
        this.interval = interval;
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
        final float currentAngle = imuDriver.getAngle().getAngleX();
        listener.angleReceived(currentAngle);

        final float regulation = regulator.getRegulation(currentAngle, desiredAngle);
        listener.regulationSignalReceived(regulation);

        waitForNextIteration(System.currentTimeMillis() - startTime);

        try {
            float powerToSet = 50 + regulation;

            motor.setPower(powerToSet);
            listener.motorPowerChanged(powerToSet);
        } catch (PwmValRangeException | PercentValRangeException e) {
            LOGGER.debug("Unable to set power on motor.");
            //TODO do something
        }
    }

    private void waitForNextIteration(long delay) throws InterruptedException {
        if (delay < interval) {
            TimeUnit.MILLISECONDS.sleep(interval - delay);
        } else {
            LOGGER.warn("FlightController math took longer than interval(" + interval + "ms): " + delay + "ms.");
        }
    }
}
