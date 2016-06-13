package com.ajurczyk.hardware.pwm;

import com.ajurczyk.hardware.pwm.exceptions.PwmValRangeException;
import com.ajurczyk.hardware.pwm.exceptions.WholeNumException;

/**
 * @author aleksander.jurczyk@gmail.com on 26.12.15.
 */
public interface IPwmController {

    float getDuty();

    void setDuty(float value) throws PwmValRangeException;

    float getFrequency();

    int getPeriodMs();

    void setPeriodMs(int periodMs) throws PwmValRangeException, WholeNumException;
}
