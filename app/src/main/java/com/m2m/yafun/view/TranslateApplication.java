package com.m2m.yafun.view;

import android.app.Application;

import com.m2m.yafun.model.api.TranslateApi;
import com.m2m.yafun.model.api.service.OnLanguagesReceivedListener;
import com.m2m.yafun.model.api.service.result.Languages;
import com.m2m.yafun.model.db.DatabaseContext;
import com.m2m.yafun.model.db.IDatabaseContext;
import com.m2m.yafun.model.db.gateway.ILanguagesGateway;

public class TranslateApplication extends Application implements OnLanguagesReceivedListener {

    private DatabaseContext dbContext;
    private TranslateApi translateApi;

    @Override
    public void onCreate() {
        super.onCreate();
        dbContext = new DatabaseContext(this);
        translateApi = new TranslateApi(dbContext.createLanguagesGateway().getLanguages());
        translateApi.getLanguages(this);
    }

    public IDatabaseContext getDbContext() {
        return dbContext;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        dbContext.close();
    }

    public TranslateApi getTranslateApi() {
        return translateApi;
    }

    @Override
    public void onLanguagesReceived(Languages languages) {
        if (languages == null)
            return;
        ILanguagesGateway languagesGateway = dbContext.createLanguagesGateway();
        languagesGateway.saveLanguages(languages);
    }

    @Override
    public void onLanguagesReceiveError(String error) {
        translateApi.getLanguages(this);
    }
}
