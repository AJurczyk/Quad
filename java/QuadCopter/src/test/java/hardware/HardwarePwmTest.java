package hardware;

import com.pi4j.wiringpi.Gpio;
import hardware.exception.PwmValueOutOfRange;
import hardware.impl.HardwarePwm;
import hardware.impl.pi4jGpio;
import org.testng.Assert;
import org.testng.annotations.Test;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


/**
 * @author aleksander.jurczyk@gmail.com on 30.12.15.
 */
public class HardwarePwmTest {

    private static final int PIN = 1;
    private static final int PERIOD = 20;
    private static final int PROPELLER_PERIOD = 5;

    @Test
    public final void calculateClock(){

    }
    @Test
    public final void servoTest() throws PwmValueOutOfRange {
//    given
        IGpioControl controller = mock(pi4jGpio.class);
        IPwm pwm = new HardwarePwm(controller, PIN, PERIOD);
//    when
        pwm.setDuty(1.8f);

//    then
        verify(controller, times(1)).pwmSetClock(1920);
        verify(controller, times(1)).pwmSetRange(200);
        verify(controller, times(1)).pwmWrite(PIN, 18);
    }

    @Test
    public final void propellerTest() throws PwmValueOutOfRange {
        //    given
        IGpioControl controller = mock(pi4jGpio.class);
        IPwm pwm = new HardwarePwm(controller, PIN, PROPELLER_PERIOD);
//    when
        pwm.setDuty(4f);

//    then
        verify(controller, times(1)).pwmSetClock(1920);
        verify(controller, times(1)).pwmSetRange(200);
        verify(controller, times(1)).pwmWrite(PIN, 18);
    }
}
