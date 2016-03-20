package io.api.rest;

/**
 * @author aleksander.jurczyk@seedlabs.io
 */
public class GyroResult {
    double accX;
    double accY;
    double accZ;
    double gyroX;
    double gyroY;
    double gyroZ;

    public GyroResult(double accX, double accY, double accZ, double gyroX, double gyroY, double gyroZ) {
        this.accX = accX;
        this.accY = accY;
        this.accZ = accZ;
        this.gyroX = gyroX;
        this.gyroY = gyroY;
        this.gyroZ = gyroZ;
    }
}
