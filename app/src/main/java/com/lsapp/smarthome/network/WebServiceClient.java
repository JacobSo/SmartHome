package com.lsapp.smarthome.network;

import com.lsapp.smarthome.app.Log;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by Administrator on 2016/2/29.
 */
public class WebServiceClient {
    public static final String BASE_URL = "http://apicloud.mob.com/v1/";

    private static final WebServiceClient instance = new WebServiceClient();

    public static WebServiceClient getInstance() {
        return instance;
    }

    private Retrofit retrofit;
    public WebServiceClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(message -> Log.i("RxJava", message));
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }


    public <T> T create(Class<?> clazz) {
        return (T) retrofit.create(clazz);
    }

}
