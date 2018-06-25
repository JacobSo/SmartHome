package com.lsapp.smarthome.event;

/**
 * Created by Administrator on 2016/9/26.
 *
 * control the weather roof view visible
 *
 * action:MainPagerFragment
 * usage:MainVerticalPagerFragment
 *
 */
public class WeatherVisibleEvent {
    boolean visible;

    public WeatherVisibleEvent(boolean visible) {
        this.visible = visible;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
