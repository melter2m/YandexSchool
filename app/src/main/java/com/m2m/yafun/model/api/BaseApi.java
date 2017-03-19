package com.m2m.yafun.model.api;

abstract class BaseApi {

    public static final String ApiKey = "trnsl.1.1.20170318T084327Z.cd081dcaacbcc4ac.8b6ea739420fad2e59745cf0cd3e61a5b43897a0";

    String getApiBaseUrl() {
        return "https://translate.yandex.net";
    }

    String getApiKey() {
        return ApiKey;
    }

}
