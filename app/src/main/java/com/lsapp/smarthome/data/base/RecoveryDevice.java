package com.lsapp.smarthome.data.base;

/**
 * Created by Administrator on 2017/1/12.
 */

public class RecoveryDevice {
    private String UpdateTime;
    private int ItemType;
    private String UserName;
    private String SkuCode;
    private String Mac;
    private int IsThirdParty;
    private String PicPath;
    private String UserId;
    private String ItemName;
    private String ItemTypeName;

    public void setItemTypeName(String itemTypeName) {
        ItemTypeName = itemTypeName;
    }

    public String getTypeName() {
        if (getItemType()==1) {
            return "设备类型：检测设备";
        } else if (getItemType()==2) {
            return "设备类型：执行设备";

        } else if (getItemType()==3) {
            return "设备类型：检测与执行";

        } else {
            return "设备类型：未知";

        }
    }

    public String getTypeNamePure() {
        if (getItemType()==1) {
            return "检测设备";
        } else if (getItemType()==2) {
            return "执行设备";

        } else if (getItemType()==3) {
            return "检测与执行";

        } else {
            return "未知";

        }
    }
    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public String getUpdateTime() {
        return UpdateTime;
    }

    public void setUpdateTime(String updateTime) {
        UpdateTime = updateTime;
    }

    public int getItemType() {
        return ItemType;
    }

    public void setItemType(int itemType) {
        ItemType = itemType;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
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

    public int getIsThirdParty() {
        return IsThirdParty;
    }

    public void setIsThirdParty(int isThirdParty) {
        IsThirdParty = isThirdParty;
    }

    public String getPicPath() {
        return PicPath;
    }

    public void setPicPath(String picPath) {
        PicPath = picPath;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }
}
