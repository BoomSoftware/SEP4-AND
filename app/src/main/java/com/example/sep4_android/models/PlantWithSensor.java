package com.example.sep4_android.models;

public class PlantWithSensor {
    private Plant plant;
    private Sensor sensor;

    public PlantWithSensor(Plant plant, Sensor sensor) {
        this.plant = plant;
        this.sensor = sensor;
    }

    public Plant getPlant() {
        return plant;
    }

    public void setPlant(Plant plant) {
        this.plant = plant;
    }

    public Sensor getSensor() {
        return sensor;
    }

    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }
}
