package com.pjt.xianyu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pjt.xianyu.pojo.County;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/06/02.
 */
public class CountyAdapter extends BaseAdapter{

    private Context context=null;
    public static  ArrayList<County>list=null;
    private LayoutInflater layoutInflater=null;


    public CountyAdapter(Context context, ArrayList<County> countyList) {
        this.context=context;
        this.list=countyList;
        this.layoutInflater=LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public County getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHodler=null;
        if(convertView==null){
            convertView=layoutInflater.inflate(R.layout.county_item,parent,false);
            viewHodler=new ViewHolder(convertView);
            convertView.setTag(viewHodler);
        }else{
            viewHodler= (ViewHolder) convertView.getTag();
        }
        viewHodler.binder(list.get(position));


        return convertView;
    }

    private class ViewHolder {

        TextView textView=null;

        public ViewHolder(View convertView) {
            textView= (TextView) convertView.findViewById(R.id.city_name);

        }

        public void binder(County county) {
            textView.setText(county.getName());
        }
    }
}
