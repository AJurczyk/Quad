package io.api.rest;

/**
 * @author aleksander.jurczyk@seedlabs.io
 */

import com.ajurczyk.hardware.gyroacc.exception.AccGyroIncorrectAxisException;
import com.ajurczyk.hardware.gyroacc.exception.AccGyroReadValueException;
import com.ajurczyk.hardware.gyroacc.impl.AccGyroData;
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
public class Controller implements IImuReaderListener {

    private static final int MAX_EVENTS_SIZE = 200;
    private final List<GyroEvent> gyroEvents = new ArrayList<>();
    @Autowired
    private IImuDriver imuDriver;

    public void setImuDriver(IImuDriver imuDriver) {
        this.imuDriver = imuDriver;
    }

    /**
     * Get stored GyroEvents.
     *
     * @return list of measurements
     * @throws AccGyroIncorrectAxisException something went wrong
     * @throws AccGyroReadValueException     something went wrong
     * @throws InterruptedException          something went wrong
     */
    @RequestMapping(value = "/getGyroEvents")
    public List<GyroEvent> getGyroEvents() throws AccGyroIncorrectAxisException, AccGyroReadValueException,
        InterruptedException {
        final List<GyroEvent> flyEventsCopy = new ArrayList<>();
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
        addGyroEvent(new GyroEvent(EventType.GYRO_CLEAN, data));
    }

    @Override
    public void rawReadingReceived(AccGyroData data) {
        addGyroEvent(new GyroEvent(EventType.GYRO_RAW, data));
    }

    @Override
    public void angleReceived(PositionAngle angle) {
        addGyroEvent(new GyroEvent(EventType.GYRO_ANGLE, angle));
    }

    private void addGyroEvent(GyroEvent event) {
        if (gyroEvents.size() >= MAX_EVENTS_SIZE) {
            gyroEvents.clear();
        }
        gyroEvents.add(event);
    }
}