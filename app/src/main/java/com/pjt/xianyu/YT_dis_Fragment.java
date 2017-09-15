package com.pjt.xianyu;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.location.LocationManager;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.LoopPagerAdapter;
import com.jude.rollviewpager.hintview.ColorPointHintView;
import com.pjt.xianyu.pojo.Product;
import com.pjt.xianyu.pojo.YuTang;
import com.pjt.xianyu.pojo.YuTangCate;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class YT_dis_Fragment extends Fragment implements AMap.OnMapLoadedListener {

    private ReFlashListView listView = null;
    private Context context = null;
    LayoutInflater layoutInflater = null;
    RollPagerView rollPagerView = null;
    MapView mapView = null;
    View header = null;
    AMap aMap = null;
    LatLng p1 = new LatLng(28.1963829, 112.9789191);
    ProductAdapter productAdapter = null;
    ArrayList<YuTangCate> cate_list = null;
    YuTangCateAdapter cateAdapter = null;
    RelativeLayout btn_tuijian1 = null;
    RelativeLayout btn_tuijian2 = null;
    ArrayList<Product> list = null;
    StringRequest productRequest = null;
    Gson gson = null;

    ImageView mapview_bit = null;
    ImageView img_fujin1 = null;
    StringRequest mapRequest=null;

    ImageView img_tuijian1=null;
    ImageView img_tuijian2=null;
    TextView  name_tuijian1=null;
    TextView  name_tuijian2=null;
    TextView  postCount_tuijian1=null;
    TextView postCount_tuijian2=null;
    TextView pop_tuijian1=null;
    TextView pop_tuijian2=null;
    TextView juli_tuijian1=null;
    TextView juli_tuijian2=null;
    StringRequest yutangRequest=null;
    RequestQueue requestQueue=null;
    StringRequest allRequest=null;
    LocationManager locationmanager=null;
    ImageLoader imageLoader=null;

    YuTang y1=null;
    YuTang y2=null;


    public YT_dis_Fragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        requestQueue=Volley.newRequestQueue(context);
        layoutInflater = LayoutInflater.from(context);
        imageLoader=new ImageLoader(requestQueue, new ImageLoader.ImageCache() {
            private LruCache<String,Bitmap>lruCahche =new LruCache<>(1024*1024*3);
            @Override
            public Bitmap getBitmap(String s) {

                return lruCahche.get(s);
            }

            @Override
            public void putBitmap(String s, Bitmap bitmap) {

                lruCahche.put(s,bitmap);
            }
        });
        header = layoutInflater.inflate(R.layout.yutang_header, null);
        mapView = (MapView) header.findViewById(R.id.map_view);
        img_fujin1 = (ImageView) header.findViewById(R.id.img_fujin1);
        mapview_bit = (ImageView) header.findViewById(R.id.mapview_bit);
        img_tuijian1= (ImageView) header.findViewById(R.id.img_fujin1);
        img_tuijian2=(ImageView) header.findViewById(R.id.img_tuijian2);
        name_tuijian1= (TextView) header.findViewById(R.id.name_tuijian1);
       name_tuijian2= (TextView) header.findViewById(R.id.name_tuijian2);
        postCount_tuijian1= (TextView) header.findViewById(R.id.postcount_tuijian1);
        postCount_tuijian2= (TextView) header.findViewById(R.id.postcount_tuijian2);
        pop_tuijian1= (TextView) header.findViewById(R.id.pop_tuijian1);
        pop_tuijian2= (TextView) header.findViewById(R.id.pop_tuijian2);
        juli_tuijian1= (TextView) header.findViewById(R.id.juli_tuijian1);
        juli_tuijian2= (TextView) header.findViewById(R.id.juli_tuijian2);

        locationmanager= (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);


        mapView.setEnabled(false);
        btn_tuijian1 = (RelativeLayout) header.findViewById(R.id.btn_tuijian1);
        btn_tuijian2 = (RelativeLayout) header.findViewById(R.id.btn_tuijian2);
        ButtonListener buttonListener = new ButtonListener();
        btn_tuijian1.setOnClickListener(buttonListener);
        btn_tuijian2.setOnClickListener(buttonListener);
        mapView.onCreate(savedInstanceState);
        requestQueue = Volley.newRequestQueue(context);
        gson = new Gson();
        initMap();
        findProximityYutang();

    }

    private void findProximityYutang() {

       yutangRequest=new StringRequest(Util.URL+"YutangServlet?type=android&action=findProximity",
               new Response.Listener<String>() {
                   @Override
                   public void onResponse(String s) {
                       if(s.trim().length()>0){
                          double lat=28.1963829;
                           double lng=112.9789191;
                           ArrayList<YuTang>list=gson.fromJson(s, new TypeToken<ArrayList<YuTang>>() {
                           }.getType());
                           int count=0;
                           for(YuTang y:list){
                               double r=Math.sqrt(
                                       Math.pow((lat-Double.parseDouble(y.getLat())),2)
                                       + Math.pow((lng-Double.parseDouble(y.getLng())),2)
                               );

                               if(r<1){
                                   if(count==0){
                                       y1=y;
                                       ImageLoader.ImageListener imageListener=imageLoader.getImageListener(img_tuijian1,
                                               R.drawable.loading_failed_big_icon,R.drawable.loading_failed_big_icon );
                                       name_tuijian1.setText(y.getName());
                                       imageLoader.get(Util.URL + "yutang/" + y.getImgurl(), imageListener);
                                       juli_tuijian1.setText(Math.round(r * 1200)+ "m");
                                       pop_tuijian1.setText(String.valueOf(y.getPopularity()));
                                       postCount_tuijian1.setText(String.valueOf(y.getPost_num()));

                                   }else{
                                       y2=y;
                                       ImageLoader.ImageListener imageListener=imageLoader.getImageListener(img_tuijian2,
                                               R.drawable.loading_failed_big_icon,R.drawable.loading_failed_big_icon );
                                       name_tuijian2.setText(y.getName());
                                       imageLoader.get(Util.URL + "yutang/" + y.getImgurl(), imageListener);
                                       juli_tuijian2.setText(Math.round(r * 1200 )+"m");
                                       pop_tuijian2.setText(String.valueOf(y.getPopularity()));
                                       postCount_tuijian2.setText(String.valueOf(y.getPost_num()));

                                   }
                                   count++;
                               }
                           }
                       }

                   }
               }, new Response.ErrorListener() {
           @Override
           public void onErrorResponse(VolleyError volleyError) {

           }
       }
       );
        requestQueue.add(yutangRequest);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_yt_dis_, container, false);
        listView = (ReFlashListView) view.findViewById(R.id.yutang_dis_listview);
        View header1 = layoutInflater.inflate(R.layout.view_mode, null);
        rollPagerView = (RollPagerView) header.findViewById(R.id.yutang_rollviewpager);
        TestLoopAdapter contentAdapter = new TestLoopAdapter(rollPagerView);
        rollPagerView.setAdapter(contentAdapter);
        rollPagerView.setPlayDelay(3000);
        rollPagerView.setHintView(new ColorPointHintView(context, Color.RED, Color.GRAY));


        listView.addHeaderView(header, null, true);

        //鱼塘新鲜事的数据显示
        View footerView = layoutInflater.inflate(R.layout.yutang_footer, null);
        list = new ArrayList<>();
        productAdapter = new ProductAdapter(context, list);


        NoScrollListView footer_list = (NoScrollListView)
                footerView.findViewById(R.id.yutang_footer_listview);
        footer_list.setAdapter(productAdapter);
        listView.setInterface(new ReFlashListView.IReflashListener() {
            @Override
            public void onReflash() {
                findProduct();

            }
        });


        findProduct();
        listView.addFooterView(footerView);
        findAll();


        return view;
    }

    private void findAll() {
        allRequest=new StringRequest(Util.URL + "YutangServlet?type=android&action=findAll",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if(s.trim().length()>0){
                           cate_list=gson.fromJson(s,new TypeToken<ArrayList<YuTangCate>>(){}.getType());

                            cateAdapter = new YuTangCateAdapter(context, cate_list);
                            listView.setAdapter(cateAdapter);

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        requestQueue.add(allRequest);


    }

    private void findProduct() {
        productRequest = new StringRequest(Util.URL + "ProductServlet?type=android&action=findall&pageno=1",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (s.trim().length() > 0) {
                            list.clear();
                            ArrayList<Product> array = gson.fromJson(s, new TypeToken<ArrayList<Product>>() {
                            }.getType());
                            for (Product p : array) {
                                list.add(p);
                            }
                            listView.post(new Runnable() {
                                @Override
                                public void run() {
                                    productAdapter.notifyDataSetChanged();
                                    listView.setState(0);
                                }
                            });
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        requestQueue.add(productRequest);

    }


    private void initMap() {

        aMap = mapView.getMap();
        aMap.getUiSettings().setMyLocationButtonEnabled(false);//定位当前位置的按钮

//            aMap.addNavigateArrow(new NavigateArrowOptions().add(p1, p2, p3, p4)
//                    .width(20));

        aMap.getUiSettings().setScrollGesturesEnabled(false);
        aMap.getUiSettings().setAllGesturesEnabled(false);
        aMap.getUiSettings().setZoomControlsEnabled(false);
        aMap.setOnMapLoadedListener(this);

        Bitmap bmp = BitmapFactory.decodeResource(getResources(),
                R.drawable.tmlayer_pond_map_poi_icon);
        bmp = ThumbnailUtils.extractThumbnail(bmp, 60, 60);

        aMap.moveCamera(CameraUpdateFactory.changeLatLng(p1));//移动到起点坐标
        aMap.moveCamera(CameraUpdateFactory.zoomTo(12));//放大倍率，12倍
        aMap.addMarker(new MarkerOptions().position(p1).
                icon(BitmapDescriptorFactory.fromBitmap(bmp)).title("壹号公馆"));


    }


    private Bitmap getViewBitmap(View v) {
        v.clearFocus();
        v.setPressed(false);
        boolean willNotCache = v.willNotCacheDrawing();
        v.setWillNotCacheDrawing(false);
        int color = v.getDrawingCacheBackgroundColor();
        v.setDrawingCacheBackgroundColor(0);
        if (color != 0) {
            v.destroyDrawingCache();
        }
        v.buildDrawingCache();
        Bitmap cacheBitmap = null;
        while (cacheBitmap == null) {
            cacheBitmap = v.getDrawingCache();
        }
        Bitmap bitmap = Bitmap.createBitmap(cacheBitmap);

        return bitmap;
    }




    @Override
    public void onMapLoaded() {

    }


private class TestLoopAdapter extends LoopPagerAdapter {
    private int[] imgs = {
            R.drawable.yutang1,
            R.drawable.yutang2

    };

    public TestLoopAdapter(RollPagerView viewPager) {
        super(viewPager);
    }

    @Override
    public View getView(ViewGroup container, int position) {
        ImageView view = new ImageView(container.getContext());
        view.setImageResource(imgs[position]);
        view.setScaleType(ImageView.ScaleType.CENTER_CROP);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        return view;
    }

    @Override
    public int getRealCount() {
        return imgs.length;
    }

}

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();


    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);


    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

private class ButtonListener implements View.OnClickListener {
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_tuijian1:

                Intent intent = new Intent(context, YuTangActivity.class);
                intent.putExtra("yutang_id",y1.getYutang_id());

                startActivity(intent);
                break;

            case R.id.btn_tuijian2:

                Intent intent1 = new Intent(context, YuTangActivity.class);
                intent1.putExtra("yutang_id",y2.getYutang_id());
                startActivity(intent1);
                break;
        }
    }
}
}
