package com.pjt.xianyu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pjt.xianyu.pojo.Province;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/06/02.
 */
public class ProvinceAdapter extends BaseAdapter {

    private Context context=null;
    private ArrayList<Province>list=null;
    private LayoutInflater layoutInflater=null;


    public ProvinceAdapter(Context context, ArrayList<Province> provincesList) {
        this.context=context;
        this.list=provincesList;
        this.layoutInflater=LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Province getItem(int position) {
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
            convertView=layoutInflater.inflate(R.layout.province_item,parent,false);
            viewHolder=new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) convertView.getTag();
        }

        viewHolder.binder(list.get(position));




        return convertView;
    }

    private class ViewHolder {
        TextView textView=null;
        public ViewHolder(View convertView) {
            textView= (TextView) convertView.findViewById(R.id.city_name);

        }

        public void binder(Province province) {
            textView.setText(province.getName());
        }
    }
}
