package com.pjt.xianyu.pojo;

public class User {
	private long id;
	private String username;
	private String password;
	private String sex;
	private String address;
	private String phone;
	private String img;
	private long commentcount;
	
	
	public User(){
		
	}

	public String getImg() {
		return img;
	}

	public long getCommentcount() {
		return commentcount;
	}

	public void setCommentcount(long commentcount) {
		this.commentcount = commentcount;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	

}
