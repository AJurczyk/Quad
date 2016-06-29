package com.ajurczyk.software.imudriver.impl;

import com.ajurczyk.hardware.gyroacc.impl.AccGyroData;
import com.ajurczyk.software.imudriver.ICalibrationManager;
import com.ajurczyk.software.imudriver.IImuFilteredReader;
import com.ajurczyk.software.imudriver.exception.CalibrationManagerException;
import com.ajurczyk.software.imudriver.exception.ImuFilteredReaderException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author aleksander.jurczyk@gmail.com on 13.06.16.
 */
public class CalibrationManager implements ICalibrationManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(CalibrationManager.class);
    private static final int DT_MS = 20;
    private int caliProbes = 100;
    @Autowired
    private IImuFilteredReader reader;

    private String compensationFile = "";

    protected void setCaliProbes(int caliProbes) {
        this.caliProbes = caliProbes;
    }

    public void setCompensationFile(String compensationFile) {
        this.compensationFile = compensationFile;
    }

    protected void setReader(IImuFilteredReader reader) {
        this.reader = reader;
    }

    @Override
    public void calibrate() throws CalibrationManagerException {
        try {
            double meanGyroX = 0;
            double meanGyroY = 0;
            double meanGyroZ = 0;

            reader.enableGyroCompensation(false);

            for (int i = 0; i < caliProbes; i++) {
                final AccGyroData reading = reader.getFilteredReading();

                meanGyroX += reading.getGyroX();
                meanGyroY += reading.getGyroY();
                meanGyroZ += reading.getGyroZ();
                Thread.sleep(DT_MS);
            }
            meanGyroX /= caliProbes;
            meanGyroY /= caliProbes;
            meanGyroZ /= caliProbes;
            saveCompensation(meanGyroX, meanGyroY, meanGyroZ);

        } catch (InterruptedException | ImuFilteredReaderException | IOException e) {
            throw new CalibrationManagerException(e.getMessage(), e);
        } finally {
            reader.enableGyroCompensation(true);
            try {
                reader.reloadCompensation();
            } catch (ImuFilteredReaderException e) {
                LOGGER.warn("Unable to reload compensation. " + e.getMessage());
            }
        }
    }

    private void saveCompensation(double gyroX, double gyroY, double gyroZ) throws IOException {
        final File file = new File(compensationFile);
        if (!file.exists()) {
            file.createNewFile();
        }
        final StringBuilder builder = new StringBuilder(18);
        final String newline = System.getProperty("line.separator");
        builder.append("gyroX=").append(String.valueOf(gyroX)).append(newline)
                .append("gyroY=").append(String.valueOf(gyroY)).append(newline)
                .append("gyroZ=").append(String.valueOf(gyroZ));

        final FileOutputStream fos = new FileOutputStream(compensationFile);
        try {
            fos.write(builder.toString().getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            fos.close();
        }
        fos.close();
    }
}
