package com.pjt.xianyu.pojo;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/05/25.
 */
public class Product {
	private long id;
	private String title;
	private String info;
	private long user_id;
	private String username;
	private String userimg;
	private String address;
	private double price;
	private String priceinfo;
	private long yutang_id;
	private String yutang_name;
	private String time;
	private ArrayList<String> bitsurl;
	private String location;
	private int comment;
	private int good;
	private long lookcount;
	private long category_id;

	public Product() {
	}

	public String getAddress() {
		return address;
	}

	public long getCategory_id() {
		return category_id;
	}

	public void setCategory_id(long categoryId) {
		category_id = categoryId;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public long getId() {
		return id;
	}

	public long getLookcount() {
		return lookcount;
	}

	public void setLookcount(long lookcount) {
		this.lookcount = lookcount;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public long getUser_id() {
		return user_id;
	}

	public void setUser_id(long userId) {
		user_id = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUserimg() {
		return userimg;
	}

	public void setUserimg(String userimg) {
		this.userimg = userimg;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getPriceinfo() {
		return priceinfo;
	}

	public void setPriceinfo(String priceinfo) {
		this.priceinfo = priceinfo;
	}

	public long getYutang_id() {
		return yutang_id;
	}

	public void setYutang_id(long yutangId) {
		yutang_id = yutangId;
	}

	public String getYutang_name() {
		return yutang_name;
	}

	public void setYutang_name(String yutangName) {
		yutang_name = yutangName;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public ArrayList<String> getBitsurl() {
		return bitsurl;
	}

	public void setBitsurl(ArrayList<String> bitsurl) {
		this.bitsurl = bitsurl;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public int getComment() {
		return comment;
	}

	public void setComment(int comment) {
		this.comment = comment;
	}

	public int getGood() {
		return good;
	}

	public void setGood(int good) {
		this.good = good;
	}

}
