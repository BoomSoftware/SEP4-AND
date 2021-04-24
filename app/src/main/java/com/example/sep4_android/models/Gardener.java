package com.example.sep4_android.models;

public class Gardener {
    private int id;
    private String name;
    private String googleID;
    private boolean isOwner;

    public Gardener(){}

    public Gardener(String name, String googleID, boolean isOwner) {
        this.name = name;
        this.googleID = googleID;
        this.isOwner = isOwner;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGoogleID() {
        return googleID;
    }

    public void setGoogleID(String googleID) {
        this.googleID = googleID;
    }

    public boolean isOwner() {
        return isOwner;
    }

    public void setOwner(boolean owner) {
        isOwner = owner;
    }
}
