package com.pjt.xianyu;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.pjt.xianyu.pojo.YuTang;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/05/31.
 */
public class MyYTAdapter extends BaseAdapter {

    ArrayList<YuTang>list=null;
    Context context=null;
    LayoutInflater layoutInflater=null;
    ImageLoader imageLoader=null;
    RequestQueue requestQueue=null;


    public MyYTAdapter(Context context, ArrayList<YuTang> list) {

        this.list=list;
        this.context=context;
        this.layoutInflater=LayoutInflater.from(context);
        requestQueue= Volley.newRequestQueue(context);
        imageLoader=new ImageLoader(requestQueue, new ImageLoader.ImageCache() {
            private LruCache<String,Bitmap>lruCache=new LruCache<>(1024*1024*6);
            @Override
            public Bitmap getBitmap(String s) {
                return lruCache.get(s);
            }

            @Override
            public void putBitmap(String s, Bitmap bitmap) {
                   lruCache.put(s,bitmap);
            }
        });
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public YuTang getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder=null;
        if(convertView==null){
            convertView=layoutInflater.inflate(R.layout.yt_my_item,parent,false);
            viewHolder=new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) convertView.getTag();
        }

        viewHolder.binder(list.get(position));


        return convertView;
    }

    private class ViewHolder {
        TextView textname=null;
        TextView text_pop=null;
        ImageView img=null;


        public ViewHolder(View convertView) {
         textname= (TextView) convertView.findViewById(R.id.myyt_name);
            text_pop= (TextView) convertView.findViewById(R.id.my_yt_pop);
            img= (ImageView) convertView.findViewById(R.id.myyt_img);

        }

        public void binder(YuTang yuTang) {
             textname.setText(yuTang.getName());
            ImageLoader.ImageListener imageListener=imageLoader.getImageListener(img,
                    R.drawable.loading_failed_big_icon,R.drawable.loading_failed_big_icon);
            imageLoader.get(Util.URL+"yutang/"+yuTang.getImgurl(),imageListener);

            text_pop.setText(String.valueOf(yuTang.getPopularity()));

        }
    }
}
