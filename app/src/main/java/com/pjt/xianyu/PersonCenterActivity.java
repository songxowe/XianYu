package com.pjt.xianyu;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.renderscript.Allocation;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.LruCache;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pjt.xianyu.pojo.Product;
import com.pjt.xianyu.pojo.User;

import java.util.ArrayList;

public class PersonCenterActivity extends AppCompatActivity {

    private RelativeLayout person_R=null;

    private ImageView top_img=null;
    private NoScrollListView listView=null;
    private ProductAdapter productAdapter=null;
    private ArrayList<Product>list=null;
    private long user_id=0;
    private RequestQueue requestQueue=null;
    private StringRequest stringRequest=null;

    private TextView  username=null;
    private CircleImageView user_img=null;
    private TextView text_sex=null;
    private TextView text_info=null;
    private TextView comment_count=null;
    ImageLoader loader=null;
    StringRequest productRequest=null;

    Gson gson=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_center);
        requestQueue= Volley.newRequestQueue(getApplicationContext());
        gson=new Gson();
        loader=new ImageLoader(requestQueue, new ImageLoader.ImageCache() {
            private LruCache<String,Bitmap>lruCache=new LruCache<>(1024*1024*3);

            @Override
            public Bitmap getBitmap(String s) {
                return lruCache.get(s);
            }

            @Override
            public void putBitmap(String s, Bitmap bitmap) {
                 lruCache.put(s,bitmap);
            }
        });
        gson=new Gson();
        initView();
        Intent data=getIntent();
        if(data!=null){
           user_id=data.getLongExtra("user_id",0);
            if(user_id!=0){
                findPerson();
            }
        }

        person_R= (RelativeLayout) findViewById(R.id.person_center_r);
        top_img= (ImageView) findViewById(R.id.top_img);
        listView= (NoScrollListView) findViewById(R.id.Mycenter_listview);
        list=new ArrayList<>();
        productAdapter=new ProductAdapter(PersonCenterActivity.this,list);
        listView.setFocusable(false);
        listView.setAdapter(productAdapter);
        findUserProduct();

    }

    private void findUserProduct() {
        productRequest=new StringRequest(Util.URL + "UserServlet?action=userProduct&type=android" +
                "&user_id="+user_id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                       if(s.trim().length()>0){
                           list.clear();
                           ArrayList<Product>array=gson.fromJson(s,new TypeToken<ArrayList<Product>>(){}.getType());
                           for(Product p:array){
                              list.add(p);
                           }
                           listView.post(new Runnable() {
                               @Override
                               public void run() {
                                   productAdapter.notifyDataSetChanged();
                               }
                           });

                       }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        requestQueue.add(productRequest);

    }

    private void initView() {
        username= (TextView) findViewById(R.id.user_center_name);
        user_img= (CircleImageView) findViewById(R.id.user_img);
        text_sex= (TextView) findViewById(R.id.address_sex);
        text_info= (TextView) findViewById(R.id.user_info);
        comment_count= (TextView) findViewById(R.id.user_pingjia);

    }

    private void findPerson() {
        stringRequest=new StringRequest(Util.URL + "UserServlet?action=findUser&type=android" +
                "&user_id="+user_id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if(s.trim().length()>0){
                            User user=gson.fromJson(s, User.class);
                            username.setText(user.getUsername());
                            ImageLoader.ImageListener imageListener=
                                    loader.getImageListener(user_img,R.drawable.loading_failed_big_icon,
                                            R.drawable.loading_failed_big_icon);
                            loader.get(Util.URL+"user/"+user.getImg(),imageListener);
                            text_sex.setText(user.getAddress() + "|" + user.getSex());
                            text_info.setText("这家伙很懒，什么都没留下");
                            comment_count.setText("收到评价"+user.getCommentcount()+"条");

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        requestQueue.add(stringRequest);

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

}
