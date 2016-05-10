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
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author aleksander.jurczyk@seedlabs.io
 */
public class ImuDriver implements IImuDriver, Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImuDriver.class);

    private static final int DT_MS = 20;
    private static final int MEDIAN_SIZE = 3;
    private final RotatingList<AccGyroData> previousReadings = new RotatingList<>(MEDIAN_SIZE - 1);
    private final List<IImuReadingListener> listeners = new ArrayList<>();
    private AccGyroData compensation = new AccGyroData();

    @Autowired
    private IGyroAcc gyroAcc;
    private double angleX;
    private double angleY;
    private double angleZ;

    private Thread runner;

    /**
     * Constructor with gyroAcc.
     *
     * @param gyroAcc gyroAcc controller
     */
    public ImuDriver(IGyroAcc gyroAcc) {
        super();
        this.gyroAcc = gyroAcc;
    }

    public void setGyroAcc(IGyroAcc gyroAcc) {
        this.gyroAcc = gyroAcc;
    }

    public void registerListener(IImuReadingListener listener) {
        listeners.add(listener);
    }

    @Override
    public PositionAngle getAngle() {
        return new PositionAngle(angleX, angleY, angleZ);
    }

    @Override
    public void startReading() {
        runner = new Thread(this);
        runner.start();
    }

    @Override
    public void stopReading() {
        try {
            runner.interrupt();
            runner.join();
        } catch (InterruptedException e) {
            LOGGER.debug("Waiting for gyroscope to end was interrupted.", e);
        }
    }

    @Override
    public boolean isReading() {
        return runner.isAlive();
    }

    @Override
    public void run() {
        LOGGER.debug("Start reading gyro.");
        calibrate();
        long systemDelay;
        try {
            while (true) {
                systemDelay = System.currentTimeMillis();
                final AccGyroData cleanReading = readCleanData();
                angleX += cleanReading.getGyroX() * (DT_MS / 1000d);
                angleY += cleanReading.getGyroY() * (DT_MS / 1000d);
                angleZ += cleanReading.getGyroZ() * (DT_MS / 1000d);
                systemDelay = System.currentTimeMillis() - systemDelay;
                if (systemDelay < DT_MS) {
                    Thread.sleep(DT_MS - systemDelay);
                }
            }

        } catch (InterruptedException | AccGyroIncorrectAxisException | AccGyroReadValueException e) {
            LOGGER.error(e.toString());
        } finally {
            LOGGER.debug("Reading gyro finished.");
        }
    }

    private AccGyroData readCleanData() throws AccGyroIncorrectAxisException, AccGyroReadValueException {
        final AccGyroData raw = gyroAcc.readAll();
        final AccGyroData filtered = medianFilter(raw);
        final AccGyroData compensated = compensateData(filtered);

        previousReadings.add(raw);

        listeners.forEach(listener -> listener.ReadingReceived(compensated));
        listeners.forEach(listener -> listener.ReadingReceived(raw));
        return compensated;
    }

    private AccGyroData medianFilter(AccGyroData rawReading) {
        if (previousReadings.size() < MEDIAN_SIZE - 1) {
            return rawReading;
        }

        double[] accXforMedian = new double[MEDIAN_SIZE];
        double[] accYforMedian = new double[MEDIAN_SIZE];
        double[] accZforMedian = new double[MEDIAN_SIZE];
        double[] gyroXforMedian = new double[MEDIAN_SIZE];
        double[] gyroYforMedian = new double[MEDIAN_SIZE];
        double[] gyroZforMedian = new double[MEDIAN_SIZE];

        for (int i = 0; i < MEDIAN_SIZE - 1; i++) {
            accXforMedian[i] = previousReadings.get(previousReadings.size() - i - 1).getAccX();
            accYforMedian[i] = previousReadings.get(previousReadings.size() - i - 1).getAccY();
            accZforMedian[i] = previousReadings.get(previousReadings.size() - i - 1).getAccZ();
            gyroXforMedian[i] = previousReadings.get(previousReadings.size() - i - 1).getGyroX();
            gyroYforMedian[i] = previousReadings.get(previousReadings.size() - i - 1).getGyroY();
            gyroZforMedian[i] = previousReadings.get(previousReadings.size() - i - 1).getGyroZ();
        }

        accXforMedian[MEDIAN_SIZE - 1] = rawReading.getAccX();
        accYforMedian[MEDIAN_SIZE - 1] = rawReading.getAccY();
        accZforMedian[MEDIAN_SIZE - 1] = rawReading.getAccZ();
        gyroXforMedian[MEDIAN_SIZE - 1] = rawReading.getGyroX();
        gyroYforMedian[MEDIAN_SIZE - 1] = rawReading.getGyroY();
        gyroZforMedian[MEDIAN_SIZE - 1] = rawReading.getGyroZ();

        return new AccGyroData(
                MathUtils.median(accXforMedian),
                MathUtils.median(accYforMedian),
                MathUtils.median(accZforMedian),
                MathUtils.median(gyroXforMedian),
                MathUtils.median(gyroYforMedian),
                MathUtils.median(gyroZforMedian)
        );
    }

    private AccGyroData compensateData(AccGyroData data) {
        return new AccGyroData(
                data.getAccX(),
                data.getAccY(),
                data.getAccZ(),
                data.getGyroX() - compensation.getGyroX(),
                data.getGyroY() - compensation.getGyroY(),
                data.getGyroZ() - compensation.getGyroZ()
        );
    }

    private void calibrate() {
        try {
            compensation = new AccGyroData();
            final int probesAmount = 100;
            double meanGyroX = 0;
            double meanGyroY = 0;
            double meanGyroZ = 0;

            for (int i = 0; i < probesAmount; i++) {
                final AccGyroData filteredReading = readCleanData();

                meanGyroX += filteredReading.getGyroX();
                meanGyroY += filteredReading.getGyroY();
                meanGyroZ += filteredReading.getGyroZ();
                Thread.sleep(DT_MS);
            }
            meanGyroX /= probesAmount;
            meanGyroY /= probesAmount;
            meanGyroZ /= probesAmount;

            compensation = new AccGyroData(0, 0, 0, meanGyroX, meanGyroY, meanGyroZ);
            angleX = 0;
            angleY = 0;
            angleZ = 0;
        } catch (InterruptedException | AccGyroIncorrectAxisException | AccGyroReadValueException e) {
            LOGGER.error(e.toString());
        }
    }
}
