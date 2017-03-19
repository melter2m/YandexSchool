package com.m2m.yafun.model.api.service;


import com.m2m.yafun.model.api.service.result.TranslateResult;

public interface OnTranslateListener {
    void onTranslated(String toTranslate, TranslateResult translateResult);

    void onTranslateError(String error);
}
