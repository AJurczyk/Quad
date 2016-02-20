package hardware.gyroacc.impl;

import hardware.gyroacc.enums.Axis;
import hardware.gyroacc.exception.AccGyroIncorrectAxisException;
import hardware.gyroacc.exception.AccGyroReadValueException;
import hardware.gyroacc.exception.InvalidConversionFactor;
import hardware.i2c.II2cController;
import hardware.i2c.exception.I2cDeviceNotInitializedException;
import hardware.i2c.exception.I2cInitException;
import hardware.i2c.exception.I2cReadException;
import hardware.i2c.exception.I2cWriteException;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.mockito.Mockito.*;

/**
 * @author aleksander.jurczyk@gmail.com on 31.01.16.
 */
@Test(groups = {"unitTests"})
public class Mpu6050Test {

    private static final int I2C_ADDRESS = 0x68;

    private static final byte ACC_X_LSB_ADDR = 60;
    private static final byte ACC_X_MSB_ADDR = 59;
    private static final byte SENSITIVITY = 2;

    /**
     * Data provider for accelerometer test.
     * @return input and expected data
     */
    @DataProvider(name = "accData")
    public static Object[][] accData() {
        return new Object[][]{
            {(short) 0xFFFF, 1 * SENSITIVITY},
            {(short) 0x0000, -1 * SENSITIVITY}
        };
    }

    @Test(dataProvider = "accData")
    public final void readAccX(short rawValue, double expectedAccInG) throws I2cReadException, I2cDeviceNotInitializedException,
        I2cInitException, InvalidConversionFactor, I2cWriteException, AccGyroIncorrectAxisException, AccGyroReadValueException {
        //given
        final II2cController i2c = mock(II2cController.class);
        when(i2c.readTwoBytes(ACC_X_LSB_ADDR, ACC_X_MSB_ADDR)).thenReturn((short) rawValue);

        final Mpu6050 mpu6050 = new Mpu6050(i2c);

        //when
        final double accX = mpu6050.readAccInG(Axis.X);

        //then
        verify(i2c, times(1)).initI2cDevice(I2C_ADDRESS);
        //TODO verify config has been written
        verify(i2c, times(1)).readTwoBytes(ACC_X_LSB_ADDR, ACC_X_MSB_ADDR);
        Assert.assertEquals(accX, expectedAccInG);
    }
}
