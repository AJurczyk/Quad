package hardware;

import hardware.exception.PwmValueOutOfRange;
import hardware.exception.WholeNumberException;
import hardware.impl.HardwarePwm;
import hardware.impl.pi4jGpio;
import org.testng.annotations.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


/**
 * @author aleksander.jurczyk@gmail.com on 30.12.15.
 */
@Test(groups = "unitTests")
public class HardwarePwmTest {

    private static final int PIN = 1;
    private static final int PERIOD = 20;
    private static final int PROPELLER_PERIOD = 5;


    @Test
    public final void servoTest() throws PwmValueOutOfRange, WholeNumberException {
//    given
        IGpioControl controller = mock(pi4jGpio.class);
        IPwm pwm = new HardwarePwm(controller, PIN, PERIOD);
//    when
        pwm.setDuty(1.8f);

//    then
        verify(controller, times(1)).pwmSetClock(96);
        verify(controller, times(1)).pwmSetRange(4000);
        verify(controller, times(1)).pwmWrite(PIN, 360);
    }

    @Test
    public final void propellerTest() throws PwmValueOutOfRange, WholeNumberException {
        //given
        IGpioControl controller = mock(pi4jGpio.class);
        IPwm pwm = new HardwarePwm(controller, PIN, PROPELLER_PERIOD);
        //when
        pwm.setDuty(4f);

        //then
        verify(controller, times(1)).pwmSetClock(24);
        verify(controller, times(1)).pwmSetRange(4000);
        verify(controller, times(1)).pwmWrite(PIN, 3200);
    }

    @Test(expectedExceptions = PwmValueOutOfRange.class)
    public final void writeTooHighDuty() throws WholeNumberException, PwmValueOutOfRange {
        //given
        IGpioControl controller = mock(pi4jGpio.class);
        IPwm pwm = new HardwarePwm(controller, PIN, PERIOD);

        //when
        pwm.setDuty((float) (PERIOD + 1));

        //then
        //throws exception
    }

    @Test(expectedExceptions = PwmValueOutOfRange.class)
    public final void writeTooLowDuty() throws WholeNumberException, PwmValueOutOfRange {
        //given
        IGpioControl controller = mock(pi4jGpio.class);
        IPwm pwm = new HardwarePwm(controller, PIN, PERIOD);

        //when
        pwm.setDuty((float) (PERIOD + 1));

        //then
        //throws exception
    }

    @Test(expectedExceptions = WholeNumberException.class)
    public final void setInvalidPeriodForClock() throws WholeNumberException, PwmValueOutOfRange {
        //given
        IGpioControl controller = mock(pi4jGpio.class);
        IPwm pwm = new HardwarePwm(controller, PIN, PERIOD);

        //when
        pwm.setPeriodMs(3);

        //then
        //throws exception
    }

    @Test(expectedExceptions = WholeNumberException.class)
    public final void setInvalidPeriodForSetna() throws WholeNumberException, PwmValueOutOfRange {
        //given
        IGpioControl controller = mock(pi4jGpio.class);
        IPwm pwm = new HardwarePwm(controller, PIN, PERIOD);

        //when
        pwm.setPeriodMs(15);

        //then
        //throws exception
    }
}
