package com.ajurczyk.hardware.gyroacc.impl;

/**
 * Object holds all measurement data.
 *
 * @author aleksander.jurczyk@seedlabs.io
 */
public class AccGyroData {
    private float accX;
    private float accY;
    private float accZ;

    private float gyroX;
    private float gyroY;
    private float gyroZ;

    public AccGyroData(){

    }

    /**
     * Main constructor.
     * @param accX acceleration reading X axis
     * @param accY acceleration reading y axis
     * @param accZ acceleration reading z axis
     * @param gyroX gyro reading X axis
     * @param gyroY gyro reading Y axis
     * @param gyroZ gyro reading Z axis
     */
    public AccGyroData(float accX, float accY, float accZ, float gyroX, float gyroY, float gyroZ) {
        this.accX = accX;
        this.accY = accY;
        this.accZ = accZ;
        this.gyroX = gyroX;
        this.gyroY = gyroY;
        this.gyroZ = gyroZ;
    }

    public float getAccX() {
        return accX;
    }

    public void setAccX(float accX) {
        this.accX = accX;
    }

    public float getAccY() {
        return accY;
    }

    public void setAccY(float accY) {
        this.accY = accY;
    }

    public float getAccZ() {
        return accZ;
    }

    public void setAccZ(float accZ) {
        this.accZ = accZ;
    }

    public float getGyroX() {
        return gyroX;
    }

    public void setGyroX(float gyroX) {
        this.gyroX = gyroX;
    }

    public float getGyroY() {
        return gyroY;
    }

    public void setGyroY(float gyroY) {
        this.gyroY = gyroY;
    }

    public float getGyroZ() {
        return gyroZ;
    }

    public void setGyroZ(float gyroZ) {
        this.gyroZ = gyroZ;
    }
}
