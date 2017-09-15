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
import com.pjt.xianyu.pojo.Comment;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/06/14.
 */
public class MyCommentAdapter extends BaseAdapter {
    private Context context=null;
    private ArrayList<Comment>list=null;
    private LayoutInflater layoutInflater=null;
    private RequestQueue requestQueue=null;
    private ImageLoader imageLoader=null;

    public MyCommentAdapter(Context context, ArrayList<Comment> list) {
        this.context=context;
        this.list=list;
        this.layoutInflater=LayoutInflater.from(context);
        this.requestQueue= Volley.newRequestQueue(context);
        this.imageLoader=new ImageLoader(requestQueue, new ImageLoader.ImageCache() {
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
    public Comment getItem(int position) {
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
            convertView=layoutInflater.inflate(R.layout.mycomment_item,parent,false);
            viewHolder=new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) convertView.getTag();
        }
        viewHolder.binder(list.get(position));



        return convertView;
    }

    private class ViewHolder {
        ImageView p_img=null;
        TextView username=null;
        TextView text_comment=null;
        TextView text_time=null;


        public ViewHolder(View convertView) {
            p_img= (ImageView) convertView.findViewById(R.id.p_img);
            username= (TextView) convertView.findViewById(R.id.user_name);
            text_comment= (TextView) convertView.findViewById(R.id.text_comment);
            text_time= (TextView) convertView.findViewById(R.id.comment_time);
        }

        public void binder(Comment comment) {
            ImageLoader.ImageListener imageListener=imageLoader.getImageListener(p_img,
                    R.drawable.loading_failed_big_icon,R.drawable.loading_failed_big_icon);
            username.setText(comment.getUsername());
            text_comment.setText(comment.getText()+" |");
            text_time.setText(comment.getTime());
            imageLoader.get(Util.URL+"product/"+comment.getP_img(),imageListener);

        }
    }
}
