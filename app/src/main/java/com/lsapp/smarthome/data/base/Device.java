package com.lsapp.smarthome.data.base;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/9/18.
 */
public class Device implements Serializable {
    private String DeviceId;
    private int ItemType;
    private String TypeName;

    private String ItemCode;
    private String SkuName;
    private String SkuCode;
    private String PicPath;
    private String ItemName;
    private int IsConnect=0;
    private int UpInterval;
    private Control airClerState = null;
    private List<Sensor> SensorInfo;

    private int worklightOn;
    private int fanOn;

    public Control getAirClerState() {
        return airClerState;
    }

    public void setAirClerState(Control airClerState) {
        this.airClerState = airClerState;
    }

    public String getTypeName() {
        if (getItemType()==1) {
            return "设备类型：检测器";
        } else if (getItemType()==2) {
            return "设备类型：净化器";

        } else if (getItemType()==3) {
            return "设备类型：检测与净化";

        } else {
            return "设备类型：未知";

        }
    }

    public int getWorklightOn() {
        return worklightOn;
    }

    public void setWorklightOn(int worklightOn) {
        this.worklightOn = worklightOn;
    }

    public int getFanOn() {
        return fanOn;
    }

    public void setFanOn(int fanOn) {
        this.fanOn = fanOn;
    }

    public int getUpInterval() {
        return UpInterval;
    }

    public void setUpInterval(int upInterval) {
        UpInterval = upInterval;
    }

    public void setTypeName(String typeName) {
        TypeName = typeName;
    }

    public int getIsConnect() {
        return IsConnect;
    }

    public void setIsConnect(int isConnect) {
        IsConnect = isConnect;
    }


    public String getDeviceId() {
        return DeviceId;
    }

    public void setDeviceId(String deviceId) {
        DeviceId = deviceId;
    }

    public int getItemType() {
        return ItemType;
    }

    public void setItemType(int itemType) {
        ItemType = itemType;
    }

    public List<Sensor> getSensorInfo() {
        return SensorInfo;
    }

    public void setSensorInfo(List<Sensor> sensorInfo) {
        SensorInfo = sensorInfo;
    }

    public String getItemCode() {
        return ItemCode;
    }

    public void setItemCode(String itemCode) {
        ItemCode = itemCode;
    }

    public String getSkuName() {
        return SkuName;
    }

    public void setSkuName(String skuName) {
        SkuName = skuName;
    }

    public String getSkuCode() {
        return SkuCode;
    }

    public void setSkuCode(String skuCode) {
        SkuCode = skuCode;
    }

    public String getPicPath() {
        return PicPath;
    }

    public void setPicPath(String picPath) {
        PicPath = picPath;
    }

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }
}
