package com.pjt.xianyu.pojo;

/**
 * Created by Administrator on 2016/05/27.
 */
public class CateGory {
	private long id;
    private String cate_name;
    private String cate_img;

    public CateGory() {
    }

    
    
    public long getId() {
		return id;
	}



	public void setId(long id) {
		this.id = id;
	}



	public CateGory(String cate_name) {
        this.cate_name = cate_name;
    }

    public CateGory(String cate_name, String cate_img) {
        this.cate_name = cate_name;
        this.cate_img = cate_img;
    }

    public String getCate_img() {
        return cate_img;
    }

    public void setCate_img(String cate_img) {
        this.cate_img = cate_img;
    }

    public String getCate_name() {
        return cate_name;
    }

    public void setCate_name(String cate_name) {
        this.cate_name = cate_name;
    }
}
