package com.pjt.xianyu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/06/02.
 */
public class StringAdapter extends BaseAdapter {

    String[]sts=null;
    Context context=null;
    LayoutInflater layoutInflater=null;


    public StringAdapter(Context context, String[] fenlei) {
        this.sts=fenlei;
        this.context=context;
        this.layoutInflater=LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return sts.length;
    }

    @Override
    public String getItem(int position) {
        return sts[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder=null;
        if(convertView==null){
            convertView=layoutInflater.inflate(R.layout.fenlei_item,parent,false);
            viewHolder=new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) convertView.getTag();
        }

        viewHolder.binder(sts[position]);


        return convertView;
    }

    private class ViewHolder {

        TextView textView=null;

        public ViewHolder(View convertView) {
            textView= (TextView) convertView.findViewById(R.id.text_item);
        }

        public void binder(String st) {
            textView.setText(st);

        }
    }
}
