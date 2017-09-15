package com.pjt.xianyu;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/05/25.
 */
public class Util {

    //圆形图片
    public static Bitmap createCirBitmap(Bitmap bitmap, int min) {
        final Paint paint = new Paint();
        paint.setAntiAlias(true);
        Bitmap newBit = Bitmap.createBitmap(min, min, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newBit);

        canvas.drawCircle(min / 2, min / 2, min / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, 0, 0, paint);

        return newBit;
    }


    public static final String URL = "http://192.168.43.141:8088/XianYuService/";


    public static SharedPreferences getSp(Context context) {

        SharedPreferences sp = context.getSharedPreferences
                ("f1", Context.MODE_WORLD_READABLE + Context.MODE_WORLD_WRITEABLE);

        return sp;
    }

    public static  void Toast(Context context,String st){
        Toast.makeText(context, st, Toast.LENGTH_SHORT).show();
    }

    static ArrayList<Context> ContextArray=new ArrayList<>();

    public  static void clearActivity(){
        for(Context context:ContextArray){
            ((Activity)context).finish();
        }

    }


}
