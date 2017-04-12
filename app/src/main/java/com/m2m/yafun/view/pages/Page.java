package com.m2m.yafun.view.pages;


import android.app.Application;
import android.content.Context;
import android.support.v4.app.Fragment;

import com.m2m.yafun.model.api.TranslateApi;
import com.m2m.yafun.model.db.IDatabaseContext;
import com.m2m.yafun.view.OnUpdateListener;
import com.m2m.yafun.view.TranslateApplication;
import com.m2m.yafun.view.Updater;

public abstract class Page extends Fragment implements OnUpdateListener {

    protected Updater updater;

    public TranslateApi getTranslateApi() {
        return getApplication().getTranslateApi();
    }

    public IDatabaseContext getDatabaseContext() {
        return getApplication().getDbContext();
    }

    private TranslateApplication getApplication() {
        Application application = getActivity().getApplication();
        if (application instanceof TranslateApplication)
            return (TranslateApplication) application;
        throw new RuntimeException("Application must be TranslateApplication");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Updater) {
            updater = (Updater) context;
            updater.registerUpdateListener(this);
        } else {
            throw new RuntimeException(context.toString() + " must implement OnUpdateListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        updater.unRegisterUpdateListener(this);
        updater = null;
    }

    @Override
    public void notifyOthersToUpdate() {
        updater.notifyOthersToUpdate();
    }

}
