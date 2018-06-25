package com.lsapp.smarthome.data.base;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/9/18.
 */
public class Sensor implements Serializable{
    private String SensorName;
    private int State;
    private String DataTime;

    public String getSensorName() {
        return SensorName;
    }

    public void setSensorName(String sensorName) {
        SensorName = sensorName;
    }

    public int getState() {
        return State;
    }

    public void setState(int state) {
        State = state;
    }

    public String getDataTime() {
        return DataTime;
    }

    public void setDataTime(String dataTime) {
        DataTime = dataTime;
    }
}
