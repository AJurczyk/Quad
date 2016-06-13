package com.ajurczyk.hardware.i2c.impl;

import com.ajurczyk.hardware.i2c.II2cController;
import com.ajurczyk.hardware.i2c.exception.I2cDeviceNotInitializedException;
import com.ajurczyk.hardware.i2c.exception.I2cInitException;
import com.ajurczyk.hardware.i2c.exception.I2cReadException;
import com.ajurczyk.hardware.i2c.exception.I2cWriteException;
import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.ajurczyk.utils.ByteArrayUtils;
import com.ajurczyk.utils.exceptions.InvalidArgException;

import java.io.IOException;

/**
 * Implementation of i2c device communication using PI4J library.
 *
 * @author aleksander.jurczyk@gmail.com on 30.01.16.
 */
public class Pi4jI2c implements II2cController {

    private static final Logger LOGGER = LoggerFactory.getLogger(Pi4jI2c.class);

    private I2CDevice device;

    private static final I2CBus BUS;

    static {
        I2CBus busTmp = null;
        try {
            busTmp = I2CFactory.getInstance(I2CBus.BUS_1);
        } catch (IOException e) {
            LOGGER.error("Error while init i2c bus. " + e.getCause());
        }
        BUS = busTmp;
    }


    /**
     * Takes a device with specific address from the BUS.
     *
     * @param address i2c address of the device
     */
    public void initI2cDevice(int address) throws I2cInitException {
        if (BUS == null) {
            throw new I2cInitException("I2C bus hasn't been set.");
        }
        try {
            device = BUS.getDevice(address);
            LOGGER.debug("I2C init successefull.");
        } catch (IOException e) {
            throw new I2cInitException("Error while getting i2c device.", e);
        }
    }

    @Override
    public byte read(int register) throws I2cReadException, I2cDeviceNotInitializedException {
        if (device == null) {
            throw new I2cDeviceNotInitializedException("Error while reading register " + register
                + ". I2C Device has not been initialized");
        }
        try {
            final byte value = (byte) device.read(register);
            LOGGER.debug("I2C Read register " + register + ". Value: " + value + ".");
            return value;
        } catch (IOException e) {
            throw new I2cReadException("Can't read i2c register " + register, e);
        }
    }

    @Override
    public short readTwoBytes(byte lsbReg, byte msbReg) throws I2cReadException, I2cDeviceNotInitializedException {
        if (device == null) {
            throw new I2cDeviceNotInitializedException("Error while reading register " + lsbReg + " and " + msbReg
                + ". I2C Device has not been initialized");
        }
        try {
            byte[] value = new byte[2];
            value[0] = read(msbReg);
            value[1] = read(lsbReg);
            final short valueShort = ByteArrayUtils.castToShort(value);
            LOGGER.debug("I2C Read 2 bytes: " + msbReg + ", " + lsbReg + ". Value: " + valueShort + ".");
            return valueShort;

        } catch (I2cDeviceNotInitializedException | I2cReadException | InvalidArgException e) {
            throw new I2cReadException("Can't read i2c register " + lsbReg + " or " + msbReg, e);
        }
    }

    @Override
    public void write(byte register, byte value) throws I2cWriteException, I2cDeviceNotInitializedException {
        if (device == null) {
            throw new I2cDeviceNotInitializedException("Error while writing to register " + register
                + ". I2C Device has not been initialized");
        }
        try {
            device.write(register, value);
        } catch (IOException e) {
            throw new I2cWriteException("Can't write to i2c register " + register, e);
        }
    }
}
