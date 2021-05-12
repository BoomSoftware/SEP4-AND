package com.example.sep4_android.models;

import java.time.LocalDate;

public class Plant {
    private int plant_ID;
    private String garden_name;
    private int height;
    private int width;
    private String stage_of_growth;
    private String soil_type;
    private int own_soil_volume;
    private String garden_location;
    private String common_plant_name;
    private String category_name;
    private String seeded_at;
    private String harvested_at;

    public Plant(String garden_name, int height, int width, String stage_of_growth, String soil_type, int own_soil_volume, String common_plant_name, String category_name) {
        this.garden_name = garden_name;
        this.height = height;
        this.width = width;
        this.stage_of_growth = stage_of_growth;
        this.soil_type = soil_type;
        this.own_soil_volume = own_soil_volume;
        this.common_plant_name = common_plant_name;
        this.category_name = category_name;
        seeded_at = LocalDate.now().toString();
    }

    public int getPlant_ID() {
        return plant_ID;
    }

    public void setPlant_ID(int plant_ID) {
        this.plant_ID = plant_ID;
    }

    public String getGarden_name() {
        return garden_name;
    }

    public void setGarden_name(String garden_name) {
        this.garden_name = garden_name;
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

    public String getStage_of_growth() {
        return stage_of_growth;
    }

    public void setStage_of_growth(String stage_of_growth) {
        this.stage_of_growth = stage_of_growth;
    }

    public String getSoil_type() {
        return soil_type;
    }

    public void setSoil_type(String soil_type) {
        this.soil_type = soil_type;
    }

    public int getOwn_soil_volume() {
        return own_soil_volume;
    }

    public void setOwn_soil_volume(int own_soil_volume) {
        this.own_soil_volume = own_soil_volume;
    }

    public String getGarden_location() {
        return garden_location;
    }

    public void setGarden_location(String garden_location) {
        this.garden_location = garden_location;
    }

    public String getCommon_plant_name() {
        return common_plant_name;
    }

    public void setCommon_plant_name(String common_plant_name) {
        this.common_plant_name = common_plant_name;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getSeeded_at() {
        return seeded_at;
    }

    public void setSeeded_at(String  seeded_at) {
        this.seeded_at = seeded_at;
    }

    public String getHarvested_at() {
        return harvested_at;
    }

    public void setHarvested_at(String harvested_at) {
        this.harvested_at = harvested_at;
    }
}
