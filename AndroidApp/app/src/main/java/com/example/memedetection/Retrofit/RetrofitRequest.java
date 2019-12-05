package com.example.memedetection.Retrofit;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitRequest {

    private static Retrofit retrofit;
//    private static final String BASE_URL = "http://127.0.0.22:5001/";
     private static final String BASE_URL = "http://192.168.1.104:5001";
//    private static final String BASE_URL = "http://192.168.43.10:5001";
//    private static final String BASE_URL = "http://192.168.1.255:5001/";

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {


            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(2, TimeUnit.MINUTES)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(15, TimeUnit.SECONDS)
                    .build();

            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
