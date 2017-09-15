package com.pjt.pojo;

/**
 * Created by Administrator on 2016/05/27.
 */
public class YuTang {
	private long yutang_id;
    private String name;
    private long popularity;
    private String imgurl;
    private long post_num;
    private int last_post_num;
    private String identity;
    private String lat;
    private String lng;


    public YuTang() {
    }

    public String getIdentity() {
		return identity;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getLng() {
		return lng;
	}

	public void setLng(String lng) {
		this.lng = lng;
	}

	public void setIdentity(String identity) {
		this.identity = identity;
	}

	public YuTang(String name, long popularity, String imgurl, long post_num, int last_post_num) {
        this.name = name;
        this.popularity = popularity;
        this.imgurl = imgurl;
        this.post_num = post_num;
        this.last_post_num = last_post_num;
    }

    public long getYutang_id() {
		return yutang_id;
	}

	public void setYutang_id(long yutangId) {
		yutang_id = yutangId;
	}

	public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getPopularity() {
        return popularity;
    }

    public void setPopularity(long popularity) {
        this.popularity = popularity;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public long getPost_num() {
        return post_num;
    }

    public void setPost_num(long post_num) {
        this.post_num = post_num;
    }

    public int getLast_post_num() {
        return last_post_num;
    }

    public void setLast_post_num(int last_post_num) {
        this.last_post_num = last_post_num;
    }
}
