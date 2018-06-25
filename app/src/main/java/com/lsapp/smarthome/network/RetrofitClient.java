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
public class RetrofitClient {
   //   public static final String BASE_URL = "http://192.168.1.190:8806/lslive/";
  //  private static final String BASE_URL = "http://119.145.166.182:8806/lslive/";
//   public static final String BASE_URL = "http://121.42.33.88/lslive/";
    private static final String BASE_URL = "https://www.lsmuyprt.com/lslivetest/";
 //   private static final String BASE_URL = "https://www.lsmuyprt.com/lslivetest/";

    private static final RetrofitClient instance = new RetrofitClient();

    public static RetrofitClient getInstance() {
        return instance;
    }

    private Retrofit retrofit;

    private RetrofitClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(message -> Log.i("RxJava", message));
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20,TimeUnit.SECONDS)
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
