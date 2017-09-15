package com.pjt.xianyu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pjt.xianyu.pojo.Message;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/05/27.
 */
public class MessageAdapter extends BaseAdapter {
    private Context context=null;
    private LayoutInflater layoutInflater=null;
    private ArrayList<Message>list=null;


    public MessageAdapter(Context context, ArrayList<Message> list) {
        this.list=list;
        this.context=context;
        this.layoutInflater=LayoutInflater.from(context);

    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder=null;
        if(convertView==null){
            convertView=layoutInflater.inflate(R.layout.message_item,parent,false);
            viewHolder=new ViewHolder(convertView);
            convertView.setTag(viewHolder);

        }else{
            viewHolder= (ViewHolder) convertView.getTag();
        }

        viewHolder.binder(list.get(position));


        return convertView;
    }

    private class ViewHolder {
        private ImageView msg_imageView=null;
        private TextView text_title=null;
        private TextView text_info=null;
        private TextView text_state=null;


        public ViewHolder(View convertView) {
            msg_imageView= (ImageView) convertView.findViewById(R.id.message_img);
            text_title= (TextView) convertView.findViewById(R.id.message_title);
            text_info= (TextView) convertView.findViewById(R.id.message_info);
            text_state= (TextView) convertView.findViewById(R.id.message_state);

        }

        public void binder(Message message) {
            text_title.setText(message.getTitle());
            text_info.setText(message.getMessage());
            if(message.getSystem_img()!=0){
                msg_imageView.setImageResource(message.getSystem_img());
            }
        }
    }
}
