package com.pjt.xianyu;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ThumbnailUtils;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/05/29.
 */
public class LovePersonView extends View {

    ArrayList<Bitmap>bits=new ArrayList<>();

    public LovePersonView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int height=canvas.getHeight();
        float tempheight=(height-100)/2;
        Paint paint=new Paint();
        paint.setAntiAlias(true);
        int count=20;
        for(Bitmap bitmap:bits){
            canvas.drawBitmap(bitmap,count,tempheight,paint);
            count+=70;
        }


    }


    public void setLovePerson(ArrayList<Bitmap>bits){
        this.bits.clear();
        for(Bitmap bit:bits){
            this.bits.add(Util.createCirBitmap(ThumbnailUtils.extractThumbnail(
                            bit,
                            100, 100),
                    100));
        }
        this.postInvalidate();
    }
}
