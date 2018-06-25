package com.lsapp.smarthome.data.base;

import com.lsapp.smarthome.R;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/11/15.
 */
public class DateCheck implements Serializable {
    private int voc;
    private int isopenAirCleaner;
    private int VocType = -1;
    private double VocValue;
    private String dt;
    private String date;
    private int vocColor;

    public void setVocType(int vocType) {
        VocType = vocType;
    }

    public int getVocType() {
        return VocType;
    }

    public int getVocColor() {
         if(VocType==0){
            return R.drawable.calendar_green;
        } else if(VocType==1){
            return R.drawable.calendar_blue;
        } else if(VocType==2){
            return R.drawable.calendar_yellow;
        } else if(VocType==3){
            return R.drawable.calendar_red;
        }  else  return R.drawable.calendar_green;

    }

    public void setVocColor(int vocColor) {
        this.vocColor = vocColor;
    }

    public String getDate() {
        //2016-09-06T00:00:00
        String temp = dt.substring(0, 10);
        return temp;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getVoc() {
        return voc;
    }

    public void setVoc(int voc) {
        this.voc = voc;
    }

    public int getIsopenAirCleaner() {
        return isopenAirCleaner;
    }

    public void setIsopenAirCleaner(int isopenAirCleaner) {
        this.isopenAirCleaner = isopenAirCleaner;
    }

    public double getVocValue() {
        return VocValue;
    }

    public void setVocValue(double vocValue) {
        VocValue = vocValue;
    }

    public String getDt() {
        return dt;
    }

    public void setDt(String dt) {
        this.dt = dt;
    }
}
