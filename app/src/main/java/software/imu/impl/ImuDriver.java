package software.imu.impl;

import hardware.gyroacc.enums.Axis;
import software.imu.IImuDriver;

/**
 * @author aleksander.jurczyk@seedlabs.io
 */
public class ImuDriver implements IImuDriver {

    private static final int DT_MS = 10;

    public ImuDriver() {

    }

    @Override
    public float getAngle(Axis axis) {
        return 0;
    }


}
