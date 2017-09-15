package com.pjt.xianyu;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.hardware.input.InputManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pjt.xianyu.pojo.Comment;
import com.pjt.xianyu.pojo.Product;
import com.pjt.xianyu.pojo.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class Product_InfoActivity extends AppCompatActivity {

    RelativeLayout btn_comment = null;
    RelativeLayout btn_good = null;
    Button btn_want = null;
    ImageView btn_jianpan = null;
    Button btn_postMsg = null;
    EditText comment_edit = null;

    RelativeLayout r_liuyan = null;
    RelativeLayout r_post_comment = null;
    InputMethodManager inputMethodManager = null;
    CommentAdapter commentAdapter = null;
    ListView listview = null;
    LayoutInflater layoutInflater = null;
    ArrayList<Comment> list = null;
    NoScrollListView headerListView = null;
    ArrayList<String> imglist = null;
    CommentImgAdapter imgAdapter = null;
    NoScrollGridView footer_gridView = null;
    ProductInfoFooterAdapter infoFooterAdapter = null;
    StringRequest stringRequest = null;
    RequestQueue requestQueue = null;
    Product p = null;
    Gson gson = null;

    TextView text_username = null;
    TextView text_time = null;
    ImageView user_img = null;
    TextView p_price = null;
    TextView p_info = null;
    TextView p_location = null;
    TextView p_goodcount = null;
    TextView P_lookcount = null;
    ImageLoader imageLoader = null;
    ImageLoader.ImageListener imageListener = null;
    StringRequest commentRequest = null;
    View header = null;

    TextView comment_count = null;
    SharedPreferences sp = null;

    long user_id = 0;
    long to_user_id = 0;
    ArrayList<Product> p_list = null;

    StringRequest tuijianRequest = null;

    long category_id = 0;
    long pid = 0;
    StringRequest goodRequest = null;
    boolean iszan = false;
    ImageView imgzan = null;

    StringRequest removeRequest = null;

    LovePersonView lovePersonView=null;

    ArrayList<Bitmap>bits=null;

    StringRequest bitRequest=null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product__info);
        Util.ContextArray.add(Product_InfoActivity.this);
        sp = Util.getSp(getApplicationContext());
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        bits=new ArrayList<>();
        user_id = sp.getLong("user_id", 0);
        initView();
        gson = new Gson();
        layoutInflater = LayoutInflater.from(getApplicationContext());
        header = layoutInflater.inflate(R.layout.product_info_header, null);
        lovePersonView= (LovePersonView) header.findViewById(R.id.product_info_love);
        lovePersonView.setLovePerson(bits);
        text_username = (TextView) header.findViewById(R.id.p_info_username);
        text_time = (TextView) header.findViewById(R.id.p_info_time);
        user_img = (ImageView) header.findViewById(R.id.p_info_userimg);
        p_price = (TextView) header.findViewById(R.id.p_info_price);
        p_info = (TextView) header.findViewById(R.id.p_info_p_info);
        p_location = (TextView) header.findViewById(R.id.p_info_location);
        p_goodcount = (TextView) header.findViewById(R.id.p_info_goodcount);
        P_lookcount = (TextView) header.findViewById(R.id.p_info_lookcount);
        comment_count = (TextView) header.findViewById(R.id.comment_count);

        headerListView = (NoScrollListView) header.findViewById(R.id.product_header_listview);
        Intent in = getIntent();
        pid = in.getLongExtra("p_id", 0);



        showzan();
        //查询点赞人的头像放到自定义控件里
        findClickPerson();

        imageLoader = new ImageLoader(requestQueue, new ImageLoader.ImageCache() {
            private LruCache<String, Bitmap> lruCache = new LruCache<>(1024 * 1024 * 3);

            @Override
            public Bitmap getBitmap(String s) {
                return lruCache.get(s);
            }

            @Override
            public void putBitmap(String s, Bitmap bitmap) {

                lruCache.put(s, bitmap);

            }
        });

        findHeader();

        listview.addHeaderView(header, null, true);
        View footer = layoutInflater.inflate(R.layout.product_info_footer, null);
        footer_gridView = (NoScrollGridView) footer.findViewById(R.id.p_info_footer_gridview);
        p_list = new ArrayList<>();
        infoFooterAdapter = new ProductInfoFooterAdapter(getApplicationContext(), p_list);
        footer_gridView.setAdapter(infoFooterAdapter);


        footer_gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent1 = new Intent(Product_InfoActivity.this, Product_InfoActivity.class);
                intent1.putExtra("p_id", p_list.get(position).getId());
                startActivity(intent1);
            }
        });


        listview.addFooterView(footer, null, true);

        //给详情页的listview添加数据

        list = new ArrayList<>();
        commentAdapter = new CommentAdapter(getApplicationContext(), list);
        listview.setAdapter(commentAdapter);
        findComment();


        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                r_post_comment.setVisibility(View.VISIBLE);
                r_liuyan.setVisibility(View.GONE);
                inputMethodManager.showSoftInput(comment_edit, 1);
                to_user_id = list.get(position-1).getUser_id();
            }
        });
    }

    private void findClickPerson() {
        bits.clear();
        bitRequest=new StringRequest(Util.URL + "UserServlet?action=findClickPerson&type=android" +
                "&product_id="+pid,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if(s.trim().length()>0){
                            ArrayList<User>users=gson.fromJson(s,new TypeToken<ArrayList<User>>(){}.getType());
                            for(User u:users){
                            ImageRequest  imageRequest=new ImageRequest(Util.URL + "user/" +u.getImg(),
                                        new Response.Listener<Bitmap>() {
                                            @Override
                                            public void onResponse(Bitmap bitmap) {
                                                  bits.add(bitmap);
                                                lovePersonView.setLovePerson(bits);
                                            }
                                        }, 100, 100, Bitmap.Config.ARGB_8888,
                                        new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError volleyError) {

                                            }
                                        });
                                requestQueue.add(imageRequest);
                            }
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        requestQueue.add(bitRequest);

    }

    private void showzan() {
        StringRequest LikeRequest = new StringRequest(Util.URL + "ProductServlet" +
                "?type=android&action=findLike&pid=" + pid + "&user_id=" + user_id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (s.trim().length() > 0) {
                            if (s.trim().equals("ok")) {
                                iszan = true;
                                changeLike();
                            } else {
                                iszan = false;
                                changeLike();
                            }

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        requestQueue.add(LikeRequest);

    }

    private void findTuijian() {
        tuijianRequest = new StringRequest(Util.URL + "ProductServlet?type=android&action=findByTuijian" +
                "&category_id=" + category_id + "&pid=" + pid,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (s.trim().length() > 0) {
                            p_list.clear();
                            ArrayList<Product> array = gson.fromJson(s, new TypeToken<ArrayList<Product>>() {
                            }.getType());

                            for (Product p : array) {
                                p_list.add(p);
                            }
                            infoFooterAdapter.notifyDataSetChanged();

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        requestQueue.add(tuijianRequest);


    }

    private void findHeader() {
        stringRequest = new StringRequest(Util.URL + "ProductServlet?" +
                "type=android&action=findById&pid=" + pid
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if (s.trim().length() > 0) {
                    p = gson.fromJson(s.trim(), Product.class);
                    imglist = p.getBitsurl();
                    imgAdapter = new CommentImgAdapter(getApplicationContext(), imglist);
                    headerListView.setAdapter(imgAdapter);

                    text_username.setText(p.getUsername());
                    text_time.setText(p.getTime());
                    imageListener = imageLoader.getImageListener(user_img, R.drawable.loading_failed_big_icon,
                            R.drawable.loading_failed_big_icon);
                    imageLoader.get(Util.URL + "user/" + p.getUserimg(), imageListener);
                    p_price.setText(p.getPriceinfo() != null ? p.getPriceinfo() : "￥ " + p.getPrice());
                    p_info.setText(p.getTitle() + "，" + p.getInfo());
                    p_location.setText(p.getAddress() + "  鱼塘|" + p.getYutang_name());
                    p_goodcount.setText(String.format("%d人点赞", p.getGood()));
                    P_lookcount.setText(String.format("%d人浏览", p.getLookcount()));
                    comment_count.setText("热门留言(" + p.getComment() + ")");
                    category_id = p.getCategory_id();
                    findTuijian();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }
        );
        requestQueue.add(stringRequest);


    }

    private void findComment() {
        commentRequest = new StringRequest(Util.URL
                + "ProductServlet?type=android&action=findComment&pid=" + pid,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (s.trim().length() > 0) {
                            list.clear();
                            ArrayList<Comment> array = gson.fromJson(s, new TypeToken<ArrayList<Comment>>() {
                            }.getType());
                            for (Comment c : array) {
                                list.add(c);
                            }
                            listview.post(new Runnable() {
                                @Override
                                public void run() {
                                    commentAdapter.notifyDataSetChanged();
                                }
                            });
                            View emptyView = header.findViewById(R.id.comment_empty_view);
                            if (list.size() > 0) {
                                emptyView.setVisibility(View.GONE);
                            } else {
                                emptyView.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        requestQueue.add(commentRequest);

    }


    public void initView() {
        imgzan = (ImageView) findViewById(R.id.img_zan);
        btn_comment = (RelativeLayout) findViewById(R.id.product_info_liuyan);
        btn_good = (RelativeLayout) findViewById(R.id.product_info_dianzan);
        btn_want = (Button) findViewById(R.id.btn_want_buy);
        btn_jianpan = (ImageView) findViewById(R.id.btn_jianpan);
        btn_postMsg = (Button) findViewById(R.id.btn_post_pinglun);
        comment_edit = (EditText) findViewById(R.id.edit_pinglun);
        listview = (ListView) findViewById(R.id.product_info_listview);
        r_liuyan = (RelativeLayout) findViewById(R.id.procut_info_want);
        r_post_comment = (RelativeLayout) findViewById(R.id.product_info_jianpan);
        inputMethodManager = (InputMethodManager) comment_edit.getContext().getSystemService
                (INPUT_METHOD_SERVICE);

        ButtonListener buttonListener = new ButtonListener();
        btn_comment.setOnClickListener(buttonListener);
        btn_good.setOnClickListener(buttonListener);
        btn_want.setOnClickListener(buttonListener);
        btn_jianpan.setOnClickListener(buttonListener);
        btn_postMsg.setOnClickListener(buttonListener);
    }

    public void changeLike() {
        imgzan.setImageResource(iszan ? R.drawable.love_red : R.drawable.love_gray);
    }

    private class ButtonListener implements View.OnClickListener {
        String msg = null;

        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.product_info_liuyan:
                    r_post_comment.setVisibility(View.VISIBLE);
                    r_liuyan.setVisibility(View.GONE);

                    inputMethodManager.showSoftInput(comment_edit, 1);
                    to_user_id=0;

                    break;

                case R.id.product_info_dianzan:

                    if (!iszan) {
                        //没有赞，点赞
                        btn_good.setEnabled(false);
                        goodRequest = new StringRequest(Util.URL + "ProductServlet" +
                                "?type=android&action=clickLike&pid=" + pid + "&user_id=" + user_id,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String s) {
                                        btn_good.setEnabled(true);
                                        if (s.trim().length() > 0) {

                                            if (s.trim().equals("ok")) {
                                                Util.Toast(getApplicationContext(),
                                                        "得到一枚赞，好鸡冻!");
                                                iszan = true;
                                                changeLike();
                                                findClickPerson();
                                            } else {
                                                Util.Toast(getApplicationContext(), "点赞失败");
                                            }
                                        }

                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                btn_good.setEnabled(true);
                            }
                        });
                        requestQueue.add(goodRequest);
                    } else {
                        //取消赞
                        btn_good.setEnabled(false);
                        removeRequest = new StringRequest(Util.URL + "ProductServlet" +
                                "?type=android&action=removeLike&pid=" + pid + "&user_id=" + user_id,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String s) {
                                        btn_good.setEnabled(true);
                                        if (s.trim().length() > 0) {
                                            if (s.trim().equals("ok")) {
                                                Util.Toast(getApplicationContext(), "居然不爱我了，受伤!");
                                                iszan = false;
                                                changeLike();
                                                findClickPerson();

                                            } else {
                                                Util.Toast(getApplicationContext(), "网络连接失败");
                                            }
                                        }
                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                btn_good.setEnabled(true);
                            }
                        });

                        requestQueue.add(removeRequest);
                    }


                    break;

               //跳转到售前页面
                case R.id.btn_want_buy:
                    Intent intent = new Intent(getApplicationContext(), EnquiryActivity.class);
                    String st=gson.toJson(p);
                    intent.putExtra("p",st);
                    startActivity(intent);

                    break;

                case R.id.btn_jianpan:
                    r_post_comment.setVisibility(View.GONE);
                    r_liuyan.setVisibility(View.VISIBLE);
                    inputMethodManager.hideSoftInputFromWindow(comment_edit.getWindowToken(),
                            0);
                    to_user_id=0;

                    break;


                case R.id.btn_post_pinglun:
                    msg = comment_edit.getText().toString().trim();
                    if (msg.length() > 0) {
                        btn_postMsg.setEnabled(false);
                        StringRequest commentRequest = new StringRequest(Request.Method.POST,
                                Util.URL + "ProductServlet?type=android&action=addComment",
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String s) {
                                        comment_edit.setText("");
                                        btn_postMsg.setEnabled(true);
                                        if (s.trim().length() > 0) {
                                            if (s.trim().equals("ok")) {
                                                Util.Toast(getApplicationContext(), "评论成功");
                                                findComment();
                                            } else {
                                                Util.Toast(getApplicationContext(), "评论失败");
                                            }

                                        }

                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError volleyError) {
                                        btn_postMsg.setEnabled(true);
                                        Util.Toast(getApplicationContext(), "网络连接超时");
                                    }
                                }
                        ) {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                HashMap<String, String> map = new HashMap<>();
                                map.put("text", msg);
                                map.put("pid", String.valueOf(pid));
                                map.put("user_id", String.valueOf(user_id));
                                map.put("to_user_id", String.valueOf(to_user_id));
                                return map;
                            }
                        };

                        requestQueue.add(commentRequest);


                    } else {
                        Util.Toast(getApplicationContext(), "亲，请输入评论内容");
                    }


                    break;
            }
        }
    }
}
