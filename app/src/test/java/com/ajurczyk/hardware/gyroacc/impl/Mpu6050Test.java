package com.ajurczyk.hardware.gyroacc.impl;

import com.ajurczyk.hardware.gyroacc.enums.Axis;
import com.ajurczyk.hardware.gyroacc.exception.AccGyroIncorrectAxisException;
import com.ajurczyk.hardware.gyroacc.exception.AccGyroReadValueException;
import com.ajurczyk.hardware.gyroacc.exception.InvalidConversionFactor;
import com.ajurczyk.hardware.i2c.II2cController;
import com.ajurczyk.hardware.i2c.exception.I2cDeviceNotInitializedException;
import com.ajurczyk.hardware.i2c.exception.I2cInitException;
import com.ajurczyk.hardware.i2c.exception.I2cReadException;
import com.ajurczyk.hardware.i2c.exception.I2cWriteException;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.mockito.Mockito.*;

/**
 * @author aleksander.jurczyk@gmail.com on 31.01.16.
 */
@Test(groups = {"unitTests"})
public class Mpu6050Test {

    private static final int I2C_ADDRESS = 0x68;

    private static final byte ACC_X_LSB_ADDR = 60;
    private static final byte ACC_X_MSB_ADDR = 59;

    private static final byte ACC_Y_LSB_ADDR = 62;
    private static final byte ACC_Y_MSB_ADDR = 61;

    private static final byte ACC_Z_LSB_ADDR = 64;
    private static final byte ACC_Z_MSB_ADDR = 63;
    private static final byte ACC_SENSITIVITY = 4;

    private static final byte GYRO_X_LSB_ADDR = 68;
    private static final byte GYRO_X_MSB_ADDR = 67;

    private static final byte GYRO_Y_LSB_ADDR = 70;
    private static final byte GYRO_Y_MSB_ADDR = 69;

    private static final byte GYRO_Z_LSB_ADDR = 72;
    private static final byte GYRO_Z_MSB_ADDR = 71;
    private static final int GYRO_SENSITIVITY = 2000;

    /**
     * Data provider for accelerometer test.
     *
     * @return input and expected data
     */
    @DataProvider(name = "accData")
    public static Object[][] accData() {
        return new Object[][]{
            {(short) 32767, 1 * ACC_SENSITIVITY},
            {(short) 0, 0 * ACC_SENSITIVITY},
            {(short) 7836, 0.9565721750259399},
            {(short) -32767, -1 * ACC_SENSITIVITY}
        };
    }

    /**
     * Data provider for gyroscope test.
     *
     * @return input and expected data
     */
    @DataProvider(name = "gyroData")
    public static Object[][] gyroData() {
        return new Object[][]{
            {(short) 32767, 1 * GYRO_SENSITIVITY},
            {(short) 0, 0 * GYRO_SENSITIVITY},
            {(short) -73, -4.455702304840088},
            {(short) -32767, -1 * GYRO_SENSITIVITY}
        };
    }

    /**
     * Read acceleration X value and confirm conversion.
     */
    @Test(dataProvider = "accData")
    public final void readAccX(short rawValue, double expectedAccInG) throws I2cReadException, I2cDeviceNotInitializedException,
        I2cInitException, InvalidConversionFactor, I2cWriteException, AccGyroIncorrectAxisException, AccGyroReadValueException {
        //given
        final II2cController i2c = mock(II2cController.class);
        when(i2c.readTwoBytes(ACC_X_LSB_ADDR, ACC_X_MSB_ADDR)).thenReturn((short) rawValue);

        final Mpu6050 mpu6050 = new Mpu6050(i2c);

        //when
        final double accX = mpu6050.readAccInG(Axis.X);

        //then
        verify(i2c, times(1)).initI2cDevice(I2C_ADDRESS);
        verify(i2c, times(1)).readTwoBytes(ACC_X_LSB_ADDR, ACC_X_MSB_ADDR);
        Assert.assertEquals(accX, expectedAccInG);
    }

