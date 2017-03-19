package com.m2m.yafun.model.api.service;

import com.m2m.yafun.model.api.service.result.DetectedLanguage;
import com.m2m.yafun.model.api.service.result.Languages;
import com.m2m.yafun.model.api.service.result.TranslateResult;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface YandexTranslateService {

    @GET("api/v1.5/tr.json/getLangs")
    Call<Languages> getLanguages(@Query("key") String apiKey, @Query("ui") String ui);

    @GET("api/v1.5/tr.json/detect")
    Call<DetectedLanguage> detectLanguage(@Query("key") String apiKey, @Query("text") String text);

    @GET("api/v1.5/tr.json/detect")
    Call<DetectedLanguage> detectLanguage(@Query("key") String apiKey, @Query("text") String text, @Query("hint") String hint);

    @POST("/api/v1.5/tr.json/translate")
    Call<TranslateResult> translate(@Query("key") String apiKey, @Query("lang") String translateDirection, @Field("text") String toTranslate);
}
