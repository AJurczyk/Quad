package software.imu.impl;

/**
 * Inclination from plains.
 * @author aleksander.jurczyk@seedlabs.io
 */
public class PositionAngle {

    private float AngleXY;

    private float AngleXZ;

    private float AngleYZ;

    public PositionAngle(float angleXY, float angleXZ, float angleYZ) {
        AngleXY = angleXY;
        AngleXZ = angleXZ;
        AngleYZ = angleYZ;
    }

    public float getAngleXY() {
        return AngleXY;
    }

    public float getAngleXZ() {
        return AngleXZ;
    }

    public float getAngleYZ() {
        return AngleYZ;
    }
}
