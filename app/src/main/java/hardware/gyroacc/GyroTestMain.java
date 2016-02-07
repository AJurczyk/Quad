package hardware.gyroacc;

import hardware.gyroacc.enums.Axis;
import hardware.gyroacc.exception.AccGyroReadValueException;
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
    public static void main(String... args) throws Exception {

        II2cController i2c = new Pi4jI2c();
        Mpu6050 mpu6050 = new Mpu6050(i2c);

        double accX;
        double accY;
        double accZ;
        double gyroX;
        double gyroY;
        double gyroZ;

        //double angle;

        for (int i = 0; i < 300; i++) {
            //for (int i = 0; i < 1; i++) {
            accX = mpu6050.readAccInG(Axis.X);
            accY = mpu6050.readAccInG(Axis.Y);
            accZ = mpu6050.readAccInG(Axis.Z);

            gyroX = mpu6050.readGyroDeg(Axis.X);
            gyroY = mpu6050.readGyroDeg(Axis.Y);
            gyroZ = mpu6050.readGyroDeg(Axis.Z);
            //angle=mpu6050.readAngle(Axis.X);
            System.out.println(
                "X: " + String.format("% 2.2f", accX)
                    + "\tY: " + String.format("% 2.2f", accY)
                    + "\tZ: " + String.format("% 2.2f", accZ)
                    + "\tgyroX: " + String.format("% 4.2f", gyroX)
                    + "\tgyroY: " + String.format("% 4.2f", gyroY)
                    + "\tgyroZ: " + String.format("% 4.2f", gyroZ));
            //System.out.println(angle);
            Thread.sleep(100);
        }
    }
}
