package software.imudriver.impl;

import hardware.gyroacc.IGyroAcc;
import hardware.gyroacc.exception.AccGyroIncorrectAxisException;
import hardware.gyroacc.exception.AccGyroReadValueException;
import hardware.gyroacc.impl.AccGyroData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import software.imudriver.IImuDriver;
import software.imudriver.IImuReadingListener;
import utils.MathUtils;
import utils.RotatingList;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author aleksander.jurczyk@seedlabs.io
 */
public class ImuDriver implements IImuDriver, Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImuDriver.class);
    private static final int DT_MS = 20;
    private static final int MEDIAN_SIZE = 3;
    private static final int CALI_PROBES = 100;

    private final RotatingList<AccGyroData> previousReadings = new RotatingList<>(MEDIAN_SIZE - 1);

    private final List<IImuReadingListener> listeners = new ArrayList<>();

    private Thread runner;

    private AccGyroData compensation = new AccGyroData();

    @Autowired
    private IGyroAcc gyroAcc;

    private PositionAngle positionAngle;

    public void registerListener(IImuReadingListener listener) {
        listeners.add(listener);
    }

    @Override
    public PositionAngle getAngle() {
        return positionAngle;
    }

    @Override
    public void startWorking() {
        runner = new Thread(this);
        runner.start();
    }

    @Override
    public void stopWorking() {
        try {
            runner.interrupt();
            runner.join();
        } catch (InterruptedException e) {
            LOGGER.debug("Waiting for gyroscope to end was interrupted.", e);
        }
    }

    @Override
    public boolean isWorking() {
        return runner.isAlive();
    }

    @Override
    public void run() {
        LOGGER.debug("Start reading gyro.");
        long systemDelay;

        positionAngle = new PositionAngle();

        try {
            calibrate();
            while (true) {
                systemDelay = System.currentTimeMillis();
                final AccGyroData rawReading = gyroAcc.readAll();
                final AccGyroData cleanReading = applyCompensation(applyMedianFilter(rawReading));
                previousReadings.add(rawReading);

                listeners.forEach(listener -> listener.readingReceived(rawReading));
                listeners.forEach(listener -> listener.readingReceived(cleanReading));

                reCalcAngle(cleanReading);

                systemDelay = System.currentTimeMillis() - systemDelay;

                if (systemDelay < DT_MS) {
                    Thread.sleep(DT_MS - systemDelay);
                } else {
                    LOGGER.warn("Gyro math took longer than dt(" + DT_MS + "ms): " + systemDelay + "ms.");
                }
            }

        } catch (InterruptedException | AccGyroIncorrectAxisException | AccGyroReadValueException e) {
            LOGGER.error(e.toString());
        } finally {
            LOGGER.debug("Reading gyro finished.");
        }
    }

    private void calibrate() throws InterruptedException, AccGyroIncorrectAxisException, AccGyroReadValueException {

        double meanGyroX = 0;
        double meanGyroY = 0;
        double meanGyroZ = 0;

        for (int i = 0; i < CALI_PROBES; i++) {
            final AccGyroData reading = applyMedianFilter(gyroAcc.readAll());

            listeners.forEach(listener -> listener.readingReceived(reading));
            listeners.forEach(listener -> listener.readingReceived(reading));

            meanGyroX += reading.getGyroX();
            meanGyroY += reading.getGyroY();
            meanGyroZ += reading.getGyroZ();
            Thread.sleep(DT_MS);
        }
        meanGyroX /= CALI_PROBES;
        meanGyroY /= CALI_PROBES;
        meanGyroZ /= CALI_PROBES;

        compensation = new AccGyroData(0, 0, 0, meanGyroX, meanGyroY, meanGyroZ);
    }

    private AccGyroData applyMedianFilter(AccGyroData rawReading) {
        if (previousReadings.size() < MEDIAN_SIZE - 1) {
            return rawReading;
        }

        final List<AccGyroData> dataForMedian = new ArrayList<>();
        for (int i = 1; i < MEDIAN_SIZE; i++) {
            dataForMedian.add(previousReadings.get(previousReadings.size() - i));
        }
        dataForMedian.add(rawReading);

        return new AccGyroData(
                MathUtils.median(dataForMedian.stream().map(AccGyroData::getAccX).collect(Collectors.toList())),
                MathUtils.median(dataForMedian.stream().map(AccGyroData::getAccY).collect(Collectors.toList())),
                MathUtils.median(dataForMedian.stream().map(AccGyroData::getAccZ).collect(Collectors.toList())),
                MathUtils.median(dataForMedian.stream().map(AccGyroData::getGyroX).collect(Collectors.toList())),
                MathUtils.median(dataForMedian.stream().map(AccGyroData::getGyroY).collect(Collectors.toList())),
                MathUtils.median(dataForMedian.stream().map(AccGyroData::getGyroZ).collect(Collectors.toList()))
        );
    }

    private AccGyroData applyCompensation(AccGyroData data) {
        return new AccGyroData(
                data.getAccX(),
                data.getAccY(),
                data.getAccZ(),
                data.getGyroX() - compensation.getGyroX(),
                data.getGyroY() - compensation.getGyroY(),
                data.getGyroZ() - compensation.getGyroZ()
        );
    }

    private void reCalcAngle(AccGyroData cleanReading) {
        positionAngle.setAngleX(positionAngle.getAngleX() + cleanReading.getGyroX() * (DT_MS / 1000d));
        positionAngle.setAngleY(positionAngle.getAngleY() + cleanReading.getGyroY() * (DT_MS / 1000d));
        positionAngle.setAngleZ(positionAngle.getAngleZ() + cleanReading.getGyroZ() * (DT_MS / 1000d));
    }
}
