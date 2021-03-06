package com.ajurczyk.software.imudriver.impl;

import com.ajurczyk.hardware.gyroacc.IGyroAcc;
import com.ajurczyk.hardware.gyroacc.exception.AccGyroIncorrectAxisException;
import com.ajurczyk.hardware.gyroacc.exception.AccGyroReadValueException;
import com.ajurczyk.hardware.gyroacc.impl.AccGyroData;
import com.ajurczyk.properties.PropertyNotFoundException;
import com.ajurczyk.properties.impl.PropertiesManager;
import com.ajurczyk.software.imudriver.IImuFilteredReader;
import com.ajurczyk.software.imudriver.IImuReaderListener;
import com.ajurczyk.software.imudriver.exception.ImuFilteredReaderException;
import com.ajurczyk.utils.MathUtils;
import com.ajurczyk.utils.RotatingList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

/**
 * @author aleksander.jurczyk@gmail.com on 13.06.16.
 */
public class ImuFilteredReader implements IImuFilteredReader {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImuFilteredReader.class);

    private static final int MEDIAN_SIZE = 3;
    private final RotatingList<AccGyroData> previousReadings = new RotatingList<>(MEDIAN_SIZE);
    @Autowired
    private IImuReaderListener listener;
    @Autowired
    private IGyroAcc gyroAcc;
    private String compensationFile;
    private AccGyroData compensation;
    private boolean compensate = true;

    /**
     * Default constructor.
     */
    public ImuFilteredReader() {

    }

    protected ImuFilteredReader(String compensationFile) {
        this.compensationFile = compensationFile;
    }

    public String getCompensationFile() {
        return compensationFile;
    }

    public void setCompensationFile(String compensationFile) {
        this.compensationFile = compensationFile;
    }

    public void setListener(IImuReaderListener listener) {
        this.listener = listener;
    }

    /**
     * Initialization of reader. Loads compensation from file.
     *
     * @throws ImuFilteredReaderException thrown when some problems with compensation file occurs
     */
    @PostConstruct
    public void init() throws ImuFilteredReaderException {
        try {
            loadCompensationFromFile(compensationFile);
        } catch (IOException | PropertyNotFoundException e) {
            throw new ImuFilteredReaderException(e.getMessage(), e);
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

    @SuppressWarnings("PMD.ShortVariable")
    @Override
    public AccGyroData getFilteredReading() throws ImuFilteredReaderException {
        readRaw();
        AccGyroData cleanReading = getMedian();

        if (compensate) {
            cleanReading = compensateGyro(cleanReading);
        }

        listener.cleanReadingReceived(cleanReading);
        return cleanReading;
    }

    @Override
    public void reloadCompensation() throws ImuFilteredReaderException {
        try {
            loadCompensationFromFile(compensationFile);
            LOGGER.debug("Compensation reloaded.");
        } catch (PropertyNotFoundException | IOException e) {
            throw new ImuFilteredReaderException(e.getMessage(), e);
        }
    }

    @Override
    public void enableGyroCompensation(boolean state) {
        compensate = state;
    }

    protected RotatingList<AccGyroData> getPreviousReadings() {
        return previousReadings;
    }

    @SuppressWarnings("PMD.ShortVariable")
    private void readRaw() throws ImuFilteredReaderException {
        try {
            final AccGyroData reading = gyroAcc.readAll();
            previousReadings.add(reading);
            listener.rawReadingReceived(reading);
        } catch (AccGyroIncorrectAxisException | AccGyroReadValueException e) {
            throw new ImuFilteredReaderException(e.getMessage(), e);
        }
    }

    private AccGyroData getMedian() {
        if (previousReadings.size() < MEDIAN_SIZE) {
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

    private void loadCompensationFromFile(String filePath) throws IOException, PropertyNotFoundException {
        final PropertiesManager properties = new PropertiesManager(filePath);
        compensation = new AccGyroData(0f, 0f, 0f,
            Float.valueOf(properties.getProperty("gyroX")),
            Float.valueOf(properties.getProperty("gyroY")),
            Float.valueOf(properties.getProperty("gyroZ")));
    }
}
