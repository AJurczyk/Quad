package software.imudriver.impl;

/**
 * Inclination from plains.
 * @author aleksander.jurczyk@seedlabs.io
 */
public class PositionAngle {

    private float AngleX;

    private float AngleY;

    private float AngleZ;

    public PositionAngle(float angleX, float angleY, float angleZ) {
        AngleX = angleX;
        AngleY = angleY;
        AngleZ = angleZ;
    }

    public float getAngleX() {
        return AngleX;
    }

    public float getAngleY() {
        return AngleY;
    }

    public float getAngleZ() {
        return AngleZ;
    }
}
