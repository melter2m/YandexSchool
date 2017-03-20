package com.m2m.yafun.model.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.m2m.yafun.model.db.scheme.DbScheme;

public class DbOpenHelper extends SQLiteOpenHelper {

    private static final int DbVersion = 1;

    private static final String CreateTable = "CREATE TABLE ";
    private static final String IdFieldCreate = DbScheme.Base.Id + " INTEGER PRIMARY KEY AUTOINCREMENT ";

    private static final String FieldsBegin = " (";
    private static final String FieldsEnd = ");";
    private static final String AddField = ", ";

    private static final String IntegerType = " INTEGER";
    private static final String StringType = " TEXT";

    public DbOpenHelper(Context context) {
        super(context, DbScheme.DbName, null, DbVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createLanguagesTable(db);
        createHistoryTable(db);
    }


    private void createLanguagesTable(SQLiteDatabase db) {
        db.execSQL(CreateTable + DbScheme.Languages.TableName + FieldsBegin
                + IdFieldCreate
                + AddField + DbScheme.Languages.Directions + StringType
                + AddField + DbScheme.Languages.Languages + StringType
                + AddField + DbScheme.History.Date + IntegerType
                + FieldsEnd);
    }

    private void createHistoryTable(SQLiteDatabase db) {
        db.execSQL(CreateTable + DbScheme.History.TableName + FieldsBegin
                + IdFieldCreate
                + AddField + DbScheme.History.Text + StringType
                + AddField + DbScheme.History.Direction + StringType
                + AddField + DbScheme.History.Translated + StringType
                + AddField + DbScheme.History.Date + IntegerType
                + AddField + DbScheme.History.IsFavorite + IntegerType
                + FieldsEnd);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


}
