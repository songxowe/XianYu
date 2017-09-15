package com.pjt.xianyu.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.pjt.xianyu.pojo.County;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/06/02.
 */
public class CountyDao {

    SQLiteDatabase db = null;
    static Context context = null;

    public CountyDao(Context context) {
        this.context = context;
    }

    public ArrayList<County> findAll() {
        ArrayList<County> list = new ArrayList<>();
        db = DBUtil.openDatabase(context);
        try {
            Cursor cursor = db.query("t_county", null, null, null, null, null, null);

            while (cursor.moveToNext()) {
                County county = new County();
                county.setId(cursor.getInt(0));
                county.setNo(cursor.getString(1));
                county.setName(cursor.getString(2));
                list.add(county);
            }


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(db);
        }


        return list;
    }

    public ArrayList<County> findByNo(String no) {
        ArrayList<County> list = new ArrayList<>();
        db = DBUtil.openDatabase(context);
        try {
            Cursor cursor = db.query("t_county", null, "no like ?",new String[]{no+"%"}, null, null, null);

            while (cursor.moveToNext()) {
                County county = new County();
                county.setId(cursor.getInt(0));
                county.setNo(cursor.getString(1));
                county.setName(cursor.getString(2));
                list.add(county);
            }


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(db);
        }


        return list;
    }

    public boolean addCounty(ArrayList<County> list) {
        boolean bool = false;
        try {
            db = DBUtil.openDatabase(context);
            for (County c : list) {
                ContentValues cv = new ContentValues();
                cv.put("no", c.getNo());
                cv.put("name", c.getName());
                db.insert("t_county", null, cv);
            }
            bool = true;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(db);
        }


        return bool;
    }
}
