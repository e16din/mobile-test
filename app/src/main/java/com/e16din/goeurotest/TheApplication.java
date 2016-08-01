package com.e16din.goeurotest;

import android.app.Application;

import com.e16din.lightutils.LightUtils;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class TheApplication extends Application {

    public static final String BASE_API_URL = "http://api.goeuro.com/api/v2/";

    private static Services sServices;


    @Override
    public void onCreate() {
        super.onCreate();
        LightUtils.init(this);

        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS);
        final HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        clientBuilder.addInterceptor(loggingInterceptor);

        Retrofit retrofit = new Retrofit.Builder()
                .client(clientBuilder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_API_URL)
                .build();

        sServices = retrofit.create(Services.class);
    }

    public static Services getServices() {
        return sServices;
    }
}
