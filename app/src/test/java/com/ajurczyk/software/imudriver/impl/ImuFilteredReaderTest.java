package com.ajurczyk.software.imudriver.impl;

import com.ajurczyk.hardware.gyroacc.IGyroAcc;
import com.ajurczyk.hardware.gyroacc.exception.AccGyroIncorrectAxisException;
import com.ajurczyk.hardware.gyroacc.exception.AccGyroReadValueException;
import com.ajurczyk.hardware.gyroacc.impl.AccGyroData;
import com.ajurczyk.properties.PropertyNotFoundException;
import com.ajurczyk.software.imudriver.exception.ImuFilteredReaderException;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.mockito.Mockito.*;


/**
 * Test of {@link ImuFilteredReader}.
 *
 * @author aleksander.jurczyk@gmail.com on 13.06.16.
 */
public class ImuFilteredReaderTest {

    private static final String COMPENSATION_FILE = "src/test/resources/compensation.properties";
    private static final Double GYRO_X_COMPENS = 1.3d;
    private static final Double GYRO_Y_COMPENS = 2.3d;
    private static final Double GYRO_Z_COMPENS = 3.3d;

    private final AccGyroData[] fakeReadings = new AccGyroData[]{
        new AccGyroData(0d, 0.5d, 1d, 2d, 2.5d, 3d),
        new AccGyroData(0.5d, 3d, 9.7d, 2d, 12.5d, -5d),
        new AccGyroData(1d, 5d, 2.6d, 2d, 1.2d, -6d),
        new AccGyroData(1.7d, 3d, 4d, 20d, 20d, -7d)};

    /**
     * Create temporary compensation file.
     *
     * @throws IOException problem while creating file
     */
    @BeforeMethod
    public final void createCompensationFile() throws IOException {
        final StringBuilder builder = new StringBuilder(18);
        final String newline = System.getProperty("line.separator");
        builder.append("gyroX=").append(String.valueOf(GYRO_X_COMPENS)).append(newline)
                .append("gyroY=").append(String.valueOf(GYRO_Y_COMPENS)).append(newline)
                .append("gyroZ=").append(String.valueOf(GYRO_Z_COMPENS));

        final File file = new File(COMPENSATION_FILE);
        if (!file.exists()) {
            file.createNewFile();
        }

        final FileOutputStream fos = new FileOutputStream(COMPENSATION_FILE);
        try {
            fos.write(builder.toString().getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            fos.close();
        }
        fos.close();
    }

    /**
     * Method that deletes temporary compensation file.
     */
    @AfterClass
    public final void deleteCompensationFile() {
        final File file = new File(COMPENSATION_FILE);
        if (file.exists()) {
            file.delete();
        }
    }

    @Test
    public final void reloadFile() throws IOException, PropertyNotFoundException {
        //given
        final ImuFilteredReader filteredReader = new ImuFilteredReader();
        filteredReader.setCompensationFile(COMPENSATION_FILE);

        //when
        filteredReader.reloadCompensation();
        final AccGyroData compensation = filteredReader.getCompensation();

        //then
        Assert.assertEquals(compensation.getAccX(), 0.0d, 0.0001d);
        Assert.assertEquals(compensation.getAccY(), 0.0d, 0.0001d);
        Assert.assertEquals(compensation.getAccZ(), 0.0d, 0.0001d);
        Assert.assertEquals(compensation.getGyroX(), GYRO_X_COMPENS, 0.0001d);
        Assert.assertEquals(compensation.getGyroY(), GYRO_Y_COMPENS, 0.0001d);
        Assert.assertEquals(compensation.getGyroZ(), GYRO_Z_COMPENS, 0.0001d);
    }

    @Test
    public final void readRaw() throws IOException, PropertyNotFoundException, ImuFilteredReaderException,
            AccGyroIncorrectAxisException, AccGyroReadValueException {
        //given
        final ImuFilteredReader filteredReader = new ImuFilteredReader();
        final IGyroAcc gyroAcc = mock(IGyroAcc.class);
        filteredReader.setGyroAcc(gyroAcc);

        //when
        filteredReader.readRaw();

        //then
        verify(gyroAcc, times(1)).readAll();
        verifyNoMoreInteractions(gyroAcc);
    }

    @Test
    public final void readClean() throws IOException, PropertyNotFoundException, ImuFilteredReaderException,
            AccGyroIncorrectAxisException, AccGyroReadValueException {
        //given
        final ImuFilteredReader filteredReader = new ImuFilteredReader();
        filteredReader.setCompensationFile(COMPENSATION_FILE);
        filteredReader.reloadCompensation();

        final IGyroAcc gyroAcc = mock(IGyroAcc.class);
        when(gyroAcc.readAll()).thenAnswer(new Answer<AccGyroData>() {
            private int counter;

            @Override
            public AccGyroData answer(InvocationOnMock invocation) throws Throwable {
                return fakeReadings[counter++];
            }
        });
        filteredReader.setGyroAcc(gyroAcc);

        //when
        for (int i = 0; i < 3; i++) {
            filteredReader.readClean();
        }
        final AccGyroData reading = filteredReader.readClean();

        //then
        Assert.assertEquals(reading.getAccX(), 1.0d, 0.001d);
        Assert.assertEquals(reading.getAccY(), 3.0d, 0.001d);
        Assert.assertEquals(reading.getAccZ(), 4.0d, 0.001d);
        Assert.assertEquals(reading.getGyroX(), 0.7d, 0.001d);
        Assert.assertEquals(reading.getGyroY(), 10.2d, 0.001d);
        Assert.assertEquals(reading.getGyroZ(), -9.3, 0.001d);
    }

    @Test
    public final void clear() throws IOException, PropertyNotFoundException, AccGyroIncorrectAxisException,
            AccGyroReadValueException, ImuFilteredReaderException {
        //given
        final ImuFilteredReader filteredReader = new ImuFilteredReader();
        filteredReader.setCompensationFile(COMPENSATION_FILE);
        filteredReader.reloadCompensation();

        final IGyroAcc gyroAcc = mock(IGyroAcc.class);
        when(gyroAcc.readAll()).thenAnswer(new Answer<AccGyroData>() {
            private int counter;

            @Override
            public AccGyroData answer(InvocationOnMock invocation) throws Throwable {
                return fakeReadings[counter++];
            }
        });
        filteredReader.setGyroAcc(gyroAcc);

        //when
        filteredReader.readClean();
        filteredReader.readRaw();
        filteredReader.clear();

        //then
        Assert.assertEquals(filteredReader.getPreviousReadings().size(), 0);
    }

    @Test
    public final void enableCompensate() {
        //given
        //when
        //then
    }
}
