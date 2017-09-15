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
 * Created by Administrator on 2016/05/29.
 */
public class CommentAdapter extends BaseAdapter {
    private ArrayList<Comment>list=null;
    private Context context=null;
    private LayoutInflater layoutInflater=null;
    private RequestQueue requestQueue=null;
    ImageLoader imageLoader=null;


    public CommentAdapter(Context context, ArrayList<Comment> list) {
        this.list=list;
        this.context=context;
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
            convertView=layoutInflater.inflate(R.layout.product_info_item,parent,false);
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
        TextView  user_name=null;
        TextView  text_comment=null;
        TextView text_time=null;


        public ViewHolder(View convertView) {
            user_img= (CircleImageView) convertView.findViewById(R.id.user_img);
            user_name= (TextView) convertView.findViewById(R.id.user_name);
            text_comment= (TextView) convertView.findViewById(R.id.text_comment);
            text_time= (TextView) convertView.findViewById(R.id.comment_time);
        }

        public void binder(Comment comment) {
            user_name.setText(comment.getUsername());

            text_time.setText(comment.getTime());
            if(comment.getUserimg()!=null){
                ImageLoader.ImageListener listener=imageLoader.getImageListener(user_img,
                        R.drawable.loading_failed_big_icon,R.drawable.loading_failed_big_icon);
                imageLoader.get(Util.URL+"user/"+comment.getUserimg(),listener);
            }
            if(comment.getTo_user_id()!=0){
                text_comment.setText("回复@"+comment.getTo_username()+": "+comment.getText());
            }else{
                text_comment.setText(comment.getText());
            }
        }
    }
}
