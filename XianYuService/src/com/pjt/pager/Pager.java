package com.pjt.pager;

import java.util.ArrayList;

import com.pjt.pojo.Product;

public class Pager<T> {
  private ArrayList<T>list=null;
  private static int size=8;
  private int totalPager=0;
  
  public Pager(){
	  
  }

public ArrayList<T> getList() {
	return list;
}

public void setList(ArrayList<T> list) {
	this.list = list;
}

public static int getSize() {
	return size;
}

public static void setSize(int size) {
	Pager.size = size;
}

public int getTotalPager() {
	return totalPager;
}

public void setTotalPager(int totalPager) {
	this.totalPager = totalPager;
}
  
  

}
