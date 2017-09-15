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
import com.pjt.xianyu.pojo.CateGory;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/05/27.
 */
public class CateAdapter extends BaseAdapter {

    private Context context=null;
    private ArrayList<CateGory>list=null;
    private LayoutInflater layoutInflater=null;
    private RequestQueue requestQueue=null;

    public CateAdapter(Context context, ArrayList<CateGory> list) {
        this.list=list;
        this.context=context;
        this.layoutInflater=LayoutInflater.from(context);
        requestQueue= Volley.newRequestQueue(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public CateGory getItem(int position) {
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
            convertView=layoutInflater.inflate(R.layout.cate_item,parent,false);
            viewHolder=new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) convertView.getTag();
        }

        viewHolder.binder(list.get(position));





        return convertView;
    }

    private class ViewHolder {
        private ImageView imageView=null;
        private TextView cate_name=null;
        ImageLoader imageLoader=new ImageLoader(requestQueue, new ImageLoader.ImageCache() {

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


        public ViewHolder(View convertView) {
            imageView= (ImageView) convertView.findViewById(R.id.cate_img);
            cate_name= (TextView) convertView.findViewById(R.id.cate_name);
        }

        public void binder(CateGory cateGory) {
            cate_name.setText(cateGory.getCate_name());
            ImageLoader.ImageListener listener=imageLoader.getImageListener(imageView,R.drawable.
            loading_failed_big_icon,R.drawable.loading_failed_big_icon);
            imageLoader.get(Util.URL+"category/"+cateGory.getCate_img(),listener);
        }
    }
}
