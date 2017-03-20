package com.m2m.yafun.model.db.gateway;

import android.content.ContentValues;
import android.database.Cursor;

import com.m2m.yafun.model.db.DbOpenHelper;
import com.m2m.yafun.model.db.entities.DatedEntity;
import com.m2m.yafun.model.db.scheme.DbScheme;

import java.util.Calendar;
import java.util.List;

public abstract class DatedEntityGateway<T extends DatedEntity> extends TableGateway<T> {

    protected DatedEntityGateway(String tableName, DbOpenHelper dbOpenHelper) {
        super(tableName, dbOpenHelper);
    }

    protected Calendar getDate(Cursor c) {
        return getDate(c.getLong(c.getColumnIndex(DbScheme.DatedEntity.Date)));
    }

    @Override
    protected ContentValues getContentValues(T item) {
        ContentValues cv = getCvWithDate(item);
        fillContentValuesWithSpecificData(item, cv);
        return cv;
    }

    protected abstract void fillContentValuesWithSpecificData(T item, ContentValues cv);

    private static ContentValues getCvWithDate(DatedEntity item) {
        ContentValues cv = new ContentValues();
        cv.put(DbScheme.DatedEntity.Date, item.getDate().getTimeInMillis());
        return cv;
    }

    public List<T> getAllItemsSortedByDate(boolean ascending) {
        String order = ascending ? DbScheme.DatedEntity.Date : DbScheme.DatedEntity.Date + DescendingOrder;
        return getItems(null, null, null, null, null, order);
    }

    static public Calendar getDate(long dateInMillis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(dateInMillis);
        return calendar;
    }

}
