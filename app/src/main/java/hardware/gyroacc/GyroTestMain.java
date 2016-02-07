package hardware.gyroacc;

import hardware.gyroacc.enums.Axis;
import hardware.gyroacc.exception.AccGyroReadValueException;
import hardware.gyroacc.exception.AccGyroIncorrectAxisException;
import hardware.gyroacc.impl.Mpu6050;
import hardware.i2c.II2cController;
import hardware.i2c.exception.I2cDeviceNotInitializedException;
import hardware.i2c.exception.I2cInitException;
import hardware.i2c.exception.I2cWriteException;
import hardware.i2c.impl.Pi4jI2c;


/**
 * Test method to provide some main.
 *
 * @author aleksander.jurczyk@gmail.com on 26.01.16.
 */
@SuppressWarnings("PMD")
public class GyroTestMain {
    //TODO to remove class

    /**
     * Main to be removed.
     *
     * @param args some args
     * @throws I2cDeviceNotInitializedException some problems
     * @throws I2cInitException                 some problems
     * @throws I2cWriteException                some problems
     * @throws AccGyroReadValueException        some problems
     */
    public static void main(String... args) throws I2cDeviceNotInitializedException, I2cInitException,
            I2cWriteException, AccGyroReadValueException, AccGyroIncorrectAxisException, InterruptedException {

        II2cController i2c = new Pi4jI2c();
        Mpu6050 mpu6050 = new Mpu6050(i2c);

        int accX;

        for (int i = 0; i < 100; i++) {
            accX = mpu6050.readAcc(Axis.Z);
            System.out.println(accX);
            Thread.sleep(100);
        }
    }
}
