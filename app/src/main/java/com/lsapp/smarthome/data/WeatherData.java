package com.lsapp.smarthome.data;

import android.graphics.Color;
import android.text.TextUtils;

import com.lsapp.smarthome.utils.WeatherUtil;

import java.util.List;

/**
 * Created by Administrator on 2016/8/13.
 */
public class WeatherData {
    private String retCode;
    private String msg;
    private List<CityResult> result;

    public class CityResult {
        private List<Future> future;
        private String airCondition;
        private String city;
        private String coldIndex;
        private String date;
        private String distrct;
        private String dressingIndex;
        private String exerciseIndex;

        private String humidity;
        private String pollutionIndex;
        private String province;
        private String sunrise;
        private String sunset;
        private String temperature;
        private String time;
        private String updateTime;
        private String washIndex;
        private String weather;
        private String week;
        private String wind;

        private int gradeColor;


        private String getFormat(String temp) {
            if (temp == null || TextUtils.isEmpty(temp)) {
                return "无";
            } else {
                if (temp.contains("湿度："))
                    return temp.replace("湿度：", "");
                else return temp;
            }
        }


        public int getGradeColor() {
            return WeatherUtil.getGradeColor(airCondition);
        }

        public void setGradeColor(int gradeColor) {
            this.gradeColor = gradeColor;
        }


        public List<Future> getFuture() {
            return future;
        }

        public void setFuture(List<Future> future) {
            this.future = future;
        }

        public String getAirCondition() {
            return getFormat(airCondition);
        }

        public void setAirCondition(String airCondition) {
            this.airCondition = airCondition;
        }

        public String getCity() {
            return getFormat(city);
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getColdIndex() {
            return getFormat(coldIndex);
        }

        public void setColdIndex(String coldIndex) {
            this.coldIndex = coldIndex;
        }

        public String getDate() {
            return getFormat(date);
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getDistrct() {
            return getFormat(distrct);
        }

        public void setDistrct(String distrct) {
            this.distrct = distrct;
        }

        public String getDressingIndex() {
            return getFormat(dressingIndex);
        }

        public void setDressingIndex(String dressingIndex) {
            this.dressingIndex = dressingIndex;
        }

        public String getExerciseIndex() {
            return getFormat(exerciseIndex);
        }

        public void setExerciseIndex(String exerciseIndex) {
            this.exerciseIndex = exerciseIndex;
        }

        public String getHumidity() {
            return getFormat(humidity);
        }

        public void setHumidity(String humidity) {
            this.humidity = humidity;
        }

        public String getPollutionIndex() {
            return getFormat(pollutionIndex);
        }

        public void setPollutionIndex(String pollutionIndex) {
            this.pollutionIndex = pollutionIndex;
        }

        public String getProvince() {
            return getFormat(province);
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getSunrise() {
            return getFormat(sunrise);
        }

        public void setSunrise(String sunrise) {
            this.sunrise = sunrise;
        }

        public String getSunset() {
            return getFormat(sunset);
        }

        public void setSunset(String sunset) {
            this.sunset = sunset;
        }

        public String getTemperature() {
            return getFormat(temperature);
        }

        public void setTemperature(String temperature) {
            this.temperature = temperature;
        }

        public String getTime() {
            return getFormat(time);
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getUpdateTime() {
            return getFormat(updateTime);
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }

        public String getWashIndex() {
            return getFormat(washIndex);
        }

        public void setWashIndex(String washIndex) {
            this.washIndex = washIndex;
        }

        public String getWeather() {
            return getFormat(weather);
        }

        public void setWeather(String weather) {
            this.weather = weather;
        }

        public String getWeek() {
            return getFormat(week);
        }

        public void setWeek(String week) {
            this.week = week;
        }

        public String getWind() {
            return getFormat(wind);
        }

        public void setWind(String wind) {
            this.wind = wind;
        }
    }

    public class Future {
        private String date;
        private String dayTime;
        private String night;
        private String temperature;
        private String week;
        private String wind;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getDayTime() {
            return dayTime;
        }

        public void setDayTime(String dayTime) {
            this.dayTime = dayTime;
        }

        public String getNight() {
            return night;
        }

        public void setNight(String night) {
            this.night = night;
        }

        public String getTemperature() {
            return temperature;
        }

        public void setTemperature(String temperature) {
            this.temperature = temperature;
        }

        public String getWeek() {
            return week;
        }

        public void setWeek(String week) {
            this.week = week;
        }

        public String getWind() {
            return wind;
        }

        public void setWind(String wind) {
            this.wind = wind;
        }
    }

    public String getRetCode() {
        return retCode;
    }

    public void setRetCode(String retCode) {
        this.retCode = retCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<CityResult> getResult() {
        return result;
    }

    public void setResult(List<CityResult> result) {
        this.result = result;
    }
}
