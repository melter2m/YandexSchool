package com.m2m.yafun.model.db;


import android.content.Context;

import com.m2m.yafun.model.db.gateway.HistoryGateway;
import com.m2m.yafun.model.db.gateway.IHistoryGateway;
import com.m2m.yafun.model.db.gateway.ILanguagesGateway;
import com.m2m.yafun.model.db.gateway.LanguagesGateway;

public class DatabaseContext implements IDatabaseContext{

    private final DbOpenHelper dbOpenHelper;

    public DatabaseContext(Context context) {
        dbOpenHelper = new DbOpenHelper(context);
    }

    @Override
    public IHistoryGateway createHistoryGateway() {
        return new HistoryGateway(dbOpenHelper);
    }

    @Override
    public ILanguagesGateway createLanguagesGateway() {
        return new LanguagesGateway(dbOpenHelper);
    }

    @Override
    public void close() {
        dbOpenHelper.close();
    }

}
