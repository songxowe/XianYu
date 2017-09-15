package com.pjt.pojo;



/**
 * Created by Administrator on 2016/05/27.
 */
public class Message {
    private String title;
    private String message;
    private String imgurl;
    private String order_img;
    private String state;
    private int system_img;

    public Message() {
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public int getSystem_img() {
        return system_img;
    }

    public void setSystem_img(int system_img) {
        this.system_img = system_img;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getImg() {
        return imgurl;
    }

    public void setImg(String img) {
        this.imgurl = img;
    }

    public String getOrder_img() {
        return order_img;
    }

    public void setOrder_img(String order_img) {
        this.order_img = order_img;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
