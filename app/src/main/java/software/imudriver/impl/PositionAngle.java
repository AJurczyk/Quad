package software.imudriver.impl;

/**
 * Inclination from plains.
 * @author aleksander.jurczyk@seedlabs.io
 */
public class PositionAngle {

    private final float angleX;

    private final float angleY;

    private final float angleZ;

    /**
     * Constructor with all the angles.
     * @param angleX x axis inclination
     * @param angleY y axis inclination
     * @param angleZ z axis inclination
     */
    public PositionAngle(float angleX, float angleY, float angleZ) {
        this.angleX = angleX;
        this.angleY = angleY;
        this.angleZ = angleZ;
    }

    public float getAngleX() {
        return angleX;
    }

    public float getAngleY() {
        return angleY;
    }

    public float getAngleZ() {
        return angleZ;
    }
}
