package com.m2m.yafun.model.db.gateway;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.m2m.yafun.model.db.DbOpenHelper;
import com.m2m.yafun.model.db.entities.EntityBase;
import com.m2m.yafun.model.db.scheme.DbScheme;

import java.util.ArrayList;
import java.util.List;

public abstract class TableGateway<T extends EntityBase> implements ITableGateway<T> {
    protected DbOpenHelper dbOpenHelper;
    protected String tableName;

    protected final static String EqualsValue = " = ? ";
    protected final static String And = " AND ";
    protected final static String DescendingOrder = " desc";

    protected TableGateway(String tableName, DbOpenHelper dbOpenHelper) {
        this.tableName = tableName;
        this.dbOpenHelper = dbOpenHelper;
    }

    protected String getTableName() {
        return tableName;
    }

    @Override
    public boolean isItemExist(long id) {
        try {
            getItemById(id);
            return true;
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
    }

    @Override
    public long insertItem(T itemToInsert) {
        ContentValues cv = getContentValues(itemToInsert);
        return insert(cv);
    }

    protected long insert(ContentValues contentValues) {
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        return db.insert(tableName, null, contentValues);
    }

    @Override
    public void deleteItem(long id) {
        deleteItems(DbScheme.Base.Id + EqualsValue, new String[]{"" + id});
    }

    protected void deleteItems(String where, String[] whereArgs) {
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        db.delete(tableName, where, whereArgs);
    }

    @Override
    public T getItemById(long id) {
        T result;
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        Cursor c = db.query(
                getTableName(),
                null,
                DbScheme.Base.Id + EqualsValue, new String[]{id + ""},
                null, null, null
        );
        if (c.moveToFirst())
            result = extractItem(c);
        else
            result = null;
        c.close();
        return result;
    }

    @Override
    public List<T> getAllItems() {
        return getItems(null, null, null, null, null, null);
    }

    /// Все параметры задаются так же, как и в SQLiteDatabase.query, так что rtfm.
    protected List<T> getItems(String[] columns, String selection,
                               String[] selectionArgs, String groupBy, String having,
                               String orderBy) {
        List<T> result = new ArrayList<>();

        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        Cursor c = db.query(getTableName(), columns, selection, selectionArgs, groupBy, having, orderBy);

        if (c.moveToFirst()) {
            do {
                T item = extractItem(c);
                result.add(item);
            } while (c.moveToNext());
        }
        c.close();

        db.close();
        return result;
    }

    @Override
    public List<Long> getIds() {
        List<Long> result = new ArrayList<>();

        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        Cursor c = db.query(getTableName(), new String[]{DbScheme.Base.Id}, null, null, null, null, null);

        if (c.moveToFirst()) {
            do {
                long id = getId(c);
                result.add(id);
            } while (c.moveToNext());
        }
        c.close();
        db.close();
        return result;
    }

    @Override
    public void updateItem(long id, T newItemValue) {
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        ContentValues cv = getContentValues(newItemValue);
        db.update(getTableName(), cv, DbScheme.Base.Id + EqualsValue, new String[]{id + ""});
    }

    protected abstract T extractItem(Cursor c);

    protected long getId(Cursor c) {
        return c.getLong(c.getColumnIndex(DbScheme.Base.Id));
    }

    protected abstract ContentValues getContentValues(T item);
}