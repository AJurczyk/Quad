package utils;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author aleksander.jurczyk@seedlabs.io
 */
@Test(groups = {"unitTests"})
public class RotatingListTest {

    @Test
    void rotating() {
        //given
        int size = 3;
        RotatingList<Integer> list = new RotatingList<>(size);

        //when
        list.push(1);
        list.push(2);
        list.push(3);
        list.push(4);

        //then
        Assert.assertEquals(list.size(), size);
        Assert.assertEquals((Integer)list.get(0),(Integer)2);
        Assert.assertEquals((Integer)list.get(1),(Integer)3);
        Assert.assertEquals((Integer)list.get(2),(Integer)4);
    }
}
