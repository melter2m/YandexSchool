package com.m2m.yafun.model.api.service.result;

public class DetectedLanguage {

    private final int code;
    private final String lang;

    public DetectedLanguage(int code, String lang) {
        this.code = code;
        this.lang = lang;
    }

    public int getResultCode() {
        return code;
    }

    public String getLanguage() {
        return lang;
    }
}
