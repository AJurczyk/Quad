package com.ajurczyk.hardware.motor.impl;

import com.ajurczyk.hardware.motor.IMotor;
import com.ajurczyk.hardware.motor.exception.MotorException;
import com.ajurczyk.hardware.pwm.IPwmController;

/**
 * @author aleksander.jurczyk@gmail.com on 13.01.16.
 */
public class EmaxCf2822 implements IMotor {

    private int pwmMinDutyMs = 1;
    private int pwmMaxDutyMs = 2;
    private int pwmPeriodMs = 5;

    protected void setPwmMinDutyMs(int pwmMinDutyMs) {
        this.pwmMinDutyMs = pwmMinDutyMs;
    }

    protected void setPwmMaxDutyMs(int pwmMaxDutyMs) {
        this.pwmMaxDutyMs = pwmMaxDutyMs;
    }

    protected void setPwmPeriodMs(int pwmPeriodMs) {
        this.pwmPeriodMs = pwmPeriodMs;
    }

    private float rpmPrcnt;
    private IPwmController pwmController;

    protected IPwmController getPwmController() {
        return pwmController;
    }

    public void setPwmController(IPwmController pwmController) {
        this.pwmController = pwmController;
    }

    @Override
    public void setRpmPrcnt(float percent) {

    }

    @Override
    public float getRpmPrcnt() {
        return 0;
    }

    @Override
    public void stop() throws MotorException {

    }

    @Override
    public void setRpmPrcnLimit(float rpmPrcnLimit) {

    }

    @Override
    public float getRpmPrcntLimit() {
        return 0;
    }

    @Override
    public float getThrustPercent() {
        return 0;
    }

    @Override
    public void setThrustPercent(float thrustPercent) {

    }

    @Override
    public float getThrustNewtons() {
        return 0;
    }

    @Override
    public void setThrustFile(String path) {

    }

    @Override
    public void init() throws MotorException {

    }

    @Override
    public void setThrustNewtons(float thrustInNewtons) {

    }
}
