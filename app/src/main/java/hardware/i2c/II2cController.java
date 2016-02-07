package hardware.i2c;

import hardware.i2c.exception.I2cDeviceNotInitializedException;
import hardware.i2c.exception.I2cInitException;
import hardware.i2c.exception.I2cReadException;
import hardware.i2c.exception.I2cWriteException;

/**
 * Allows to read/write values to specific I2C device.
 *
 * @author aleksander.jurczyk@gmail.com on 30.01.16.
 */
public interface II2cController {

    /**
     * Takes a device with specific address from the bus.
     *
     * @param address device i2c address
     * @throws I2cInitException initialization fail
     */
    void initI2cDevice(int address) throws I2cInitException;

    /**
     * Read a single value from i2c device.
     *
     * @param register register of value to read
     * @return value read
     */
    byte read(int register) throws I2cReadException, I2cDeviceNotInitializedException;

    /**
     * Write to i2c device.
     *
     * @param register register to which write
     * @param value    value to write
     */
    void write(byte register, byte value) throws I2cWriteException, I2cDeviceNotInitializedException;
}
