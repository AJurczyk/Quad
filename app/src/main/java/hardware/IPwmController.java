package hardware;

import hardware.exception.PwmValueOutOfRange;
import hardware.exception.WholeNumberException;

/**
 * @author aleksander.jurczyk@gmail.com on 26.12.15.
 */
public interface IPwmController {

    float getDuty();

    void setDuty(float value) throws PwmValueOutOfRange;

    float getFrequency();

    void setPeriodMs(int periodMs) throws PwmValueOutOfRange, WholeNumberException;

    int getPeriodMs();
}
