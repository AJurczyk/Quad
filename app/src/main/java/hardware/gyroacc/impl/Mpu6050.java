package hardware.gyroacc.impl;

import hardware.gyroacc.IGyroAcc;
import hardware.gyroacc.enums.Axis;
import hardware.gyroacc.enums.Mpu6050Config;
import hardware.gyroacc.enums.Mpu6050Registers;
import hardware.gyroacc.exception.AccGyroReadValueException;
import hardware.gyroacc.exception.AccGyroUnhandledAxisException;
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

    private static final byte ACC_X_L = 0x3B;
    private static final byte ACC_X_H = 0x3C;

    private static final byte ACC_Y_L = 0x3D;
    private static final byte ACC_Y_H = 0x3E;

    private static final byte ACC_Z_L = 0x3F;
    private static final byte ACC_Z_H = 0x40;

    private static final byte ROT_X = 0x00; //TODO get the address
    private static final byte ROT_Y = 0x00; //TODO get the address
    private static final byte ROT_Z = 0x00; //TODO get the address

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
    public int readAcc(Axis axis) throws AccGyroReadValueException, AccGyroUnhandledAxisException {
        switch (axis) {
            case X: {
                return readAccFromReg(ACC_X_L, ACC_X_H);
            }
            case Y: {
                return readAccFromReg(ACC_Y_L, ACC_Y_H);
            }
            case Z: {
                return readAccFromReg(ACC_Z_L, ACC_Z_H);
            }
            default: {
                throw new AccGyroUnhandledAxisException("Unhandled axis");
            }
        }
    }

    @Override
    public int readRot(Axis axis) throws AccGyroReadValueException, AccGyroUnhandledAxisException {
        switch (axis) {
            case X: {
                return readRotFromReg(ROT_X);
            }
            case Y: {
                return readRotFromReg(ROT_Y);
            }
            case Z: {
                return readRotFromReg(ROT_Z);
            }
            default: {
                throw new AccGyroUnhandledAxisException("Unhandled axis");
            }
        }
    }


    private int readAccFromReg(byte lsb, byte hsb) throws AccGyroReadValueException {
        short rawValue = 0;
        try {
            byte lsbValue = i2c.read(lsb);
            byte hsbValue = i2c.read(hsb);
            rawValue = mergeIntoShort(lsbValue, hsbValue);

        } catch (I2cReadException | I2cDeviceNotInitializedException e) {
            throw new AccGyroReadValueException("Error while reading acceleration.", e);
        }
        return convertRawAccToPerc(rawValue);
    }

    private short mergeIntoShort(byte lsb, byte hsb) {
        return (short) ((lsb << 8) | (hsb & 0xFF));
    }

    private int readRotFromReg(byte register) throws AccGyroReadValueException {
        int rawValue = 0;
        try {
            rawValue = i2c.read(register);
        } catch (I2cReadException | I2cDeviceNotInitializedException e) {
            throw new AccGyroReadValueException("Error while reading rotation.", e);
        }
        return convertRawRotToAngle(rawValue);
    }

    private int convertRawAccToPerc(int rawvalue) {
        return rawvalue;
        //TODO: to implement;
    }

    private int convertRawRotToAngle(int rawvalue) {
        return rawvalue;
        //TODO: to implement;
    }

    private void writeConfig() throws I2cDeviceNotInitializedException, I2cWriteException {
        //1 Waking the device up
        i2c.write(Mpu6050Registers.MPU6050_RA_PWR_MGMT_1,
                Mpu6050Config.MPU6050_RA_PWR_MGMT_1);

        //2 Configure sample rate
        i2c.write(Mpu6050Registers.MPU6050_RA_SMPLRT_DIV,
                Mpu6050Config.MPU6050_RA_SMPLRT_DIV);

        //3 Setting global config
        i2c.write(Mpu6050Registers.MPU6050_RA_CONFIG,
                Mpu6050Config.MPU6050_RA_CONFIG);

        //4 Configure Gyroscope
        i2c.write(Mpu6050Registers.MPU6050_RA_GYRO_CONFIG,
                Mpu6050Config.MPU6050_RA_GYRO_CONFIG);

        //5 Configure Accelerometer
        i2c.write(Mpu6050Registers.MPU6050_RA_ACCEL_CONFIG,
                Mpu6050Config.MPU6050_RA_ACCEL_CONFIG);

        //6 Configure interrupts
        i2c.write(Mpu6050Registers.MPU6050_RA_INT_ENABLE,
                Mpu6050Config.MPU6050_RA_INT_ENABLE);

        //7 Configure low power operations
        i2c.write(Mpu6050Registers.MPU6050_RA_PWR_MGMT_2,
                Mpu6050Config.MPU6050_RA_PWR_MGMT_2);
    }
}
