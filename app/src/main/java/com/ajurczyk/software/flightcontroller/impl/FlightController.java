package com.ajurczyk.software.flightcontroller.impl;

import com.ajurczyk.hardware.motor.IMotor;
import com.ajurczyk.hardware.motor.exception.MotorException;
import com.ajurczyk.software.flightcontroller.IFlightController;
import com.ajurczyk.software.flightcontroller.IFlightControllerListener;
import com.ajurczyk.software.flightcontroller.exception.FlightControllerException;
import com.ajurczyk.software.imudriver.IImuDriver;
import com.ajurczyk.software.regulators.IRegulator;
import com.ajurczyk.software.regulators.impl.RegulatorPid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.TimeUnit;

/**
 * @author aleksander.jurczyk@gmail.com on 07.09.16.
 */
public class FlightController implements IFlightController, Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(FlightController.class);

    private static final float GRAVITY_CONST = 9.81f;
    private IMotor motor;

    private IImuDriver imuDriver;

    @Autowired
    private IFlightControllerListener listener;

    private IRegulator regulator;
    private Thread runner;
    private int interval = 20;
    private float desiredAngle;
    private float mass;
    private float maxThrust;

    public void setMass(float mass) {
        this.mass = mass;
    }

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
    public void start() throws FlightControllerException {
        LOGGER.debug("Start flight controller.");
        ((RegulatorPid) regulator).clear();
        if (maxThrust == 0) {
            LOGGER.error("Error starting flight controller. motor doesn't have its max thrust set.");
            throw new FlightControllerException("Motor doesn't have its max thrust set.");
        }
        try {
            TimeUnit.MILLISECONDS.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        imuDriver.startWorking();
        listener.motorThrustPrcntChanged(motor.getCurrentThrustPercent());
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
        try {
            while (true) {
                mainLoop();
            }
        } catch (InterruptedException e) {
            try {
                motor.stop();
            } catch (MotorException e1) {
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
        final float currentAngle = imuDriver.getAngle().getAngleY();
        listener.angleReceived(currentAngle);

        final float regulation = regulator.getRegulation(currentAngle, desiredAngle);
        listener.regulationSignalReceived(regulation);

        waitForNextIteration(System.currentTimeMillis() - startTime);

        try {
            final float thrustToSet = (float) (regulation + mass * GRAVITY_CONST * Math.cos(Math.toRadians(currentAngle)));
            motor.setThrustNewtons(thrustToSet);
            listener.motorThrustPrcntChanged(motor.getCurrentThrustPercent());
        } catch (MotorException e) {
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
