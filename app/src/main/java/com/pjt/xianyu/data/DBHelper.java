package com.pjt.xianyu.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2016/06/02.
 */
public class DBHelper extends SQLiteOpenHelper {


    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table t_province(" +
                "_id integer primary key autoincrement," +
                "no text not null," +
                "name text not null )");
        db.execSQL("create table t_county(" +
                "_id integer primary key autoincrement," +
                "no text not null," +
                "name text not null)");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table t_province");
        db.execSQL("drop table t_county");
        db.execSQL("create table t_province(" +
                "_id integer primary key autoincrement," +
                "no integer not null," +
                "name text not null )");
        db.execSQL("create table t_county(" +
                "_id integer primary key autoincrement," +
                "no integer not null," +
                "name text not null)");
    }
}
