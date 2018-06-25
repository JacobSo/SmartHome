package com.lsapp.smarthome.data.base;

import android.graphics.Color;

import com.lsapp.smarthome.utils.ColorUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/7.
 */
public class CheckData implements Serializable{
    private String Name;
    private String Data;
    private String Unit;
    private String DetectTime;
    private int Level;
    private ArrayList<CheckDataPoint> DataList;
    private String levelName;
    private int levelColor;
    private String maxvalue;
    private String minvalue;
    private int DataType;
    private String dt;//week data param

    public String getMaxvalue() {
        return maxvalue;
    }

    public void setMaxvalue(String maxvalue) {
        this.maxvalue = maxvalue;
    }

    public String getMinvalue() {
        return minvalue;
    }

    public void setMinvalue(String minvalue) {
        this.minvalue = minvalue;
    }

    public int getLevelColor() {
        if(getLevel()==0){
            return ColorUtil.getQualityGreen();
        }else if(getLevel()==1){
            return ColorUtil.getQualityBlue();
        }else if(getLevel()==2){
            return ColorUtil.getQualityYellow();
        }else if(getLevel()==3){
            return  ColorUtil.getQualityYellow();
        }else if(getLevel()==4){
            return  ColorUtil.getQualityRed();
        }else if(getLevel()==5){
            return ColorUtil.getQualityRed();
        }else{
            return ColorUtil.getQualityNull();
        }
    }

    public void setLevelColor(int levelColor) {
        this.levelColor = levelColor;
    }

    public String getLevelName() {
        if(getLevel()==0){
            return "优";
        }else if(getLevel()==1){
            return "良";
        }else if(getLevel()==2){
            return "轻度污染";
        }else if(getLevel()==3){
            return "中度污染";
        }else if(getLevel()==4){
            return "重度污染";
        }else if(getLevel()==5){
            return "严重污染";
        }else{
            return "-";
        }
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public int getDataType() {
        return DataType;
    }

    public void setDataType(int dataType) {
        DataType = dataType;
    }

    private int color;

    public int getLevel() {
        return Level;
    }

    public void setLevel(int level) {
        Level = level;
    }

    public int getColor() {
        return ColorUtil.getDarkTypeColor(getDataType());
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getData() {
        return Data;
    }

    public void setData(String data) {
        Data = data;
    }

    public String getUnit() {
        return Unit==null?"":Unit;
    }

    public void setUnit(String unit) {
        Unit = unit;
    }

    public String getDetectTime() {
        return DetectTime;
    }

    public void setDetectTime(String detectTime) {
        DetectTime = detectTime;
    }

    public ArrayList<CheckDataPoint> getDataList() {
        return DataList;
    }

    public void setDataList(ArrayList<CheckDataPoint> dataList) {
        DataList = dataList;
    }
}
