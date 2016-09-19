package com.ajurczyk.software.regulators.impl;

import com.ajurczyk.software.regulators.IRegulator;

import java.util.Locale;

/**
 * @author aleksander.jurczyk@gmail.com on 18.09.16.
 */
public class RegulatorPid implements IRegulator {
    float lastError = 0;
    private float proportional;
    private float integral;
    private float derivative;
    private float errSum = 0;
    private long lastTime = System.currentTimeMillis();

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

    private float compute(float desiredVal, float currentVal) {
        StringBuilder builder = new StringBuilder();

        long now = System.currentTimeMillis();
        long timeChange = (now - lastTime);
        float error = desiredVal - currentVal;
        errSum += (error * timeChange);

        float dErr = (error - lastError) / timeChange;

        lastError = error;
        lastTime = now;

        float regulation = proportional * error + integral * errSum + derivative * dErr;

        builder.append(String.format(Locale.CANADA, "%.2f", currentVal)).append("\t");
        builder.append(String.format(Locale.CANADA, "%.2f", regulation)).append("\t");
        builder.append(String.format(Locale.CANADA, "%.2f", error)).append("\t");
        builder.append(timeChange).append("\t");
        builder.append(String.format(Locale.CANADA, "%.2f", errSum)).append("\t");
        builder.append(String.format(Locale.CANADA, "%.2f", dErr)).append("\t");
        System.out.println(builder.toString());
        return regulation;
    }

    public void clear() {
        lastError = 0;
        lastTime = System.currentTimeMillis();
        errSum = 0;
    }
}
