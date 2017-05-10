package com.google.cloud.android.longdo.services;

import com.google.cloud.android.longdo.responses.TranslateResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Copyright © 2016 AsianTech inc.
 * Created by HungTQB on 08/11/2016.
 */
public interface IWebservice {

    @GET("language/translate/v2")
    Call<TranslateResponse> translate(@Query("key") String key, @Query("q") String content, @Query("source") String source, @Query("target") String target);
}
