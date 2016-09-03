package com.ajurczyk.software.imudriver.impl;

import com.ajurczyk.hardware.gyroacc.impl.AccGyroData;
import com.ajurczyk.software.imudriver.IImuDriver;
import com.ajurczyk.software.imudriver.IImuReaderListener;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Mock of {@link IImuDriver} that generates random events.
 *
 * @author aleksander.jurczyk@gmail.com on 03.09.16.
 */
public class ImuDriverMock implements IImuDriver {

    private int eventsInterval;
    private boolean working;
    @Autowired
    private IImuReaderListener listener;

    public void setEventsInterval(int eventsInterval) {
        this.eventsInterval = eventsInterval;
    }

    @Override
    public PositionAngle getAngle() {
        return null;
    }

    @Override
    public void startWorking() {
        working = true;
        while (working) {
            final AccGyroData rawReading = new AccGyroData(getRandomAcc(),
                getRandomAcc(),
                getRandomAcc(),
                getRandomGyro(),
                getRandomGyro(),
                getRandomGyro()
            );

            final AccGyroData cleanReading = new AccGyroData(rawReading.getAccX() + 0.5,
                rawReading.getAccY() + 0.5,
                rawReading.getAccZ() + 0.5,
                rawReading.getGyroX() + 50,
                rawReading.getGyroY() + 50,
                rawReading.getGyroZ() + 50
            );
            listener.rawReadingReceived(rawReading);
            listener.cleanReadingReceived(cleanReading);
            try {
                TimeUnit.MILLISECONDS.sleep(eventsInterval);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void stopWorking() {
        working = false;
    }

    @Override
    public boolean isWorking() {
        return working;
    }

    private double getRandomAcc() {
        return randomWithinRange(-1d, 1d);
    }

    private double getRandomGyro() {
        return randomWithinRange(-255d, 200d);
    }

    private double randomWithinRange(double min, double max) {
        final Random random = new Random();
        return min + (max - min) * random.nextDouble();
    }

}
