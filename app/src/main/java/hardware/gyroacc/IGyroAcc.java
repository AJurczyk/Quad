package hardware.gyroacc;

import hardware.gyroacc.enums.Axis;
import hardware.gyroacc.exception.AccGyroIncorrectAxisException;
import hardware.gyroacc.exception.AccGyroReadValueException;

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

    double readGyroDeg(Axis axis) throws AccGyroReadValueException, AccGyroIncorrectAxisException;

    double readAngle(Axis axis) throws AccGyroIncorrectAxisException, AccGyroReadValueException;
}
