package com.pjt.xianyu;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class MessageService extends Service {

    RequestQueue requestQueue=null;
    boolean isrun=false;
    MyHandler handler=null;
    SharedPreferences sp=null;
    long comment_id=0;
    long zan_id=0;
    long user_id=0;

    class MyHandler extends Handler {
        public MyHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            user_id=sp.getLong("user_id",0);
            zan_id=sp.getLong("zan_id",0);
            comment_id=sp.getLong("comment_id",0);
            switch (msg.what){
                case 100:
                 StringRequest  stringRequest =new StringRequest(Util.URL + "UserServlet?type=android&action=findMaxGood&user_id="
                          +user_id+"&zan_id="+zan_id,
                          new Response.Listener<String>() {
                              @Override
                              public void onResponse(String s) {
                                 if(s.trim().length()>0){
                                     SharedPreferences.Editor editor=sp.edit();
                                     editor.putLong("zan_id",Long.parseLong(s.trim()));
                                     editor.commit();
                                     Notification.Builder builder = new Notification.Builder(getApplicationContext());
                                     builder.setSmallIcon(R.drawable.head5);
                                     builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.head5));
                                     builder.setContentTitle("你收到了一个赞");
                                     builder.setDefaults(Notification.DEFAULT_ALL);
                                     Notification notification=builder.build();


                                     NotificationManager notificationManager= (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                     notificationManager.notify(1,notification);

                                 }

                              }
                          }, new Response.ErrorListener() {
                      @Override
                      public void onErrorResponse(VolleyError volleyError) {

                      }
                  });
                    requestQueue.add(stringRequest);


                    break;


                case 200:
                   StringRequest stringRequest1 =new StringRequest(Util.URL + "UserServlet?type=android&action=findMaxComment&user_id="
                            + user_id+"&comment_id="+comment_id,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String s) {
                                    if(s.trim().length()>0){
                                        SharedPreferences.Editor editor=sp.edit();
                                        editor.putLong("comment_id",Long.parseLong(s.trim()));
                                        editor.commit();
                                        Notification.Builder builder = new Notification.Builder(getApplicationContext());
                                        builder.setSmallIcon(R.drawable.head5);
                                        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.head5));
                                        builder.setContentTitle("你有一个新的留言");
                                        builder.setDefaults(Notification.DEFAULT_ALL);
                                        Notification notification=builder.build();
                                        NotificationManager notificationManager= (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                        notificationManager.notify(1,notification);

                                    }

                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {

                        }
                    });
                    requestQueue.add(stringRequest1);



                    break;
            }
        }
    }

    public MessageService() {

    }

    @Override
    public IBinder onBind(Intent intent) {

        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        handler=new MyHandler(getMainLooper());
        sp = Util.getSp(getApplicationContext());
        zan_id=sp.getLong("zan_id", 0);
        comment_id=sp.getLong("comment_id",0);
        requestQueue= Volley.newRequestQueue(getApplicationContext());
        isrun=true;
        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                while(isrun){
                    Message msg=new Message();
                    msg.what=100;
                    handler.sendMessage(msg);
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();

        Thread thread1=new Thread(new Runnable() {
            @Override
            public void run() {
                while(isrun){
                    Message msg=new Message();
                    msg.what=200;
                    handler.sendMessage(msg);
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread1.start();

    }


}
