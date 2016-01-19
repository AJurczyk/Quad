package hardware.impl;

import hardware.IPwmController;
import hardware.exception.PrcValueOutOfRange;
import hardware.exception.PwmValueOutOfRange;
import hardware.exception.WholeNumberException;
import org.testng.annotations.Test;

import static org.mockito.Mockito.*;

/**
 * @author aleksander.jurczyk@gmail.com on 13.01.16.
 */
@Test(groups = "unitTests")
public class EmaxCf2822Test {

    private static final float EMAX_PWM_MIN = 1f;
    private static final float EMAX_PWM_MAX = 2f;
    private static final float PWM_17_PRC = 1.17f;
    private static final int EMAX_PERIOD_MS = 5;

    @Test
    public final void setPercentage() throws WholeNumberException, PwmValueOutOfRange, PrcValueOutOfRange {
        //given

        IPwmController pwm = mock(RPi2HardwarePwm.class);
        EmaxCf2822 motor = new EmaxCf2822(pwm);

        //when
        motor.setPercent(17);

        //then
        verify(pwm, times(1)).setDuty(PWM_17_PRC);
    }

    @Test (expectedExceptions = PrcValueOutOfRange.class)
    public final void setPrcAbove100() throws WholeNumberException, PwmValueOutOfRange, PrcValueOutOfRange {
        //given

        IPwmController pwm = mock(RPi2HardwarePwm.class);
        EmaxCf2822 motor = new EmaxCf2822(pwm);

        //when
        motor.setPercent(101);

        //then
        //throwsException
    }

    @Test (expectedExceptions = PrcValueOutOfRange.class)
    public final void setPrcBelow0() throws WholeNumberException, PwmValueOutOfRange, PrcValueOutOfRange {
        //given

        IPwmController pwm = mock(RPi2HardwarePwm.class);
        EmaxCf2822 motor = new EmaxCf2822(pwm);

        //when
        motor.setPercent(-1);

        //then
        //throwsException
    }

    @Test
    public final void init() throws WholeNumberException, PwmValueOutOfRange {
        //given

        IPwmController pwm = mock(RPi2HardwarePwm.class);

        //when
        EmaxCf2822 motor = new EmaxCf2822(pwm);

        //then
        verify(pwm, times(1)).setPeriodMs(EMAX_PERIOD_MS);
        verify(pwm, times(1)).setDuty(EMAX_PWM_MAX);
    }
}
