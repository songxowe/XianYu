package com.pjt.xianyu.pojo;

/**
 * Created by Administrator on 2016/05/29.
 */
public class Comment {
    private long id;
    private String text;
    private long user_id;
    private long to_user_id;
    private long product_id;
    private String time;
    private String username;
    private String userimg=null;
    private String to_username=null;
    private String p_img=null;
    
    
    public Comment(){
    	
    }

	public String getP_img() {
		return p_img;
	}

	public void setP_img(String pImg) {
		p_img = pImg;
	}

	public String getTo_username() {
		return to_username;
	}

	public void setTo_username(String toUsername) {
		to_username = toUsername;
	}

	public String getUserimg() {
		return userimg;
	}

	public void setUserimg(String userimg) {
		this.userimg = userimg;
	}

	public long getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public long getUser_id() {
		return user_id;
	}

	public void setUser_id(long userId) {
		user_id = userId;
	}

	public long getTo_user_id() {
		return to_user_id;
	}

	public void setTo_user_id(long toUserId) {
		to_user_id = toUserId;
	}

	public long getProduct_id() {
		return product_id;
	}

	public void setProduct_id(long productId) {
		product_id = productId;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}
    
    
}
