package com.m2m.yafun.model.db.scheme;


import android.provider.BaseColumns;

public class DbScheme {

    public final static String DbName = "translate.db";

    public interface Base extends BaseColumns {
        String Id = BaseColumns._ID;
    }

    public interface DatedEntity extends Base {
        String Date = "date";
    }

    public interface History extends DatedEntity {
        String TableName = "history";
        String Direction = "direction";
        String Text = "text";
        String Translated = "translated";
        String IsFavorite = "is_favorite";
    }

    public interface Languages extends DatedEntity {
        String TableName = "languages";
        String Directions = "dirs";
        String Languages = "lang";
    }

}
