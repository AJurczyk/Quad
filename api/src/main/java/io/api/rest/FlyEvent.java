package io.api.rest;

import io.api.rest.enums.EventType;

/**
 * Event with data from flight controller.
 *
 * @author aleksander.jurczyk@gmail.com on 02.07.16.
 */
public class FlyEvent {
    private final EventType type;

    private final Object value;

    public FlyEvent(EventType type, Object value) {
        this.type = type;
        this.value = value;
    }

    public EventType getType() {
        return type;
    }

    public Object getValue() {
        return value;
    }
}
