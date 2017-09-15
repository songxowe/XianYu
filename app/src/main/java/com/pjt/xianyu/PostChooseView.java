package com.pjt.xianyu;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.ThumbnailUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Administrator on 2016/05/30.
 */
public class PostChooseView extends View {


    private int state=1;
    private Canvas canvas=null;
    private float bitx=0;
    private float bity=0;
    private Bitmap lastBitmap=null;
    private int laststate=1;




    public PostChooseView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);
        Paint paint=new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(5);
        this.canvas=canvas;


        Bitmap bit=BitmapFactory.decodeResource(getResources(),R.drawable.slide_bar_bg);
        Bitmap center_bit=BitmapFactory.decodeResource(getResources(), R.drawable.slide_bar_center_bg);



        Matrix matrix=new Matrix();
        matrix.postScale(0.5f, 0.5f);

        matrix.postTranslate(150, canvas.getHeight() / 2);

        canvas.drawBitmap(bit, matrix, paint);
        Matrix matrix1=new Matrix();
        matrix1.postTranslate(1000,
                canvas.getHeight() / 2 + 150);
        matrix1.postScale(0.5f, 0.5f);

        canvas.drawBitmap(center_bit, matrix1, paint);

        if(state==1) {
            paint.setTextSize(50);
            paint.setColor(Color.RED);
            canvas.drawText("一口价", 150 - 60, 250, paint);
            paint.setColor(Color.BLACK);
            canvas.drawText("拍卖", canvas.getWidth() / 2 - 60, 250, paint);
            canvas.drawText("不开价", canvas.getWidth() - 270, 250, paint);
            Bitmap onebit = BitmapFactory.decodeResource(getResources(), R.drawable.post_thumb_for_money_on);
            Matrix matrix2 = new Matrix();
            matrix2.postScale(0.5f, 0.5f);
            matrix2.postTranslate(120, canvas.getHeight() / 2 - 40);
            canvas.drawBitmap(onebit, matrix2, paint);
            this.lastBitmap=onebit;
            this.laststate=1;
        }else if(state==2){

            paint.setTextSize(50);
            paint.setColor(Color.RED);
            canvas.drawText("拍卖", canvas.getWidth() / 2 - 60, 250, paint);
            paint.setColor(Color.BLACK);
            canvas.drawText("一口价", 150 - 60, 250, paint);
            canvas.drawText("不开价", canvas.getWidth() - 270, 250, paint);

            Bitmap paimai = BitmapFactory.decodeResource(getResources(),
                    R.drawable.post_thumb_auctions_on);

            Matrix matrix2 = new Matrix();
            matrix2.postScale(0.5f, 0.5f);
            matrix2.postTranslate(canvas.getWidth() / 2 - 60, canvas.getHeight() / 2 - 40);
            canvas.drawBitmap(paimai, matrix2, paint);
            this.lastBitmap=paimai;
            this.laststate=2;

        }else if(state==3){

            paint.setTextSize(50);
            paint.setColor(Color.RED);
            canvas.drawText("不开价", canvas.getWidth() - 270, 250, paint);

            paint.setColor(Color.BLACK);
            canvas.drawText("一口价", 150 - 60, 250, paint);
            canvas.drawText("拍卖", canvas.getWidth() / 2 - 60, 250, paint);

            Bitmap nomoney = BitmapFactory.decodeResource(getResources(),
                    R.drawable.post_thumb_not_for_money_on);

            Matrix matrix2 = new Matrix();
            matrix2.postScale(0.5f, 0.5f);
            matrix2.postTranslate(canvas.getWidth() - 270, canvas.getHeight() / 2 - 40);
            canvas.drawBitmap(nomoney, matrix2, paint);
            this.lastBitmap=nomoney;
            this.laststate=3;
        }else{
            paint.setTextSize(50);

            paint.setColor(laststate == 1 ? Color.RED : Color.BLACK);
            canvas.drawText("一口价", 150 - 60, 250, paint);
            paint.setColor(laststate == 2 ? Color.RED : Color.BLACK);
            canvas.drawText("拍卖", canvas.getWidth() / 2 - 60, 250, paint);
            paint.setColor(laststate == 3 ? Color.RED : Color.BLACK);
            canvas.drawText("不开价", canvas.getWidth() - 270, 250, paint);
            Matrix matrix2 = new Matrix();
            matrix2.postScale(0.5f, 0.5f);
            matrix2.postTranslate(bitx, canvas.getHeight() / 2 - 40);
            canvas.drawBitmap(lastBitmap, matrix2, paint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x=0;
        float y=0;
        this.getParent().requestDisallowInterceptTouchEvent(true);
        switch (event.getAction()){
            case MotionEvent.ACTION_MOVE:
                bitx=event.getX();
                bity=event.getY();
                if(bitx>canvas.getWidth() - 270){
                    bitx=canvas.getWidth()-270;
                }

                if(bitx<120){
                    bitx=120;
                }
                state=0;
                this.postInvalidate();

                return true;


            case MotionEvent.ACTION_UP:
                bitx=event.getX();
                bity=event.getY();
                if(Math.abs(bitx-120)<Math.abs(bitx-(canvas.getWidth() / 2 - 60))
                        &&Math.abs(bitx-120)<Math.abs(bitx-(canvas.getWidth() - 270))) {
                    state=1;
                    sendState();
                }else if(Math.abs(bitx-(canvas.getWidth() / 2 - 60))<Math.abs(bitx-120)
                        &&Math.abs(bitx-(canvas.getWidth() / 2 - 60))<Math.abs(bitx-(canvas.getWidth() - 270))){
                    state=2;
                    sendState();
                }else if(Math.abs(bitx-(canvas.getWidth() - 270))<Math.abs(bitx-120)
                        &&Math.abs(bitx-(canvas.getWidth() - 270))<Math.abs(bitx-(canvas.getWidth() / 2 - 60))){
                    state=3;
                    sendState();
                }
                this.postInvalidate();

                return true;


            case MotionEvent.ACTION_DOWN:
                x=event.getX();
                y=event.getY();
                Log.d("消息","x"+x);

                if(x>=100&&x<=200&&y>=120&&y<=250){
                    this.state=1;
                }else if(x>=getWidth() / 2 - 60&&x<=getWidth() / 2&&y>=120&&y<=250){
                    this.state=2;

                }else if(x>=getWidth() - 280&&x<=getWidth() - 200&&y>=120&&y<=250){
                    this.state=3;

                }

                this.postInvalidate();


                return true;



        }






        return super.onTouchEvent(event);
    }


    public void sendState(){
        Intent intent=new Intent("com.pjt.post.state");
        intent.putExtra("state",this.state);
        getContext().sendBroadcast(intent);
    }
}
