package com.ajurczyk.utils;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author aleksander.jurczyk@seedlabs.io
 */
@Test(groups = {"unitTests"})
public class MathUtilsTest {

    @Test
    public final void oddSet() {
        //when
        final float median = MathUtils.median(1f, 80f, 2f, 3f, 90f);

        //then
        Assert.assertEquals(median, 3f);
    }

    @Test
    public final void evenSet() {
        //when
        final float median = MathUtils.median(1f, 80f, 2f, 4f);

        //then
        Assert.assertEquals(median, 3f);
    }
}
