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
import com.pjt.xianyu.pojo.Product;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/06/02.
 */
public class ProductInfoFooterAdapter extends BaseAdapter {
    Context context=null;
    ArrayList<Product>list=null;
    LayoutInflater layoutInflater=null;
    RequestQueue requestQueue=null;
    ImageLoader imageLoader=null;

    public ProductInfoFooterAdapter(Context applicationContext, ArrayList<Product> p_list) {
        this.context=applicationContext;
        this.list=p_list;
        this.layoutInflater=LayoutInflater.from(context);
        this.requestQueue= Volley.newRequestQueue(context);
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
    public Product getItem(int position) {
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
            convertView=layoutInflater.inflate(R.layout.product_info_footer_item,parent,false);
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
        TextView text_name=null;
        TextView text_price=null;
        TextView text_zan=null;
        TextView text_pinglun=null;
        public ViewHolder(View convertView) {
            imageView= (ImageView) convertView.findViewById(R.id.info_footer_img);
            text_name= (TextView) convertView.findViewById(R.id.info_footer_name);
            text_price= (TextView) convertView.findViewById(R.id.info_footer_price);
            text_zan= (TextView) convertView.findViewById(R.id.info_footer_zan);
            text_pinglun= (TextView) convertView.findViewById(R.id.info_footer_pinglun);
        }

        public void binder(Product product) {
            if(product.getBitsurl()!=null) {
                ImageLoader.ImageListener imageListener = imageLoader.getImageListener(imageView,
                        R.drawable.loading_failed_big_icon, R.drawable.loading_failed_big_icon);
                imageLoader.get(Util.URL+"product/"+product.getBitsurl().get(0),imageListener);
            }
            text_name.setText(product.getTitle()+"，"+product.getInfo());
            text_price.setText(product.getPriceinfo()!=null?product.getPriceinfo():"￥"+product.getPrice());
            text_pinglun.setText(product.getComment()+" 评论");
            text_zan.setText(product.getGood()+" 赞");
        }
    }
}
