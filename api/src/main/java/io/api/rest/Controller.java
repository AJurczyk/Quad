package io.api.rest;

/**
 * @author aleksander.jurczyk@seedlabs.io
 */

import com.ajurczyk.hardware.gyroacc.exception.AccGyroIncorrectAxisException;
import com.ajurczyk.hardware.gyroacc.exception.AccGyroReadValueException;
import com.ajurczyk.hardware.gyroacc.impl.AccGyroData;
import com.ajurczyk.software.imudriver.IImuDriver;
import com.ajurczyk.software.imudriver.IImuReaderListener;
import com.ajurczyk.software.imudriver.exception.CalibrationManagerException;
import com.ajurczyk.software.imudriver.impl.PositionAngle;
import io.api.rest.enums.EventType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class Controller implements IImuReaderListener {

    private final List<FlyEvent> flyEvents = new ArrayList<>();

    @Autowired
    private IImuDriver imuDriver;

    public void setImuDriver(IImuDriver imuDriver) {
        this.imuDriver = imuDriver;
    }

    /**
     * Get reading from storedReadings list.
     *
     * @return list of measurements
     * @throws AccGyroIncorrectAxisException something went wrong
     * @throws AccGyroReadValueException     something went wrong
     * @throws InterruptedException          something went wrong
     */
    @RequestMapping(value = "/getEvents")
    public List<FlyEvent> getEvents() throws AccGyroIncorrectAxisException, AccGyroReadValueException,
            InterruptedException {
        final List<FlyEvent> flyEventsCopy = new ArrayList<>();
        flyEventsCopy.addAll(flyEvents);
        flyEvents.clear();
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
        flyEvents.add(new FlyEvent(EventType.GYRO_CLEAN, data));
    }

    @Override
    public void rawReadingReceived(AccGyroData data) {
        flyEvents.add(new FlyEvent(EventType.GYRO_RAW, data));
    }

    @Override
    public void angleReceived(PositionAngle angle) {
        flyEvents.add(new FlyEvent(EventType.GYRO_ANGLE, angle));
    }
}