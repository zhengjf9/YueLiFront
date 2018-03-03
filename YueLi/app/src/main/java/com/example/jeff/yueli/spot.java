package com.example.jeff.yueli;

import android.graphics.Bitmap;

/**
 * Created by XDDN2 on 2018/3/1.
 */

public class spot {
    private Bitmap image;
    private String description;
    private String local;
    private double latitude, longtitude;
    public spot(Bitmap img, String des, String l, double la, double lo) {
        image = img;
        description = des;
        local = l;
        latitude = la;
        longtitude = lo;
    }

    public Bitmap getImage() {
        return image;
    }

    public String getDescription() {
        return description;
    }

    public String getLocal() {
        return local;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongtitude() {
        return longtitude;
    }
}
