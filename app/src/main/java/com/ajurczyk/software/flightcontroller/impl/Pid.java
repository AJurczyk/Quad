package com.ajurczyk.software.flightcontroller.impl;

import com.ajurczyk.software.flightcontroller.IPid;

/**
 * @author aleksander.jurczyk@gmail.com on 09.09.16.
 */
public class Pid implements IPid {

    double lastError = 0;
    private float P;
    private float I;
    private float D;
    private float errSum = 0;
    private long lastTime = System.currentTimeMillis();

    @Override
    public void setP(float value) {
        this.P = value;
    }

    @Override
    public void setI(float value) {
        this.I = value;
    }

    @Override
    public void setD(float value) {
        this.D = value;
    }

    @Override
    public double getRegulation(double desiredValue, double currentValue) {
        return compute(desiredValue, currentValue);
    }

    private double compute(double desiredVal, double currentVal) {
        StringBuilder builder = new StringBuilder();

        long now = System.currentTimeMillis();
        long timeChange = (now - lastTime);
        double error = desiredVal - currentVal;
        errSum += (error * timeChange);

        double dErr = (error - lastError) / timeChange;

        lastError = error;
        lastTime = now;

        double regulation = P * error + I * errSum + D * dErr;

        builder.append("desired: \t").append(desiredVal).append("\t current: \t").append(currentVal);
        builder.append("\terror: \t").append(error);
        builder.append("\tdt: \t").append(timeChange);
        builder.append("\terrSum: \t").append(errSum);
        builder.append("\tdErr: \t").append(dErr);
        builder.append("\tregulation: \t").append(regulation);
        System.out.println(builder.toString());
        return regulation;
    }

    public void clear() {
        lastError = 0;
        lastTime = System.currentTimeMillis();
        errSum = 0;
    }
}
