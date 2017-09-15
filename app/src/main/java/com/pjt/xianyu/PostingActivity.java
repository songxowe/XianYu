package com.pjt.xianyu;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
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
import com.pjt.xianyu.pojo.Province;
import com.pjt.xianyu.pojo.YuTang;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PostingActivity extends AppCompatActivity {

    GridView gridView = null;
    ArrayList<Bitmap> bits = null;
    PostingImageAdapter adapter = null;
    PostChooseView postChooseView = null;
    PostReceiver postReceiver = null;
    GridView nomoney_grid = null;
    NomoneyAdapter nomoneyAdapter = null;
    RelativeLayout r_post = null;
    RelativeLayout r_paimai = null;
    RelativeLayout r_nomoney = null;
    String[] fenlei = new String[]{"全部分类",
            "手机", "相机/摄像机", "电脑及配件",
            "3C数码", "女装", "男装", "鞋包配饰", "化妆品", "奢侈名品", "家居用品", "家用电器", "母婴用品", "宠物", "门票及服务", "书刊音像",
            "交通工具", "珠宝首饰", "艺术品"};


    RelativeLayout btn_price = null;
    RelativeLayout btn_fenlei = null;
    RelativeLayout btn_yutang = null;
    Button btn_post = null;
    ArrayList<File> filelist = null;
    TextView text_price = null;
    EditText edit_price = null;
    TextView text_fenlei = null;
    double Myprice = 0;

    String[] yutangs = null;

    StringRequest yutangRequest = null;
    RequestQueue requestQueue = null;
    Gson gson = null;
    long user_id = 0;
    SharedPreferences sp = null;
    TextView text_yutang = null;

    EditText edit_title = null;
    EditText edit_info = null;

    StringRequest stringRequest = null;


    ArrayList<Boolean> isUploads = null;

    ArrayList<YuTang> yutangArray = null;
    long yutang_id = 0;

    long cate_id = 0;
    TextView text_location=null;
    ArrayList<Province> provinceList = null;

    ProvinceDao provinceDao=null;
    int state=1;

    String priceinfo=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posting);
        isUploads = new ArrayList<>();
        sp = Util.getSp(getApplicationContext());
        user_id = sp.getLong("user_id", 0);
        requestQueue = Volley.newRequestQueue(PostingActivity.this);
        gson = new Gson();
        filelist = new ArrayList<>();
        initView();
        findYutang();
        adapter = new PostingImageAdapter(PostingActivity.this, bits);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == bits.size()) {
                    Intent intent1 = new Intent();
                    intent1.setAction(Intent.ACTION_PICK);
                    Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    String type = "image/*";
                    intent1.setDataAndType(uri, type);
                    startActivityForResult(intent1, 1);
                } else {
                    Intent intent = new Intent();
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setAction(Intent.ACTION_VIEW);
                    String type = "image/*";
                    intent.setDataAndType(Uri.fromFile(filelist.get(position)), type);
                    startActivity(intent);
                }
            }
        });
        ButtonListener buttonListener = new ButtonListener();
        btn_yutang.setOnClickListener(buttonListener);
        btn_fenlei.setOnClickListener(buttonListener);
        btn_price.setOnClickListener(buttonListener);
        btn_post.setOnClickListener(buttonListener);
        text_location.setOnClickListener(buttonListener);


    }

    private void findYutang() {
        yutangRequest = new StringRequest(Util.URL + "YutangServlet?action=findMyYutang&type=android" +
                "&user_id=" + user_id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (s.trim().length() > 0) {
                            yutangArray = gson.fromJson(s, new TypeToken<ArrayList<YuTang>>() {
                            }.getType());
                            text_yutang.setText(yutangArray.get(0).getName());
                            yutang_id=yutangArray.get(0).getYutang_id();
                            yutangs = new String[yutangArray.size()];
                            for (int i = 0; i < yutangArray.size(); i++) {
                                yutangs[i] = yutangArray.get(i).getName();
                            }

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        requestQueue.add(yutangRequest);

    }

    private void initView() {
        text_location= (TextView) findViewById(R.id.text_location);
        edit_title = (EditText) findViewById(R.id.postting_title);
        edit_info = (EditText) findViewById(R.id.postting_info);
        text_yutang = (TextView) findViewById(R.id.text_yutang);
        text_fenlei = (TextView) findViewById(R.id.text_fenlei);
        text_price = (TextView) findViewById(R.id.text_price);
        btn_post = (Button) findViewById(R.id.postting_btn_post);
        btn_price = (RelativeLayout) findViewById(R.id.postting_r_price);
        btn_fenlei = (RelativeLayout) findViewById(R.id.postting_r_fenlei);
        btn_yutang = (RelativeLayout) findViewById(R.id.postting_r_yutang);
        gridView = (GridView) findViewById(R.id.posting_gridview);
        r_post = (RelativeLayout) findViewById(R.id.r_post_money);
        r_paimai = (RelativeLayout) findViewById(R.id.r_paimai);
        r_nomoney = (RelativeLayout) findViewById(R.id.r_nomoney);

        bits = new ArrayList<>();
        Intent data = getIntent();
        if (data != null) {
            byte[] bytes = data.getExtras().getByteArray("bit");
            if (bytes != null) {
                bits.add(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
            }
            String abspath = data.getExtras().getString("absfile");
            File file = new File(abspath);
            filelist.add(file);
            isUploads.add(false);


        }

        postChooseView = (PostChooseView) findViewById(R.id.postView);
        postReceiver = new PostReceiver();
        IntentFilter intentFilter = new IntentFilter("com.pjt.post.state");
        registerReceiver(postReceiver, intentFilter);
        nomoney_grid = (GridView) findViewById(R.id.post_nomoney_grid);
        initGrid();


    }


    private void initGrid() {
        final String[] sts = new String[]{"求购", "求点赞", "只求一顿饭", "本宫周末有空求约", "免费送", "你开价"};
        nomoneyAdapter = new NomoneyAdapter(getApplicationContext(), sts);
        int size = sts.length;
        int length = 120;
        DisplayMetrics dm = new DisplayMetrics();

        getWindowManager().getDefaultDisplay().getMetrics(dm);

        float density = dm.density;

        int gridviewWidth = (int) (size * (length + 4) * density);

        int itemWidth = (int) (length * density);

        nomoney_grid.setColumnWidth(itemWidth); // 设置列表项宽

        nomoney_grid.setHorizontalSpacing(15); // 设置列表项水平间距

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                gridviewWidth, LinearLayout.LayoutParams.FILL_PARENT);
        nomoney_grid.setNumColumns(size);
        nomoney_grid.setLayoutParams(params);
        nomoney_grid.setAdapter(nomoneyAdapter);
        nomoney_grid.requestDisallowInterceptTouchEvent(true);

        nomoney_grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                priceinfo=sts[position];
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    String abspath = getAbsoluteImagePath(uri);
                    File file = new File(abspath);
                    filelist.add(file);
                    isUploads.add(false);
                    bits.add(ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(abspath),
                            120, 120));
                    adapter.notifyDataSetChanged();


                }
                break;
        }
    }


    protected String getAbsoluteImagePath(Uri uri) {
        // can post image
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri,
                proj,                 // Which columns to return
                null,       // WHERE clause; which rows to return (all rows)
                null,       // WHERE clause selection arguments (none)
                null);                 // Order-by clause (ascending by name)

        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();

        return cursor.getString(column_index);

    }

    class PostReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
           state = intent.getIntExtra("state", 1);

            switch (state) {
                case 1:
                    r_post.setVisibility(View.VISIBLE);
                    r_paimai.setVisibility(View.GONE);
                    r_nomoney.setVisibility(View.GONE);
                    break;

                case 2:
                    r_post.setVisibility(View.GONE);
                    r_paimai.setVisibility(View.VISIBLE);
                    r_nomoney.setVisibility(View.GONE);
                    break;

                case 3:
                    r_post.setVisibility(View.GONE);
                    r_paimai.setVisibility(View.GONE);
                    r_nomoney.setVisibility(View.VISIBLE);

                    break;
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(postReceiver);
        postReceiver = null;
    }


    private class ButtonListener implements View.OnClickListener {


        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.postting_r_price:
                    Log.d("消息", "饿13");
                    View view = LayoutInflater.from(PostingActivity.this)
                            .inflate(R.layout.price_mode, null);
                    edit_price = (EditText) view.findViewById(R.id.edit_price);
                    new AlertDialog.Builder(PostingActivity.this)
                            .setTitle("请输入价格").setView(view).setIcon(R.drawable.flaunt_share_rmb_logo)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (edit_price.getText().toString().trim().length() > 0) {
                                        Myprice = Double.parseDouble(edit_price.getText().toString());
                                        text_price.setText("￥" + edit_price.getText().toString());
                                    }
                                }
                            }).create().show();

                    break;


                case R.id.postting_r_fenlei:

                    new AlertDialog.Builder(PostingActivity.this)
                            .setTitle("类目").setIcon(R.drawable.category)
                            .setItems(fenlei, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    text_fenlei.setText(fenlei[which]);
                                    cate_id = which;
                                }
                            }).create().show();


                    break;

                case R.id.postting_btn_post:
                    if (edit_title.getText().toString().trim().length() > 0) {
                        if (filelist.size() > 0) {
                            stringRequest = new StringRequest(Request.Method.POST,
                                    Util.URL + "ProductServlet?action=addProduct&type=android",
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String s) {
                                            if (s.trim().equals("ok")) {
                                                Intent intent = new Intent(PostingActivity.this, MainActivity.class);
                                                startActivity(intent);
                                                finish();
                                            }

                                        }
                                    }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError volleyError) {

                                }
                            }) {
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    HashMap<String, String> map = new HashMap<>();
                                    String st = "";
                                    for (File file : filelist) {
                                        st += file.getName();
                                        st += "*";
                                    }

                                    map.put("imgurl", st);
                                    map.put("title", edit_title.getText().toString());
                                    map.put("info", edit_info.getText().toString());
                                    map.put("user_id", user_id + "");
                                    map.put("price", Myprice+"");
                                    if(state==3){
                                        map.put("priceinfo", priceinfo);
                                    }else {
                                        map.put("priceinfo", "");
                                    }
                                    map.put("yutang_id", yutang_id + "");
                                    map.put("cate_id", cate_id + "");


                                    return map;
                                }
                            };
                            requestQueue.add(stringRequest);
                        } else {
                            Toast.makeText(PostingActivity.this, "至少添加一张图片", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(PostingActivity.this, "请给商品添加个标题", Toast.LENGTH_SHORT).show();
                    }


                    break;

                case R.id.postting_r_yutang:
                    new AlertDialog.Builder(PostingActivity.this)
                            .setTitle("选择一个鱼塘").setIcon(R.drawable.comui_tab_pond_selected)
                            .setItems(yutangs, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    text_yutang.setText(yutangs[which]);
                                    yutang_id = yutangArray.get(which).getYutang_id();
                                }
                            }).create().show();


                    break;


                case R.id.text_location:
                    provinceDao = new ProvinceDao(getApplicationContext());
                    provinceList = provinceDao.findAll();
                    final String[] provinces;
                    provinces = new String[provinceList.size()];
                    for (int i = 0; i < provinceList.size(); i++) {
                        provinces[i] = provinceList.get(i).getName();
                    }

                    new AlertDialog.Builder(PostingActivity.this).setIcon(R.drawable.icon_city)
                            .setTitle("请选择发布地址")
                            .setItems(provinces, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    text_location.setText(provinces[which]);

                                }
                            }).create().show();


                    break;
            }
        }
    }
}
