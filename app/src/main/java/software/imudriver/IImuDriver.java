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
    void startWorking();

    /**
     * Stops measuring loop.
     */
    void stopWorking();

    /**
     * Check if measuring loop is running.
     */
    boolean isWorking();

    /**
     * Register listener that handles measurements.
     *
     * @param listener listener to register
     */
    void registerListener(IImuReadingListener listener);
}
