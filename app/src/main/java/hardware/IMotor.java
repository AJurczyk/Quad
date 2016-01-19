package hardware;

import hardware.exception.PercentValRangeException;
import hardware.exception.PwmValRangeException;

/**
 * @author aleksander.jurczyk@gmail.com on 13.01.16.
 */
public interface IMotor {

    void stop() throws PwmValRangeException, PercentValRangeException;

    void setPercent(int value) throws PwmValRangeException, PercentValRangeException;

    int getPercent();
}
