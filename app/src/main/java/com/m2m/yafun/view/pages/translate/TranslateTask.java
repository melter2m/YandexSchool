package com.m2m.yafun.view.pages.translate;

import android.os.AsyncTask;

import com.m2m.yafun.model.api.service.OnTranslateListener;
import com.m2m.yafun.model.api.service.result.TranslateResult;

class TranslateTask extends AsyncTask<String, Void, Void> {

    private TranslatePage translatePage;

    private String toTranslateCache;

    TranslateTask(TranslatePage translatePage) {
        this.translatePage = translatePage;
    }

    @Override
    protected Void doInBackground(String... params) {
        if (params.length < 2)
            return null;
        toTranslateCache = params[0];
        String direction = params[1];
        if (toTranslateCache == null || toTranslateCache.length() == 0 || direction == null || direction.length() == 0)
            return null;
        translatePage.getTranslateApi().translate(toTranslateCache, direction, new OnTranslateListener() {
            @Override
            public void onTranslated(final String toTranslate, final TranslateResult translateResult) {
                translatePage.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        translatePage.onTranslated(toTranslate, translateResult);
                    }
                });

            }

            @Override
            public void onTranslateError(final String error) {
                translatePage.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        translatePage.onTranslateError(error);
                    }
                });

            }
        });
        return null;
    }

}
