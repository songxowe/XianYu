package com.pjt.xianyu;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pjt.xianyu.pojo.ClickLike;

import java.util.ArrayList;

public class MyZanActivity extends AppCompatActivity {

    RequestQueue requestQueue = null;
    StringRequest stringRequest = null;
    ReFlashListView listView = null;
    ZanAdapter adapter = null;
    ArrayList<ClickLike> list = null;
    Gson gson = null;
    long user_id=0;
    SharedPreferences sp=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_zan);
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        listView = (ReFlashListView) findViewById(R.id.zan_listview);
        gson = new Gson();
        sp=Util.getSp(getApplicationContext());
        user_id=sp.getLong("user_id",0);
        listView.setInterface(new ReFlashListView.IReflashListener() {
            @Override
            public void onReflash() {
                findMyZan();
            }
        });
        if(user_id!=0) {

            findMyZan();
            Log.d("r","sdasd");

        }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(MyZanActivity.this,Product_InfoActivity.class);
                intent.putExtra("p_id",list.get(position-1).getProduct_id());
                startActivity(intent);
            }
        });

    }

    private void findMyZan() {
        stringRequest = new StringRequest(Util.URL + "UserServlet?type=android&action=findMyGood&user_id="
                +user_id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (s.trim().length() > 0) {
                            list = gson.fromJson(s, new TypeToken<ArrayList<ClickLike>>() {
                            }.getType());

                            adapter = new ZanAdapter(getApplicationContext(), list);
                            listView.setAdapter(adapter);
                            listView.setState(0);

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                    }
                }

        );
        requestQueue.add(stringRequest);
    }
}
