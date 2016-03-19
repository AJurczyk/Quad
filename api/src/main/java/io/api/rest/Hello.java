package io.api.rest;

/**
 * @author aleksander.jurczyk@seedlabs.io
 */
public class Hello {
    private int id;
    private String name;

    public Hello(int id, String name){
        this.name = name;
        this. id = id;
    }

    public int getId(){
        return id;
    }

    public String getName() {
        return name;
    }
}
