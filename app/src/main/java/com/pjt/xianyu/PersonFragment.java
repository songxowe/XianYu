package com.pjt.xianyu;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.pjt.xianyu.pojo.User;


/**
 * A simple {@link Fragment} subclass.
 */
public class PersonFragment extends Fragment {

    private Button to_Login;
    private CircleImageView person_img;
    private Context context=null;
    private int[]setids=new int[]{R.id.set_r_main,R.id.set_myyubei,R.id.set_mypost,
    R.id.set_maichu,R.id.set_maidao,R.id.set_zanguo,R.id.set_mypaimai,R.id.set_faxian,
    R.id.set_help,R.id.set_shezhi};
    long user_id=0;
    TextView text_info=null;
    SharedPreferences sp=null;
    RelativeLayout nologin=null;
    StringRequest stringRequest=null;
    RequestQueue requestQueue=null;
    Gson gson=null;
    TextView text_name=null;
    ImageLoader loader=null;


    public PersonFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=getActivity();
        sp=Util.getSp(context);
        requestQueue= Volley.newRequestQueue(context);
       gson=new Gson();
        loader=new ImageLoader(requestQueue, new ImageLoader.ImageCache() {
            private LruCache<String,Bitmap> lruCache=new LruCache<>(1024*1024*3);

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_person, container, false);
        to_Login= (Button) view.findViewById(R.id.btn_tologin);
        nologin= (RelativeLayout) view.findViewById(R.id.set_nologin);
        text_info= (TextView) view.findViewById(R.id.set_text_info);
        text_name= (TextView) view.findViewById(R.id.set_text_name);
        person_img= (CircleImageView) view.findViewById(R.id.person_img);


        user_id=sp.getLong("user_id", 0);
        if(user_id!=0){
            //已经登陆
            nologin.setVisibility(View.VISIBLE);
            text_info.setVisibility(View.VISIBLE);
            text_name.setText(sp.getString("username",null));
            to_Login.setVisibility(View.GONE);



        }else{
            //未登录
            to_Login.setVisibility(View.VISIBLE);
            text_name.setText("你还没有登录哦。");
            text_info.setVisibility(View.GONE);
            nologin.setVisibility(View.GONE);
        }
        ButtonListener buttonListener=new ButtonListener();
        to_Login.setOnClickListener(buttonListener);
        for(int i:setids){
            RelativeLayout relativeLayout= (RelativeLayout) view.findViewById(i);
            relativeLayout.setOnClickListener(buttonListener);
        }

        findUser();


        return view;
    }

    private void findUser() {
        stringRequest=new StringRequest(Util.URL + "UserServlet?action=findUser&type=android" +
                "&user_id="+user_id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if(s.trim().length()>0){
                            User user=gson.fromJson(s, User.class);

                            ImageLoader.ImageListener imageListener=
                                    loader.getImageListener(person_img, R.drawable.loading_failed_big_icon,
                                            R.drawable.loading_failed_big_icon);
                            loader.get(Util.URL+"user/"+user.getImg(),imageListener);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        requestQueue.add(stringRequest);

    }

    private class ButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_tologin:

                    Intent intent=new Intent(context,LoginActivity.class);
                    startActivity(intent);
                    Util.ContextArray.add(context);


                    break;
                case R.id.set_r_main:
                    if(sp.getLong("user_id",0)!=0) {
                        Intent intent1 = new Intent(context, PersonCenterActivity.class);
                        intent1.putExtra("user_id",sp.getLong("user_id",0));
                        startActivity(intent1);
                        Util.ContextArray.add(context);
                    }
                    break;

                case R.id.set_shezhi:
                    Util.ContextArray.add(context);
                    Intent intent1=new Intent(context,SetingActivity.class);
                    startActivity(intent1);


                    break;




            }
        }
    }
}
