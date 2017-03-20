package com.m2m.yafun.model.db;


import com.m2m.yafun.model.db.gateway.IHistoryGateway;
import com.m2m.yafun.model.db.gateway.ILanguagesGateway;

public interface IDatabaseContext {

    IHistoryGateway createHistoryGateway();

    ILanguagesGateway createLanguagesGateway();

    void close();
}
