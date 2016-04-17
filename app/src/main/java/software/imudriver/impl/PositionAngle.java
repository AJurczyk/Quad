package software.imudriver.impl;

/**
 * Inclination from plains.
 * @author aleksander.jurczyk@seedlabs.io
 */
public class PositionAngle {

    private final double angleX;
    private final double angleY;
    private final double angleZ;

    /**
     * Constructor with all the angles.
     * @param angleX x axis inclination
     * @param angleY y axis inclination
     * @param angleZ z axis inclination
     */
    public PositionAngle(double angleX, double angleY, double angleZ) {
        this.angleX = angleX;
        this.angleY = angleY;
        this.angleZ = angleZ;
    }

    public double getAngleX() {
        return angleX;
    }

    public double getAngleY() {
        return angleY;
    }

    public double getAngleZ() {
        return angleZ;
    }
}
