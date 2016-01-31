package hardware.gyroacc;

import hardware.gyroacc.enums.Axis;
import hardware.gyroacc.exception.AccGyroReadValueException;
import hardware.gyroacc.exception.AccGyroUnhandledAxisException;
import hardware.i2c.exception.I2cReadException;

/**
 * @author aleksander.jurczyk@gmail.com on 26.01.16.
 */
public interface IGyroAcc {

    int readAcc(Axis axis) throws I2cReadException, AccGyroReadValueException, AccGyroUnhandledAxisException;

    int readRot(Axis axis) throws I2cReadException, AccGyroReadValueException, AccGyroUnhandledAxisException;
}
