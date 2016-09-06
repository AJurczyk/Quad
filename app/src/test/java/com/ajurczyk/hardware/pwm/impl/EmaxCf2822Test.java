package com.ajurczyk.hardware.pwm.impl;

import com.ajurczyk.hardware.motor.impl.EmaxCf2822;
import com.ajurczyk.hardware.pwm.IPwmController;
import com.ajurczyk.hardware.pwm.exceptions.PercentValRangeException;
import com.ajurczyk.hardware.pwm.exceptions.PwmValRangeException;
import com.ajurczyk.hardware.pwm.exceptions.WholeNumException;
import org.testng.annotations.Test;

import static org.mockito.Mockito.*;

/**
 * @author aleksander.jurczyk@gmail.com on 13.01.16.
 */
@Test(groups = "unitTests")
public class EmaxCf2822Test {

    //private static final float EMAX_PWM_MIN = 1f;
    private static final float EMAX_PWM_MAX = 2f;
    private static final float PWM_17_PRC = 1.17f;
    private static final int EMAX_PERIOD_MS = 5;

    @Test
    public final void setPercentage() throws WholeNumException, PwmValRangeException, PercentValRangeException {
        //given

        final IPwmController pwm = mock(RPi2HardwarePwm.class);
        final EmaxCf2822 motor = new EmaxCf2822(pwm);

        //when
        motor.setPower(17);

        //then
        verify(pwm, times(1)).setDuty(PWM_17_PRC);
    }

    @Test(expectedExceptions = PercentValRangeException.class)
    public final void setPrcAbove100() throws WholeNumException, PwmValRangeException, PercentValRangeException {
        //given

        final IPwmController pwm = mock(RPi2HardwarePwm.class);
        final EmaxCf2822 motor = new EmaxCf2822(pwm);

        //when
        motor.setPower(101);

        //then
        //throwsException
    }

    @Test(expectedExceptions = PercentValRangeException.class)
    public final void setPrcBelow0() throws WholeNumException, PwmValRangeException, PercentValRangeException {
        //given

        final IPwmController pwm = mock(RPi2HardwarePwm.class);
        final EmaxCf2822 motor = new EmaxCf2822(pwm);

        //when
        motor.setPower(-1);

        //then
        //throwsException
    }

    @Test
    public final void init() throws WholeNumException, PwmValRangeException {
        //given

        final IPwmController pwm = mock(RPi2HardwarePwm.class);

        //when
        new EmaxCf2822(pwm);

        //then
        verify(pwm, times(1)).setPeriodMs(EMAX_PERIOD_MS);
        verify(pwm, times(1)).setDuty(EMAX_PWM_MAX);
    }
}
