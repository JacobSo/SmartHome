package com.lsapp.smarthome.data.base;

import com.zuni.library.utils.zDateUtil;

import java.io.Serializable;
import java.text.ParseException;

/**
 * Created by Administrator on 2016/8/26.
 */
public class UserMessage implements Serializable{
    private String ReceiveUserName;
    private String ReceiveThirdParty;
    private String SendTime;
    private String Title;
    private String Msg;
    private String MsgType;
    private String SendUserName;
    private String SendThirdParty;
    private String SendDt;

    private String formatDate;

    private int SecondMsgType;

    public String getFormatDate(){
        return zDateUtil.getFormatDate(getSendDt(),"yyyy-MM-dd HH:mm:ss","MM-dd HH:mm");
    }

    public int getSecondMsgType() {
        return SecondMsgType;
    }

    public void setSecondMsgType(int secondMsgType) {
        SecondMsgType = secondMsgType;
    }

    public void setFormatDate(String formatDate) {
        this.formatDate = formatDate;
    }

    public String getReceiveUserName() {
        return ReceiveUserName;
    }

    public void setReceiveUserName(String receiveUserName) {
        ReceiveUserName = receiveUserName;
    }

    public String getReceiveThirdParty() {
        return ReceiveThirdParty;
    }

    public void setReceiveThirdParty(String receiveThirdParty) {
        ReceiveThirdParty = receiveThirdParty;
    }

    public String getSendTime() {
        return SendTime;
    }

    public void setSendTime(String sendTime) {
        SendTime = sendTime;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getMsg() {
        return Msg;
    }

    public void setMsg(String msg) {
        Msg = msg;
    }

    public String getMsgType() {
        return MsgType;
    }

    public void setMsgType(String msgType) {
        MsgType = msgType;
    }

    public String getSendUserName() {
        return SendUserName;
    }

    public void setSendUserName(String sendUserName) {
        SendUserName = sendUserName;
    }

    public String getSendThirdParty() {
        return SendThirdParty;
    }

    public void setSendThirdParty(String sendThirdParty) {
        SendThirdParty = sendThirdParty;
    }

    public String getSendDt() {
        return SendDt;
    }

    public void setSendDt(String sendDt) {
        SendDt = sendDt;
    }
}
