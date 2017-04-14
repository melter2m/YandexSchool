package com.m2m.yafun.view.pages.translate;

import android.os.AsyncTask;

import com.m2m.yafun.model.api.service.result.TranslateResult;

class AddToHistoryTask extends AsyncTask<String, Void, Void> {

    private final TranslatePage translatePage;
    private final String original;
    private final TranslateResult translateResult;

    AddToHistoryTask(TranslatePage fragment, String original, TranslateResult translateResult) {
        this.translatePage = fragment;
        this.original = original;
        this.translateResult = translateResult;
    }

    @Override
    protected Void doInBackground(String... params) {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if (original == null || original.isEmpty() || translateResult == null)
            return;
        translatePage.addToHistory(original, translateResult, false);
        translatePage.notifyOthersToUpdate();
    }
}
