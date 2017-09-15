package com.pjt.xianyu.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.pjt.xianyu.pojo.Province;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/06/02.
 */
public class ProvinceDao {
    SQLiteDatabase db = null;
    static Context context = null;

    public ProvinceDao(Context context) {
        this.context = context;
    }

    public ArrayList<Province> findAll() {
        ArrayList<Province> list = new ArrayList<>();
        db = DBUtil.openDatabase(context);
        try {
            Cursor cursor = db.query("t_province", null, null, null, null, null, null);

            while (cursor.moveToNext()) {
                Province province = new Province();
                province.setId(cursor.getInt(0));
                province.setNo(cursor.getString(1));
                province.setName(cursor.getString(2));
                list.add(province);
            }


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(db);
        }


        return list;
    }

    public boolean addProvince(ArrayList<Province>list){
        boolean bool=false;
        try{
            db=DBUtil.openDatabase(context);
            for(Province p:list) {
                ContentValues cv=new ContentValues();
                cv.put("no",p.getNo());
                cv.put("name",p.getName());
                db.insert("t_province", null,cv );
            }
            bool=true;

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            DBUtil.close(db);
        }



        return bool;
    }

    public ArrayList<Province> findByNo(String no) {
        ArrayList<Province> list = new ArrayList<>();
        db = DBUtil.openDatabase(context);
        try {
            Cursor cursor = db.query("t_county", null, "no like ?",new String[]{no+"%"}, null, null, null);

            while (cursor.moveToNext()) {
                Province county = new Province();
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
}
