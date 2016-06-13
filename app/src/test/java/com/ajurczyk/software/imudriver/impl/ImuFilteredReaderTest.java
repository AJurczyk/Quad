package com.ajurczyk.software.imudriver.impl;

import com.ajurczyk.hardware.gyroacc.impl.AccGyroData;
import com.ajurczyk.properties.PropertyNotFoundException;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Test of {@link ImuFilteredReader}.
 *
 * @author aleksander.jurczyk@gmail.com on 13.06.16.
 */
public class ImuFilteredReaderTest {

    private final static String COMPENSATION_FILE = "/home/alek/compensation.properties";

    @Test
    public final void reloadFile() throws IOException, PropertyNotFoundException {
        //given
        final ImuFilteredReader filteredReader = new ImuFilteredReader();
        filteredReader.setCompensationFile(COMPENSATION_FILE);

        //when
        filteredReader.reloadCompensation();
        AccGyroData compensation = filteredReader.getCompensation();

        //then
        Assert.assertEquals(compensation.getAccX(), 0.0d, 0.0001d);
        Assert.assertEquals(compensation.getAccY(), 0.0d, 0.0001d);
        Assert.assertEquals(compensation.getAccZ(), 0.0d, 0.0001d);
        Assert.assertEquals(compensation.getGyroX(), 1.3d, 0.0001d);
        Assert.assertEquals(compensation.getGyroY(), 2.3d, 0.0001d);
        Assert.assertEquals(compensation.getGyroZ(), 3.3d, 0.0001d);
    }

    @BeforeMethod
    private void createCompensationFile() throws IOException {
        final StringBuilder builder = new StringBuilder();
        final String newline = System.getProperty("line.separator");
        builder.append("gyroX=").append(String.valueOf(1.3d)).append(newline)
                .append("gyroY=").append(String.valueOf(2.3d)).append(newline)
                .append("gyroZ=").append(String.valueOf(3.3d));

        final FileOutputStream fos = new FileOutputStream(COMPENSATION_FILE, false);
        try {
            fos.write(builder.toString().getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            fos.close();
        }
    }

    @AfterClass
    private void deleteCompensationFile() {
        File file = new File(COMPENSATION_FILE);
        if (file.exists()) {
            file.delete();
        }
    }
}
