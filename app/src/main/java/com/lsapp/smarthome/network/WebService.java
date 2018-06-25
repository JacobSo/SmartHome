package com.lsapp.smarthome.network;

import com.lsapp.smarthome.data.WeatherData;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Administrator on 2016/8/11.
 */
public interface WebService {
    @GET("weather/query")
    Observable<WeatherData> weather(@Query("key") String key, @Query("city") String city, @Query("province") String province);




}
