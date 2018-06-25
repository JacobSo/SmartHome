package com.lsapp.smarthome.data.base;

import android.graphics.Color;

import com.lsapp.smarthome.utils.ColorUtil;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/11/17.
 */
public class DateData implements Serializable {
    private String name;
    private String minvalue;
    private String maxvalue;
    private String unit;
    private String level;
    private String levelName;
    private int levelColor;
    private int DataType;

    public String getLevelName() {
        if (getLevel() == null) {
            return "-";
        } else {
            if (getLevel().equals("0")) {
                return "优";
            } else if (getLevel().equals("1")) {
                return "良";
            } else if (getLevel().equals("2")) {
                return "轻度污染";
            } else if (getLevel().equals("3")) {
                return "中度污染";
            } else if (getLevel().equals("4")) {
                return "重度污染";
            } else if (getLevel().equals("5")) {
                return "严重污染";
            } else {
                return "-";
            }
        }
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public int getLevelColor() {
        if (getLevel() == null) {
            return Color.parseColor("#FF63727B");
        } else {
            if (getLevel().equals("0")) {
                return ColorUtil.getQualityGreen();
            } else if (getLevel().equals("1")) {
                return ColorUtil.getQualityBlue();
            } else if (getLevel().equals("2")) {
                return  ColorUtil.getQualityYellow();
            } else if (getLevel().equals("3")) {
                return ColorUtil.getQualityYellow();
            } else if (getLevel().equals("4")) {
                return  ColorUtil.getQualityRed();
            } else if (getLevel().equals("5")) {
                return  ColorUtil.getQualityRed();
            } else {
                return  ColorUtil.getQualityNull();
            }
        }

    }

    public void setLevelColor(int levelColor) {
        this.levelColor = levelColor;
    }


    public int getDataType() {
        return DataType;
    }

    public void setDataType(int dataType) {
        DataType = dataType;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    private int color;

    public int getColor() {
        return ColorUtil.getDarkTypeColor(getDataType());
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMinvalue() {
        return minvalue;
    }

    public void setMinvalue(String minvalue) {
        this.minvalue = minvalue;
    }

    public String getMaxvalue() {
        return maxvalue;
    }

    public void setMaxvalue(String maxvalue) {
        this.maxvalue = maxvalue;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
