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

    private final RotatingList<AccGyroData> previousReadings = new RotatingList<>(MEDIAN_SIZE);

    private String compensationFile = "";

    private AccGyroData compensation;//TODO load from file

    private boolean compensate = true;

    @Autowired
    private IGyroAcc gyroAcc;

    /**
     * Constructor that tries to load compensation file.
     *
     * @throws IOException               file not found
     * @throws PropertyNotFoundException asked compensation not found
     */
    public ImuFilteredReader() throws IOException, PropertyNotFoundException {
        if (!compensationFile.isEmpty()) {
            loadCompensationFromFile(compensationFile);
        }
    }

    protected AccGyroData getCompensation() {
        return compensation;
    }

    protected void setGyroAcc(IGyroAcc gyroAcc) {
        this.gyroAcc = gyroAcc;
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
        readRaw();

        AccGyroData cleanReading = getMedian();

        if (compensate) {
            cleanReading = compensateGyro(cleanReading);
        }
        return cleanReading;
    }

    protected void setCompensationFile(String compensationFile) {
        this.compensationFile = compensationFile;
    }

    protected RotatingList<AccGyroData> getPreviousReadings() {
        return previousReadings;
    }

    private AccGyroData getMedian() {
        if (previousReadings.size() < MEDIAN_SIZE) { //TODO what to do now? exception? null? raw?
            return previousReadings.get(previousReadings.size() - 1);
        }

        final List<AccGyroData> dataForMedian = new ArrayList<>();
        for (int i = 1; i < MEDIAN_SIZE + 1; i++) {
            dataForMedian.add(previousReadings.get(previousReadings.size() - i));
        }

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
        loadCompensationFromFile(compensationFile);
    }

    private void loadCompensationFromFile(String filePath) throws IOException, PropertyNotFoundException {
        final PropertiesManager properties = new PropertiesManager(filePath);
        compensation = new AccGyroData(0, 0, 0,
                Double.valueOf(properties.getProperty("gyroX")),
                Double.valueOf(properties.getProperty("gyroY")),
                Double.valueOf(properties.getProperty("gyroZ")));
    }

    @Override
    public void enableGyroCompensation(boolean state) {
        compensate = state;
    }
}
