package com.ajurczyk.hardware.motor.impl;

import com.ajurczyk.hardware.motor.IMotor;
import com.ajurczyk.hardware.motor.exception.MotorException;
import com.ajurczyk.hardware.pwm.IPwmController;
import com.ajurczyk.hardware.pwm.exceptions.PwmValRangeException;
import com.ajurczyk.hardware.pwm.exceptions.WholeNumException;
import com.ajurczyk.properties.PropertyNotFoundException;
import com.ajurczyk.properties.impl.PropertiesManager;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

/**
 * @author aleksander.jurczyk@gmail.com on 13.01.16.
 */
public class EmaxCf2822 implements IMotor {
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(EmaxCf2822.class);

    private int pwmMinDutyMs = 1;
    private int pwmMaxDutyMs = 2;
    private int pwmPeriodMs = 5;
    private float rpmPrcnLimit = 100f;
    private IPwmController pwmController;

    private String thrustMapFile;
    private TreeMap<Float, Float> thrustRpmPrcntMap;
    private float maxThrust;

    private float currentRpmPrcnt;
    private float currentThrustPrcnt;

    protected void setPwmMinDutyMs(int pwmMinDutyMs) {
        this.pwmMinDutyMs = pwmMinDutyMs;
    }

    protected void setPwmMaxDutyMs(int pwmMaxDutyMs) {
        this.pwmMaxDutyMs = pwmMaxDutyMs;
    }

    protected void setPwmPeriodMs(int pwmPeriodMs) {
        this.pwmPeriodMs = pwmPeriodMs;
    }

    protected IPwmController getPwmController() {
        return pwmController;
    }

    public void setPwmController(IPwmController pwmController) {
        this.pwmController = pwmController;
    }

    @Override
    public void setRpmPrcnLimit(float rpmPrcnLimit) {
        this.rpmPrcnLimit = rpmPrcnLimit;
    }

    @Override
    public float getRpmPrcntLimit() {
        return rpmPrcnLimit;
    }

    @Override
    public float getCurrentRpmPrcnt() {
        return currentRpmPrcnt;
    }

    @Override
    public float getCurrentThrustPrcnt() {
        return currentThrustPrcnt;
    }

    @Override
    public float getMaxThrustInNewtons() {
        return maxThrust;
    }

    @Override
    public void init() throws MotorException {
        try {
            LOGGER.debug("[EMAX] Init started.");
            loadThrustMapFromFile(thrustMapFile);
            pwmController.setPeriodMs(pwmPeriodMs);
            pwmController.setDuty(pwmMaxDutyMs);
            LOGGER.debug("[EMAX] Init successful.");
        } catch (PwmValRangeException | WholeNumException e) {
            throw new MotorException(e.getMessage(), e);
        }
    }

    @Override
    public void stop() throws MotorException {
        setRpmPrcnt(0);
    }

    @Override
    public void setThrustMapFile(String path) {
        this.thrustMapFile = path;
    }

    @Override
    public void setRpmPrcnt(float rpmPrcnt) throws MotorException {
        try {
            LOGGER.debug("[EMAX] Set rpm: " + rpmPrcnt + "%.");
            if (rpmPrcnt > 100 || rpmPrcnt < 0) {
                stop();
                throw new MotorException("Invalid rpm percent value " + rpmPrcnt + "%. Motor has been stopped.");
            }
            float rpmToSet = rpmPrcnt;
            if (rpmPrcnt > rpmPrcnLimit) {
                LOGGER.warn("Rpm will be set to limit value. Expected: " + rpmPrcnt + "%. Limit: " + rpmPrcnLimit + "%");
                rpmToSet = rpmPrcnLimit;
            }
            pwmController.setDuty(calcPwmFromRpmPercent(rpmToSet));

            //TODO optimization - conversion is inefficient, if setThrustPrcnt methos is used we know thrustPrcnt already
            this.currentThrustPrcnt = convertRpmPrcntToThrustPrcnt(rpmToSet);

            this.currentRpmPrcnt = rpmToSet;
        } catch (PwmValRangeException e) {
            throw new MotorException(e.getMessage(), e);
        }
    }

