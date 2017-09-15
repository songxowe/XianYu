package com.pjt.xianyu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/05/31.
 */
public class NomoneyAdapter extends BaseAdapter {

    private Context context=null;
    private String[]sts=null;
    private LayoutInflater layoutInflater=null;


    public NomoneyAdapter(Context applicationContext, String[] sts) {
        this.context=applicationContext;
        this.sts=sts;
        this.layoutInflater=LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return sts.length;
    }

    @Override
    public Object getItem(int position) {
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
            convertView=layoutInflater.inflate(R.layout.nomoney_grid_item,parent,false);
            viewHolder=new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) convertView.getTag();
        }

        viewHolder.binder(sts[position]);

        return convertView;
    }

    private class ViewHolder {

        TextView button=null;

        public ViewHolder(View convertView) {
            button= (TextView) convertView.findViewById(R.id.text_nomoney);

        }

        public void binder(String st) {
            button.setText(st);
        }
    }
}
