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
        final double median = MathUtils.median(1d, 80d, 2d, 3d, 90d);

        //then
        Assert.assertEquals(median, 3d);
    }

    @Test
    public final void evenSet() {
        //when
        final double median = MathUtils.median(1d, 80d, 2d, 4d);

        //then
        Assert.assertEquals(median, 3d);
    }
}
