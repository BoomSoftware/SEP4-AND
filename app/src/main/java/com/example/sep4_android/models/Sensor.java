package com.example.sep4_android.models;

public class Sensor {
    int lightSensorId;
    int co2SensorId;
    int tempHumiditySensorId;

    public Sensor(int lightSensorId, int co2SensorId, int tempHumiditySensorId) {
        this.lightSensorId = lightSensorId;
        this.co2SensorId = co2SensorId;
        this.tempHumiditySensorId = tempHumiditySensorId;
    }

    public int getLightSensorId() {
        return lightSensorId;
    }

    public void setLightSensorId(int lightSensorId) {
        this.lightSensorId = lightSensorId;
    }

    public int getCo2SensorId() {
        return co2SensorId;
    }

    public void setCo2SensorId(int co2SensorId) {
        this.co2SensorId = co2SensorId;
    }

    public int getTempHumiditySensorId() {
        return tempHumiditySensorId;
    }

    public void setTempHumiditySensorId(int tempHumiditySensorId) {
        this.tempHumiditySensorId = tempHumiditySensorId;
    }
}
