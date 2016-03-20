package io.api.rest;

/**
 * Hello class for testing purpose. //TODO to remove
 * @author aleksander.jurczyk@seedlabs.io
 */
@SuppressWarnings("PMD")
public class Hello {
    private final int id;
    private final String name;

    public Hello(int id, String name) {
        this.name = name;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