    /**
     * Read acceleration Y value.
     */
    @Test
    public final void readAccY() throws InvalidConversionFactor, I2cInitException, I2cWriteException,
        I2cDeviceNotInitializedException, AccGyroIncorrectAxisException, AccGyroReadValueException,
        I2cReadException {
        //given
        final II2cController i2c = mock(II2cController.class);

        final Mpu6050 mpu6050 = new Mpu6050(i2c);

        //when
        mpu6050.readAccInG(Axis.Y);

        //then
        verify(i2c, times(1)).initI2cDevice(I2C_ADDRESS);
        verify(i2c, times(1)).readTwoBytes(ACC_Y_LSB_ADDR, ACC_Y_MSB_ADDR);
    }

    /**
     * Read acceleration Z value.
     */
    @Test
    public final void readAccZ() throws InvalidConversionFactor, I2cInitException, I2cWriteException,
        I2cDeviceNotInitializedException, AccGyroIncorrectAxisException, AccGyroReadValueException,
        I2cReadException {
        //given
        final II2cController i2c = mock(II2cController.class);

        final Mpu6050 mpu6050 = new Mpu6050(i2c);

        //when
        mpu6050.readAccInG(Axis.Z);

        //then
        verify(i2c, times(1)).initI2cDevice(I2C_ADDRESS);
        verify(i2c, times(1)).readTwoBytes(ACC_Z_LSB_ADDR, ACC_Z_MSB_ADDR);
    }

    /**
     * Read acceleration X value and confirm conversion.
     */
    @Test(dataProvider = "gyroData")
    public final void readGyroX(short rawValue, double expectedAccInG) throws I2cReadException, I2cDeviceNotInitializedException,
        AccGyroIncorrectAxisException, AccGyroReadValueException, I2cInitException, InvalidConversionFactor, I2cWriteException {
        //given
        final II2cController i2c = mock(II2cController.class);
        when(i2c.readTwoBytes(GYRO_X_LSB_ADDR, GYRO_X_MSB_ADDR)).thenReturn((short) rawValue);

        final Mpu6050 mpu6050 = new Mpu6050(i2c);

        //when
        final double gyroX = mpu6050.readGyroDeg(Axis.X);

        //then
        verify(i2c, times(1)).initI2cDevice(I2C_ADDRESS);
        verify(i2c, times(1)).readTwoBytes(GYRO_X_LSB_ADDR, GYRO_X_MSB_ADDR);
        Assert.assertEquals(gyroX, expectedAccInG);
    }

    /**
     * Read gyro Y value.
     */
    @Test
    public final void readGyroY() throws InvalidConversionFactor, I2cInitException, I2cWriteException,
        I2cDeviceNotInitializedException, AccGyroIncorrectAxisException, AccGyroReadValueException, I2cReadException {
        //given
        final II2cController i2c = mock(II2cController.class);

        final Mpu6050 mpu6050 = new Mpu6050(i2c);

        //when
        mpu6050.readGyroDeg(Axis.Y);

        //then
        verify(i2c, times(1)).initI2cDevice(I2C_ADDRESS);
        verify(i2c, times(1)).readTwoBytes(GYRO_Y_LSB_ADDR, GYRO_Y_MSB_ADDR);
    }

    /**
     * Read gyro Z value.
     */
    @Test
    public final void readGyroZ() throws InvalidConversionFactor, I2cInitException, I2cWriteException,
        I2cDeviceNotInitializedException, AccGyroIncorrectAxisException, AccGyroReadValueException, I2cReadException {
        //given
        final II2cController i2c = mock(II2cController.class);

        final Mpu6050 mpu6050 = new Mpu6050(i2c);

        //when
        mpu6050.readGyroDeg(Axis.Z);

        //then
        verify(i2c, times(1)).initI2cDevice(I2C_ADDRESS);
        verify(i2c, times(1)).readTwoBytes(GYRO_Z_LSB_ADDR, GYRO_Z_MSB_ADDR);
    }
}
