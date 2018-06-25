package com.lsapp.smarthome.utils;

import android.graphics.Color;

/**
 * Created by Administrator on 2016/4/15.
 */
public class ColorUtil {
    public static String[] darkColors = new String[]{
            "#512DA8",
            "#00796B",
            "#303F9F",
            "#689F38",
            "#455A64",
            "#F57C00",
            "#0288D1",
            "#D32F2F",
            "#FBC02D",
            "#0288D1"
    };
    public static String[] lightColors = new String[]{
            "#673AB7",
            "#009688",
            "#3F51B5",
            "#8BC34A",
            "#607D8B",
            "#FF9800",
            "#03A9F4",
            "#F44336",
            "#FFEB3B",
            "#03A9F4"
    };

    public static int getDarkColor(String example) {
        if (example != null && !example.equals("")) {

            int code = example.hashCode() < 0 ? -example.hashCode() : example.hashCode();
            return Color.parseColor(darkColors[code % 10]);
        } else return Color.parseColor(darkColors[1]);
    }

    public static int getLightColor(String example) {
        if (example != null && !example.equals("")) {
            int code = example.hashCode() < 0 ? -example.hashCode() : example.hashCode();
            return Color.parseColor(lightColors[code % 10]);
        } else {
            return Color.parseColor(lightColors[1]);
        }
    }

    public static int getLightTypeColor(int flag) {
        if (flag < 10)
            return Color.parseColor(lightColors[flag]);
        else return Color.parseColor(lightColors[flag % 10]);
    }

    public static int getDarkTypeColor(int flag) {
        if (flag < 10)
            return Color.parseColor(darkColors[flag]);
        else return Color.parseColor(darkColors[flag % 10]);
    }

    public static int getQualityGreen(){
        return  Color.parseColor("#0288D1");
    }
    public static int getQualityBlue(){
        return  Color.parseColor("#4CAF50");
    }
    public static int getQualityYellow(){
        return  Color.parseColor("#FF9800");
    }
    public static int getQualityRed(){
        return  Color.parseColor("#FF5252");
    }
    public static int getQualityNull(){
        return  Color.parseColor("#FF63727B");
    }

}
