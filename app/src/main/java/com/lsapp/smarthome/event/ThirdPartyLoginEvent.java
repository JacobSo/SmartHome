package com.lsapp.smarthome.event;

/**
 * Created by Administrator on 2016/8/18.
 */
public class ThirdPartyLoginEvent {
    private int platform;
    private String name;
    private String id;
    private boolean isUnbind = false;

    public ThirdPartyLoginEvent(int platform, String name, String id) {
        this.platform = platform;
        this.name = name;
        this.id = id;
    }

    public ThirdPartyLoginEvent(int platform, String name, String id, boolean isUnbind) {
        this.platform = platform;
        this.name = name;
        this.id = id;
        this.isUnbind = isUnbind;
    }

    public int getPlatform() {
        return platform;
    }

    public void setPlatform(int platform) {
        this.platform = platform;
    }

    public String getName() {
        return name.replaceAll("[\\ud800\\udc00-\\udbff\\udfff\\ud800-\\udfff]","");
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isUnbind() {
        return isUnbind;
    }

    public void setUnbind(boolean unbind) {
        isUnbind = unbind;
    }
}
