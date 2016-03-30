package hardware.gyroacc;

import hardware.gyroacc.enums.Axis;
import hardware.gyroacc.exception.AccGyroIncorrectAxisException;
import hardware.gyroacc.exception.AccGyroReadValueException;
import hardware.gyroacc.impl.AccGyroReadOut;

/**
 * @author aleksander.jurczyk@gmail.com on 26.01.16.
 */
public interface IGyroAcc {

    /**
     * Read acceleration value in G of given axis.
     *
     * @param axis axis to be read
     * @return acceleration value measured in [G] unit
     * @throws AccGyroReadValueException     communication problems
     * @throws AccGyroIncorrectAxisException invalid axis
     */
    double readAccInG(Axis axis) throws AccGyroReadValueException, AccGyroIncorrectAxisException;

    /**
     * Read gyroscope in degrees of given axis.
     * @param axis axis to be read
     * @return gyroscope value measured in degrees
     * @throws AccGyroReadValueException communication problems
     * @throws AccGyroIncorrectAxisException invalid axis
     */
    double readGyroDeg(Axis axis) throws AccGyroReadValueException, AccGyroIncorrectAxisException;

    /**
     * Read gyro [deg] and acc [G] on all 3 axis.
     * @return object containing all measurements.
     */
    AccGyroReadOut readAll() throws AccGyroIncorrectAxisException, AccGyroReadValueException;
}
