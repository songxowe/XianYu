package com.pjt.xianyu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.pjt.xianyu.pojo.YuTang;
import com.pjt.xianyu.pojo.YuTangCate;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/05/27.
 */
public class YuTangCateAdapter extends BaseAdapter {
    private Context context=null;
    private ArrayList<YuTangCate>list=null;
    private LayoutInflater layoutInflater=null;
    private RequestQueue requestQueue=null;
    private ImageLoader imageLoader=null;

    public YuTangCateAdapter(Context context, ArrayList<YuTangCate> cate_list) {
        this.context=context;
        this.list=cate_list;
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
    public YuTangCate getItem(int position) {
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
            convertView=layoutInflater.inflate(R.layout.yutang_list_mode,parent,false);
            viewHolder=new ViewHolder(convertView);
            convertView.setTag(viewHolder);

        }else{
            viewHolder= (ViewHolder) convertView.getTag();
        }

        viewHolder.binder(list.get(position));


        return convertView;
    }

    private class ViewHolder {

        NoScrollGridView gridView=null;
        TextView text_title=null;
        ArrayList<YuTang>yt_list=null;


        public ViewHolder(View convertView) {
            gridView= (NoScrollGridView) convertView.findViewById(R.id.yutang_cate_grid);
            text_title= (TextView) convertView.findViewById(R.id.yt_cate_title);

        }

        public void binder(final YuTangCate yuTangCate) {
            text_title.setText(yuTangCate.getTitle());
            yt_list=yuTangCate.getList();
            YutangAdapter adapter=new YutangAdapter(context,yt_list);
            gridView.setAdapter(adapter);

            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent=new Intent(context,YuTangActivity.class);
                    intent.putExtra("yutang_id",yuTangCate.getList().get(position).getYutang_id());
                   context.startActivity(intent);

                }
            });

        }

        private class YutangAdapter extends BaseAdapter{

            private Context context=null;
            private ArrayList<YuTang>list=null;
            private LayoutInflater layoutInflater=null;

            public YutangAdapter(Context context, ArrayList<YuTang> list) {
                this.list=list;
                this.context=context;
                this.layoutInflater=LayoutInflater.from(context);

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

                ItemViewHolder  itemViewHolder=null;
                if(convertView==null){
                    convertView=layoutInflater.inflate(R.layout.yutang_item_mode,parent,false);
                    itemViewHolder=new ItemViewHolder(convertView);
                    convertView.setTag(itemViewHolder);
                }else{
                    itemViewHolder= (ItemViewHolder) convertView.getTag();
                }

                itemViewHolder.binder(list.get(position));

                return convertView;
            }

            private class ItemViewHolder {
                private TextView yt_name;
                private TextView yt_pop;
                private ImageView yt_img;

                public ItemViewHolder(View convertView) {
                    yt_name= (TextView) convertView.findViewById(R.id.cate_item_name);
                    yt_pop= (TextView) convertView.findViewById(R.id.cate_item_pop);
                    yt_img= (ImageView) convertView.findViewById(R.id.cate_item_img);


                }

                public void binder(YuTang yuTang) {
                    yt_name.setText(yuTang.getName());
                    yt_pop.setText("人气"+yuTang.getPopularity());
                    ImageLoader.ImageListener imageListener=imageLoader.getImageListener(yt_img,
                            R.drawable.loading_failed_big_icon,R.drawable.loading_failed_big_icon);
                    imageLoader.get(Util.URL+"yutang/"+yuTang.getImgurl(),imageListener);
                }
            }
        }
    }


}
