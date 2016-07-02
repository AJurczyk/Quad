package com.ajurczyk.hardware.gyroacc.impl;

import com.ajurczyk.hardware.gyroacc.IGyroAcc;
import com.ajurczyk.hardware.gyroacc.enums.Axis;
import com.ajurczyk.hardware.gyroacc.enums.Mpu6050Conf;
import com.ajurczyk.hardware.gyroacc.enums.Mpu6050Reg;
import com.ajurczyk.hardware.gyroacc.exception.AccGyroIncorrectAxisException;
import com.ajurczyk.hardware.gyroacc.exception.AccGyroReadValueException;
import com.ajurczyk.hardware.gyroacc.exception.InvalidConversionFactor;
import com.ajurczyk.hardware.i2c.II2cController;
import com.ajurczyk.hardware.i2c.exception.I2cDeviceNotInitializedException;
import com.ajurczyk.hardware.i2c.exception.I2cInitException;
import com.ajurczyk.hardware.i2c.exception.I2cReadException;
import com.ajurczyk.hardware.i2c.exception.I2cWriteException;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * MPU6050 gyro and acc basic implementation.
 *
 * @author aleksander.jurczyk@gmail.com on 26.01.16.
 */
public class Mpu6050 implements IGyroAcc {

    private static final int I2C_ADDRESS = 0x68;

    @Autowired
    private final II2cController i2c;

    private final double accRaw2Gfactor;

    private final double gyroRaw2DegFactor;

    /**
     * Main constructor that initializes i2c device.
     *
     * @param ii2cController i2c controller
     * @throws I2cInitException                 error while getting i2c bus
     * @throws I2cDeviceNotInitializedException run initI2cDevice first
     * @throws I2cWriteException                sleepEnable throws this
     * @throws InvalidConversionFactor          raw won't be calculated to given unit
     */
    public Mpu6050(II2cController ii2cController) throws I2cInitException, I2cDeviceNotInitializedException,
            I2cWriteException, InvalidConversionFactor {
        i2c = ii2cController;
        i2c.initI2cDevice(I2C_ADDRESS);
        writeConfig();
        accRaw2Gfactor = calcConversionFactor(getAccSensitivityInG());
        gyroRaw2DegFactor = calcConversionFactor(getGyroSensitivity());
    }

    @Override
    public double readAccInG(Axis axis) throws AccGyroIncorrectAxisException, AccGyroReadValueException {
        return readAccRaw(axis) * accRaw2Gfactor;
    }

    @Override
    public double readGyroDeg(Axis axis) throws AccGyroReadValueException, AccGyroIncorrectAxisException {
        return readGyroRaw(axis) * gyroRaw2DegFactor;
    }

    @Override
    public AccGyroData readAll() throws AccGyroIncorrectAxisException, AccGyroReadValueException {
        return new AccGyroData(readAccInG(Axis.X),
                readAccInG(Axis.Y),
                readAccInG(Axis.Z),
                readGyroDeg(Axis.X),
                readGyroDeg(Axis.Y),
                readGyroDeg(Axis.Z));
    }

    private short readAccRaw(Axis axis) throws AccGyroReadValueException, AccGyroIncorrectAxisException {
        try {
            switch (axis) {
                case X: {
                    return i2c.readTwoBytes(Mpu6050Reg.ACCEL_XOUT_L, Mpu6050Reg.ACCEL_XOUT_H);
                }
                case Y: {
                    return i2c.readTwoBytes(Mpu6050Reg.ACCEL_YOUT_L, Mpu6050Reg.ACCEL_YOUT_H);
                }
                case Z: {
                    return i2c.readTwoBytes(Mpu6050Reg.ACCEL_ZOUT_L, Mpu6050Reg.ACCEL_ZOUT_H);
                }
                default: {
                    throw new AccGyroIncorrectAxisException("Unhandled axis");
                }
            }
        } catch (I2cDeviceNotInitializedException | I2cReadException e) {
            throw new AccGyroReadValueException("Error while reading acc.", e);
        }
    }

    private short readGyroRaw(Axis axis) throws AccGyroReadValueException, AccGyroIncorrectAxisException {
        try {
            switch (axis) {
                case X: {
                    return i2c.readTwoBytes(Mpu6050Reg.GYRO_XOUT_L, Mpu6050Reg.GYRO_XOUT_H);
                }
                case Y: {
                    return i2c.readTwoBytes(Mpu6050Reg.GYRO_YOUT_L, Mpu6050Reg.GYRO_YOUT_H);
                }
                case Z: {
                    return i2c.readTwoBytes(Mpu6050Reg.GYRO_ZOUT_L, Mpu6050Reg.GYRO_ZOUT_H);
                }
                default: {
                    throw new AccGyroIncorrectAxisException("Unhandled axis");
                }
            }
        } catch (I2cDeviceNotInitializedException | I2cReadException e) {
            throw new AccGyroReadValueException("Error while reading gyro.", e);
        }
    }

    private double calcConversionFactor(int maxFinalValue) throws InvalidConversionFactor {
        final int maxRaw = (int) 0xFFFF / 2;
        final double factor = (double) maxFinalValue / (double) maxRaw;
        if (factor <= 0) {
            throw new InvalidConversionFactor("factor cant be less or equall than 0. Is: " + factor);
        }
        return factor;
    }

    private int getAccSensitivityInG() {
        final int setting = Mpu6050Conf.ACCEL_CONFIG >> 3;
        return (int) Math.pow(2, setting + 1);
    }

    private int getGyroSensitivity() {
        final int setting = Mpu6050Conf.GYRO_CONFIG >> 3;
        return (int) ((double) 250 * Math.pow(2, setting));
    }

    private void writeConfig() throws I2cDeviceNotInitializedException, I2cWriteException {
        //1 Waking the device up
        i2c.write(Mpu6050Reg.PWR_MGMT_1,
                Mpu6050Conf.PWR_MGMT_1);

        //2 Configure sample rate
        i2c.write(Mpu6050Reg.SMPLRT_DIV,
                Mpu6050Conf.SMPLRT_DIV);

        //3 Setting global config
        i2c.write(Mpu6050Reg.CONFIG,
                Mpu6050Conf.CONFIG);

        //4 Configure Gyroscope
        i2c.write(Mpu6050Reg.GYRO_CONFIG,
                Mpu6050Conf.GYRO_CONFIG);

        //5 Configure Accelerometer
        i2c.write(Mpu6050Reg.ACCEL_CONFIG,
                Mpu6050Conf.ACCEL_CONFIG);

        //6 Configure interrupts
        i2c.write(Mpu6050Reg.INT_ENABLE,
                Mpu6050Conf.INT_ENABLE);

        //7 Configure low power operations
        i2c.write(Mpu6050Reg.PWR_MGMT_2,
                Mpu6050Conf.PWR_MGMT_2);
    }
}
