package hardware;

import hardware.exception.PwmValueOutOfRange;

/**
 * @author aleksander.jurczyk@gmail.com on 26.12.15.
 */
public interface IPwm {

    float getDuty();

    void setDuty(float value) throws PwmValueOutOfRange;

    float getFrequency();

    void setPeriodMs(int periodMs) throws PwmValueOutOfRange;

    int getPeriodMs();
}
