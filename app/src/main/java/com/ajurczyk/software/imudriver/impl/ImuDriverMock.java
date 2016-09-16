package com.ajurczyk.software.imudriver.impl;

import com.ajurczyk.hardware.gyroacc.impl.AccGyroData;
import com.ajurczyk.software.imudriver.IImuDriver;
import com.ajurczyk.software.imudriver.IImuReaderListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Mock of {@link IImuDriver} that generates random events.
 *
 * @author aleksander.jurczyk@gmail.com on 03.09.16.
 */
public class ImuDriverMock implements IImuDriver {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImuDriverMock.class);

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
    @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
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

            final AccGyroData cleanReading = new AccGyroData(rawReading.getAccX() + 0.5f,
                rawReading.getAccY() + 0.5f,
                rawReading.getAccZ() + 0.5f,
                rawReading.getGyroX() + 50,
                rawReading.getGyroY() + 50,
                rawReading.getGyroZ() + 50
            );
            listener.rawReadingReceived(rawReading);
            listener.cleanReadingReceived(cleanReading);
            try {
                TimeUnit.MILLISECONDS.sleep(eventsInterval);
            } catch (InterruptedException e) {
                LOGGER.error(e.toString());
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

    private float getRandomAcc() {
        return randomWithinRange(-1f, 1f);
    }

    private float getRandomGyro() {
        return randomWithinRange(-255f, 200f);
    }

    private float randomWithinRange(float min, float max) {
        final Random random = new Random();
        return min + (max - min) * random.nextFloat();
    }
}
