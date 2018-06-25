package com.lsapp.smarthome.data;

import com.lsapp.smarthome.data.base.BaseData;
import com.lsapp.smarthome.data.base.SystemMessage;
import com.lsapp.smarthome.data.base.UserMessage;

import java.util.List;

/**
 * Created by Administrator on 2016/8/26.
 */
public class MessageData extends BaseData {
    List<UserMessage> list;
    List<UserMessage> UserMsgList;
    int NextPageStartSendTime;

    public List<UserMessage> getList() {
        return list;
    }

    public void setList(List<UserMessage> list) {
        this.list = list;
    }

    public List<UserMessage> getUserMsgList() {
        return UserMsgList;
    }

    public void setUserMsgList(List<UserMessage> userMsgList) {
        UserMsgList = userMsgList;
    }

    public int getNextPageStartSendTime() {
        return NextPageStartSendTime;
    }

    public void setNextPageStartSendTime(int nextPageStartSendTime) {
        NextPageStartSendTime = nextPageStartSendTime;
    }
}
