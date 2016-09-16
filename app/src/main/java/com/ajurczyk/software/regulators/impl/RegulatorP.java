package com.ajurczyk.software.regulators.impl;

import com.ajurczyk.software.regulators.Iregulator;

/**
 * Regulator P.
 *
 * @author aleksander.jurczyk@gmail.com on 16.09.16.
 */
public class RegulatorP implements Iregulator {

    private float pFactor;

    @Override
    public float getRegulation(float currentValue, float desiredValue) {
        float error = desiredValue - currentValue;
        return pFactor * error;
    }

    @Override
    public void setP(float factor) {
        this.pFactor = factor;
    }

    @Override
    public void setI(float factor) {
        //do nothing
    }

    @Override
    public void setD(float factor) {
        //do nothing
    }
}
