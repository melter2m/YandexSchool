package com.m2m.yafun.view.pages.translate;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ArrayAdapter;

import com.m2m.yafun.model.api.service.result.Languages;

import java.util.ArrayList;
import java.util.List;

class LanguagesAdapter extends ArrayAdapter<String>{

    protected Languages languages;
    List<String> languageIds;

    LanguagesAdapter(@NonNull Context context, Languages languages) {
        super(context, android.R.layout.simple_spinner_dropdown_item);
        this.languages = languages;
        languageIds = new ArrayList<>(languages.getLanguages().keySet());
    }

    @Override
    public int getCount() {
        return languages.getLanguagesNames().size();
    }

    @Nullable
    @Override
    public String getItem(int position) {
        String lang = languageIds.get(position);
        return languages.getLanguages().get(lang);
    }

    int getLanguagePosition(String lang) {
        return languageIds.indexOf(lang);
    }

    public String getLanguageId(int position) {
        return languageIds.get(position);
    }
}
