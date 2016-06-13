package com.ajurczyk.software.imudriver.impl;

import com.ajurczyk.hardware.gyroacc.IGyroAcc;
import com.ajurczyk.hardware.gyroacc.exception.AccGyroIncorrectAxisException;
import com.ajurczyk.hardware.gyroacc.exception.AccGyroReadValueException;
import com.ajurczyk.hardware.gyroacc.impl.AccGyroData;
import com.ajurczyk.properties.PropertyNotFoundException;
import com.ajurczyk.properties.impl.PropertiesManager;
import com.ajurczyk.software.imudriver.IImuFilteredReader;
import com.ajurczyk.software.imudriver.exception.ImuFilteredReaderException;
import com.ajurczyk.utils.MathUtils;
import com.ajurczyk.utils.RotatingList;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author aleksander.jurczyk@gmail.com on 13.06.16.
 */
public class ImuFilteredReader implements IImuFilteredReader {
    //TODO implement listeners, Logger
    private static final int MEDIAN_SIZE = 3;

    private final RotatingList<AccGyroData> previousReadings = new RotatingList<>(MEDIAN_SIZE - 1);

    private String compensationFile = "";

    private AccGyroData compensation;//TODO load from file

    protected AccGyroData getCompensation() {
        return compensation;
    }

    @Autowired
    private IGyroAcc gyroAcc;

    public ImuFilteredReader() throws IOException, PropertyNotFoundException {
        if(!compensationFile.isEmpty()){
            reloadCompensation();
        }
    }

    @Override
    public void clear() {
        previousReadings.clear();
    }

    @Override
    public AccGyroData readRaw() throws ImuFilteredReaderException {
        try {
            final AccGyroData reading = gyroAcc.readAll();
            previousReadings.add(reading);
            return reading;
        } catch (AccGyroIncorrectAxisException | AccGyroReadValueException e) {
            throw new ImuFilteredReaderException(e.getMessage(), e);
        }
    }

    @Override
    public AccGyroData readClean() throws ImuFilteredReaderException {
        final AccGyroData cleanReading;
        try {
            cleanReading = compensateGyro(getMedian(gyroAcc.readAll()));
            return cleanReading;
        } catch (AccGyroIncorrectAxisException | AccGyroReadValueException e) {
            throw new ImuFilteredReaderException(e.getMessage(), e);
        }
    }

    protected void setCompensationFile(String compensationFile) {
        this.compensationFile = compensationFile;
    }

    private AccGyroData getMedian(AccGyroData dataToFilter) {
        if (previousReadings.size() < MEDIAN_SIZE - 1) { //TODO what to do now? exception? null? raw?
            return dataToFilter;
        }

        final List<AccGyroData> dataForMedian = new ArrayList<>();
        for (int i = 1; i < MEDIAN_SIZE; i++) {
            dataForMedian.add(previousReadings.get(previousReadings.size() - i));
        }
        dataForMedian.add(dataToFilter);

        return new AccGyroData(
                MathUtils.median(dataForMedian.stream().map(AccGyroData::getAccX).collect(Collectors.toList())),
                MathUtils.median(dataForMedian.stream().map(AccGyroData::getAccY).collect(Collectors.toList())),
                MathUtils.median(dataForMedian.stream().map(AccGyroData::getAccZ).collect(Collectors.toList())),
                MathUtils.median(dataForMedian.stream().map(AccGyroData::getGyroX).collect(Collectors.toList())),
                MathUtils.median(dataForMedian.stream().map(AccGyroData::getGyroY).collect(Collectors.toList())),
                MathUtils.median(dataForMedian.stream().map(AccGyroData::getGyroZ).collect(Collectors.toList()))
        );
    }

    private AccGyroData compensateGyro(AccGyroData dataToFilter) {
        return new AccGyroData(
                dataToFilter.getAccX(),
                dataToFilter.getAccY(),
                dataToFilter.getAccZ(),
                dataToFilter.getGyroX() - compensation.getGyroX(),
                dataToFilter.getGyroY() - compensation.getGyroY(),
                dataToFilter.getGyroZ() - compensation.getGyroZ()
        );
    }

    @Override
    public void reloadCompensation() throws IOException, PropertyNotFoundException {
        PropertiesManager properties = new PropertiesManager(compensationFile);
        compensation = new AccGyroData(0, 0, 0,
                Double.valueOf(properties.getProperty("gyroX")),
                Double.valueOf(properties.getProperty("gyroY")),
                Double.valueOf(properties.getProperty("gyroZ")));
    }

    @Override
    public void enableGyroCompensation(boolean state) {

    }
}
