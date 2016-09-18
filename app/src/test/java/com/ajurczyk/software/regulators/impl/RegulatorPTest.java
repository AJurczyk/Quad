package com.ajurczyk.software.regulators.impl;

import com.ajurczyk.software.regulators.IRegulator;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author aleksander.jurczyk@gmail.com on 16.09.16.
 */
public class RegulatorPTest {


    private static final float DESIRED_VALUE = 45.4f;
    private static final float CURRENT_VALUE = 25.2f;
    private static final float EXP_REGULATION = 10.1f;
    private static final float P_FACTOR = 0.5f;

    @Test
    public final void calculateRegulation() {
        //given
        final IRegulator regulator = new RegulatorP();
        regulator.setProportional(P_FACTOR);

        //when
        final float regulation = regulator.getRegulation(CURRENT_VALUE, DESIRED_VALUE);

        //then
        Assert.assertEquals(regulation, EXP_REGULATION);
    }

    @Test
    public final void setPIdoesntThrow() {
        //given
        final IRegulator regulator = new RegulatorP();

        //when
        regulator.setIntegral(P_FACTOR);
        regulator.setDerivative(P_FACTOR);

        //then
        //doesn't throw
    }
}
