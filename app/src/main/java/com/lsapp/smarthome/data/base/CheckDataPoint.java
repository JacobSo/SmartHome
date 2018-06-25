package com.lsapp.smarthome.data.base;

import android.graphics.Color;

import com.lsapp.smarthome.utils.ColorUtil;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/9/21.
 */
public class CheckDataPoint implements Serializable {
    private String value;
    private String DetectTime;
    private String Level;
    private String levelStr;
    private int levelColor;

    public int getLevelColor() {
        if (Level == null || Level.equals("")) {
            levelColor = ColorUtil.getQualityNull();
        } else {
            switch (Integer.valueOf(Level)) {
                case 0://优
                    levelColor =  ColorUtil.getQualityGreen();
                    break;
                case 1://良
                    levelColor =ColorUtil.getQualityBlue();
                    break;
                case 2://轻度
                    levelColor =  ColorUtil.getQualityYellow();
                    break;
                case 3://中度
                    levelColor =  ColorUtil.getQualityYellow();
                    break;
                case 4://重度
                    levelColor = ColorUtil.getQualityRed();
                    break;
                case 5://严重
                    levelColor =  ColorUtil.getQualityRed();
                    break;

            }
        }
        return levelColor;
    }

    public void setLevelColor(int levelColor) {
        this.levelColor = levelColor;
    }

    public String getLevelStr() {
        if (Level == null || Level.equals("")) {
            levelStr = "无标准";
        } else {
            switch (Integer.valueOf(Level)) {
                case 0:
                    levelStr = "优";
                    break;
                case 1:
                    levelStr = "良";
                    break;
                case 2:
                    levelStr = "轻度污染";
                    break;
                case 3:
                    levelStr = "中度污染";
                    break;
                case 4:
                    levelStr = "重度污染";
                    break;
                case 5:
                    levelStr = "严重污染";
                    break;

            }
        }

        return levelStr;
    }

    public void setLevelStr(String levelStr) {
        this.levelStr = levelStr;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDetectTime() {
        return DetectTime;
    }

    public void setDetectTime(String detectTime) {
        DetectTime = detectTime;
    }

    public String getLevel() {
        return Level;
    }

    public void setLevel(String level) {
        Level = level;
    }
}
