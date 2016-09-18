package com.ajurczyk.software.regulators.impl;

import com.ajurczyk.software.regulators.IRegulator;

/**
 * Regulator proportional.
 *
 * @author aleksander.jurczyk@gmail.com on 16.09.16.
 */
public class RegulatorP implements IRegulator {

    private float proportional;

    @Override
    public float getRegulation(float currentValue, float desiredValue) {
        final float error = desiredValue - currentValue;
        return proportional * error;
    }

    @Override
    public void setProportional(float factor) {
        this.proportional = factor;
    }

    @Override
    public void setIntegral(float factor) {
        //do nothing
    }

    @Override
    public void setDerivative(float factor) {
        //do nothing
    }
}
