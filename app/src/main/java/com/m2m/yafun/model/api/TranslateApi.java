package com.m2m.yafun.model.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.m2m.yafun.model.api.service.result.DetectedLanguage;
import com.m2m.yafun.model.api.service.OnLanguageDetermineListener;
import com.m2m.yafun.model.api.service.OnLanguagesReceivedListener;
import com.m2m.yafun.model.api.service.OnTranslateListener;
import com.m2m.yafun.model.api.service.result.TranslateResult;
import com.m2m.yafun.model.api.service.YandexTranslateService;
import com.m2m.yafun.model.api.service.result.Languages;

import java.io.IOException;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TranslateApi extends BaseApi {

    private Languages languagesCache;

    public void getLanguages(final OnLanguagesReceivedListener listener) {
        YandexTranslateService service = getService();
        String lang = Locale.getDefault().getCountry();
        service.getLanguages(getApiKey(), lang).enqueue(new Callback<Languages>() {
            @Override
            public void onResponse(Call<Languages> call, Response<Languages> response) {
                if(response.isSuccessful()) {
                    languagesCache = response.body();
                    listener.onLanguagesReceived(languagesCache);
                } else {
                    listener.onLanguagesReceiveError(getError(response, "Getting languages error"));
                }
            }

            @Override
            public void onFailure(Call<Languages> call, Throwable t) {
                t.printStackTrace();
                listener.onLanguagesReceiveError(t.getMessage());
            }
        });
    }

    public void detectLanguage(final String text, final OnLanguageDetermineListener listener) {
        YandexTranslateService service = getService();
        service.detectLanguage(getApiKey(), text).enqueue(new Callback<DetectedLanguage>() {
            @Override
            public void onResponse(Call<DetectedLanguage> call, Response<DetectedLanguage> response) {
                if(response.isSuccessful())
                    listener.onLanguageDetermined(text, response.body().getLanguage());
                else
                    listener.onLanguagesDetermineError(getError(response, "Getting languages error"));
            }

            @Override
            public void onFailure(Call<DetectedLanguage> call, Throwable t) {
                t.printStackTrace();
                listener.onLanguagesDetermineError(t.getMessage());
            }
        });
    }

    public void translate(final String text, String translateDirection, final OnTranslateListener listener) {
        getService().translate(getApiKey(), translateDirection, text).enqueue(new Callback<TranslateResult>() {
            @Override
            public void onResponse(Call<TranslateResult> call, Response<TranslateResult> response) {
                if(response.isSuccessful())
                    listener.onTranslated(text, response.body());
                else
                    listener.onTranslateError(getError(response, "Translate error"));
            }

            @Override
            public void onFailure(Call<TranslateResult> call, Throwable t) {
                t.printStackTrace();
                listener.onTranslateError(t.getMessage());
            }
        });
    }

    private static <T> String getError(Response<T> response, String defaultError) {
        try {
            return response.errorBody().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return defaultError;
    }

    private YandexTranslateService getService() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getApiBaseUrl())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        return retrofit.create(YandexTranslateService.class);
    }

}
