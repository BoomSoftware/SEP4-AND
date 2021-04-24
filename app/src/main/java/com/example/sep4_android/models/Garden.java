package com.example.sep4_android.models;

public class Garden {
    private int id;
    private String name;
    private double landArea;
    private String city;
    private String street;
    private String number;


    public Garden(String name, double landArea, String city, String street, String number) {
        this.name = name;
        this.landArea = landArea;
        this.city = city;
        this.street = street;
        this.number = number;
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

    public double getLandArea() {
        return landArea;
    }

    public void setLandArea(double landArea) {
        this.landArea = landArea;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
