package com.m2m.yafun.model.api.service;

public interface OnLanguageDetermineListener {
    void onLanguageDetermined(String toDetermine, String language);

    void onLanguagesDetermineError(String error);
}
