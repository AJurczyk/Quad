package io.api.rest;

/**
 * @author aleksander.jurczyk@seedlabs.io
 */

import com.ajurczyk.hardware.gyroacc.exception.AccGyroIncorrectAxisException;
import com.ajurczyk.hardware.gyroacc.exception.AccGyroReadValueException;
import com.ajurczyk.hardware.gyroacc.impl.AccGyroData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ajurczyk.software.imudriver.IImuDriver;
import com.ajurczyk.software.imudriver.IImuReadingListener;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@RestController
public class Controller implements IImuReadingListener {

    private final List<AccGyroData> storedReadings = new ArrayList<>();
    @Autowired
    private IImuDriver imuDriver;

    @PostConstruct
    public void init() {
        imuDriver.registerListener(this);
    }

    /**
     * Get reading from storedReadings list.
     *
     * @return list of measurements
     * @throws AccGyroIncorrectAxisException something went wrong
     * @throws AccGyroReadValueException     something went wrong
     * @throws InterruptedException          something went wrong
     */
    @RequestMapping(value = "/getMeasurements")
    public List<AccGyroData> getMeasurements() throws AccGyroIncorrectAxisException, AccGyroReadValueException,
            InterruptedException {
        final List<AccGyroData> newReadings = storedReadings;
        storedReadings.clear();
        return newReadings;
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
    public void readingReceived(AccGyroData data) {
        storedReadings.add(data);
    }
}