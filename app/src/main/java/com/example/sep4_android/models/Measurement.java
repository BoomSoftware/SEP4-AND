package com.example.sep4_android.models;

public class Measurement {
    private String time;
    private String date;
    private double measurementValue;
    private String measurementType;

    public Measurement(String time, String date, double measurementValue, String measurementType) {
        this.time = time;
        this.date = date;
        this.measurementValue = measurementValue;
        this.measurementType = measurementType;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getMeasurementValue() {
        return measurementValue;
    }

    public void setMeasurementValue(double measurementValue) {
        this.measurementValue = measurementValue;
    }

    public String getMeasurementType() {
        return measurementType;
    }

    public void setMeasurementType(String measurementType) {
        this.measurementType = measurementType;
    }

    @Override
    public String toString() {
        return "Measurement{" +
                "time='" + time + '\'' +
                ", date='" + date + '\'' +
                ", measurementValue=" + measurementValue +
                ", measurementType='" + measurementType + '\'' +
                '}';
    }
}
