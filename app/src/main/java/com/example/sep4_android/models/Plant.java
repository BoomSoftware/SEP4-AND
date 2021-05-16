package com.example.sep4_android.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.LocalDate;

@Entity
public class Plant {
    @PrimaryKey
    private int plantID;
    private String gardenName;
    private int height;
    private int width;
    private String stageOfGrowth;
    private String soilType;
    private int ownSoilVolume;
    private String gardenLocation;
    private String commonPlantName;
    private String categoryName;
    private String seededAt;
    private String harvestedAt;

    public Plant(String gardenName, int height, int width, String stageOfGrowth, String soilType, int ownSoilVolume, String commonPlantName, String categoryName, String gardenLocation) {
        this.gardenName = gardenName;
        this.height = height;
        this.width = width;;
        this.stageOfGrowth = stageOfGrowth;
        this.soilType = soilType;
        this.ownSoilVolume = ownSoilVolume;
        this.commonPlantName = commonPlantName;
        this.categoryName = categoryName;
        this.gardenLocation = gardenLocation;
        seededAt = LocalDate.now().toString();
    }

    public int getPlantID() {
        return plantID;
    }

    public void setPlantID(int plantID) {
        this.plantID = plantID;
    }

    public String getGardenName() {
        return gardenName;
    }

    public void setGardenName(String gardenName) {
        this.gardenName = gardenName;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public String getStageOfGrowth() {
        return stageOfGrowth;
    }

    public void setStageOfGrowth(String stageOfGrowth) {
        this.stageOfGrowth = stageOfGrowth;
    }

    public String getSoilType() {
        return soilType;
    }

    public void setSoilType(String soilType) {
        this.soilType = soilType;
    }

    public int getOwnSoilVolume() {
        return ownSoilVolume;
    }

    public void setOwnSoilVolume(int ownSoilVolume) {
        this.ownSoilVolume = ownSoilVolume;
    }

    public String getGardenLocation() {
        return gardenLocation;
    }

    public void setGardenLocation(String gardenLocation) {
        this.gardenLocation = gardenLocation;
    }

    public String getCommonPlantName() {
        return commonPlantName;
    }

    public void setCommonPlantName(String commonPlantName) {
        this.commonPlantName = commonPlantName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getSeededAt() {
        return seededAt;
    }

    public void setSeededAt(String seededAt) {
        this.seededAt = seededAt;
    }

    public String getHarvestedAt() {
        return harvestedAt;
    }

    public void setHarvestedAt(String harvestedAt) {
        this.harvestedAt = harvestedAt;
    }
}
