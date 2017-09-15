package com.pjt.xianyu;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.pjt.xianyu.pojo.Message;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class MessageFragment extends Fragment {


    private Context context=null;
    private ReFlashListView listView=null;
    private MessageAdapter messageAdapter=null;
    private ArrayList<Message> list=null;
    private StringRequest stringRequest=null;
    private RequestQueue requestQueue=null;

    public MessageFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=getActivity();
        requestQueue= Volley.newRequestQueue(context);
        Intent intent1=new Intent(context,MessageService.class);
        context.startService(intent1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_message, container, false);
        listView= (ReFlashListView) view.findViewById(R.id.message_listview);
        list=new ArrayList<>();
        Message message=new Message();
        message.setTitle("留言");
        message.setMessage("当前没有留言");
        message.setSystem_img(R.drawable.message_icon_03);
        list.add(message);
        Message message1=new Message();
        message1.setTitle("收到的赞");
        message1.setMessage("当前没有收到赞");
        message1.setSystem_img(R.drawable.message_icon_06);
        list.add(message1);
        messageAdapter=new MessageAdapter(context,list);
        listView.setAdapter(messageAdapter);
        listView.setInterface(new ReFlashListView.IReflashListener() {
            @Override
            public void onReflash() {
                listView.setState(0);
            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 2) {
                    Intent intent = new Intent(context, MyZanActivity.class);
                    Util.ContextArray.add(context);
                    startActivity(intent);

                } else {
                    Intent intent = new Intent(context, MyCommentActivity.class);
                    Util.ContextArray.add(context);
                    startActivity(intent);
                }
            }
        });



        return view;
    }

}
