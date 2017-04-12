package com.m2m.yafun.model.api.service.result;

import java.util.List;

public class TranslateResult {

    private final int code;
    private final String lang;
    private final List<String> text;

    public TranslateResult(int code, String lang, List<String> text) {
        this.code = code;
        this.lang = lang;
        this.text = text;
    }

    public int getResultCode() {
        return code;
    }

    public String getTranslateDirection() {
        return lang;
    }

    public List<String> getTranslatedText() {
        return text;
    }

    public String getTranslationTotalString() {
        String result = "";
        for (String translation: text) {
            result += translation;
            result += "\n";
        }
        return result;
    }
}
