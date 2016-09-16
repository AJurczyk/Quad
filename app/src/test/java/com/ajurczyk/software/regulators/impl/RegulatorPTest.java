package com.ajurczyk.software.regulators.impl;

import com.ajurczyk.software.regulators.Iregulator;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author aleksander.jurczyk@gmail.com on 16.09.16.
 */
public class RegulatorPTest {


    private static final float DESIRED_VALUE = 45.4f;
    private static final float CURRENT_VALUE = 25.2f;
    private static final float EXPECTED_REGULATION = 10.1f;
    private static final float P_FACTOR = 0.5f;

    @Test
    public final void calculateRegulation() {
        //given
        final Iregulator regulator = new RegulatorP();
        regulator.setP(P_FACTOR);

        //when
        final float regulation = regulator.getRegulation(CURRENT_VALUE, DESIRED_VALUE);

        //then
        Assert.assertEquals(regulation, EXPECTED_REGULATION);
    }

    @Test
    public final void setPIdoesntThrow() {
        //given
        final Iregulator regulator = new RegulatorP();

        //when
        regulator.setI(P_FACTOR);
        regulator.setD(P_FACTOR);

        //then
        //doesn't throw
    }
}
