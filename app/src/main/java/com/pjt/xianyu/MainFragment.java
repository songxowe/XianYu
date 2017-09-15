package com.pjt.xianyu;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.LoopPagerAdapter;
import com.jude.rollviewpager.hintview.ColorPointHintView;
import com.pjt.xianyu.pojo.Product;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {

    Context context=null;
    RelativeLayout btn_search=null;
    LayoutInflater layoutInflater=null;
    ReFlashListView listView=null;
    ProductAdapter productAdapter=null;
    RelativeLayout btn_cate=null;
    ArrayList<Product>productArrayList=null;
    StringRequest stringRequest=null;
    RequestQueue requestQueue=null;
    Gson gson=null;
    public MainFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=getActivity();
        layoutInflater=LayoutInflater.from(context);

        requestQueue= Volley.newRequestQueue(context);
        gson=new Gson();
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_main, container, false);
        btn_cate= (RelativeLayout) view.findViewById(R.id.btn_cate);
        listView= (ReFlashListView) view.findViewById(R.id.main_listview);
        View headerView=layoutInflater.inflate(R.layout.main_view,null);
        RollPagerView rollPagerView= (RollPagerView) headerView.findViewById(R.id.rollviewpager);
        TestLoopAdapter contentAdapter=new TestLoopAdapter(rollPagerView);
        rollPagerView.setAdapter(contentAdapter);
        rollPagerView.setPlayDelay(3000);
        rollPagerView.setHintView(new ColorPointHintView(context, Color.RED, Color.GRAY));
        View header=layoutInflater.inflate(R.layout.view_mode,null);
        listView.addHeaderView(headerView, null, true);
        productArrayList=new ArrayList<>();
        productArrayList=new ArrayList<>();
        productAdapter=new ProductAdapter(context,productArrayList);
        listView.setAdapter(productAdapter);
        findProduct();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e("消息", "" + position);
                long p_id = productArrayList.get(position - 2).getId();
                Intent intent = new Intent(context, Product_InfoActivity.class);
                intent.putExtra("p_id", p_id);
                startActivity(intent);

            }
        });
        ButtonListener buttonListener=new ButtonListener();
        btn_cate.setOnClickListener(buttonListener);

        listView.setInterface(new ReFlashListView.IReflashListener() {
            @Override
            public void onReflash() {
                 findProduct();
            }
        });

        return view;
    }

    private void findProduct() {
        stringRequest=new StringRequest(Util.URL + "ProductServlet?type=android&action=findall&pageno=1",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if(s.trim().length()>0){
                            productArrayList.clear();
                            ArrayList<Product>array=gson.fromJson(s,new TypeToken<ArrayList<Product>>(){}.getType());
                            for(Product p:array){
                                productArrayList.add(p);
                            }
                            listView.post(new Runnable() {
                                @Override
                                public void run() {
                                    productAdapter.notifyDataSetChanged();
                                }
                            });
                            listView.setState(0);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        requestQueue.add(stringRequest);


    }

    private class TestLoopAdapter extends LoopPagerAdapter {
        private int[] imgs = {
                R.drawable.play_1,
                R.drawable.play_2

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


    private class ButtonListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_cate:

                    Intent intent1=new Intent(context,CateGoryActivity.class);
                    startActivity(intent1);

                    break;
            }
        }
    }
}
