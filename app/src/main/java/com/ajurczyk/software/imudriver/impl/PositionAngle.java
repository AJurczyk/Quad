package com.ajurczyk.software.imudriver.impl;

/**
 * Inclination from plains.
 *
 * @author aleksander.jurczyk@seedlabs.io
 */
public class PositionAngle {

    private double angleX;
    private double angleY;
    private double angleZ;

    /**
     * Constructor with all the angles.
     *
     * @param angleX x axis inclination
     * @param angleY y axis inclination
     * @param angleZ z axis inclination
     */
    public PositionAngle(double angleX, double angleY, double angleZ) {
        this.angleX = angleX;
        this.angleY = angleY;
        this.angleZ = angleZ;
    }

    public PositionAngle() {

    }

    public double getAngleX() {
        return angleX;
    }

    public void setAngleX(double angleX) {
        this.angleX = angleX;
    }

    public double getAngleY() {
        return angleY;
    }

    public void setAngleY(double angleY) {
        this.angleY = angleY;
    }

    public double getAngleZ() {
        return angleZ;
    }

    public void setAngleZ(double angleZ) {
        this.angleZ = angleZ;
    }
}
