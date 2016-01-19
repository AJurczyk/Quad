package hardware;

import hardware.exception.PwmValRangeException;
import hardware.exception.WholeNumException;

/**
 * @author aleksander.jurczyk@gmail.com on 26.12.15.
 */
public interface IPwmController {

    float getDuty();

    void setDuty(float value) throws PwmValRangeException;

    float getFrequency();

    void setPeriodMs(int periodMs) throws PwmValRangeException, WholeNumException;

    int getPeriodMs();
}
