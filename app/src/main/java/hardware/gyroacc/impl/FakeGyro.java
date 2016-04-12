package hardware.gyroacc.impl;

import hardware.gyroacc.IGyroAcc;
import hardware.gyroacc.enums.Axis;
import hardware.gyroacc.exception.AccGyroIncorrectAxisException;
import hardware.gyroacc.exception.AccGyroReadValueException;

import java.util.Random;

/**
 * @author aleksander.jurczyk@seedlabs.io
 */
public class FakeGyro implements IGyroAcc {

    private static final double RANGE_MIN = 0;
    private static final double RANGE_MAX = 0.5;

    @Override
    public double readAccInG(Axis axis) throws AccGyroReadValueException, AccGyroIncorrectAxisException {
        final Random random = new Random();
        return RANGE_MIN + (RANGE_MAX - RANGE_MIN) * random.nextDouble();
    }

    @Override
    public double readGyroDeg(Axis axis) throws AccGyroReadValueException, AccGyroIncorrectAxisException {
        return 0;
    }

    @Override
    public AccGyroData readAll() throws AccGyroIncorrectAxisException, AccGyroReadValueException {
        return new AccGyroData(
            readAccInG(Axis.X),
            readAccInG(Axis.Y),
            readAccInG(Axis.Z),
            readGyroDeg(Axis.X),
            readGyroDeg(Axis.Y),
            readGyroDeg(Axis.Z));
    }

}
