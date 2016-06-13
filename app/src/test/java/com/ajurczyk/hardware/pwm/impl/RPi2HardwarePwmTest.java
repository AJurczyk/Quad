package com.ajurczyk.hardware.pwm.impl;

import com.ajurczyk.hardware.pwm.IGpioController;
import com.ajurczyk.hardware.pwm.IPwmController;
import com.ajurczyk.hardware.pwm.exceptions.PwmValRangeException;
import com.ajurczyk.hardware.pwm.exceptions.WholeNumException;
import com.ajurczyk.hardware.pwm.impl.Pi4jGpio;
import com.ajurczyk.hardware.pwm.impl.RPi2HardwarePwm;
import org.testng.annotations.Test;

import static org.mockito.Mockito.*;


/**
 * @author aleksander.jurczyk@gmail.com on 30.12.15.
 */
@Test(groups = "unitTests")
public class RPi2HardwarePwmTest {

    private static final int PIN = 1;
    private static final int PERIOD = 20;
    private static final int PROPELLER_PERIOD = 5;


    @Test
    public final void servoTest() throws PwmValRangeException, WholeNumException {
        //given
        final IGpioController controller = mock(Pi4jGpio.class);
        final IPwmController pwm = new RPi2HardwarePwm(controller, PIN, PERIOD);
        //when
        pwm.setDuty(1.8f);
        //then
        verify(controller, times(1)).pwmSetClock(96);
        verify(controller, times(1)).pwmSetRange(4000);
        verify(controller, times(1)).pwmWrite(PIN, 360);
    }

    @Test
    public final void propellerTest() throws PwmValRangeException, WholeNumException {
        //given
        final IGpioController controller = mock(Pi4jGpio.class);
        final IPwmController pwm = new RPi2HardwarePwm(controller, PIN, PROPELLER_PERIOD);
        //when
        pwm.setDuty(3.80f);

        //then
        verify(controller, times(1)).pwmSetClock(24);
        verify(controller, times(1)).pwmSetRange(4000);
        verify(controller, times(1)).pwmWrite(PIN, 3040);
    }

    @Test(expectedExceptions = PwmValRangeException.class)
    public final void writeTooHighDuty() throws WholeNumException, PwmValRangeException {
        //given
        final IGpioController controller = mock(Pi4jGpio.class);
        final IPwmController pwm = new RPi2HardwarePwm(controller, PIN, PERIOD);

        //when
        pwm.setDuty((float) (PERIOD + 1));

        //then
        //throws exceptions
    }

    @Test(expectedExceptions = PwmValRangeException.class)
    public final void writeTooLowDuty() throws WholeNumException, PwmValRangeException {
        //given
        final IGpioController controller = mock(Pi4jGpio.class);
        final IPwmController pwm = new RPi2HardwarePwm(controller, PIN, PERIOD);

        //when
        pwm.setDuty((float) (PERIOD + 1));

        //then
        //throws exceptions
    }

    @Test(expectedExceptions = WholeNumException.class)
    public final void setInvalidPeriodForClock() throws WholeNumException, PwmValRangeException {
        //given
        final IGpioController controller = mock(Pi4jGpio.class);
        final IPwmController pwm = new RPi2HardwarePwm(controller, PIN, PERIOD);

        //when
        pwm.setPeriodMs(3);

        //then
        //throws exceptions
    }

    @Test(expectedExceptions = WholeNumException.class)
    public final void setInvalidPeriodForSetna() throws WholeNumException, PwmValRangeException {
        //given
        final IGpioController controller = mock(Pi4jGpio.class);
        final IPwmController pwm = new RPi2HardwarePwm(controller, PIN, PERIOD);

        //when
        pwm.setPeriodMs(15);

        //then
        //throws exceptions
    }
}
