package com.ajurczyk.software.imudriver.impl;

import com.ajurczyk.hardware.gyroacc.impl.AccGyroData;
import com.ajurczyk.software.imudriver.IImuDriver;
import com.ajurczyk.software.imudriver.IImuFilteredReader;
import com.ajurczyk.software.imudriver.IImuReaderListener;
import com.ajurczyk.software.imudriver.exception.ImuFilteredReaderException;
import com.ajurczyk.time.IClock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.testng.Assert;
import org.testng.annotations.Test;

import static org.mockito.Mockito.*;

/**
 * @author aleksander.jurczyk@gmail.com on 17.06.16.
 */
public class ImuDriverTest {

    private static final int CYCLES = 5;
    private static final long DT_MS = ImuDriver.getDtMs();

    /**
     * Factor used by complementary filter.
     */
    private static final float ACC_FACTOR = 0.02f;

    private static final AccGyroData[] READINGS = {
        new AccGyroData(0.1f, 0.2f, 0.3f, 1, 2, 3),
        new AccGyroData(0.3f, 0.2f, 0.1f, 0, 0, 0),
        new AccGyroData(0.1f, 0.2f, 0.3f, -1, -2, -3),
        new AccGyroData(0.3f, 0.2f, 0.1f, 1, 1, 1),
        new AccGyroData(0.2f, 0.2f, 0.2f, 0, 0, 0)
    };

    @Test
    public final void readerThread() throws ImuFilteredReaderException, InterruptedException {
        //given
        final IImuReaderListener listener = mock(IImuReaderListener.class);
        final IImuFilteredReader reader = mock(IImuFilteredReader.class);
        final CalibrationManager calibrationMgr = mock(CalibrationManager.class);
        final IClock clock = mock(IClock.class);
        when(clock.getMiliseconds()).thenReturn(
            0L, DT_MS - 1,
            0L, DT_MS - 1,
            0L, DT_MS - 1,
            0L, DT_MS - 1,
            0L, DT_MS - 1);

        final IImuDriver imuDriver = new ImuDriver();
        ((ImuDriver) imuDriver).setCalibrationMgr(calibrationMgr);
        ((ImuDriver) imuDriver).setClock(clock);
        ((ImuDriver) imuDriver).setListener(listener);
        ((ImuDriver) imuDriver).setFilteredReader(reader);
        ((ImuDriver) imuDriver).setPositionAngle(new PositionAngle());

        when(reader.getFilteredReading()).thenAnswer(new Answer<AccGyroData>() {
            int counter;

            @Override
            public AccGyroData answer(InvocationOnMock invocation) throws Throwable {
                return READINGS[counter++];
            }
        });

        //when
        for (int i = 0; i < CYCLES; i++) {
            ((ImuDriver) imuDriver).mainReader();
        }
        final PositionAngle position = imuDriver.getAngle();

        //then
        verify(reader, times(CYCLES)).getFilteredReading();
        verify(clock, times(CYCLES * 2)).getMiliseconds();
        verifyNoMoreInteractions(reader);

        final PositionAngle expectedAngle = getExpectedAngle();

        Assert.assertEquals(position.getAngleX(), expectedAngle.getAngleX());
        Assert.assertEquals(position.getAngleY(), expectedAngle.getAngleY());
        Assert.assertEquals(position.getAngleZ(), expectedAngle.getAngleZ());
    }

    private PositionAngle getExpectedAngle() {
        float angleX = 0f;
        float angleY = 0f;
        final float gyroFactor = 1f - ACC_FACTOR;

        for (int i = 0; i < CYCLES; i++) {
            final float accXangle = (float)Math.toDegrees(Math.atan2(READINGS[i].getAccY(), READINGS[i].getAccZ()));
            final float accYangle = (float)Math.toDegrees(Math.atan2(READINGS[i].getAccX(), READINGS[i].getAccZ()));

            final float gyroXangle = angleX + READINGS[i].getGyroX() * (DT_MS / 1000f);
            final float gyroYangle = angleY + READINGS[i].getGyroY() * (DT_MS / 1000f);
            angleX = gyroFactor * gyroXangle + ACC_FACTOR * accXangle;
            angleY = gyroFactor * gyroYangle + ACC_FACTOR * accYangle;
        }
        return new PositionAngle(angleX, angleY, 0);
    }
    //TODO add test when complementary filter fails
}
