package com.ajurczyk.hardware.motor.impl;

import com.ajurczyk.hardware.motor.IMotor;
import com.ajurczyk.hardware.motor.exception.MotorException;
import com.ajurczyk.hardware.pwm.IPwmController;
import com.ajurczyk.hardware.pwm.exceptions.PwmValRangeException;
import com.ajurczyk.hardware.pwm.exceptions.WholeNumException;
import com.ajurczyk.utils.FileUtils;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Test of {@link com.ajurczyk.hardware.motor.impl.EmaxCf2822}.
 *
 * @author aleksander.jurczyk@gmail.com on 16.10.16.
 */
@Test(groups = "unitTests")
public class EmaxCf2822Test {
    private static final int PWM_MIN_DUTY_MS = 1;
    private static final int PWM_MAX_DUTY_MS = 2;
    private static final int PWM_PERIOD_MS = 5;

    private static final float VALUE_BELOW_0 = -10.4f;
    private static final float PRCNT_IN_BETWEEN = 42.5f;
    private static final float PRCNT_ABOVE_100 = 100.34f;
    private static final float RPM_PRCNT_LIMIT = 80f;
    private static final float NEWTON_LIMIT = 80f;
    private static final float THRUST_PRCNT_IN_BETWEEN = 27.5f;

    private static final float MAX_THROTTLE = 100f;
    private static final String THRUST_FILE = "src/test/resources/thrustTestFile.properties";
    private final TreeMap<Float, Float> thrustToPercent = new TreeMap<>();

    /**
     * Data provider with percentage values to check.
     *
     * @return percentage values
     */
    @DataProvider(name = "percentValues")
    public Object[][] percentValues() {
        return new Object[][]{
            {0f},
            {25f},
            {33f},
            {65f},
            {90f},
            {100f}
        };
    }

    @BeforeClass
    public final void beforeClass() throws IOException {
        initPercentAndThrustMap();
        createThrustFile();
    }

    @AfterClass
    public final void removeFile() {
        FileUtils.removeFile(THRUST_FILE);
    }

    private void initPercentAndThrustMap() {
        thrustToPercent.put(0f, 0f);
        thrustToPercent.put(5f, 10f);
        thrustToPercent.put(10f, 20f);
        thrustToPercent.put(20f, 30f);
        thrustToPercent.put(25f, 40f);
        thrustToPercent.put(35f, 50f);
        thrustToPercent.put(45f, 60f);
        thrustToPercent.put(65f, 70f);
        thrustToPercent.put(NEWTON_LIMIT, RPM_PRCNT_LIMIT);
        thrustToPercent.put(95f, 90f);
        thrustToPercent.put(MAX_THROTTLE, 100f);
    }

    private void createThrustFile() throws IOException {
        FileUtils.removeFile(THRUST_FILE);
        final StringBuilder builder = new StringBuilder();
        for (final Map.Entry<Float, Float> entry : thrustToPercent.entrySet()) {
            builder.append(entry.getKey()).append('=').append(entry.getValue()).append(System.getProperty("line.separator"));
        }
        FileUtils.save(builder.toString(), THRUST_FILE, true);
    }

    private float convertPercentToPwm(float percentVal) {
        return PWM_MIN_DUTY_MS + ((float) (PWM_MAX_DUTY_MS - PWM_MIN_DUTY_MS)) / 100 * percentVal;
    }

    @Test
    public final void init() throws MotorException, PwmValRangeException, WholeNumException {
        //given
        final IMotor motor = new EmaxCf2822();
        final IPwmController pwm = mock(IPwmController.class);
        motor.setThrustMapFile(THRUST_FILE);
        ((EmaxCf2822) motor).setPwmController(pwm);
        ((EmaxCf2822) motor).setPwmMaxDutyMs(PWM_MAX_DUTY_MS);
        ((EmaxCf2822) motor).setPwmMinDutyMs(PWM_MIN_DUTY_MS);
        ((EmaxCf2822) motor).setPwmPeriodMs(PWM_PERIOD_MS);

        //when
        motor.init();

        //then
        verify(pwm).setDuty(PWM_MAX_DUTY_MS);
        verify(pwm).setPeriodMs(PWM_PERIOD_MS);
    }

