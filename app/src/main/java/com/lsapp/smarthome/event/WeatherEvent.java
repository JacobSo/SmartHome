package com.lsapp.smarthome.event;

import com.lsapp.smarthome.data.WeatherData;

/**
 * Created by Administrator on 2016/8/13.
 *
 * call the weather view initiation
 *
 * action:MainPagerFragment
 * usage:BaseActivity
 */
public class WeatherEvent {
    private WeatherData weatherData;

    public WeatherEvent(WeatherData weatherData) {
        this.weatherData = weatherData;
    }

    public WeatherData getWeatherData() {
        return weatherData;
    }

    public void setWeatherData(WeatherData weatherData) {
        this.weatherData = weatherData;
    }
}
