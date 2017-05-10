package com.google.cloud.android.longdo.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Copyright © 2016 AsianTech inc.
 * Created by HungTQB on 08/11/2016.
 */
public class WebserviceUtil {
    private static final String BASE_URL = "https://www.googleapis.com/";
    private static IWebservice sWebservice;

    private WebserviceUtil() {
    }

    public static IWebservice getInstance() {
        if (sWebservice == null) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
            Gson gson = new GsonBuilder().setLenient().create();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(client)
                    .build();
            sWebservice = retrofit.create(IWebservice.class);
        }
        return sWebservice;
    }

}
