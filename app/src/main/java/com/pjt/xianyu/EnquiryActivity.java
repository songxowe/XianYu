package com.pjt.xianyu;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.LruCache;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.pjt.xianyu.pojo.Product;

public class EnquiryActivity extends AppCompatActivity {

    Button btn_buy=null;
    ImageView imageView=null;
    TextView text_price=null;
    TextView username=null;
    Gson gson=null;
    ImageLoader imageLoader=null;
    RequestQueue requestQueue=null;
    Product p=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enquiry);
        Util.ContextArray.add(EnquiryActivity.this);
        gson=new Gson();
        requestQueue=Volley.newRequestQueue(getApplicationContext());
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
        username= (TextView) findViewById(R.id.p_user_name);
        text_price= (TextView) findViewById(R.id.p_price);
        imageView= (ImageView) findViewById(R.id.p_img);
        btn_buy= (Button) findViewById(R.id.btn_buy);
        ButtonListener buttonListener=new ButtonListener();
        btn_buy.setOnClickListener(buttonListener);
        Intent data=getIntent();
        if(data!=null){
            String st=data.getStringExtra("p");
             p=gson.fromJson(st, Product.class);
            ImageLoader.ImageListener imageListener=imageLoader.getImageListener(imageView,
                    R.drawable.loading_failed_big_icon,R.drawable.loading_failed_big_icon);
            imageLoader.get(Util.URL+"product/"+p.getBitsurl().get(0),imageListener);
            username.setText(p.getUsername());
            text_price.setText(p.getPriceinfo()==null?"￥ "+p.getPrice():p.getPriceinfo());
        }


    }


    private class ButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_buy:
                    if(p!=null) {
                        Intent intent = new Intent(getApplicationContext(), ShopingActivity.class);
                        String st=gson.toJson(p);
                        intent.putExtra("p",st);
                        startActivity(intent);


                    }

                    break;
            }
        }
    }
}
