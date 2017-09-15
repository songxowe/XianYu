package com.pjt.xianyu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pjt.xianyu.pojo.CateGory;

import java.util.ArrayList;

public class CateGoryActivity extends AppCompatActivity {

    private GridView gridView=null;
    private ArrayList<CateGory> list=null;
    private CateAdapter adapter=null;
    private StringRequest stringRequest=null;
    private RequestQueue requestQueue=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cate_gory);
        gridView= (GridView) findViewById(R.id.cate_gridview);
        list=new ArrayList<>();
         requestQueue= Volley.newRequestQueue(getApplicationContext());
        stringRequest=new StringRequest(Util.URL + "CateGroyServlet?type=android",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                       Gson gson=new Gson();
                        if(s.trim().length()>0) {
                            ArrayList<CateGory> array = gson.fromJson(s,new TypeToken<ArrayList<CateGory>>()
                            {}.getType());
                            list=array;
                            adapter=new CateAdapter(getApplicationContext(),list);
                            gridView.setAdapter(adapter);

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        requestQueue.add(stringRequest);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(CateGoryActivity.this,ProductListActivity.class);
                intent.putExtra("position",position);
                intent.putExtra("cate_id",list.get(position).getId());
                startActivity(intent);
            }
        });


    }
}
