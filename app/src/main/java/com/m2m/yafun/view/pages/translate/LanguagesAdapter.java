package com.m2m.yafun.view.pages.translate;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ArrayAdapter;

import com.m2m.yafun.model.api.service.result.Languages;

import java.util.ArrayList;
import java.util.List;

public class LanguagesAdapter extends ArrayAdapter<String>{

    private Languages languages;
    private List<String> directions;

    public LanguagesAdapter(@NonNull Context context, Languages languages) {
        super(context, android.R.layout.simple_spinner_dropdown_item);
        this.languages = languages;
        directions = new ArrayList<>(languages.getLanguages().keySet());
    }

    @Override
    public int getCount() {
        return languages.getLanguagesNames().size();
    }

    @Nullable
    @Override
    public String getItem(int position) {
        return languages.getLanguagesNames().get(position);
    }

    public String getLanguageForDirection(int position) {
        return directions.get(position);
    }
}
