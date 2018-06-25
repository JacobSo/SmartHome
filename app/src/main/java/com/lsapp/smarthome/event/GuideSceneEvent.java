package com.lsapp.smarthome.event;

/**
 * Created by Administrator on 2017/1/2.
 */

public class GuideSceneEvent {
    private int position;
    private boolean isExist = false;

    public GuideSceneEvent(int position, boolean isExist) {
        this.position = position;
        this.isExist = isExist;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public boolean isExist() {
        return isExist;
    }

    public void setExist(boolean exist) {
        isExist = exist;
    }
}
