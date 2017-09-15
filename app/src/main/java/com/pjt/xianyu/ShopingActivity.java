package com.pjt.xianyu;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.LruCache;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.pjt.xianyu.pojo.Product;

public class ShopingActivity extends AppCompatActivity {

    ImageView p_img=null;
    TextView  p_title=null;
    TextView p_price=null;
    TextView p_money=null;
    Product p=null;
    Gson gson=null;
    ImageLoader imageLoader=null;
    RequestQueue requestQueue=null;
    Button btn_shop=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shoping);
        p_img= (ImageView) findViewById(R.id.p_img);
        p_title= (TextView) findViewById(R.id.p_title);
        p_price= (TextView) findViewById(R.id.p_price);
        p_money= (TextView) findViewById(R.id.text_money);
        btn_shop= (Button) findViewById(R.id.btn_shop);
        requestQueue= Volley.newRequestQueue(getApplicationContext());
        imageLoader=new ImageLoader(requestQueue, new ImageLoader.ImageCache() {
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
        Intent data=getIntent();
        if(data!=null){
            String st=data.getStringExtra("p");
            p=gson.fromJson(st,Product.class);
            ImageLoader.ImageListener imageListener=imageLoader.getImageListener(p_img,R.drawable.loading_failed_big_icon,
                    R.drawable.loading_failed_big_icon);
            imageLoader.get(Util.URL+"product/"+p.getBitsurl().get(0),imageListener);
            p_title.setText(p.getTitle() + "，" + p.getInfo());
            p_price.setText(p.getPriceinfo() == null ? "￥" + p.getPrice() : p.getPriceinfo());
            p_money.setText("￥"+p.getPrice());

        }

        btn_shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(ShopingActivity.this).setTitle("需支付金额")
                        .setMessage("￥ "+p_money.getText().toString()).setNegativeButton("取消",null)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent1=new Intent(ShopingActivity.this,MainActivity.class);
                                startActivity(intent1);
                                finish();
                                Util.clearActivity();
                                Toast.makeText(ShopingActivity.this, "购买成功", Toast.LENGTH_SHORT).show();
                            }
                        })
                .create().show();
            }
        });

    }
}
