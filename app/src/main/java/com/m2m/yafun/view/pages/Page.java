package com.m2m.yafun.view.pages;


import android.app.Application;
import android.support.v4.app.Fragment;

import com.m2m.yafun.model.api.TranslateApi;
import com.m2m.yafun.view.TranslateApplication;

public abstract class Page extends Fragment {

    public TranslateApi getTranslateApi() {
        return getApplication().getTranslateApi();
    }

    private TranslateApplication getApplication() {
        Application application = getActivity().getApplication();
        if (application instanceof TranslateApplication)
            return (TranslateApplication) application;
        throw new RuntimeException("Application must be TranslateApplication");
    }

}
