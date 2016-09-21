package com.ajurczyk.software.regulators.impl;

import com.ajurczyk.software.regulators.IRegulator;

import java.util.Locale;

/**
 * @author aleksander.jurczyk@gmail.com on 18.09.16.
 */
public class RegulatorPid implements IRegulator {
    private float lastError;
    private float proportional;
    private float integral;
    private float derivative;
    private float errSum;
    private long lastTime = System.currentTimeMillis();

    private static final String DECIMAL_FORMAT = "%.2f";

    @Override
    public void setProportional(float proportional) {
        this.proportional = proportional;
    }

    @Override
    public void setIntegral(float integral) {
        this.integral = integral;
    }

    @Override
    public void setDerivative(float derivative) {
        this.derivative = derivative;
    }

    @Override
    public float getRegulation(float currentValue, float desiredValue) {
        return compute(desiredValue, currentValue);
    }

    @SuppressWarnings("PMD.SystemPrintln")
    private float compute(float desiredVal, float currentVal) {
        final long now = System.currentTimeMillis();
        final long timeChange = (now - lastTime);
        final float error = desiredVal - currentVal;
        errSum += error * timeChange;

        final float deltaError = (error - lastError) / timeChange;

        lastError = error;
        lastTime = now;

        final float regulation = proportional * error + integral * errSum + derivative * deltaError;

        final StringBuilder builder = new StringBuilder();
        builder.append(String.format(Locale.CANADA, DECIMAL_FORMAT, currentVal)).append('\t')
            .append(String.format(Locale.CANADA, DECIMAL_FORMAT, regulation)).append('\t')
            .append(String.format(Locale.CANADA, DECIMAL_FORMAT, error)).append('\t')
            .append(timeChange).append('\t')
            .append(String.format(Locale.CANADA, DECIMAL_FORMAT, errSum)).append('\t')
            .append(String.format(Locale.CANADA, DECIMAL_FORMAT, deltaError)).append('\t');
        System.out.println(builder.toString());//TODO: remove printing to console
        return regulation;
    }

    /**
     * Clear all info about errors from previous runs.
     */
    public void clear() {
        lastError = 0;
        lastTime = System.currentTimeMillis();
        errSum = 0;
    }
}