    @Override
    public void setThrustPrcnt(float thrustPrcnt) throws MotorException {
        LOGGER.debug("[EMAX] Set thrust: " + thrustPrcnt + "%.");
        if (thrustPrcnt < 0 || thrustPrcnt > 100) {
            stop();
            throw new MotorException("Invalid thrust percent value " + thrustPrcnt + "%. Motor has been stopped.");
        }
        setRpmPrcnt(convertThrustPrcntToRpmPrcnt(thrustPrcnt));
    }

    protected void loadThrustMapFromFile(String path) throws MotorException {
        thrustRpmPrcntMap = new TreeMap<>();
        try {
            final PropertiesManager propertiesMgr = new PropertiesManager(path);
            for (final String propertyName : propertiesMgr.getPropertiesNames()) {
                thrustRpmPrcntMap.put(new Float(propertyName), new Float(propertiesMgr.getProperty(propertyName)));
            }
            maxThrust = thrustRpmPrcntMap.lastKey();
        } catch (IOException | PropertyNotFoundException e) {
            throw new MotorException(e.getMessage(), e);
        }
    }

    private float convertRpmPrcntToThrustPrcnt(float rpmPrcnt) {
        if (rpmPrcnt == 0) {
            return thrustRpmPrcntMap.firstEntry().getValue();
        } else if (rpmPrcnt == 100) {
            return thrustRpmPrcntMap.lastEntry().getValue();
        }
        Map.Entry<Float, Float> lowerEntry = thrustRpmPrcntMap.entrySet().stream().filter(entry -> entry.getValue() < rpmPrcnt).max(Comparator.comparing(Map.Entry::getValue)).get();
        Map.Entry<Float, Float> higherEntry = thrustRpmPrcntMap.entrySet().stream().filter(entry -> entry.getValue() > rpmPrcnt).min(Comparator.comparing(Map.Entry::getValue)).get();
        final Float diffFraction = (rpmPrcnt - lowerEntry.getValue()) / (higherEntry.getValue() - lowerEntry.getValue());
        final float thrustNewtons = lowerEntry.getKey() + (higherEntry.getKey() - lowerEntry.getKey()) * diffFraction;
        return thrustNewtons / maxThrust * 100;
    }

    private float convertThrustPrcntToRpmPrcnt(float thrustPrcnt) {
        final float thrustNewtons = thrustPrcnt / 100 * maxThrust;
        if (thrustRpmPrcntMap.containsKey(thrustNewtons)) {
            return thrustRpmPrcntMap.get(thrustNewtons);
        }
        final Map.Entry<Float, Float> lowerEntry = thrustRpmPrcntMap.lowerEntry(thrustNewtons);
        final Map.Entry<Float, Float> higherEntry = thrustRpmPrcntMap.higherEntry(thrustNewtons);
        final Float diffFraction = (thrustNewtons - lowerEntry.getKey()) / (higherEntry.getKey() - lowerEntry.getKey());
        return lowerEntry.getValue() + (higherEntry.getValue() - lowerEntry.getValue()) * diffFraction;
    }

    private float calcPwmFromRpmPercent(float percentValue) throws PwmValRangeException {
        final float pwmValue = pwmMinDutyMs + ((float) (pwmMaxDutyMs - pwmMinDutyMs)) / 100 * percentValue;
        if (pwmValue < pwmMinDutyMs || pwmValue > pwmMaxDutyMs) {
            throw new PwmValRangeException("Calculated pwm value " + pwmValue
                + " is out of EMAX CF2822 range" + pwmMinDutyMs + " - " + pwmMaxDutyMs + ".");
        }
        return pwmValue;
    }
}
