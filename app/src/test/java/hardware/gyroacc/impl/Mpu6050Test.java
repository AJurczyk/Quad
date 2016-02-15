package hardware.gyroacc.impl;

import hardware.gyroacc.enums.Axis;
import hardware.gyroacc.exception.AccGyroIncorrectAxisException;
import hardware.gyroacc.exception.AccGyroReadValueException;
import hardware.gyroacc.exception.invalidConversionFactor;
import hardware.i2c.II2cController;
import hardware.i2c.exception.I2cDeviceNotInitializedException;
import hardware.i2c.exception.I2cInitException;
import hardware.i2c.exception.I2cReadException;
import hardware.i2c.exception.I2cWriteException;
import org.testng.Assert;
import org.testng.annotations.Test;

import static org.mockito.Mockito.*;

/**
 * @author aleksander.jurczyk@gmail.com on 31.01.16.
 */
@Test(groups = {"unitTests"})
public class Mpu6050Test {

    private static final int I2C_ADDRESS = 0x68;

    private static final byte ACC_X_LSB_ADDR = 0x3B;
    private static final byte ACC_X_MSB_ADDR = 0x3C;



    @Test
    public final void readAccX() throws I2cReadException, I2cDeviceNotInitializedException,
        I2cInitException, invalidConversionFactor, I2cWriteException, AccGyroIncorrectAxisException, AccGyroReadValueException {
        //given
        int rawValue=1; int finalValue=3;
        II2cController i2c = mock(II2cController.class);
        when(i2c.readTwoBytes(ACC_X_LSB_ADDR, ACC_X_MSB_ADDR)).thenReturn((short)rawValue);

        Mpu6050 mpu6050 = new Mpu6050(i2c);

        //when
        final double accX = mpu6050.readAccInG(Axis.X);

        //then
        verify(i2c, times(1)).initI2cDevice(I2C_ADDRESS);
        //TODO verify config has been written
        verify(i2c, times(1)).readTwoBytes(ACC_X_LSB_ADDR, ACC_X_MSB_ADDR);
        Assert.assertEquals(accX, finalValue);
    }
}
