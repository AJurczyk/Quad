package hardware.pwm;

import hardware.exception.PwmValRangeException;
import hardware.exception.WholeNumException;

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