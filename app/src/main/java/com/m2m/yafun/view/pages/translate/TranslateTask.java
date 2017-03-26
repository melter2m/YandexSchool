package com.m2m.yafun.view.pages.translate;

import android.os.AsyncTask;
import android.widget.Toast;

import com.m2m.yafun.model.api.service.OnTranslateListener;
import com.m2m.yafun.model.api.service.result.TranslateResult;

class TranslateTask extends AsyncTask<String, Void, Void> {

    private TranslateFragment translateFragment;

    private String toTranslateCache;

    TranslateTask(TranslateFragment translateFragment) {
        this.translateFragment = translateFragment;
    }

    @Override
    protected Void doInBackground(String... params) {
        if (params.length < 2)
            return null;
        toTranslateCache = params[0];
        String direction = params[1];
        if (toTranslateCache == null || toTranslateCache.length() == 0 || direction == null || direction.length() == 0)
            return null;
        translateFragment.getTranslateApi().translate(toTranslateCache, direction, new OnTranslateListener() {
            @Override
            public void onTranslated(final String toTranslate, final TranslateResult translateResult) {
                translateFragment.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        translateFragment.onTranslated(toTranslate, translateResult);
                    }
                });

            }

            @Override
            public void onTranslateError(final String error) {
                translateFragment.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        translateFragment.onTranslateError(error);
                    }
                });

            }
        });
        return null;
    }

}
