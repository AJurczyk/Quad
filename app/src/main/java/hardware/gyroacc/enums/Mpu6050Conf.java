package hardware.gyroacc.enums;

/**
 * Config values of mpu6050.
 *
 * @author aleksander.jurczyk@gmail.com on 31.01.16.
 */
public class Mpu6050Conf {
    /**
     * Just wakes the device up, because it sets the sleep bit to 0. Also sets
     * the clock source to internal.
     */
    public static final byte PWR_MGMT_1 = 0b00000000;

    /**
     * Sets the full scale range of the gyroscopes to ± 2000 °/s.
     */
    public static final byte GYRO_CONFIG = 0b00011000;

    /**
     * Sets the smaple rate divider for the gyroscopes and accelerometers. This
     * means<br> acc-rate = 1kHz / 1+ sample-rate<br> and <br>gyro-rate = 8kHz /
     * 1+ sample-rate. <br> <br> The concrete value 0 leaves the sample rate on
     * default, which means 1kHz for acc-rate and 8kHz for gyr-rate.
     */
    public static final byte SMPLRT_DIV = 0b00000000;

    /**
     * Setting the digital low pass filter to <br>
     * Acc Bandwidth (Hz) = 184 <br>
     * Acc Delay (ms) = 2.0 <br>
     * Gyro Bandwidth (Hz) = 188 <br>
     * Gyro Delay (ms) = 1.9 <br>
     * Fs (kHz) = 1
     *
     */
    public static final byte CONFIG = 0b00000000;
    //public static final byte CONFIG = 0b00000111;

    /**
     * Setting accelerometer sensitivity to ± 4g.
     */
    public static final byte ACCEL_CONFIG = 0b00001000;

    /**
     * Disabling FIFO buffer.
     */
    public static final byte FIFO_EN = 0b00000000;

    /**
     * Disabling interrupts.
     */
    public static final byte INT_ENABLE = 0b00000000;

    /**
     * Disabling standby modes.
     */
    public static final byte PWR_MGMT_2 = 0b00000000;

}
