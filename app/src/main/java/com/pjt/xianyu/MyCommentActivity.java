package com.pjt.xianyu;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pjt.xianyu.pojo.Comment;

import java.util.ArrayList;

public class MyCommentActivity extends AppCompatActivity {

   StringRequest stringRequest=null;
    RequestQueue requestQueue=null;
    MyCommentAdapter adapter=null;
    ArrayList<Comment> list=null;
    ReFlashListView listView=null;
    long user_id=0;
    SharedPreferences sp=null;
    Gson gson=new Gson();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_comment);
        listView= (ReFlashListView) findViewById(R.id.comment_listview);
        requestQueue= Volley.newRequestQueue(getApplicationContext());
        sp=Util.getSp(getApplicationContext());
        user_id=sp.getLong("user_id",0);
        if(user_id!=0) {
            findMyComment();
        }
        listView.setInterface(new ReFlashListView.IReflashListener() {
            @Override
            public void onReflash() {
                findMyComment();
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(MyCommentActivity.this,Product_InfoActivity.class);
                intent.putExtra("p_id",list.get(position-1).getProduct_id());
                startActivity(intent);
            }
        });

    }

    private void findMyComment() {
          stringRequest=new StringRequest(Util.URL + "UserServlet?type=android&action=findMyComment&user_id="
                  + user_id,
                  new Response.Listener<String>() {
                      @Override
                      public void onResponse(String s) {
                            if(s.trim().length()>0){
                                list=gson.fromJson(s,new TypeToken<ArrayList<Comment>>(){}.getType());
                                adapter=new MyCommentAdapter(getApplicationContext(),list);
                                listView.setAdapter(adapter);
                                listView.setState(0);
                            }
                      }
                  }
                  , new Response.ErrorListener() {
              @Override
              public void onErrorResponse(VolleyError volleyError) {

              }
          }
          );
        requestQueue.add(stringRequest);

    }
}
