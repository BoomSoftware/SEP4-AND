package com.example.sep4_android.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.time.LocalDate;

@Entity
public class Plant {
    @SerializedName("plant_ID")
    @PrimaryKey
    private int plantID;
    @SerializedName("garden_name")
    private String gardenName;
    private int height;
    @SerializedName("stage_of_growth")
    private String stageOfGrowth;
    @SerializedName("soil_type")
    private String soilType;
    @SerializedName("own_soil_volume")
    private int ownSoilVolume;
    @SerializedName("garden_location")
    private String gardenLocation;
    @SerializedName("common_plant_name")
    private String commonPlantName;
    @SerializedName("category_name")
    private String categoryName;
    @SerializedName("seeded_at")
    private String seededAt;
    @SerializedName("harvested_at")
    private String harvestedAt;

    public Plant(String gardenName, int height, String stageOfGrowth, String soilType, int ownSoilVolume, String commonPlantName, String categoryName, String gardenLocation) {
        this.gardenName = gardenName;
        this.height = height;
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
