package com.pjt.xianyu.pojo;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/05/27.
 */
public class YuTangCate {
    private String title=null;
    private ArrayList<YuTang> list=null;

    public YuTangCate() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<YuTang> getList() {
        return list;
    }

    public void setList(ArrayList<YuTang> list) {
        this.list = list;
    }
}
