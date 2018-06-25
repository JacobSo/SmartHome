package com.lsapp.smarthome.data.base;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/12/30.
 */

public class GuideDevice implements Serializable{
    private String SkuCode;
    private String Mac;
    private int ItemType;
    private String Port;
    private String Ip;
    private int isConnect ;

    public int getIsConnect() {
        return isConnect;
    }

    public void setIsConnect(int isConnect) {
        this.isConnect = isConnect;
    }

    public String getSkuCode() {
        return SkuCode;
    }

    public void setSkuCode(String skuCode) {
        SkuCode = skuCode;
    }

    public String getMac() {
        return Mac;
    }

    public void setMac(String mac) {
        Mac = mac;
    }

    public int getItemType() {
        return ItemType;
    }

    public void setItemType(int itemType) {
        ItemType = itemType;
    }

    public String getPort() {
        return Port;
    }

    public void setPort(String port) {
        Port = port;
    }

    public String getIp() {
        return Ip;
    }

    public void setIp(String ip) {
        Ip = ip;
    }
}
