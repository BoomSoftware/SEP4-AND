package com.example.sep4_android.models;

public class Garden {
    private String owner_google_id;
    private int id;
    private String name;
    private double land_area;
    private String city;
    private String street;
    private String number;

    public Garden(String name, double land_area, String city, String street, String number, String owner_google_id) {
        this.name = name;
        this.land_area = land_area;
        this.city = city;
        this.street = street;
        this.number = number;
        this.owner_google_id = owner_google_id;
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

    public double getLand_area() {
        return land_area;
    }

    public void setLand_area(double land_area) {
        this.land_area = land_area;
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

    public String getOwner_google_id() {
        return owner_google_id;
    }

    public void setOwner_google_id(String owner_google_id) {
        this.owner_google_id = owner_google_id;
    }
}
