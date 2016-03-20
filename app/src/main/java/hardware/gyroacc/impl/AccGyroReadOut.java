package hardware.gyroacc.impl;

/**
 * @author aleksander.jurczyk@seedlabs.io
 */
public class AccGyroReadOut {
    private double accX;
    private double accY;
    private double accZ;

    public void setAccX(double accX) {
        this.accX = accX;
    }

    public void setAccY(double accY) {
        this.accY = accY;
    }

    public void setAccZ(double accZ) {
        this.accZ = accZ;
    }

    public void setGyroX(double gyroX) {
        this.gyroX = gyroX;
    }

    public void setGyroY(double gyroY) {
        this.gyroY = gyroY;
    }

    public void setGyroZ(double gyroZ) {
        this.gyroZ = gyroZ;
    }

    private double gyroX;
    private double gyroY;
    private double gyroZ;

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

    public double getAccY() {
        return accY;
    }

    public double getAccZ() {
        return accZ;
    }

    public double getGyroX() {
        return gyroX;
    }

    public double getGyroY() {
        return gyroY;
    }

    public double getGyroZ() {
        return gyroZ;
    }
}
