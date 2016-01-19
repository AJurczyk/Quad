package hardware;

import hardware.exception.PrcValueOutOfRange;
import hardware.exception.PwmValueOutOfRange;

/**
 * @author aleksander.jurczyk@gmail.com on 13.01.16.
 */
public interface IMotor {

    void stop() throws PwmValueOutOfRange, PrcValueOutOfRange;

    void setPercent(int value) throws PwmValueOutOfRange, PrcValueOutOfRange;

    int getPercent();
}
