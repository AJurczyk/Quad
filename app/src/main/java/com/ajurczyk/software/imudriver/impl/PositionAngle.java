package com.ajurczyk.software.imudriver.impl;

/**
 * Inclination from plains.
 *
 * @author aleksander.jurczyk@seedlabs.io
 */
public class PositionAngle {

    private float angleX;
    private float angleY;
    private float angleZ;

    /**
     * Constructor with all the angles.
     *
     * @param angleX x axis inclination
     * @param angleY y axis inclination
     * @param angleZ z axis inclination
     */
    public PositionAngle(float angleX, float angleY, float angleZ) {
        this.angleX = angleX;
        this.angleY = angleY;
        this.angleZ = angleZ;
    }

    public PositionAngle() {

    }

    public float getAngleX() {
        return angleX;
    }

    public void setAngleX(float angleX) {
        this.angleX = angleX;
    }

    public float getAngleY() {
        return angleY;
    }

    public void setAngleY(float angleY) {
        this.angleY = angleY;
    }

    public float getAngleZ() {
        return angleZ;
    }

    public void setAngleZ(float angleZ) {
        this.angleZ = angleZ;
    }
}
