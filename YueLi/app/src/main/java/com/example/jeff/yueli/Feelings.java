package com.example.jeff.yueli;

import android.graphics.Bitmap;

/**
 * Created by XDDN2 on 2018/3/7.
 */

public class Feelings {
    private int feeling_id;
    private User user;
    private Bitmap image;
    private String content;
    private String time;
    private double latitude, longtitude;
    public Feelings(User u, Bitmap img, String contents, String time, double la, double lo) {
        user = u;
        image = img;
        this.time = time;
        content = contents;
        latitude = la;
        longtitude = lo;
    }

    public void setFeeling_id(int feeling_id) {
        this.feeling_id = feeling_id;
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
        return longtitude;
    }

    public User getUser() {
        return user;
    }
}
