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
    @Override
    public double readAccInG(Axis axis) throws AccGyroReadValueException, AccGyroIncorrectAxisException {
        double rangeMin = 0;
        double rangeMax = 0.5;
        Random r = new Random();
        return rangeMin + (rangeMax - rangeMin) * r.nextDouble();
    }

    @Override
    public double readGyroDeg(Axis axis) throws AccGyroReadValueException, AccGyroIncorrectAxisException {
        return 0;
    }

    @Override
    public AccGyroReadOut readAll() throws AccGyroIncorrectAxisException, AccGyroReadValueException {
        return new AccGyroReadOut(
            readAccInG(Axis.X),
            readAccInG(Axis.Y),
            readAccInG(Axis.Z),
            readGyroDeg(Axis.X),
            readGyroDeg(Axis.Y),
            readGyroDeg(Axis.Z));
    }

}
