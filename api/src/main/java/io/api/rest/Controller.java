package io.api.rest;

/**
 * @author aleksander.jurczyk@seedlabs.io
 */

import com.ajurczyk.hardware.gyroacc.impl.AccGyroData;
import com.ajurczyk.software.flightcontroller.IFlightControllerListener;
import com.ajurczyk.software.imudriver.IImuDriver;
import com.ajurczyk.software.imudriver.IImuReaderListener;
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
    private IImuDriver imuDriver;

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

    private List<QuadEvent> getEvents(List<QuadEvent> eventsList){
        final List<QuadEvent> flyEventsCopy = new ArrayList<>();
        flyEventsCopy.addAll(gyroEvents);
        gyroEvents.clear();
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
    public void motorPowerChanged(int power) {
        addQuadEvent(flightEvents, new QuadEvent(EventType.MOTOR_POWER, power));
    }

    @Override
    public void regulationSignalReceived(double regulation) {
        addQuadEvent(flightEvents, new QuadEvent(EventType.REGULATION, regulation));
    }

    @Override
    public void angleReceived(double angle) {
        addQuadEvent(flightEvents, new QuadEvent(EventType.FLIGHT_CONTROLLER_ANGLE, angle));
    }

    private void addQuadEvent(List<QuadEvent> eventsList, QuadEvent event) {
        if (eventsList.size() >= MAX_EVENTS_SIZE) {
            eventsList.clear();
        }
        eventsList.add(event);
    }
}