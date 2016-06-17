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
import org.testng.annotations.AfterMethod;
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
    private static final int DEFAULT_CALI_PROBES = 100;

    @BeforeClass
    @AfterMethod
    private void deleteCompensationFile() {
        final File file = new File(FILE_PATH);
        if (file.exists()) {
            file.delete();
        }
    }

    private void createFile() throws IOException {
        FileOutputStream fos = new FileOutputStream(FILE_PATH);
        final String newline = System.getProperty("line.separator");
        final StringBuilder builder = new StringBuilder();
        builder.append("gyroX=").append(String.valueOf(GYRO_X_COMPENS)).append(newline)
                .append("gyroY=").append(String.valueOf(GYRO_Y_COMPENS)).append(newline)
                .append("gyroZ=").append(String.valueOf(GYRO_Z_COMPENS));

        fos.write(builder.toString().getBytes(StandardCharsets.UTF_8));
        fos.close();
    }

    @Test
    public final void calibrate() throws ImuFilteredReaderException, CalibrationManagerException, IOException, PropertyNotFoundException {
        //given
        final CalibrationManager calibrationMgr = new CalibrationManager();
        final IImuFilteredReader filteredReader = mock(IImuFilteredReader.class);
        calibrationMgr.setCompensationFile(FILE_PATH);
        calibrationMgr.setReader(filteredReader);

        when(filteredReader.readClean()).thenAnswer(new Answer<AccGyroData>() {
            int counter = 0;

            @Override
            public AccGyroData answer(InvocationOnMock invocation) throws Throwable {
                counter++;
                switch (counter % 4) {
                    case 0: {
                        return new AccGyroData(0.1d, 3.1d, 2.2d, -3d, 4.1d, 5d);
                    }
                    case 1: {
                        return new AccGyroData(3.2d, 2.4d, 2.2d, -4d, 4.3d, 3d);
                    }
                    case 2: {
                        return new AccGyroData(5.4d, 1.02d, 0d, 12.4d, 0d, 0.2d);
                    }
                    case 3: {
                        return new AccGyroData(1d, 1d, 1d, 1d, 1d, 1d);
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
        verify(filteredReader, times(100)).readClean();
        verify(filteredReader, times(1)).enableGyroCompensation(false);
        verify(filteredReader, times(1)).enableGyroCompensation(true);

        PropertiesManager properties = new PropertiesManager(FILE_PATH);
        Assert.assertEquals(properties.getPropertiesNames().size(), 3);

        Assert.assertEquals(Double.valueOf(properties.getProperty("gyroX")), 1.6d, 0.001d);
        Assert.assertEquals(Double.valueOf(properties.getProperty("gyroY")), 2.35d, 0.001d);
        Assert.assertEquals(Double.valueOf(properties.getProperty("gyroZ")), 2.3d, 0.001d);
    }
}
