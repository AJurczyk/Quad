package hardware.i2c.impl;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;
import hardware.i2c.II2cController;
import hardware.i2c.exception.I2cDeviceNotInitializedException;
import hardware.i2c.exception.I2cInitException;
import hardware.i2c.exception.I2cReadException;
import hardware.i2c.exception.I2cWriteException;

import java.io.IOException;

/**
 * Implementation of i2c device communication using PI4J library.
 *
 * @author aleksander.jurczyk@gmail.com on 30.01.16.
 */
public class Pi4jI2c implements II2cController {

    private static final I2CBus BUS;

    private I2CDevice device;

    static {
        I2CBus busTmp = null;
        try {
            busTmp = I2CFactory.getInstance(I2CBus.BUS_1);
        } catch (IOException e) {
            System.out.println("Error while init i2c bus. " + e.getCause());
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
            throw new I2cInitException("I2C bus hasn't been set");
        }
        try {
            device = BUS.getDevice(address);
        } catch (IOException e) {
            throw new I2cInitException("Error while getting i2c device", e);
        }
    }

    @Override
    public int read(int register) throws I2cReadException, I2cDeviceNotInitializedException {
        if (device == null) {
            throw new I2cDeviceNotInitializedException("Error while reading register " + register
                    + ". I2C Device has not been initialized");
        }
        try {
            return device.read(register);
        } catch (IOException e) {
            throw new I2cReadException("Can't read i2c register " + register, e);
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
