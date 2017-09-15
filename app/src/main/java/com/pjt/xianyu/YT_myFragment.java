package com.pjt.xianyu;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pjt.xianyu.pojo.YuTang;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class YT_myFragment extends Fragment {

    private ReFlashListView listView = null;
    ArrayList<YuTang> list = null;
    private Context context = null;
    MyYTAdapter adapter = null;
    StringRequest stringRequest = null;
    RequestQueue requestQueue = null;
    Gson gson = null;
    SharedPreferences sp=null;
    long user_id=0;

    public YT_myFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        list = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(context);
        gson = new Gson();
        sp=Util.getSp(context);
        user_id=sp.getLong("user_id",0);

    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_yt_my, container, false);
        listView = (ReFlashListView) view.findViewById(R.id.yt_my_listview);
        adapter = new MyYTAdapter(context, list);
        listView.setAdapter(adapter);
        listView.setInterface(new ReFlashListView.IReflashListener() {
            @Override
            public void onReflash() {
                findMyYuTang();
            }
        });

        findMyYuTang();



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(context,YuTangActivity.class);
                intent.putExtra("yutang_id",list.get(position).getYutang_id());
                startActivity(intent);
            }
        });


        return view;
    }

    private void findMyYuTang() {
        stringRequest = new StringRequest(Util.URL+"YutangServlet?action=findMyYutang&type=android" +
                "&user_id="+user_id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (s.trim().length() > 0) {
                            ArrayList<YuTang> array = gson.fromJson(s, new TypeToken<ArrayList<YuTang>>() {
                            }.getType());
                            list.clear();
                            for (YuTang y : array) {
                                list.add(y);
                            }
                            listView.post(new Runnable() {
                                @Override
                                public void run() {
                                    adapter.notifyDataSetChanged();
                                }
                            });

                             listView.setState(0);
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

}
