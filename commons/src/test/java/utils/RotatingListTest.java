package utils;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author aleksander.jurczyk@seedlabs.io
 */
@Test(groups = {"unitTests"})
public class RotatingListTest {

    private static final int MAX_SIZE = 3;

    @Test
    public final void rotating() {
        //given
        final RotatingList<Integer> list = new RotatingList<>(MAX_SIZE);

        //when
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);

        //then
        Assert.assertEquals(list.size(), MAX_SIZE);
        Assert.assertEquals(list.get(0),(Integer)2);
        Assert.assertEquals(list.get(1),(Integer)3);
        Assert.assertEquals(list.get(2),(Integer)4);
    }
}