    @Test(dataProvider = "percentValues")
    public final void setCorrectRpmPercent(float percentToSet) throws PwmValRangeException, MotorException {
        //given
        final IMotor motor = new EmaxCf2822();
        final IPwmController pwm = mock(IPwmController.class);
        motor.setThrustMapFile(THRUST_FILE);
        ((EmaxCf2822) motor).setPwmController(pwm);

        //when
        ((EmaxCf2822) motor).loadThrustMapFromFile(THRUST_FILE);
        motor.setRpmPrcnt(percentToSet);

        //then
        final float percentSet = motor.getCurrentRpmPrcnt();
        Assert.assertEquals(percentSet, percentToSet);
        verify(pwm).setDuty(convertPercentToPwm(percentSet));
    }

    @Test
    public final void setRpmPercentBelow0() throws MotorException, PwmValRangeException {
        //given
        final IMotor motor = new EmaxCf2822();
        final IPwmController pwm = mock(IPwmController.class);
        motor.setThrustMapFile(THRUST_FILE);
        ((EmaxCf2822) motor).setPwmController(pwm);

        //when
        ((EmaxCf2822) motor).loadThrustMapFromFile(THRUST_FILE);
        boolean exceptionThrown = false;
        try {
            motor.setRpmPrcnt(VALUE_BELOW_0);
        } catch (MotorException e) {
            exceptionThrown = true;
        }

        //then
        Assert.assertTrue(exceptionThrown);
        verify(pwm).setDuty(convertPercentToPwm(0));
    }

    @Test
    public final void setRpmPercentAbove100() throws MotorException, PwmValRangeException {
        //given
        final IMotor motor = new EmaxCf2822();
        final IPwmController pwm = mock(IPwmController.class);
        motor.setThrustMapFile(THRUST_FILE);
        ((EmaxCf2822) motor).setPwmController(pwm);

        //when
        ((EmaxCf2822) motor).loadThrustMapFromFile(THRUST_FILE);
        boolean exceptionThrown = false;
        try {
            motor.setRpmPrcnt(PRCNT_ABOVE_100);
        } catch (MotorException e) {
            exceptionThrown = true;
        }

        //then
        Assert.assertTrue(exceptionThrown);
        verify(pwm).setDuty(convertPercentToPwm(0));
    }

    @Test
    public final void setRpmPercentAboveLimit() throws MotorException, PwmValRangeException {
        //given
        final IMotor motor = new EmaxCf2822();
        final IPwmController pwm = mock(IPwmController.class);
        motor.setThrustMapFile(THRUST_FILE);
        motor.setRpmPrcnLimit(RPM_PRCNT_LIMIT);
        ((EmaxCf2822) motor).setPwmController(pwm);

        //when
        ((EmaxCf2822) motor).loadThrustMapFromFile(THRUST_FILE);
        motor.setRpmPrcnt(RPM_PRCNT_LIMIT + 1);

        //then
        verify(pwm).setDuty(convertPercentToPwm(RPM_PRCNT_LIMIT));
    }

    @Test
    public final void setThrustPercentBelow0() throws PwmValRangeException, MotorException {
        //given
        final IMotor motor = new EmaxCf2822();
        final IPwmController pwm = mock(IPwmController.class);
        motor.setThrustMapFile(THRUST_FILE);
        ((EmaxCf2822) motor).setPwmController(pwm);

        //when
        ((EmaxCf2822) motor).loadThrustMapFromFile(THRUST_FILE);
        boolean exceptionThrown = false;
        try {
            motor.setThrustPrcnt(VALUE_BELOW_0);
        } catch (MotorException e) {
            exceptionThrown = true;
        }

        //then
        Assert.assertTrue(exceptionThrown);
        verify(pwm).setDuty(convertPercentToPwm(0));
    }

