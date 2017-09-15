package com.pjt.pojo;

public class ClickLike {
	private long id;
	private long product_id;
	private long user_id;
	private String time;
	private String fromUsername;
	private String fromUserImg;
	private String p_img;
	
	
	public ClickLike(){
		
	}

	public String getFromUsername() {
		return fromUsername;
	}

	public void setFromUsername(String fromUsername) {
		this.fromUsername = fromUsername;
	}

	public String getFromUserImg() {
		return fromUserImg;
	}

	public void setFromUserImg(String fromUserImg) {
		this.fromUserImg = fromUserImg;
	}

	public String getP_img() {
		return p_img;
	}

	public void setP_img(String pImg) {
		p_img = pImg;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getProduct_id() {
		return product_id;
	}

	public void setProduct_id(long productId) {
		product_id = productId;
	}

	public long getUser_id() {
		return user_id;
	}

	public void setUser_id(long userId) {
		user_id = userId;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}
	

}
