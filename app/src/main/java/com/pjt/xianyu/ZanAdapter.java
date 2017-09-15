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
import com.pjt.xianyu.pojo.ClickLike;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/06/14.
 */
public class ZanAdapter extends BaseAdapter {
    private Context context=null;
    private ArrayList<ClickLike>list=null;
    private LayoutInflater layoutInflater=null;
    ImageLoader imageLoader=null;
    RequestQueue requestQueue=null;

    public ZanAdapter(Context context, ArrayList<ClickLike> list) {
        this.context=context;
        this.list=list;
        this.layoutInflater=LayoutInflater.from(context);
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
    public ClickLike getItem(int position) {
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
            convertView=layoutInflater.inflate(R.layout.good_item,parent,false);
            viewHolder=new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) convertView.getTag();
        }
        viewHolder.binder(list.get(position));

        return convertView;
    }

    private class ViewHolder {

        CircleImageView user_img=null;
        TextView username=null;
        TextView time=null;
        ImageView p_img=null;


        public ViewHolder(View convertView) {
            user_img= (CircleImageView) convertView.findViewById(R.id.user_img);
            username= (TextView) convertView.findViewById(R.id.user_name);
            time= (TextView) convertView.findViewById(R.id.time);
            p_img= (ImageView) convertView.findViewById(R.id.p_img);

        }

        public void binder(ClickLike clickLike) {
            ImageLoader.ImageListener imageListener=imageLoader.getImageListener(user_img,
                    R.drawable.loading_failed_big_icon,R.drawable.loading_failed_big_icon);
            username.setText(clickLike.getFromUsername());
            time.setText(clickLike.getTime());
            imageLoader.get(Util.URL + "user/" + clickLike.getFromUserImg(), imageListener);

            ImageLoader.ImageListener imageListener1=imageLoader.getImageListener(p_img,
                    R.drawable.loading_failed_big_icon,R.drawable.loading_failed_big_icon);

            imageLoader.get(Util.URL+"product/"+clickLike.getP_img(),imageListener1);

        }
    }
}
