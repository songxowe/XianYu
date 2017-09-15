package com.pjt.xianyu;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/05/29.
 */
public class CommentImgAdapter extends BaseAdapter {

    private Context context=null;
    private ArrayList<String>list=null;
    private LayoutInflater layoutInflater=null;
    private RequestQueue requestQueue=null;
    ImageLoader imageLoader=null;
    public CommentImgAdapter(Context context, ArrayList<String> imglist) {
        this.context=context;
        this.layoutInflater=LayoutInflater.from(context);
        this.list=imglist;
         requestQueue= Volley.newRequestQueue(context);
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
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public String getItem(int position) {
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
            convertView=layoutInflater.inflate(R.layout.p_info_header_item,parent,false);
            viewHolder=new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) convertView.getTag();
        }
        viewHolder.binder(list.get(position));

        return convertView;
    }

    private class ViewHolder {
        ImageView imageView=null;



        public ViewHolder(View convertView) {
            imageView= (ImageView) convertView.findViewById(R.id.img_item);
        }

        public void binder(String s) {
            ImageLoader.ImageListener imageListener=imageLoader.getImageListener(imageView,
                    R.drawable.loading_failed_big_icon,R.drawable.loading_failed_big_icon);
            imageLoader.get(Util.URL+"product/"+s,imageListener);
        }
    }
}
