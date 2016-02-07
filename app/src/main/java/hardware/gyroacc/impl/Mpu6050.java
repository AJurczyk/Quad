package hardware.gyroacc.impl;

import hardware.gyroacc.IGyroAcc;
import hardware.gyroacc.enums.Axis;
import hardware.gyroacc.enums.Mpu6050Conf;
import hardware.gyroacc.enums.Mpu6050Reg;
import hardware.gyroacc.exception.AccGyroIncorrectAxisException;
import hardware.gyroacc.exception.AccGyroReadValueException;
import hardware.i2c.II2cController;
import hardware.i2c.exception.I2cDeviceNotInitializedException;
import hardware.i2c.exception.I2cInitException;
import hardware.i2c.exception.I2cReadException;
import hardware.i2c.exception.I2cWriteException;

/**
 * MPU6050 gyro and acc basic implementation.
 *
 * @author aleksander.jurczyk@gmail.com on 26.01.16.
 */
public class Mpu6050 implements IGyroAcc {

    private static final int I2C_ADDRESS = 0x68;

    private final II2cController i2c;

    /**
     * Main constructor that initializes i2c device.
     *
     * @param ii2cController i2c controller
     * @throws I2cInitException                 error while getting i2c bus
     * @throws I2cDeviceNotInitializedException run initI2cDevice first
     * @throws I2cWriteException                sleepEnable throws this
     */
    public Mpu6050(II2cController ii2cController) throws I2cInitException, I2cDeviceNotInitializedException,
        I2cWriteException {
        i2c = ii2cController;
        i2c.initI2cDevice(I2C_ADDRESS);
        writeConfig();
    }

    @Override
    public int readAcc(Axis axis) throws AccGyroReadValueException, AccGyroIncorrectAxisException {
        try {
            switch (axis) {
                case X: {
                    final short rawValue = i2c.readTwoBytes(Mpu6050Reg.ACCEL_XOUT_L, Mpu6050Reg.ACCEL_XOUT_H);
                    return convertRawAccToG(rawValue);
                }
                case Y: {
                    final short rawValue = i2c.readTwoBytes(Mpu6050Reg.ACCEL_YOUT_L, Mpu6050Reg.ACCEL_YOUT_H);
                    return convertRawAccToG(rawValue);
                }
                case Z: {
                    final short rawValue = i2c.readTwoBytes(Mpu6050Reg.ACCEL_ZOUT_L, Mpu6050Reg.ACCEL_ZOUT_H);
                    return convertRawAccToG(rawValue);
                }
                default: {
                    throw new AccGyroIncorrectAxisException("Unhandled axis");
                }
            }
        } catch (I2cDeviceNotInitializedException | I2cReadException e) {
            throw new AccGyroReadValueException("Error while reading acc.", e);
        }
    }

    @Override
    public int readGyro(Axis axis) throws AccGyroReadValueException, AccGyroIncorrectAxisException {
        try {
            switch (axis) {
                case X: {
                    final short rawValue = i2c.readTwoBytes(Mpu6050Reg.GYRO_XOUT_L, Mpu6050Reg.GYRO_XOUT_H);
                    return convertRawGyroToAngle(rawValue);
                }
                case Y: {
                    final short rawValue = i2c.readTwoBytes(Mpu6050Reg.GYRO_YOUT_L, Mpu6050Reg.GYRO_YOUT_H);
                    return convertRawGyroToAngle(rawValue);
                }
                case Z: {
                    final short rawValue = i2c.readTwoBytes(Mpu6050Reg.GYRO_ZOUT_L, Mpu6050Reg.GYRO_ZOUT_H);
                    return convertRawGyroToAngle(rawValue);
                }
                default: {
                    throw new AccGyroIncorrectAxisException("Unhandled axis");
                }
            }
        } catch (I2cDeviceNotInitializedException | I2cReadException e) {
            throw new AccGyroReadValueException("Error while reading gyro.", e);
        }
    }

    @Override
    public int readAngle(Axis axis) {
        return 0;
    }

    private int convertRawAccToG(short rawvalue) {
        return (int) rawvalue;
        //TODO: to implement;
    }

    private int convertRawGyroToAngle(short rawValue) {
        //TODO to implement
        return (int) rawValue;
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
