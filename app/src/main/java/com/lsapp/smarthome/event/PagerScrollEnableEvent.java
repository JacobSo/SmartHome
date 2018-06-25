package com.lsapp.smarthome.event;

/**
 * Created by Administrator on 2016/11/5.
 *
 * pager scroll enable event
 *
 * action:MainPagerFragment
 * usage:MainVerticalPagerFragment
 *
 */
public class PagerScrollEnableEvent {
    private boolean enable = false;

    public PagerScrollEnableEvent(boolean enable) {
        this.enable = enable;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }
}
