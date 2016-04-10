package software.imudriver.impl;

import hardware.gyroacc.IGyroAcc;
import hardware.gyroacc.exception.AccGyroIncorrectAxisException;
import hardware.gyroacc.exception.AccGyroReadValueException;
import hardware.gyroacc.impl.AccGyroReadOut;
import software.imudriver.IImuDriver;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author aleksander.jurczyk@seedlabs.io
 */
public class ImuDriver extends Thread implements IImuDriver {

    private static final int DT_MS = 10;

    private IGyroAcc gyroAcc;

    private float angleX = 0;
    private float angleY = 0;
    private float angleZ = 0;

    private AccGyroReadOut[] readings = new AccGyroReadOut[3];

    private AtomicBoolean stop = new AtomicBoolean(true);

    public ImuDriver(IGyroAcc gyroAcc) {
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
                AccGyroReadOut rawReading = gyroAcc.readAll(); //TODO filter those values
//                AccGyroReadOut filteredReading = medianFilter(rawReading);
                angleX += rawReading.getGyroX()*(DT_MS/1000);
                angleY += rawReading.getGyroY()*(DT_MS/1000);
                angleZ += rawReading.getGyroZ()*(DT_MS/1000);
                Thread.sleep(DT_MS);
            }

        } catch (AccGyroIncorrectAxisException | AccGyroReadValueException e) {
            //todo log eror
        } catch (InterruptedException e) {
            e.printStackTrace();
            //todo log eror
        } finally {
            stop.set(false);
        }
    }

    private void calibrate(){
        //TODO to implement
        angleX = 0;
        angleY = 0;
        angleZ = 0;
    }
}
