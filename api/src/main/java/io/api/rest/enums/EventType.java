package io.api.rest.enums;

/**
 * Enum holds possible fly event types used by {@link io.api.rest.Controller}.
 *
 * @author aleksander.jurczyk@gmail.com on 02.07.16.
 */
public enum EventType {
    GYRO_CLEAN,
    GYRO_RAW,
    GYRO_ANGLE,
    MOTOR_POWER,
    FLIGHT_CONTROLLER_ANGLE,
    REGULATION
}
