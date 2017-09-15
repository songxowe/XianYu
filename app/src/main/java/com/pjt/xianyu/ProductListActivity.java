package com.pjt.xianyu;

import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pjt.xianyu.data.CountyDao;
import com.pjt.xianyu.data.ProvinceDao;
import com.pjt.xianyu.pojo.County;
import com.pjt.xianyu.pojo.Product;
import com.pjt.xianyu.pojo.Province;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.util.ArrayList;

public class ProductListActivity extends AppCompatActivity {

    GridView gridView = null;
    ProductListAdapter adapter = null;
    ArrayList<Product> list = null;
    RelativeLayout btn_cate = null;

    TextView text_shaixuan = null;
    ListView cate2_listView = null;
    boolean isshow = false;
    RelativeLayout cate2_r = null;
    ImageView cate2_img = null;
    RelativeLayout cate2_rr = null;
    RelativeLayout btn_scope = null;
    RelativeLayout btn_order = null;

    ListView cate3_listview = null;
    View showView = null;
    ImageView jiantou = null;
    LinearLayout cate1_r = null;
    ImageView cate1_img = null;
    ImageView cate3_img = null;

    ListView city_listView = null;
    ListView qu_listView = null;
    int state = 2;
    ArrayList<Province> provincesList = null;
    ArrayList<County> countyList = null;
    Province province = null;
    County county = null;
    ProvinceAdapter provinceAdapter=null;
    CountyAdapter countyAdapter=null;

    TextView text_quyu=null;
    TextView text_fenlei=null;
    TextView text_paixu=null;

    StringAdapter stringAdapter=null;
    StringAdapter stringAdapter2=null;

    String[]fenlei=null;
    String[]paixu=null;

    RequestQueue requestQueue=null;
    StringRequest productRequest=null;

   Gson gson=null;

    long cate_id=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        initView();
        requestQueue= Volley.newRequestQueue(getApplicationContext());
        gson=new Gson();
        Intent data=getIntent();
        if(data!=null){
            cate_id=data.getExtras().getLong("cate_id",1);
        }
        ButtonListener buttonListener = new ButtonListener();
        btn_cate.setOnClickListener(buttonListener);
        btn_order.setOnClickListener(buttonListener);
        btn_scope.setOnClickListener(buttonListener);
        cate2_r.setOnClickListener(buttonListener);

        final CountyDao countyDao = new CountyDao(ProductListActivity.this);
        ProvinceDao provinceDao = new ProvinceDao(ProductListActivity.this);


        provincesList = provinceDao.findAll();
        countyList = countyDao.findByNo("01");

        provinceAdapter=new ProvinceAdapter(ProductListActivity.this, provincesList);
        city_listView.setAdapter(provinceAdapter);
        city_listView.setItemChecked(0, true);


