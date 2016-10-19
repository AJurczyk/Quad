package com.ajurczyk.hardware.pwm.impl;

import com.ajurczyk.hardware.pwm.IPwmController;
import com.ajurczyk.hardware.pwm.exceptions.PwmValRangeException;
import com.ajurczyk.hardware.pwm.exceptions.WholeNumException;

/**
 * @author aleksander.jurczyk@gmail.com on 19.10.16.
 */
public class PwmMock implements IPwmController {

    private float duty;
    private int periodMs;


    @Override
    public float getDuty() {
        return duty;
    }

    @Override
    public void setDuty(float value) throws PwmValRangeException {
        this.duty = value;
    }

    @Override
    public float getFrequency() {
        return (1 / periodMs) * 1000;
    }

    @Override
    public int getPeriodMs() {
        return periodMs;
    }

    @Override
    public void setPeriodMs(int periodMs) throws PwmValRangeException, WholeNumException {
        this.periodMs = periodMs;
    }
}