    @Test
    public final void setThrustPercentAboveMax() throws PwmValRangeException, MotorException {
        //given
        final IMotor motor = new EmaxCf2822();
        final IPwmController pwm = mock(IPwmController.class);
        motor.setThrustMapFile(THRUST_FILE);
        motor.setRpmPrcnLimit(RPM_PRCNT_LIMIT);
        ((EmaxCf2822) motor).setPwmController(pwm);

        //when
        ((EmaxCf2822) motor).loadThrustMapFromFile(THRUST_FILE);
        boolean exceptionThrown = false;
        try {
            motor.setThrustPrcnt(PRCNT_ABOVE_100);
        } catch (MotorException e) {
            exceptionThrown = true;
        }

        //then
        Assert.assertTrue(exceptionThrown);
        verify(pwm).setDuty(convertPercentToPwm(0));
    }

    @Test
    public final void setThrustPrcntAboveLimit() throws PwmValRangeException, MotorException {
        //given
        final IMotor motor = new EmaxCf2822();
        final IPwmController pwm = mock(IPwmController.class);
        motor.setThrustMapFile(THRUST_FILE);
        motor.setRpmPrcnLimit(RPM_PRCNT_LIMIT);
        ((EmaxCf2822) motor).setPwmController(pwm);

        //when
        ((EmaxCf2822) motor).loadThrustMapFromFile(THRUST_FILE);
        motor.setThrustPrcnt(NEWTON_LIMIT + 1);

        //then
        verify(pwm).setDuty(convertPercentToPwm(RPM_PRCNT_LIMIT));
    }

    @Test
    public final void setThrustNewtonsBetweenValuesInFile() throws PwmValRangeException, MotorException {
        //given
        final IMotor motor = new EmaxCf2822();
        final IPwmController pwm = mock(IPwmController.class);
        motor.setThrustMapFile(THRUST_FILE);
        ((EmaxCf2822) motor).setPwmController(pwm);

        //when
        ((EmaxCf2822) motor).loadThrustMapFromFile(THRUST_FILE);
        motor.setThrustPrcnt(THRUST_PRCNT_IN_BETWEEN);

        //then
        verify(pwm).setDuty(convertPercentToPwm(PRCNT_IN_BETWEEN));
    }

    public final void setRpmPrcntUpdatesThrustPrcntVar() throws MotorException {
        //given
        final IMotor motor = new EmaxCf2822();
        final IPwmController pwm = mock(IPwmController.class);
        motor.setThrustMapFile(THRUST_FILE);
        ((EmaxCf2822) motor).setPwmController(pwm);

        //when
        ((EmaxCf2822) motor).loadThrustMapFromFile(THRUST_FILE);
        motor.setRpmPrcnt(RPM_PRCNT_LIMIT);

        //then
        final float rpmPrcntSet = motor.getCurrentRpmPrcnt();
        final float thrustPrcntSet = motor.getCurrentThrustPrcnt();
        final float expectedThrustPrcnt = NEWTON_LIMIT / MAX_THROTTLE * 100f;
        Assert.assertEquals(thrustPrcntSet, expectedThrustPrcnt);
        Assert.assertEquals(rpmPrcntSet, RPM_PRCNT_LIMIT);
    }

    public final void setThrustPcntUpdatesRpmPrcntVar() throws MotorException {
        //given
        final IMotor motor = new EmaxCf2822();
        final IPwmController pwm = mock(IPwmController.class);
        motor.setThrustMapFile(THRUST_FILE);
        ((EmaxCf2822) motor).setPwmController(pwm);
        final float thrustToSet = NEWTON_LIMIT / MAX_THROTTLE * 100;

        //when
        ((EmaxCf2822) motor).loadThrustMapFromFile(THRUST_FILE);
        motor.setThrustPrcnt(thrustToSet);

        //then
        final float thrustPrcntSet = motor.getCurrentThrustPrcnt();
        final float rpmPrcntSet = motor.getCurrentRpmPrcnt();
        Assert.assertEquals(thrustPrcntSet, thrustToSet);
        Assert.assertEquals(rpmPrcntSet, RPM_PRCNT_LIMIT);
    }
}
