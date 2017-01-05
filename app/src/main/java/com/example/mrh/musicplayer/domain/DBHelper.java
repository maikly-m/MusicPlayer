package com.example.mrh.musicplayer.domain;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.example.mrh.musicplayer.constant.Constant.CUSTOM_LIST;

/**
 * Created by MR.H on 2016/12/6 0006.
 */

public class DBHelper extends SQLiteOpenHelper{

    public DBHelper (Context context) {
        super(context, "musicPlayer", null, 1);
    }
    public DBHelper (Context context, String name, SQLiteDatabase.CursorFactory factory, int
            version) {
        super(context, name, factory, version);
    }

    public DBHelper (Context context, String name, SQLiteDatabase.CursorFactory factory,
                     int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate (SQLiteDatabase db) {
        String sql="CREATE TABLE " + CUSTOM_LIST + " ( ID integer primary key autoincrement" +
                ", listName vachar (50), songsCount int (20))";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade (SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
