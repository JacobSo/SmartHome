package com.lsapp.smarthome.data.base;

import com.lsapp.smarthome.R;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/11/15.
 */
public class Control implements Serializable {
    private int filter;
    private int timer;
    private int ionMode;
    private int purifier;
    private int panelLight;
    private int onOff;
    private int fanOn;
    private int worklightOn;

    private String windName;
    private String ionName;
    private String timerName;

    private int windImage;
    private int ionImage;
    private int timeImage;

    public Control(int filter, int timer, int ionMode, int purifier, int panelLight, int onOff, int fanOn, int worklightOn) {
        this.filter = filter;
        this.timer = timer;
        this.ionMode = ionMode;
        this.purifier = purifier;
        this.panelLight = panelLight;
        this.onOff = onOff;
        this.fanOn = fanOn;
        this.worklightOn = worklightOn;
    }

    public int getWindImage() {
        if (purifier == 0) return R.drawable.function_shut;
        else if (purifier == 1) return R.drawable.function_wind2;
        else if (purifier == 2) return R.drawable.function_wind1;
        else return R.drawable.function_wind3;
    }

    public void setWindImage(int windImage) {
        this.windImage = windImage;
    }

    public int getIonImage() {
        if (ionMode == 0) return  R.drawable.function_shut;
        else if (ionMode == 1) return  R.drawable.function_on1;
        else return  R.drawable.function_on2;
    }


    public int getTimeImage() {
        if (timer == 0) return R.drawable.function_timer;
        else if (timer == 1) return R.drawable.function_timer1;
        else if (timer == 2) return R.drawable.function_timer2;
        else return R.drawable.function_timer3;
    }

    public void setTimeImage(int timeImage) {
        this.timeImage = timeImage;
    }

    public void setIonImage(int ionImage) {
        this.ionImage = ionImage;
    }

    public String getTimerName() {
        if (timer == 0) return "关闭";
        else return timer + "小时";
    }

    public void setTimerName(String timerName) {
        this.timerName = timerName;
    }

    public String getWindName() {
        if (purifier == 0) return "关闭";
        else if (purifier == 1) return "日常模式";
        else if (purifier == 2) return "睡眠模式";
        else return "喷射模式";
    }

    public void setWindName(String windName) {
        this.windName = windName;
    }

    public String getIonName() {
        if (ionMode == 0) return "关闭";
        else if (ionMode == 1) return "普通模式";
        else return "加强模式";
    }

    public void setIonName(String ionName) {
        this.ionName = ionName;
    }

    public int getFanOn() {
        return fanOn;
    }

    public void setFanOn(int fanOn) {
        this.fanOn = fanOn;
    }

    public int getWorklightOn() {
        return worklightOn;
    }

    public void setWorklightOn(int worklightOn) {
        this.worklightOn = worklightOn;
    }

    public int getFilter() {
        return filter;
    }

    public void setFilter(int filter) {
        this.filter = filter;
    }

    public int getTimer() {
        return timer;
    }

    public void setTimer(int timer) {
        this.timer = timer;
    }

    public int getIonMode() {
        return ionMode;
    }

    public void setIonMode(int ionMode) {
        this.ionMode = ionMode;
    }

    public int getPurifier() {
        return purifier;
    }

    public void setPurifier(int purifier) {
        this.purifier = purifier;
    }

    public int getPanelLight() {
        return panelLight;
    }

    public void setPanelLight(int panelLight) {
        this.panelLight = panelLight;
    }

    public int getOnOff() {
        return onOff;
    }

    public void setOnOff(int onOff) {
        this.onOff = onOff;
    }
}
