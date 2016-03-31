package software.imudriver;

import software.imudriver.impl.PositionAngle;

/**
 * @author aleksander.jurczyk@seedlabs.io
 */
public interface IImuDriver {

    /**
     * Get current precise inclination from every plain.
     *
     * @return container of angles measured in in degrees
     */
    PositionAngle getAngle();

    /**
     * Starts measuring loop.
     */
    void startReading();

    /**
     * Stops measuring loop.
     */
    void stopReading();

    /**
     * Check if measuring loop is running.
     */
    boolean isReading();
}
