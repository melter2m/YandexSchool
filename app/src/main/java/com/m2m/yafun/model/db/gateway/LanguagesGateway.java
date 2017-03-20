package com.m2m.yafun.model.db.gateway;


import android.content.ContentValues;
import android.database.Cursor;

import com.m2m.yafun.model.api.service.result.Languages;
import com.m2m.yafun.model.db.DbOpenHelper;
import com.m2m.yafun.model.db.entities.LanguagesItem;
import com.m2m.yafun.model.db.scheme.DbScheme;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LanguagesGateway extends DatedEntityGateway<LanguagesItem> implements ILanguagesGateway {

    public LanguagesGateway(DbOpenHelper dbOpenHelper) {
        super(DbScheme.Languages.TableName, dbOpenHelper);
    }

    @Override
    protected void fillContentValuesWithSpecificData(LanguagesItem item, ContentValues cv) {
        cv.put(DbScheme.Languages.Directions, getDirections(item));
        cv.put(DbScheme.Languages.Languages, getLanguages(item));
    }

    private String getLanguages(LanguagesItem item) {
        JSONArray languages = new JSONArray();
        for (Map.Entry<String, String> language : item.getLanguages().entrySet()) {
            try {
                JSONObject lang = new JSONObject();
                lang.put(language.getKey(), language.getValue());
                languages.put(lang);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return languages.toString();
    }

    private String getDirections(LanguagesItem item) {
        JSONArray directions = new JSONArray();
        for (String direction : item.getDirections()) {
            directions.put(direction);
        }
        return directions.toString();
    }

    @Override
    protected LanguagesItem extractItem(Cursor c) {
        return new LanguagesItem(getId(c), getDirections(c), getLanguages(c), getDate(c));
    }

    private List<String> getDirections(Cursor c) {
        List<String> result = new ArrayList<>();
        try {
            JSONArray directions = new JSONArray(c.getString(c.getColumnIndex(DbScheme.Languages.Directions)));
            for (int i = 0; i < directions.length(); ++i) {
                result.add(directions.getString(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    private Map<String, String> getLanguages(Cursor c) {
        Map<String, String> result = new HashMap<>();
        try {
            JSONArray languages = new JSONArray(c.getString(c.getColumnIndex(DbScheme.Languages.Languages)));
            for (int i = 0;i < languages.length(); ++i) {
                JSONObject language = languages.getJSONObject(i);
                JSONArray names = language.names();
                for (int j = 0; j < names.length(); ++j) {
                    String name = names.getString(i);
                    result.put(name, language.getString(name));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public Languages getLanguages() {
        List<LanguagesItem> all = getAllItemsSortedByDate(true);
        if (all.size() == 0)
            return null;
        LanguagesItem first = all.get(0);
        return new Languages(first.getDirections(), first.getLanguages());
    }

    @Override
    public void saveLanguages(Languages languages) {
        List<Long> ids = getIds();
        LanguagesItem item = new LanguagesItem(1, languages.getDirections(), languages.getLanguages(), Calendar.getInstance());
        if (ids.contains(1L))
            updateItem(1L, item);
        else
            insertItem(item);
    }
}
