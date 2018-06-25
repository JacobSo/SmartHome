package com.lsapp.smarthome.utils;

import android.content.Context;
import android.graphics.Color;

import com.lsapp.smarthome.R;

/**
 * Created by Administrator on 2016/9/21.
 */
public class WeatherUtil {

    public static int getWeatherImage(String name) {
        if (name.contains("云")) {
            return R.drawable.weather_cloud;
        } else if (name.contains("霾")) {
            return R.drawable.weather_m;
        } else if (name.contains("晴")) {
            return R.drawable.weather_shine;
        } else if (name.contains("阴")) {
            return R.drawable.weather_half_shine;
        } else if (name.contains("雷")) {
            return R.drawable.weather_thunder;
        } else if (name.contains("阵雨")) {
            return R.drawable.weather_half_rain;
        } else if (name.contains("雨")) {
            return R.drawable.weather_rain;
        } else if (name.contains("雪")) {
            return R.drawable.weather_snow;
        } else {
            return R.drawable.weather_half_shine;
        }
    }


    public static int getWeatherRoof(String name) {
        if (name.contains("云")) {
            return R.drawable.anim_weather_cloud;
        } else if (name.contains("霾")) {
            return R.drawable.anim_weather_cloud;
        } else if (name.contains("晴")) {
            return R.drawable.anim_weather_sun;
        } else if (name.contains("阴")) {
            return R.drawable.anim_weather_cloud;
        } else if (name.contains("雷")) {
            return R.drawable.anim_weather_rain;
        } else if (name.contains("阵雨")) {
            return R.drawable.anim_weather_rain;
        } else if (name.contains("雨")) {
            return R.drawable.anim_weather_rain;
        } else if (name.contains("雪")) {
            return R.drawable.anim_weather_rain;
        } else {
            return R.drawable.anim_weather_cloud;
        }
    }

    public static int getGradeColor(String grade) {
        if (grade.contains("优")) {
            return ColorUtil.getQualityGreen();
        } else if (grade.contains("污染")) {
            return ColorUtil.getQualityRed();
        } else {
            return ColorUtil.getQualityBlue();
        }
    }
}
