package hardware.gyroacc.impl;

import hardware.gyroacc.enums.Axis;
import hardware.gyroacc.exception.AccGyroIncorrectAxisException;
import hardware.gyroacc.exception.AccGyroReadValueException;
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

    private static final byte SLEEP_DISABLE_VAL = 0x00; //TODO: check if this is disable value
    private static final byte SLEEP_REGISTRY = 0x6b;

    private static final byte FAKE_ACC_X = 0;
    private static final int EXPECTED_ACC_X = 0;

    private static final byte ACC_X_L = 0x3B;
    private static final byte ACC_X_H = 0x3C;

    private static final byte ACC_Y_L = 0x3D;
    private static final byte ACC_Y_H = 0x3E;

    private static final byte ACC_Z_L = 0x3F;
    private static final byte ACC_Z_H = 0x40;

    private static final byte ROT_X = 0x00; //TODO get the address
    private static final byte ROT_Y = 0x00; //TODO get the address
    private static final byte ROT_Z = 0x00; //TODO get the address

    @Test
    void readAccX() throws I2cDeviceNotInitializedException, I2cInitException, I2cWriteException,
        AccGyroIncorrectAxisException, AccGyroReadValueException, I2cReadException {
        //given
        II2cController i2c = mock(II2cController.class);
        when(i2c.read(ACC_X_L)).thenReturn(FAKE_ACC_X);
        when(i2c.read(ACC_X_H)).thenReturn(FAKE_ACC_X);

        Mpu6050 mpu6050 = new Mpu6050(i2c);

        //when
        final int accValue = mpu6050.readAcc(Axis.X);

        //then
        verify(i2c, times(1)).initI2cDevice(I2C_ADDRESS);
        verify(i2c, times(1)).write(SLEEP_REGISTRY, SLEEP_DISABLE_VAL);
        verify(i2c, times(1)).read(ACC_X_L);
        verify(i2c, times(1)).read(ACC_X_H);
        Assert.assertEquals(accValue, EXPECTED_ACC_X);
    }

    @Test
    void readAccY() {
    }

    @Test
    void readAccZ() {
    }
}