        countyAdapter=new CountyAdapter(ProductListActivity.this, countyList);
        qu_listView.setAdapter(countyAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ProductListActivity.this, Product_InfoActivity.class);
                intent.putExtra("p_id",list.get(position).getId());
                startActivity(intent);
            }
        });

        city_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                countyList=countyDao.findByNo(provincesList.get(position).getNo());
                CountyAdapter.list=countyList;
                countyAdapter.notifyDataSetChanged();
                qu_listView.setSelection(0);
            }
        });

        qu_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                text_quyu.setText(countyList.get(position).getName());
                closeOne();
                state=0;
                isshow=false;
                
            }
        });

        cate2_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                text_fenlei.setText(fenlei[position]);
                cate_id=position;
                findProduct();
                closeTwo();
                state=0;
                isshow=false;
            }
        });

        cate3_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                text_paixu.setText(paixu[position]);
                closeThree();
                state=0;
                isshow=false;
            }
        });



        findProduct();

    }

    private void findProduct() {
        productRequest=new StringRequest(Util.URL + "ProductServlet?action=findByCate&type=android&" +
                "cate_id="+cate_id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if(s.trim().length()>0){
                         list=gson.fromJson(s,new TypeToken<ArrayList<Product>>(){}.getType());
                            adapter = new ProductListAdapter(ProductListActivity.this, list);
                            gridView.setAdapter(adapter);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
       requestQueue.add(productRequest);
    }

    private void initView() {

        list = new ArrayList<>();
        text_quyu= (TextView) findViewById(R.id.text_quyu);
        text_fenlei= (TextView) findViewById(R.id.text_fenlei);
        text_paixu= (TextView) findViewById(R.id.text_paixu);

        cate3_listview = (ListView) findViewById(R.id.cate3_listview);
        cate1_r = (LinearLayout) findViewById(R.id.cate1_r);
        btn_scope = (RelativeLayout) findViewById(R.id.btn_scope);
        btn_order = (RelativeLayout) findViewById(R.id.btn_order);
        cate2_r = (RelativeLayout) findViewById(R.id.cate2_r);
        cate2_rr = (RelativeLayout) findViewById(R.id.cate2_rr);
        cate2_img = (ImageView) findViewById(R.id.cate2_img);
        cate1_img = (ImageView) findViewById(R.id.cate1_img);
        cate3_img = (ImageView) findViewById(R.id.cate3_img);
        city_listView = (ListView) findViewById(R.id.cate_city_listview);
        qu_listView = (ListView) findViewById(R.id.cate_qu_listview);
        gridView = (GridView) findViewById(R.id.product_list_gridview);
        btn_cate = (RelativeLayout) findViewById(R.id.btn_cate);
        text_shaixuan = (TextView) findViewById(R.id.text_shaixuan);
        cate2_listView = (ListView) findViewById(R.id.cate2_listview);
        fenlei=new String[]{"全部分类",
                "手机", "相机/摄像机", "电脑及配件",
                "3C数码", "女装", "男装", "鞋包配饰", "化妆品", "奢侈名品", "家居用品","家用电器","母婴用品","宠物","门票及服务","书刊音像",
                "交通工具","珠宝首饰","艺术品"};
        stringAdapter=new StringAdapter(getApplicationContext(),fenlei);

        cate2_listView.setAdapter(stringAdapter);
        paixu=new String[]{"默认排序",
                "最新发布", "离我最近", "价格最低", "价格最高"};
        stringAdapter2=new StringAdapter(getApplicationContext(),paixu);

        cate3_listview.setAdapter(stringAdapter2);

        Intent intent1=getIntent();
        int position=intent1.getIntExtra("position",0)+1;
        text_fenlei.setText(fenlei[position]);
    }

    private class ButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {

                case R.id.btn_scope:

                    if (!isshow) {
                        openOne();
                        cate3_listview.setVisibility(View.GONE);
                        cate2_listView.setVisibility(View.GONE);
                        cate2_listView.clearAnimation();
                        cate3_listview.clearAnimation();
                        cate2_img.setImageResource(R.drawable.more_topic);
                        cate3_img.setImageResource(R.drawable.more_topic);
                        isshow = true;
                        state = 1;

                        Log.d("消息", "打开一号");

                    } else {

                        if (state == 1) {
                            closeOne();
                            state = 0;
                            Log.d("消息", "关闭一号");
                            isshow = false;
                        } else if (state == 2) {
                            Log.d("消息", "打开一号");
                            cate2_listView.setVisibility(View.GONE);
                            cate2_listView.clearAnimation();
                            cate2_img.setImageResource(R.drawable.more_topic);
                            openOne();
                            isshow = true;
                            state = 1;

                        } else if (state == 3) {
                            cate3_listview.setVisibility(View.GONE);
                            cate3_listview.clearAnimation();
                            cate3_img.setImageResource(R.drawable.more_topic);
                            openOne();
                            isshow = true;
                            state = 1;
                        }


                    }


                    break;

                case R.id.btn_cate:

                    if (!isshow) {
                        cate1_r.setVisibility(View.GONE);
                        cate3_listview.setVisibility(View.GONE);
                        cate1_r.clearAnimation();
                        cate3_listview.clearAnimation();
                        openTwo();
                        cate3_img.setImageResource(R.drawable.more_topic);
                        cate1_img.setImageResource(R.drawable.more_topic);
                        isshow = true;
                        state = 2;

                    } else {
                        if (state == 2) {
                            closeTwo();
                            isshow = false;
                            state = 0;
                            Log.d("消息", "关闭二号");
                        } else if (state == 1) {
                            Log.d("消息", "打开二号");
                            openTwo();
                            cate1_r.setVisibility(View.GONE);
                            cate1_r.clearAnimation();
                            cate1_img.setImageResource(R.drawable.more_topic);
                            isshow = true;
                            state = 2;

                        } else if (state == 3) {
                            openTwo();
                            cate3_listview.setVisibility(View.GONE);
                            cate3_listview.clearAnimation();
                            cate3_img.setImageResource(R.drawable.more_topic);
                            isshow = true;
                            state = 2;

                        }

                    }

                    break;


                case R.id.btn_order:
                    if (!isshow) {

                        cate1_r.setVisibility(View.GONE);
                        cate2_listView.setVisibility(View.GONE);
                        cate1_r.clearAnimation();
                        cate2_listView.clearAnimation();

                        openThree();
                        cate2_img.setImageResource(R.drawable.more_topic);
                        cate1_img.setImageResource(R.drawable.more_topic);
                        isshow = true;
                        state = 3;

                    } else {
                        if (state == 3) {
                            closeThree();
                            isshow = false;
                            state = 0;

                        } else if (state == 1) {

                            openThree();
                            cate1_r.setVisibility(View.GONE);
                            cate1_r.clearAnimation();
                            cate1_img.setImageResource(R.drawable.more_topic);
                            isshow = true;
                            state = 3;

                        } else if (state == 2) {
                            openThree();
                            cate2_listView.setVisibility(View.GONE);
                            cate2_listView.clearAnimation();
                            cate2_img.setImageResource(R.drawable.more_topic);
                            isshow = true;
                            state = 3;
                        }

                    }

                    break;


                case R.id.cate2_r:

                    closeMore();

                    break;
            }
        }
    }


    public void openTwo() {
        cate2_rr.setVisibility(View.VISIBLE);
        cate2_listView.setVisibility(View.VISIBLE);
        TranslateAnimation translateAnimation = (TranslateAnimation) AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.translate_cate_fenlei);
        translateAnimation.setFillAfter(true);
        cate2_listView.startAnimation(translateAnimation);
        cate2_img.setImageResource(R.drawable.search_arrow_up);

        showView = cate2_listView;
        jiantou = cate2_img;
    }

    public void closeTwo() {
        TranslateAnimation translateAnimation = (TranslateAnimation) AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.translate_cate_fenlei_in);

        cate2_listView.startAnimation(translateAnimation);
        AlphaAnimation alphaAnimation = (AlphaAnimation) AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.alpha_cate_r);

        cate2_r.startAnimation(alphaAnimation);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                cate2_rr.setVisibility(View.GONE);
            }
        }, 500);
        cate2_img.setImageResource(R.drawable.more_topic);


    }

    public void openOne() {

        cate2_rr.setVisibility(View.VISIBLE);
        cate1_r.setVisibility(View.VISIBLE);
        TranslateAnimation translateAnimation = (TranslateAnimation) AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.translate_cate_fenlei);
        translateAnimation.setFillAfter(true);
        cate1_r.startAnimation(translateAnimation);


        cate1_img.setImageResource(R.drawable.search_arrow_up);

        showView = cate1_r;
        jiantou = cate1_img;

    }

    public void closeOne() {
        TranslateAnimation translateAnimation = (TranslateAnimation) AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.translate_cate_fenlei_in);

        cate1_r.startAnimation(translateAnimation);
        AlphaAnimation alphaAnimation = (AlphaAnimation) AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.alpha_cate_r);

        cate2_r.startAnimation(alphaAnimation);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                cate2_rr.setVisibility(View.GONE);
            }
        }, 500);
        cate1_img.setImageResource(R.drawable.more_topic);
    }

    public void openThree() {

        cate2_rr.setVisibility(View.VISIBLE);
        cate3_listview.setVisibility(View.VISIBLE);
        TranslateAnimation translateAnimation = (TranslateAnimation) AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.translate_cate_fenlei);
        translateAnimation.setFillAfter(true);
        cate3_listview.startAnimation(translateAnimation);
        cate3_img.setImageResource(R.drawable.search_arrow_up);

        showView = cate3_listview;
        jiantou = cate2_img;

    }

    public void closeThree() {
        TranslateAnimation translateAnimation = (TranslateAnimation) AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.translate_cate_fenlei_in);

        cate3_listview.startAnimation(translateAnimation);
        AlphaAnimation alphaAnimation = (AlphaAnimation) AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.alpha_cate_r);

        cate2_r.startAnimation(alphaAnimation);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                cate2_rr.setVisibility(View.GONE);
            }
        }, 500);
        cate3_img.setImageResource(R.drawable.more_topic);

    }


    public void closeMore() {
        if (isshow) {
            TranslateAnimation translateAnimation = (TranslateAnimation) AnimationUtils.loadAnimation(getApplicationContext(),
                    R.anim.translate_cate_fenlei_in);
            translateAnimation.setFillAfter(true);
            showView.startAnimation(translateAnimation);
            AlphaAnimation alphaAnimation = (AlphaAnimation) AnimationUtils.loadAnimation(getApplicationContext(),
                    R.anim.alpha_cate_r);

            cate2_r.startAnimation(alphaAnimation);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    cate2_rr.setVisibility(View.GONE);
                }
            }, 500);
            jiantou.setImageResource(R.drawable.more_topic);
            isshow = false;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isshow) {
                closeMore();


                return false;
            }

        }

        return super.onKeyDown(keyCode, event);
    }
}
