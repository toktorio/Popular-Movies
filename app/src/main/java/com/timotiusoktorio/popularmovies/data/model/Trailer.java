package com.timotiusoktorio.popularmovies.data.model;

@SuppressWarnings("unused")
public class Trailer {

    private String key;
    private String name;
    private String type;

    public Trailer() {
    }

    public Trailer(String key, String name, String type) {
        this.key = key;
        this.name = name;
        this.type = type;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}