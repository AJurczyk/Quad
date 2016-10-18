package io.api.rest;

/**
 * @author aleksander.jurczyk@seedlabs.io
 */

import com.ajurczyk.hardware.gyroacc.impl.AccGyroData;
import com.ajurczyk.hardware.motor.IMotor;
import com.ajurczyk.hardware.motor.exception.MotorException;
import com.ajurczyk.software.flightcontroller.IFlightController;
import com.ajurczyk.software.flightcontroller.IFlightControllerListener;
import com.ajurczyk.software.flightcontroller.exception.FlightControllerException;
import com.ajurczyk.software.imudriver.IImuDriver;
import com.ajurczyk.software.imudriver.IImuReaderListener;
import com.ajurczyk.software.imudriver.impl.ImuDriverSimulator;
import com.ajurczyk.software.imudriver.impl.PositionAngle;
import io.api.rest.enums.EventType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class Controller implements IImuReaderListener, IFlightControllerListener {

    private static final int MAX_EVENTS_SIZE = 200;
    private final List<QuadEvent> gyroEvents = new ArrayList<>();
    private final List<QuadEvent> flightEvents = new ArrayList<>();

    @Autowired
    private IMotor motor;

    @Autowired
    private IImuDriver imuDriver;

    @Autowired
    private IFlightController flightController;

    public void setImuDriver(IImuDriver imuDriver) {
        this.imuDriver = imuDriver;
    }

    /**
     * Get stored GyroEvents.
     *
     * @return list of gyro events
     */
    @RequestMapping(value = "/getGyroEvents")
    public List<QuadEvent> getGyroEvents() {
        return getEvents(gyroEvents);
    }

    /**
     * Get stored FlightEvents.
     *
     * @return list of flight controller events
     */
    @RequestMapping(value = "/getFlightEvents")
    public List<QuadEvent> getFlightEvents() {
        return getEvents(flightEvents);
    }

    private List<QuadEvent> getEvents(List<QuadEvent> eventsList) {
        final List<QuadEvent> flyEventsCopy = new ArrayList<>();
        flyEventsCopy.addAll(eventsList);
        eventsList.clear();
        return flyEventsCopy;//TODO make it a queue!
    }

    /**
     * Starts imudriver work.
     *
     * @param run on/off
     */
    @RequestMapping(value = "/startStopGyro")
    public void startGyroReading(@RequestParam boolean run) {
        if (run) {
            imuDriver.startWorking();
        } else {
            imuDriver.stopWorking();
        }
    }

    /**
     * Start/stop flight controller.
     *
     * @param run true = start
     */
    @RequestMapping(value = "/startStopFlightCtrl")
    public void startStopFlightController(@RequestParam boolean run) throws FlightControllerException {
        if (run) {
            flightController.start();
        } else {
            flightController.stop();
        }
    }

    @RequestMapping(value = "/setDesiredAngle")
    public void setDesiredAngle(@RequestParam int angle) {
        flightController.setDesiredAngle(angle);
    }

    @RequestMapping(value = "/setCurrentAngle")
    public void setCurrentAngle(@RequestParam int angle) {
        ((ImuDriverSimulator) imuDriver).setPositionAngle(new PositionAngle(angle, 0, 0));
    }

    @RequestMapping(value = "/setProportional")
    public void setP(@RequestParam float value) {
        flightController.getRegulator().setProportional(value);
    }

    @RequestMapping(value = "/setIntegral")
    public void setI(@RequestParam float value) {
        flightController.getRegulator().setIntegral(value);
    }

    @RequestMapping(value = "/setDerivative")
    public void setD(@RequestParam float value) {
        flightController.getRegulator().setDerivative(value);
    }

    @RequestMapping(value = "/setMotorPower")
    public void setMotorPower(@RequestParam float power) throws MotorException {
        motor.setRpm(power);
    }

    @Override
    public void cleanReadingReceived(AccGyroData data) {
        addQuadEvent(gyroEvents, new QuadEvent(EventType.GYRO_CLEAN, data));
    }

    @Override
    public void rawReadingReceived(AccGyroData data) {
        addQuadEvent(gyroEvents, new QuadEvent(EventType.GYRO_RAW, data));
    }

    @Override
    public void angleReceived(PositionAngle angle) {
        addQuadEvent(gyroEvents, new QuadEvent(EventType.GYRO_ANGLE, angle));
    }

    @Override
    public void angleReceived(double angle) {
        addQuadEvent(flightEvents, new QuadEvent(EventType.FLIGHT_CONTROLLER_ANGLE, angle));
    }

    @Override
    public void motorThrustPrcntChanged(float power) {
        addQuadEvent(flightEvents, new QuadEvent(EventType.MOTOR_POWER, power));
    }

    @Override
    public void regulationSignalReceived(double regulation) {
        addQuadEvent(flightEvents, new QuadEvent(EventType.REGULATION, regulation));
    }

    private void addQuadEvent(List<QuadEvent> eventsList, QuadEvent event) {
        if (eventsList.size() >= MAX_EVENTS_SIZE) {
            eventsList.clear();
        }
        eventsList.add(event);
    }
}