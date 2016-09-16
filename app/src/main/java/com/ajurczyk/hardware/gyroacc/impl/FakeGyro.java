package com.ajurczyk.hardware.gyroacc.impl;

import com.ajurczyk.hardware.gyroacc.IGyroAcc;
import com.ajurczyk.hardware.gyroacc.enums.Axis;
import com.ajurczyk.hardware.gyroacc.exception.AccGyroIncorrectAxisException;
import com.ajurczyk.hardware.gyroacc.exception.AccGyroReadValueException;

import java.util.Random;

/**
 * @author aleksander.jurczyk@seedlabs.io
 */
public class FakeGyro implements IGyroAcc {

    private static final float RANGE_MIN = 0f;
    private static final float RANGE_MAX = 0.5f;

    @Override
    public float readAccInG(Axis axis) throws AccGyroReadValueException, AccGyroIncorrectAxisException {
        final Random random = new Random();
        return RANGE_MIN + (RANGE_MAX - RANGE_MIN) * random.nextFloat();
    }

    @Override
    public float readGyroDeg(Axis axis) throws AccGyroReadValueException, AccGyroIncorrectAxisException {
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
