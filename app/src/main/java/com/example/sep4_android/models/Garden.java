package com.example.sep4_android.models;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.HashMap;
import java.util.Map;

@Entity
public class Garden {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String ownerGoogleId;
    private String name;
    private double landArea;
    private String city;
    private String street;
    private String number;
    @Ignore
    private Map<String, Boolean> assistantList = new HashMap<>();

    public Garden(){}

    public Garden(String name, double landArea, String city, String street, String number, String ownerGoogleId) {
        this.name = name;
        this.landArea = landArea;
        this.city = city;
        this.street = street;
        this.number = number;
        this.ownerGoogleId = ownerGoogleId;

        assistantList.put(ownerGoogleId, true);
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

    public String getOwnerGoogleId() {
        return ownerGoogleId;
    }

    public void setOwnerGoogleId(String ownerGoogleId) {
        this.ownerGoogleId = ownerGoogleId;
    }


    public Map<String, Boolean> getAssistantList() {
        return assistantList;
    }

    public void setAssistantList(Map<String, Boolean> assistantList) {
        this.assistantList = assistantList;
    }

    public void addWaitingAssistant(String assistantGoogleId){
        this.assistantList.put(assistantGoogleId, false);
    }

    public void approveAssistant(String assistantGoogleId){
        this.assistantList.put(assistantGoogleId, true);
    }

    public void removeAssistant(String assistantGoogleId){
        this.assistantList.remove(assistantGoogleId);
    }
}
