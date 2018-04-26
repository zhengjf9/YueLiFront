package com.example.jeff.yueli;

import android.graphics.Bitmap;

/**
 * Created by XDDN2 on 2018/3/7.
 */

public class Feelings {
    private int feeling_id;
    private String user_id;
    private String nickname;
    private Bitmap image;
    private String content;
    private String time;
    private double latitude, longitude;
    public Feelings(Bitmap img, String contents, String time, double la, double lo) {

        image = img;
        this.time = time;
        content = contents;
        latitude = la;
        longitude = lo;
    }

    public void setFeeling_id(int feeling_id) {
        this.feeling_id = feeling_id;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getNickname() {
        return nickname;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public Bitmap getImage() {
        return image;
    }

    public int getFeeling_id() {
        return feeling_id;
    }

    public String getContent() {
        return content;
    }

    public String getTime() {
        return time;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongtitude() {
        return longitude;
    }

}
