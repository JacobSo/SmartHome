package com.lsapp.smarthome.event;

/**
 * Created by Administrator on 2016/12/1.
 */
public class SubToMainEvent {
    private String actionId;

    public SubToMainEvent(String actionId) {
        this.actionId = actionId;
    }

    public String getActionId() {
        return actionId;
    }

    public void setActionId(String actionId) {
        this.actionId = actionId;
    }
}
