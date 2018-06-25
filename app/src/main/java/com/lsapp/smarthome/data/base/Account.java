package com.lsapp.smarthome.data.base;

import com.lsapp.smarthome.utils.ColorUtil;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/8/15.
 */
public class Account implements Serializable {
    private String UserName;
    private String UserId;
    private int IsThirdParty;
    private String RealName;
    private String Mobile;
    private String Birthday;
    private String Sex;
    private String Province;
    private String City;
    private String Area;
    private String Address;
    private int IsAllowOperat = 0;
    private int IsConfirm;
    private int IsRelationUser;
    private int IsBindingUser = 0;
    private String MasterUser;
    private int MasterUserThirdParty;

    private int IsReceiveWarn;
    private int IsReceiveSaleMsg;
    private int IsReceiveLog;
    private int issetpwd;
    private String Nickname;
    private int nameColor;

    public int getIssetpwd() {
        return issetpwd;
    }

    public void setIssetpwd(int issetpwd) {
        this.issetpwd = issetpwd;
    }

    public String getNickname() {
        return Nickname;
    }

    public void setNickname(String nickname) {
        Nickname = nickname;
    }

    public int getNameColor() {
        return ColorUtil.getLightColor(getNickname());
    }

    public void setNameColor(int nameColor) {
        this.nameColor = nameColor;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getProvince() {
        return Province == null ? "" : Province;
    }

    public void setProvince(String province) {
        this.Province = province;
    }

    public int getIsAllowOperat() {
        return IsAllowOperat;
    }

    public void setIsAllowOperat(int isAllowOperat) {
        IsAllowOperat = isAllowOperat;
    }

    public int getIsConfirm() {
        return IsConfirm;
    }

    public void setIsConfirm(int isConfirm) {
        IsConfirm = isConfirm;
    }

    public int getIsRelationUser() {
        return IsRelationUser;
    }

    public void setIsRelationUser(int isRelationUser) {
        IsRelationUser = isRelationUser;
    }

    public int getIsBindingUser() {
        return IsBindingUser;
    }

    public void setIsBindingUser(int isBindingUser) {
        IsBindingUser = isBindingUser;
    }

    public String getMasterUser() {
        return MasterUser;
    }

    public void setMasterUser(String masterUser) {
        MasterUser = masterUser;
    }

    public int getMasterUserThirdParty() {
        return MasterUserThirdParty;
    }

    public void setMasterUserThirdParty(int masterUserThirdParty) {
        MasterUserThirdParty = masterUserThirdParty;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public int getIsThirdParty() {
        return IsThirdParty;
    }

    public void setIsThirdParty(int isThirdParty) {
        IsThirdParty = isThirdParty;
    }

    public String getRealName() {
        return RealName;
    }

    public void setRealName(String realName) {
        RealName = realName;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getBirthday() {
        return Birthday;
    }

    public void setBirthday(String birthday) {
        Birthday = birthday;
    }

    public String getSex() {
        return Sex;
    }

    public void setSex(String sex) {
        Sex = sex;
    }

    public String getCity() {
        return (City == null ? "" : City);
    }

    public void setCity(String city) {
        City = city;
    }

    public String getArea() {
        return Area == null ? "" : Area;
    }

    public void setArea(String area) {
        Area = area;
    }

    public String getAddress() {
        return Address == null ? "" : Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public int getIsReceiveWarn() {
        return IsReceiveWarn;
    }

    public void setIsReceiveWarn(int isReceiveWarn) {
        IsReceiveWarn = isReceiveWarn;
    }

    public int getIsReceiveSaleMsg() {
        return IsReceiveSaleMsg;
    }

    public void setIsReceiveSaleMsg(int isReceiveSaleMsg) {
        IsReceiveSaleMsg = isReceiveSaleMsg;
    }

    public int getIsReceiveLog() {
        return IsReceiveLog;
    }

    public void setIsReceiveLog(int isReceiveLog) {
        IsReceiveLog = isReceiveLog;
    }
}
