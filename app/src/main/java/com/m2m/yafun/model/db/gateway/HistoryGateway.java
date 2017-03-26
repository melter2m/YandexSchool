package com.m2m.yafun.model.db.gateway;

import android.content.ContentValues;
import android.database.Cursor;

import com.m2m.yafun.model.db.DbOpenHelper;
import com.m2m.yafun.model.db.entities.HistoryItem;
import com.m2m.yafun.model.db.scheme.DbScheme;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class HistoryGateway extends DatedEntityGateway<HistoryItem> implements IHistoryGateway {

    public HistoryGateway(DbOpenHelper dbOpenHelper) {
        super(DbScheme.History.TableName, dbOpenHelper);
    }

    @Override
    public HistoryItem setFavorite(HistoryItem item, boolean value) {
        HistoryItem result = new HistoryItem(item.getId(), item.getDate(), item.getText(), item.getDirection(), item.getTranslation(), value);
        updateItem(item.getId(), result);
        return result;
    }

    @Override
    public List<HistoryItem> getOnlyFavorite() {
        return getItems(null, DbScheme.History.IsFavorite + " = ?", new String[] {TrueValue + ""}, null, null, null);
    }

    @Override
    protected void fillContentValuesWithSpecificData(HistoryItem item, ContentValues cv) {
        cv.put(DbScheme.History.Direction, item.getDirection());
        cv.put(DbScheme.History.Text, item.getText());
        cv.put(DbScheme.History.Translated, getTranslation(item));
        putBoolean(cv, DbScheme.History.IsFavorite, item.isFavorite());
    }

    private String getTranslation(HistoryItem item) {
        JSONArray array = new JSONArray();
        for (String translation: item.getTranslation()) {
            array.put(translation);
        }
        return array.toString();
    }

    @Override
    protected HistoryItem extractItem(Cursor c) {
        String direction = c.getString(c.getColumnIndex(DbScheme.History.Direction));
        String text = c.getString(c.getColumnIndex(DbScheme.History.Text));
        List<String> translation = getTranslation(c);
        boolean isFavorite = getBoolean(c, DbScheme.History.IsFavorite);
        return new HistoryItem(getId(c), getDate(c), text, direction, translation, isFavorite);
    }

    private List<String> getTranslation(Cursor c) {
        List<String> result = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(c.getString(c.getColumnIndex(DbScheme.History.Translated)));
            for (int i = 0; i < array.length(); ++i) {
                result.add(array.getString(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    private static final int TrueValue = 1;
    private static final int FalseValue = 0;

    private void putBoolean(ContentValues cv, String column, boolean value) {
        int toPut = value ? TrueValue : FalseValue;
        cv.put(column, toPut);
    }

    private boolean getBoolean(Cursor c, String column) {
        int value = c.getInt(c.getColumnIndex(column));
        return value == TrueValue;
    }


}
