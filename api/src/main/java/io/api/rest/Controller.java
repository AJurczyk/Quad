package io.api.rest;

/**
 * @author aleksander.jurczyk@seedlabs.io
 */

import hardware.gyroacc.exception.AccGyroIncorrectAxisException;
import hardware.gyroacc.exception.AccGyroReadValueException;
import hardware.gyroacc.impl.AccGyroData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import software.imudriver.IImuDriver;
import software.imudriver.IImuReadingListener;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


@RestController
public class Controller implements IImuReadingListener {

    @Autowired
    IImuDriver imuDriver;

    private List<AccGyroData> storedReadings = new ArrayList<>();

    @PostConstruct
    public void init() {
        imuDriver.registerListener(this);
    }

    /**
     * Perform readings from gyroscope.
     *
     * @return list of measurements
     * @throws AccGyroIncorrectAxisException something went wrong
     * @throws AccGyroReadValueException     something went wrong
     * @throws InterruptedException          something went wrong
     */
    @RequestMapping(value = "/getMeasurements")
    public List<AccGyroData> getMeasurements() throws AccGyroIncorrectAxisException, AccGyroReadValueException,
        InterruptedException {
        final List<AccGyroData> newReadings = new LinkedList<>();
        while (!storedReadings.isEmpty()) {
            newReadings.add(storedReadings.remove(0));
        }
        return newReadings;
    }

    @RequestMapping(value = "/startStopGyro")
    public void startGyroReading(@RequestParam boolean run) {
        if (run) {
            imuDriver.startReading();
        } else {
            imuDriver.stopReading();
        }
    }

    @Override
    public void ReadingReceived(AccGyroData data) {
        storedReadings.add(data);
    }
}