package com.m2m.yafun.model.db.gateway;

import com.m2m.yafun.model.api.service.result.Languages;

public interface ILanguagesGateway {

    Languages getLanguages();

    void saveLanguages(Languages languages);

}
