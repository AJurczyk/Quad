package utils;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import utils.exceptions.InvalidArgException;

/**
 * @author aleksander.jurczyk@seedlabs.io
 */


@Test(groups = {"unitTests"})
public class ByteArrayUtilsTest {

    /**
     * Data provider for cast to short test.
     * @return input and expected data
     */
    @DataProvider(name = "exampleByteArray")
    public static Object[][] exampleByteArray() {
        return new Object[][]{
            {new byte[]{0x00, 0x00}, (int) 0x0000},
            {new byte[]{0x01, 0x23}, (int) 0x0123},
            {new byte[]{(byte) 0xFF, (byte) 0xFF}, (int) 0xFFFF}
        };
    }

    @Test(dataProvider = "exampleByteArray")
    public final void castToShort(byte[] givenArray, short expected) throws InvalidArgException {
        //when
        final short converted = ByteArrayUtils.castToShort(givenArray);

        //then
        Assert.assertEquals(converted, expected);
    }
}
