package com.ajurczyk.software.imudriver.impl;

import com.ajurczyk.hardware.gyroacc.impl.AccGyroData;
import com.ajurczyk.properties.PropertyNotFoundException;
import com.ajurczyk.properties.impl.PropertiesManager;
import com.ajurczyk.software.imudriver.IImuFilteredReader;
import com.ajurczyk.software.imudriver.exception.CalibrationManagerException;
import com.ajurczyk.software.imudriver.exception.ImuFilteredReaderException;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.mockito.Mockito.*;

/**
 * @author aleksander.jurczyk@gmail.com on 15.06.16.
 */
public class CalibrationManagerTest {

    private static final String FILE_PATH = "src/test/resources/testCompensation.properties";
    private static final Double GYRO_X_COMPENS = 1.3d;
    private static final Double GYRO_Y_COMPENS = 2.3d;
    private static final Double GYRO_Z_COMPENS = 3.3d;
    private static final int CALIBRATE_PROBES = 100;

    /**
     * Delete compensation file if exists.
     */
    @AfterClass
    public void deleteCompensationFile() {
        final File file = new File(FILE_PATH);
        if (file.exists()) {
            file.delete();
        }
    }

    /**
     * Delete old compensation file and create new.
     *
     * @throws IOException some problems with file
     */
    @BeforeClass
    public void createNewCompensationFile() throws IOException {
        deleteCompensationFile();
        final FileOutputStream fos = new FileOutputStream(FILE_PATH);

        try {
            final String newline = System.getProperty("line.separator");
            final StringBuilder builder = new StringBuilder(18);
            builder.append("gyroX=").append(String.valueOf(GYRO_X_COMPENS)).append(newline)
                .append("gyroY=").append(String.valueOf(GYRO_Y_COMPENS)).append(newline)
                .append("gyroZ=").append(String.valueOf(GYRO_Z_COMPENS));

            fos.write(builder.toString().getBytes(StandardCharsets.UTF_8));
            fos.close();
        } finally {
            fos.close();
        }
    }

    @Test
    public final void calibrate() throws ImuFilteredReaderException, CalibrationManagerException, IOException, PropertyNotFoundException {
        //given
        final IImuFilteredReader filteredReader = mock(IImuFilteredReader.class);
        final CalibrationManager calibrationMgr = new CalibrationManager();
        calibrationMgr.setCaliProbes(CALIBRATE_PROBES);
        calibrationMgr.setReader(filteredReader);
        when(filteredReader.getCompensationFile()).thenReturn(FILE_PATH);

        when(filteredReader.getFilteredReading()).thenAnswer(new Answer<AccGyroData>() {
            int counter;

            @Override
            public AccGyroData answer(InvocationOnMock invocation) throws Throwable {
                counter++;
                switch (counter % 4) {
                    case 0: {
                        return new AccGyroData(0.1f, 3.1f, 2.2f, -3f, 4.1f, 5f);
                    }
                    case 1: {
                        return new AccGyroData(3.2f, 2.4f, 2.2f, -4f, 4.3f, 3f);
                    }
                    case 2: {
                        return new AccGyroData(5.4f, 1.02f, 0f, 12.4f, 0f, 0.2f);
                    }
                    case 3: {
                        return new AccGyroData(1f, 1f, 1f, 1f, 1f, 1f);
                    }
                    default: {
                        return null;
                    }
                }
            }
        });

        //when
        calibrationMgr.calibrate();

        //then
        verify(filteredReader, times(CALIBRATE_PROBES)).getFilteredReading();
        verify(filteredReader, times(1)).enableGyroCompensation(false);
        verify(filteredReader, times(1)).enableGyroCompensation(true);

        final PropertiesManager properties = new PropertiesManager(FILE_PATH);
        Assert.assertEquals(properties.getPropertiesNames().size(), 3);

        Assert.assertEquals(Double.valueOf(properties.getProperty("gyroX")), 1.6d, 0.001d);
        Assert.assertEquals(Double.valueOf(properties.getProperty("gyroY")), 2.35d, 0.001d);
        Assert.assertEquals(Double.valueOf(properties.getProperty("gyroZ")), 2.3d, 0.001d);
    }
}
