package com.m2m.yafun.model.api.service;

import com.m2m.yafun.model.api.service.result.Languages;

public interface OnLanguagesReceivedListener {
    void onLanguagesReceived(Languages languages);

    void onLanguagesReceiveError(String error);
}
