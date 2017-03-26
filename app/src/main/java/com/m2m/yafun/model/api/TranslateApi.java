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

    public TranslateApi(Languages prevLanguages) {
        this.languagesCache = prevLanguages;
    }

    public Languages getLanguagesCache() {
        return languagesCache;
    }

    public void getLanguages(final OnLanguagesReceivedListener listener) {
        getLanguages(Locale.getDefault().getLanguage(), listener);
    }

    public void getLanguages(String uiLanguage, final OnLanguagesReceivedListener listener) {
        getLanguagesCall(uiLanguage).enqueue(new Callback<Languages>() {
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

    public Languages getLanguagesSync(String uiLanguage) {
        try {
            return getLanguagesCall(uiLanguage).execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Call<Languages> getLanguagesCall(String uiLanguage) {
        YandexTranslateService service = getService();
        if (uiLanguage == null)
            uiLanguage = Locale.getDefault().getLanguage();
        return service.getLanguages(getApiKey(), uiLanguage);
    }

    public void detectLanguage(final String text, final OnLanguageDetermineListener listener) {
        getDetectLanguageCall(getApiKey()).enqueue(new Callback<DetectedLanguage>() {
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

    public DetectedLanguage detectedLanguageSync(String text) {
        try {
            return getDetectLanguageCall(text).execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Call<DetectedLanguage> getDetectLanguageCall(String text) {
        return getService().detectLanguage(getApiKey(), text);
    }

    public void translate(final String text, String translateDirection, final OnTranslateListener listener) {
        getTranslateCall(text, translateDirection).enqueue(new Callback<TranslateResult>() {
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

    public TranslateResult translateSync(String text, String translateDirection) {
        try {
            return getTranslateCall(text, translateDirection).execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Call<TranslateResult> getTranslateCall(String text, String translateDirection) {
        return getService().translate(getApiKey(), translateDirection, text);
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
