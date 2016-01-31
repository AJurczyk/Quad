package hardware.gyroacc.impl;

import hardware.gyroacc.IGyroAcc;
import hardware.gyroacc.enums.Axis;
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
    private static final byte SLEEP_ENABLE_VAL = 0x00;
    private static final byte SLEEP_DISABLE_VAL = 0x40; //TODO: check if this is disable value
    private static final byte SLEEP_REGISTRY = 0x6b;

    private static final byte ACC_X = 0x3B;
    private static final byte ACC_Y = 0x3D;
    private static final byte ACC_Z = 0x3F;

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
        sleepEnabled(false);
    }

    @Override
    public int readAcc(Axis axis) throws AccGyroReadValueException, AccGyroUnhandledAxisException {
        switch (axis) {
            case X: {
                return readAccFromReg(ACC_X);
            }
            case Y: {
                return readAccFromReg(ACC_Y);
            }
            case Z: {
                return readAccFromReg(ACC_Z);
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


    private int readAccFromReg(byte register) throws AccGyroReadValueException {
        int rawValue = 0;
        try {
            rawValue = i2c.read(register);
        } catch (I2cReadException | I2cDeviceNotInitializedException e) {
            throw new AccGyroReadValueException("Error while reading acceleration.", e);
        }
        return convertRawAccToPerc(rawValue);
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

    private void sleepEnabled(boolean state) throws I2cDeviceNotInitializedException, I2cWriteException {
        if (state) {
            i2c.write(SLEEP_REGISTRY, SLEEP_ENABLE_VAL);
        } else {
            i2c.write(SLEEP_REGISTRY, SLEEP_DISABLE_VAL);
        }
    }
}
