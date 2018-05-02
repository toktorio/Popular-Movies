package com.timotiusoktorio.popularmovies.data.model;

@SuppressWarnings("unused")
public class Cast {

    private int id;
    private String profilePath;
    private String name;
    private String character;

    public Cast() {
    }

    public Cast(int id, String profilePath, String name, String character) {
        this.id = id;
        this.profilePath = profilePath;
        this.name = name;
        this.character = character;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProfilePath() {
        return profilePath;
    }

    public void setProfilePath(String profilePath) {
        this.profilePath = profilePath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCharacter() {
        return character;
    }

    public void setCharacter(String character) {
        this.character = character;
    }
}