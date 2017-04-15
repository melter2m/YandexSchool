package com.m2m.yafun.view.pages.translate;

import android.os.AsyncTask;

import com.m2m.yafun.model.api.service.result.TranslateResult;
import com.m2m.yafun.model.db.entities.HistoryItem;
import com.m2m.yafun.model.db.gateway.IHistoryGateway;

import java.io.IOException;

import retrofit2.Response;

class TranslateTask extends AsyncTask<String, Void, TranslateResult> {

    private TranslatePage translatePage;
    private String toTranslate;
    private String error;

    TranslateTask(TranslatePage translatePage) {
        this.translatePage = translatePage;
    }

    @Override
    protected TranslateResult doInBackground(String... params) {
        if (params.length < 2)
            return null;
        toTranslate = params[0];
        String direction = params[1];
        if (toTranslate == null || toTranslate.length() == 0 || direction == null || direction.length() == 0)
            return null;
        TranslateResult cache = tryToGetFromHistory(toTranslate, direction);
        if (cache != null)
            return cache;

        Response<TranslateResult> result = translatePage.getTranslateApi().translateSync(toTranslate, direction);
        if (result == null)
            return null;

        if (result.isSuccessful())
            return result.body();

        try {
            error = result.errorBody().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(TranslateResult result) {
        if (result == null && error != null) {
            translatePage.onTranslateError(error);
            return;
        }
        if (result == null)
            return;
        translatePage.onTranslated(toTranslate, result);
    }

    private TranslateResult tryToGetFromHistory(String toTranslate, String direction) {
        IHistoryGateway gateway = translatePage.getDatabaseContext().createHistoryGateway();
        HistoryItem item = gateway.getItem(toTranslate, direction);
        if (item == null)
            return null;
        return new TranslateResult(0, item.getDirection(), item.getTranslation());
    }
}
