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
    public void init() throws MotorException {
        try {
            loadThrustMapFromFile(thrustMapFile);
            pwmController.setPeriodMs(pwmPeriodMs);
            pwmController.setDuty(pwmMaxDutyMs);
        } catch (PwmValRangeException | WholeNumException e) {
            throw new MotorException(e.getMessage(), e);
        }
    }

    @Override
    public float getCurrentRpmPrcnt() {
        return currentRpmPrcnt;
    }

    @Override
    public void setRpmPrcnt(float rpmPrcnt, boolean updateThrustVar) throws MotorException {
        try {
            LOGGER.debug("[EMAX] Set " + rpmPrcnt + "%");
            if (rpmPrcnt > 100 || rpmPrcnt < 0) {
                stop();
                throw new MotorException("Invalid rpm percent value " + rpmPrcnt + "%. Motor has been stopped.");
            }
            if (rpmPrcnt > rpmPrcnLimit) {
                LOGGER.warn("Tried to set rpm percent above limit. Expected: " + rpmPrcnt + "%. Limit: " + rpmPrcnLimit + "%");
                pwmController.setDuty(calcPwmFromRpmPercent(rpmPrcnLimit));
                this.currentRpmPrcnt = rpmPrcnLimit;
            } else {
                pwmController.setDuty(calcPwmFromRpmPercent(rpmPrcnt));
                this.currentRpmPrcnt = rpmPrcnt;
            }
        } catch (PwmValRangeException e) {
            throw new MotorException(e.getMessage(), e);
        }
        if (updateThrustVar) {
            currentThrustPrcnt = 100 * convertRpmPercentToThrust(rpmPrcnt) / maxThrust;
        }
    }

    @Override
    public void stop() throws MotorException {
        setRpmPrcnt(0, true);
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
    public float getCurrentThrustPercent() {
        return currentThrustPrcnt;
    }

    @Override
    public void setThrustPercent(float thrustPercent) throws MotorException {
        if (thrustPercent < 0 || thrustPercent > 100) {
            stop();
            throw new MotorException("Invalid thrust percent value " + thrustPercent + "%. Motor has been stopped.");
        }
        final float rpmPercent = convertThrustPercentToRpmPercent(thrustPercent);
        currentThrustPrcnt = thrustPercent;
        setRpmPrcnt(rpmPercent, false);
    }

    @Override
    public float getThrustNewtons() {
        return currentThrustPrcnt / 100 * maxThrust;
    }

    @Override
    public void setThrustNewtons(float thrustInNewtons) throws MotorException {
        if (thrustInNewtons < 0 || thrustInNewtons > maxThrust) {
            stop();
            throw new MotorException("Invalid thrust value " + thrustInNewtons + "[N]. Motor has been stopped.");
        }
        setThrustPercent(100 * thrustInNewtons / maxThrust);
    }

    @Override
    public void setThrustMapFile(String path) {
        this.thrustMapFile = path;
    }

    private float calcPwmFromRpmPercent(float percentValue) throws PwmValRangeException {
        final float pwmValue = pwmMinDutyMs + ((float) (pwmMaxDutyMs - pwmMinDutyMs)) / 100 * percentValue;
        if (pwmValue < pwmMinDutyMs || pwmValue > pwmMaxDutyMs) {
            throw new PwmValRangeException("Calculated pwm value " + pwmValue
                + " is out of EMAX CF2822 range" + pwmMinDutyMs + " - " + pwmMaxDutyMs + ".");
        }
        return pwmValue;
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

    private float convertRpmPercentToThrust(float rpmPrcnt) {
        if (rpmPrcnt == 0) {
            return thrustRpmPrcntMap.firstEntry().getValue();
        } else if (rpmPrcnt == 100) {
            return thrustRpmPrcntMap.lastEntry().getValue();
        }
        Map.Entry<Float, Float> lowerEntry = thrustRpmPrcntMap.entrySet().stream().filter(entry -> entry.getValue() < rpmPrcnt).max(Comparator.comparing(Map.Entry::getValue)).get();
        Map.Entry<Float, Float> higherEntry = thrustRpmPrcntMap.entrySet().stream().filter(entry -> entry.getValue() > rpmPrcnt).min(Comparator.comparing(Map.Entry::getValue)).get();
        final Float diffFraction = (rpmPrcnt - lowerEntry.getValue()) / (higherEntry.getValue() - lowerEntry.getValue());
        return lowerEntry.getKey() + (higherEntry.getKey() - lowerEntry.getKey()) * diffFraction;
    }

    private float convertThrustToRpmPercent(float newton) {
        if (thrustRpmPrcntMap.containsKey(newton)) {
            return thrustRpmPrcntMap.get(newton);
        }
        final Map.Entry<Float, Float> lowerEntry = thrustRpmPrcntMap.lowerEntry(newton);
        final Map.Entry<Float, Float> higherEntry = thrustRpmPrcntMap.higherEntry(newton);
        final Float diffFraction = (newton - lowerEntry.getKey()) / (higherEntry.getKey() - lowerEntry.getKey());
        return lowerEntry.getValue() + (higherEntry.getValue() - lowerEntry.getValue()) * diffFraction;
    }

    private float convertThrustPercentToRpmPercent(float thrustPrcnt) {
        return convertThrustToRpmPercent(thrustPrcnt / 100 * maxThrust);
    }
}
