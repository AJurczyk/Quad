package hardware.gyroacc.impl;

/**
 * Object holds all measurement data.
 *
 * @author aleksander.jurczyk@seedlabs.io
 */
public class AccGyroReadOut {
    private double accX;
    private double accY;
    private double accZ;

    private double gyroX;
    private double gyroY;
    private double gyroZ;

    /**
     * Main constructor.
     * @param accX acceleration reading X axis
     * @param accY acceleration reading y axis
     * @param accZ acceleration reading z axis
     * @param gyroX gyro reading X axis
     * @param gyroY gyro reading Y axis
     * @param gyroZ gyro reading Z axis
     */
    public AccGyroReadOut(double accX, double accY, double accZ, double gyroX, double gyroY, double gyroZ) {
        this.accX = accX;
        this.accY = accY;
        this.accZ = accZ;
        this.gyroX = gyroX;
        this.gyroY = gyroY;
        this.gyroZ = gyroZ;
    }

    public double getAccX() {
        return accX;
    }

    public void setAccX(double accX) {
        this.accX = accX;
    }

    public double getAccY() {
        return accY;
    }

    public void setAccY(double accY) {
        this.accY = accY;
    }

    public double getAccZ() {
        return accZ;
    }

    public void setAccZ(double accZ) {
        this.accZ = accZ;
    }

    public double getGyroX() {
        return gyroX;
    }

    public void setGyroX(double gyroX) {
        this.gyroX = gyroX;
    }

    public double getGyroY() {
        return gyroY;
    }

    public void setGyroY(double gyroY) {
        this.gyroY = gyroY;
    }

    public double getGyroZ() {
        return gyroZ;
    }

    public void setGyroZ(double gyroZ) {
        this.gyroZ = gyroZ;
    }
}
