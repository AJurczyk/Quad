package software.imudriver.impl;

import hardware.gyroacc.IGyroAcc;
import hardware.gyroacc.exception.AccGyroIncorrectAxisException;
import hardware.gyroacc.exception.AccGyroReadValueException;
import hardware.gyroacc.impl.AccGyroReadOut;
import software.imudriver.IImuDriver;
import utils.MathUtils;
import utils.RotatingList;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author aleksander.jurczyk@seedlabs.io
 */
public class ImuDriver extends Thread implements IImuDriver {

    private static final int DT_MS = 10;
    private static final int MEDIAN_SIZE = 3;
    private final RotatingList<AccGyroReadOut> previousReadings = new RotatingList<>(MEDIAN_SIZE - 1);

    private final IGyroAcc gyroAcc;
    private final AtomicBoolean stop = new AtomicBoolean(true);
    private float angleX;
    private float angleY;
    private float angleZ;

    /**
     * Constructor with gyroAcc.
     *
     * @param gyroAcc gyroAcc controller
     */
    public ImuDriver(IGyroAcc gyroAcc) {
        super();
        this.gyroAcc = gyroAcc;
        calibrate();//TODO to implement
    }

    @Override
    public PositionAngle getAngle() {
        return new PositionAngle(angleX, angleY, angleZ);
    }

    @Override
    public void startReading() {
        this.start();
    }

    @Override
    public void stopReading() {
        stop.set(true);
    }

    @Override
    public boolean isReading() {
        return !stop.get();
    }

    @Override
    public void run() {
        stop.set(false);
        try {
            while (!stop.get()) {
                final AccGyroReadOut filteredReading = medianFilter(gyroAcc.readAll());
                previousReadings.add(filteredReading);

                angleX += filteredReading.getGyroX() * (DT_MS / 1000);
                angleY += filteredReading.getGyroY() * (DT_MS / 1000);
                angleZ += filteredReading.getGyroZ() * (DT_MS / 1000);
                Thread.sleep(DT_MS);
            }

        } catch (AccGyroIncorrectAxisException | AccGyroReadValueException e) {
            //todo log eror
        } catch (InterruptedException e) {
            //todo log eror
        } finally {
            stop.set(false);
        }
    }

    private AccGyroReadOut medianFilter(AccGyroReadOut rawReading) {
        if (previousReadings.size() < MEDIAN_SIZE - 1) {
            return rawReading;
        }
        return new AccGyroReadOut(
            MathUtils.median(previousReadings.get(0).getAccX(), previousReadings.get(1).getAccX(), rawReading.getAccX()),
            MathUtils.median(previousReadings.get(0).getAccY(), previousReadings.get(1).getAccY(), rawReading.getAccY()),
            MathUtils.median(previousReadings.get(0).getAccZ(), previousReadings.get(1).getAccZ(), rawReading.getAccZ()),
            MathUtils.median(previousReadings.get(0).getGyroX(), previousReadings.get(1).getGyroX(), rawReading.getGyroX()),
            MathUtils.median(previousReadings.get(0).getGyroY(), previousReadings.get(1).getGyroY(), rawReading.getGyroY()),
            MathUtils.median(previousReadings.get(0).getGyroZ(), previousReadings.get(1).getGyroZ(), rawReading.getGyroZ())
        );
    }

    private void calibrate() {
        //TODO to implement
        angleX = 0;
        angleY = 0;
        angleZ = 0;
    }
}
