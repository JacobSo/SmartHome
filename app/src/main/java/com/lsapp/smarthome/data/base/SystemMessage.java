package com.lsapp.smarthome.data.base;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/8/26.
 */
public class SystemMessage implements Serializable {

    private String UserId;
    private String SendTime;
    private String Title;
    private String Summary;
    private String Msg;
    private String MsgType;
    private String SendDDt;

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
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

    public String getSummary() {
        return Summary;
    }

    public void setSummary(String summary) {
        Summary = summary;
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

    public String getSendDDt() {
        return SendDDt;
    }

    public void setSendDDt(String sendDDt) {
        SendDDt = sendDDt;
    }
}
