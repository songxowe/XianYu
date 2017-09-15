package com.pjt.xianyu.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Administrator on 2016/06/02.
 */
public class DBUtil {

    public static SQLiteDatabase openDatabase(Context context) {
        SQLiteDatabase db = null;
        DBHelper dbHelper = null;
        try {
            dbHelper = new DBHelper(context, "mycity.db", null, 2);
            db = dbHelper.getWritableDatabase();


        } catch (Exception e) {
            db = dbHelper.getReadableDatabase();
            e.printStackTrace();
        }


        return db;
    }


    public static void close(SQLiteDatabase db){
        try{
            if(db!=null){
                db.close();
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }


}
