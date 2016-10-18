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
@SuppressWarnings("PMD.LooseCoupling")
public class EmaxCf2822 implements IMotor {
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(EmaxCf2822.class);

    private int pwmMinDutyMs = 1;
    private int pwmMaxDutyMs = 2;
    private int pwmPeriodMs = 5;
    private float rpmLimit = 100f;
    private IPwmController pwmController;

    private String thrustMapFile;
    private TreeMap<Float, Float> thrustToRpmMap;
    private float maxThrustNewtons;

    private float currentRpmm;
    private float currentThrust;

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
    public float getRpmLimit() {
        return rpmLimit;
    }

    @Override
    public void setRpmLimit(float rpmLimit) {
        this.rpmLimit = rpmLimit;
    }

    public float getCurrentRpmm() {
        return currentRpmm;
    }

    public float getCurrentThrust() {
        return currentThrust;
    }

    @Override
    public float getMaxThrustInNewtons() {
        return maxThrustNewtons;
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
        setRpm(0);
    }

    @Override
    public void setThrustMapFile(String path) {
        this.thrustMapFile = path;
    }

    @Override
    public void setRpm(float rpm) throws MotorException {
        try {
            LOGGER.debug("[EMAX] Set rpm: " + rpm + "%.");
            if (rpm > 100 || rpm < 0) {
                stop();
                throw new MotorException("Invalid rpm percent value " + rpm + "%. Motor has been stopped.");
            }
            float rpmToSet = rpm;
            if (rpm > rpmLimit) {
                LOGGER.warn("Rpm will be set to limit value. Expected: " + rpm + "%. Limit: " + rpmLimit + "%");
                rpmToSet = rpmLimit;
            }
            pwmController.setDuty(calcPwmFromRpm(rpmToSet));

            //TODO optimization - conversion is inefficient, if setThrust methos is used we know thrustPrcnt already
            this.currentThrust = convertRpmToThrust(rpmToSet);

            this.currentRpmm = rpmToSet;
        } catch (PwmValRangeException e) {
            throw new MotorException(e.getMessage(), e);
        }
    }

    @Override
    public void setThrust(float thrust) throws MotorException {
        LOGGER.debug("[EMAX] Set thrust: " + thrust + "%.");
        if (thrust < 0 || thrust > 100) {
            stop();
            throw new MotorException("Invalid thrust percent value " + thrust + "%. Motor has been stopped.");
        }
        setRpm(convertThrustToRpm(thrust));
    }

    protected void loadThrustMapFromFile(String path) throws MotorException {
        thrustToRpmMap = new TreeMap<>();
        try {
            final PropertiesManager propertiesMgr = new PropertiesManager(path);
            for (final String propertyName : propertiesMgr.getPropertiesNames()) {
                thrustToRpmMap.put(Float.valueOf(propertyName), Float.valueOf(propertiesMgr.getProperty(propertyName)));
            }
            maxThrustNewtons = thrustToRpmMap.lastKey();
        } catch (IOException | PropertyNotFoundException e) {
            throw new MotorException(e.getMessage(), e);
        }
    }

    @SuppressWarnings("PMD.AvoidLiteralsInIfCondition")
    private float convertRpmToThrust(float rpm) {
        if (rpm == 0) {
            return thrustToRpmMap.firstEntry().getValue();
        } else if (rpm == 100) {
            return thrustToRpmMap.lastEntry().getValue();
        }
        final Map.Entry<Float, Float> lowerEntry = thrustToRpmMap.entrySet().stream().filter(entry -> entry.getValue() < rpm)
            .max(Comparator.comparing(Map.Entry::getValue)).get();

        final Map.Entry<Float, Float> higherEntry = thrustToRpmMap.entrySet().stream().filter(entry -> entry.getValue() > rpm)
            .min(Comparator.comparing(Map.Entry::getValue)).get();

        final Float diffFraction = (rpm - lowerEntry.getValue()) / (higherEntry.getValue() - lowerEntry.getValue());
        final float thrustNewtons = lowerEntry.getKey() + (higherEntry.getKey() - lowerEntry.getKey()) * diffFraction;
        return thrustNewtons / maxThrustNewtons * 100;
    }

    private float convertThrustToRpm(float thrust) {
        final float thrustNewtons = thrust / 100 * maxThrustNewtons;
        if (thrustToRpmMap.containsKey(thrustNewtons)) {
            return thrustToRpmMap.get(thrustNewtons);
        }
        final Map.Entry<Float, Float> lowerEntry = thrustToRpmMap.lowerEntry(thrustNewtons);
        final Map.Entry<Float, Float> higherEntry = thrustToRpmMap.higherEntry(thrustNewtons);
        final Float diffFraction = (thrustNewtons - lowerEntry.getKey()) / (higherEntry.getKey() - lowerEntry.getKey());
        return lowerEntry.getValue() + (higherEntry.getValue() - lowerEntry.getValue()) * diffFraction;
    }

    private float calcPwmFromRpm(float rpm) throws PwmValRangeException {
        final float pwmValue = pwmMinDutyMs + ((float) (pwmMaxDutyMs - pwmMinDutyMs)) / 100 * rpm;
        if (pwmValue < pwmMinDutyMs || pwmValue > pwmMaxDutyMs) {
            throw new PwmValRangeException("Calculated pwm value " + pwmValue
                + " is out of EMAX CF2822 range" + pwmMinDutyMs + " - " + pwmMaxDutyMs + ".");
        }
        return pwmValue;
    }
}
