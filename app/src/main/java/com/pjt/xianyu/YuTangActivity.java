package com.pjt.xianyu;

import android.animation.ArgbEvaluator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pjt.xianyu.pojo.Product;
import com.pjt.xianyu.pojo.YuTang;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class YuTangActivity extends AppCompatActivity {

    private NoScrollListView listView=null;
    private ScrollView scrollView=null;
    RelativeLayout r_top=null;
    TabLayout tab=null;
    TextView text_top=null;
    ProductAdapter adapter=null;
    TabLayout tab2=null;
    int position=0;
    ArrayList<Product> list=null;

    RelativeLayout r_jiaru=null;
    ImageView btn_post=null;
    StringRequest ytRequest=null;
    StringRequest ytinfoRequest=null;
    RequestQueue requestQueue=null;
    SharedPreferences sp=null;
    long yutang_id=0;
    long user_id=0;

    Gson gson=null;

    ImageView yutang_img=null;
    TextView yutang_name=null;
    TextView post_count=null;
    TextView pop_count=null;
    ImageLoader imageLoader=null;

    StringRequest productRequest=null;


    Button btn_addYuTang=null;

    StringRequest addRequest=null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yu_tang);
        sp=Util.getSp(getApplicationContext());

        r_jiaru= (RelativeLayout) findViewById(R.id.r_jiaru);
        btn_post= (ImageView) findViewById(R.id.btn_post);
        gson=new Gson();
        Intent data=getIntent();
        if(data!=null){
            yutang_id=data.getExtras().getLong("yutang_id",0);
        }
        user_id=sp.getLong("user_id", 0);
        requestQueue= Volley.newRequestQueue(getApplicationContext());
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

        listView= (NoScrollListView) findViewById(R.id.yutang_listview);
        scrollView= (ScrollView) findViewById(R.id.yutang_scrollview);
        r_top= (RelativeLayout) findViewById(R.id.r_top_yutang);
        tab= (TabLayout) findViewById(R.id.yutang_tab);
        tab2= (TabLayout) findViewById(R.id.yutang_tab2);
        initView();
        findisAdd();
        findYuTangInfo();
        ButtonListener buttonListener=new ButtonListener();
        btn_post.setOnClickListener(buttonListener);
        btn_addYuTang.setOnClickListener(buttonListener);

    }

    private void findYuTangInfo() {
        ytinfoRequest=new StringRequest(Util.URL + "YutangServlet?action=findInfo&type=android&yutang_id=" +
                yutang_id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if(s.trim().length()>0){
                            YuTang y=gson.fromJson(s,YuTang.class);
                            ImageLoader.ImageListener imageListener=imageLoader.getImageListener
                                    (yutang_img,R.drawable.loading_failed_big_icon,R.drawable.loading_failed_big_icon);
                            imageLoader.get(Util.URL+"yutang/"+y.getImgurl(),imageListener);

                            yutang_name.setText(y.getName());
                            pop_count.setText(y.getPopularity()+"");
                            post_count.setText(y.getPost_num()+"");

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }
        );
        requestQueue.add(ytinfoRequest);
    }

    private void findisAdd() {
        ytRequest=new StringRequest(Util.URL + "YutangServlet?action=isAddYuTang&type=android" +
                "&yutang_id="+yutang_id+"&user_id="+user_id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if(s.trim().equals("yes")){
                            btn_post.setVisibility(View.VISIBLE);
                            r_jiaru.setVisibility(View.GONE);
                        }else{
                            btn_post.setVisibility(View.GONE);
                            r_jiaru.setVisibility(View.VISIBLE);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        requestQueue.add(ytRequest);

    }

    private void initView() {

        yutang_img= (ImageView) findViewById(R.id.yutang_img);
        yutang_name= (TextView) findViewById(R.id.yutang_name);
        pop_count= (TextView) findViewById(R.id.pop_count);
        post_count= (TextView) findViewById(R.id.postcount);
        btn_addYuTang= (Button) findViewById(R.id.btn_jiaru);
        tab.setSelectedTabIndicatorColor(Color.argb(100, 255, 215, 68));
        tab2.setSelectedTabIndicatorColor(Color.argb(100, 255, 215, 68));



            tab.addTab(tab.newTab().setText("大杂烩"));
            tab.addTab(tab.newTab().setText("邻居帮帮忙"));
            tab.addTab(tab.newTab().setText("新鲜事"));


            tab2.addTab(tab2.newTab().setText("大杂烩"));
            tab2.addTab(tab2.newTab().setText("邻居帮帮忙"));
            tab2.addTab(tab2.newTab().setText("新鲜事"));



        tab.setTabMode(TabLayout.MODE_SCROLLABLE);
        tab.setTabTextColors(Color.BLACK, Color.BLACK);
        tab2.setTabMode(TabLayout.MODE_SCROLLABLE);
        tab2.setTabTextColors(Color.BLACK, Color.BLACK);

        text_top= (TextView) findViewById(R.id.text_top);
        text_top.setVisibility(View.GONE);

        tab.setTabGravity(TabLayout.GRAVITY_FILL);
        tab2.setTabGravity(TabLayout.GRAVITY_FILL);




        r_top.setBackgroundColor(Color.argb(0, 255, 255, 255));
        listView.setFocusable(false);
        scrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY < 255) {
                    r_top.setBackgroundColor(Color.argb(scrollY, 255, 255, 255));
                    text_top.setVisibility(View.GONE);
                }

                if (scrollY > 255) {
                    r_top.setBackgroundColor(Color.argb(255, 255, 255, 255));
                    text_top.setVisibility(View.VISIBLE);
                }
                if (scrollY > 750) {
                    tab2.setVisibility(View.VISIBLE);
                    tab2.setScrollPosition(tab.getSelectedTabPosition(), View.SCROLLBAR_POSITION_DEFAULT, true);
                } else {

                    tab2.setVisibility(View.GONE);

                }

            }
        });
        tab2.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab1) {
                position = tab2.getSelectedTabPosition();
                tab.setScrollPosition(position, View.SCROLLBAR_POSITION_DEFAULT, true);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        list=new ArrayList<>();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(YuTangActivity.this, Product_InfoActivity.class);
                intent.putExtra("p_id",list.get(position).getId());
                startActivity(intent);

            }
        });

        findProduct();

    }

    private void findProduct() {
        productRequest=new StringRequest(Util.URL + "ProductServlet?action=findByYt&type=android&yutang_id=" +
                yutang_id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if(s.trim().length()>0){
                            list=gson.fromJson(s,new TypeToken<ArrayList<Product>>(){}.getType());
                            adapter=new ProductAdapter(YuTangActivity.this,list);
                            listView.setAdapter(adapter);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }
        );
        requestQueue.add(productRequest);

    }


    @Override
    protected void onResume() {
        super.onResume();
    }



    private class ButtonListener  implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_post:
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_PICK);
                    //设置SD卡中的图片路径
                    Uri data= MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    String type = "image/*";
                    //设置意图和类型（从指定路径下提取一张图片）
                    intent.setDataAndType(data,type);
                    //启动意图并返回结果
                    //参数（意图，请求代码）
                    //请求代码：本App唯一，常量数字
                    startActivityForResult(intent, 2);
                    break;


                case R.id.btn_jiaru:
                    addRequest=new StringRequest(Util.URL
                            +"YutangServlet?type=android&action=addYuTang&yutang_id="+yutang_id+
                            "&user_id="+user_id,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String s) {
                                     if(s.trim().equals("yes")){
                                         r_jiaru.setVisibility(View.GONE);
                                         btn_post.setVisibility(View.VISIBLE);
                                     }
                                }
                            }
                            , new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {

                        }
                    }
                    );

                 requestQueue.add(addRequest);

                    break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==2){
            if(resultCode==RESULT_OK){

                Intent intent1=new Intent(YuTangActivity.this,PostingActivity.class);
                Uri uri=data.getData();
                String abspath=getAbsoluteImagePath(uri);
                Bitmap bitmap= ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(abspath)
                        , 120, 120);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();

                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                intent1.putExtra("bit", baos.toByteArray());
                intent1.putExtra("absfile",abspath);
                startActivity(intent1);
            }
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
}
