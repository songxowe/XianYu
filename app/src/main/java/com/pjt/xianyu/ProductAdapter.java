package com.pjt.xianyu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.pjt.xianyu.pojo.Product;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/05/25.
 */
public class ProductAdapter extends BaseAdapter {
     ArrayList<Product> list = null;
    Context context = null;
    LayoutInflater layoutInflater = null;
    RequestQueue requestQueue = null;
    ImageLoader imageLoader = null;


    public ProductAdapter(Context context, ArrayList<Product> productArrayList) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.list = productArrayList;
        requestQueue = Volley.newRequestQueue(context);
        imageLoader=new ImageLoader(requestQueue, new ImageLoader.ImageCache() {
            private LruCache<String, Bitmap> lruCache = new LruCache<>(1024 * 1024 * 10);

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
            convertView = layoutInflater.inflate(R.layout.main_mode, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.binder(list.get(position));


        return convertView;
    }

    private class ViewHolder {

        GridView gridView = null;
        TextView username = null;
        CircleImageView user_img = null;
        TextView posttime = null;
        TextView p_price = null;
        TextView p_info = null;
        TextView p_location = null;
        TextView comment_count = null;
        TextView good_count = null;



        public ViewHolder(View convertView) {
            gridView = (GridView) convertView.findViewById(R.id.main_gridView);
            username = (TextView) convertView.findViewById(R.id.main_user_name);
            user_img = (CircleImageView) convertView.findViewById(R.id.main_user_img);
            posttime = (TextView) convertView.findViewById(R.id.main_user_time);
            p_price = (TextView) convertView.findViewById(R.id.main_user_price);
            p_info = (TextView) convertView.findViewById(R.id.main_text_info);
            p_location = (TextView) convertView.findViewById(R.id.main_text_location);
            comment_count = (TextView) convertView.findViewById(R.id.main_comment);
            good_count = (TextView) convertView.findViewById(R.id.main_good);


        }

        public void binder(final Product product) {
            username.setText(product.getUsername());
            if(product.getUserimg()!=null) {
                ImageLoader.ImageListener listener = imageLoader.getImageListener(user_img, R.drawable.loading_failed_big_icon,
                        R.drawable.loading_failed_big_icon);
                imageLoader.get(Util.URL + "user/" + product.getUserimg(), listener);
            }

            posttime.setText(product.getTime());


            p_price.setText(product.getPriceinfo() != null ? product.getPriceinfo() : "￥" + product.getPrice());
            p_info.setText(product.getTitle() + "," + product.getInfo());
            p_location.setText(product.getAddress() + "  鱼塘|" + product.getYutang_name());
            comment_count.setText(product.getComment()+"");
            good_count.setText(product.getGood()+"");

            if(product.getBitsurl()!=null) {
                ArrayList<String> imglist = product.getBitsurl();

                int size = imglist.size();
                int length = 100;
                DisplayMetrics dm = new DisplayMetrics();

                ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);

                float density = dm.density;

                int gridviewWidth = (int) (size * (length + 4) * density);

                int itemWidth = (int) (length * density);

                gridView.setColumnWidth(itemWidth); // 设置列表项宽

                gridView.setHorizontalSpacing(10); // 设置列表项水平间距

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        gridviewWidth, LinearLayout.LayoutParams.FILL_PARENT);

                gridView.setLayoutParams(params);

                ImageAdapter imageAdapter = new ImageAdapter(context, imglist);
                gridView.setNumColumns(imglist.size());
                gridView.setAdapter(imageAdapter);
                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(context, Product_InfoActivity.class);
                        intent.putExtra("p_id",product.getId());
                        context.startActivity(intent);
                    }
                });

            }


        }

        private class ImageAdapter extends BaseAdapter {
            private ArrayList<String> imglist = null;
            private Context context = null;


            public ImageAdapter(Context context, ArrayList<String> list) {
                this.context = context;
                this.imglist = list;
            }


            @Override
            public int getCount() {
                return imglist.size();
            }

            @Override
            public Object getItem(int position) {
                return imglist.get(position);
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                ViewHolderChild viewHoderchild = null;
                if (convertView == null) {
                    convertView = layoutInflater.inflate(R.layout.image_mode, parent, false);
                    viewHoderchild = new ViewHolderChild(convertView);
                    convertView.setTag(viewHoderchild);
                } else {
                    viewHoderchild = (ViewHolderChild) convertView.getTag();
                }

                viewHoderchild.binder(imglist.get(position));


                return convertView;
            }
        }

        private class ViewHolderChild {

            ImageView imageView = null;


            public ViewHolderChild(View convertView) {
                imageView = (ImageView) convertView.findViewById(R.id.image_mode_item);

            }


            public void binder(String string) {
                if(string!=null) {
                    ImageLoader.ImageListener listener = imageLoader.getImageListener(imageView,
                            R.drawable.loading_failed_big_icon, R.drawable.loading_failed_big_icon);
                    imageLoader.get(Util.URL + "product/" + string, listener);
                }

            }
        }
    }
}
