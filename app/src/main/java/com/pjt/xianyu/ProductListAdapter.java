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
 * Created by Administrator on 2016/06/01.
 */
public class ProductListAdapter extends BaseAdapter {
    ArrayList<Product> list = null;
    Context context = null;
    LayoutInflater layoutInflater = null;
    ImageLoader imageLoader = null;
    RequestQueue requestQueue = null;

    public ProductListAdapter(Context context, ArrayList<Product> list) {
        this.context = context;
        this.list = list;
        this.layoutInflater = LayoutInflater.from(context);
        requestQueue = Volley.newRequestQueue(context);
        imageLoader = new ImageLoader(requestQueue, new ImageLoader.ImageCache() {
            private LruCache<String, Bitmap> lruCache = new LruCache<>(1024 * 1024 * 6);

            @Override
            public Bitmap getBitmap(String s) {
                return lruCache.get(s);
            }

            @Override
            public void putBitmap(String s, Bitmap bitmap) {
                lruCache.put(s, bitmap);
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
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.product_list_item, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.binder(list.get(position));


        return convertView;
    }

    private class ViewHolder {

        ImageView imageView = null;
        TextView text_title = null;
        TextView text_price = null;
        TextView text_noprice = null;
        TextView text_address = null;
        TextView text_time = null;


        public ViewHolder(View convertView) {
            imageView = (ImageView) convertView.findViewById(R.id.product_img);
            text_title = (TextView) convertView.findViewById(R.id.product_title);
            text_price = (TextView) convertView.findViewById(R.id.p_price);
            text_noprice = (TextView) convertView.findViewById(R.id.p_noprice);
            text_address = (TextView) convertView.findViewById(R.id.p_address);
            text_time = (TextView) convertView.findViewById(R.id.p_time);
        }

        public void binder(Product product) {
            ImageLoader.ImageListener imageListener = imageLoader.getImageListener(imageView,
                    R.drawable.loading_failed_big_icon, R.drawable.loading_failed_big_icon);
            imageLoader.get(Util.URL + "product/" + product.getBitsurl().get(0), imageListener);
            text_title.setText(product.getTitle());
            if (product.getPriceinfo() != null) {
                text_price.setText(product.getPriceinfo());
            } else {
                text_price.setText("￥" + product.getPrice());
                text_noprice.setText("￥" + product.getPrice() * 1.5);
            }
            text_address.setText(product.getAddress());
            text_time.setText(product.getTime());


        }
    }
}
