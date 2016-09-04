package com.ajurczyk.software.imudriver.impl;

import com.ajurczyk.hardware.gyroacc.impl.AccGyroData;
import com.ajurczyk.software.imudriver.ICalibrationManager;
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
    private static final AccGyroData[] READINGS = {
        new AccGyroData(1, 2, 3, 1, 2, 3),
        new AccGyroData(2, 2, 2, 0, 0, 0),
        new AccGyroData(0, 0, 0, -1, -2, -3),
        new AccGyroData(0, 0, 0, 1, 1, 1),
        new AccGyroData(-1, -1, -1, 0, 0, 0)
    };

    @Test
    public final void readerThread() throws ImuFilteredReaderException, InterruptedException {
        //given
        final IImuReaderListener listener = mock(IImuReaderListener.class);
        final IImuFilteredReader reader = mock(IImuFilteredReader.class);
        final CalibrationManager calibrationManager = mock(CalibrationManager.class);
        final IClock clock = mock(IClock.class);
        when(clock.getMiliseconds()).thenReturn(
            0L, DT_MS - 1,
            0L, DT_MS - 1,
            0L, DT_MS - 1,
            0L, DT_MS - 1,
            0L, DT_MS - 1);

        final IImuDriver imuDriver = new ImuDriver();
        ((ImuDriver) imuDriver).setCalibrationManager(calibrationManager);
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
        double angleX = 0d;
        double angleY = 0d;
        double angleZ = 0d;

        for (int i = 0; i < CYCLES; i++) {
            angleX += READINGS[i].getGyroX() * DT_MS / 1000;
            angleY += READINGS[i].getGyroY() * DT_MS / 1000;
            angleZ += READINGS[i].getGyroZ() * DT_MS / 1000;
        }

        return new PositionAngle(angleX, angleY, angleZ);
    }
}
