package com.example.jeff.yueli;

import android.graphics.Bitmap;

/**
 * Created by XDDN2 on 2018/3/1.
 */

public class spot {
    private Bitmap image;
    private int ID;
    private String name;
    private String city;
    private String description;
    private Object location;
    private int rank;

    public spot(int id, String n, String descri, String c, Object l, int r) {
        ID = id;
        name = n;
        city = c;
        description = descri;
        location = l;
        rank = r;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public int getID() {
        return ID;
    }

    public String getCity() {
        return city;
    }

    public int getRank() {
        return rank;
    }

    public String getName() {
        return name;
    }

    public Bitmap getImage() {
        return image;
    }

    public String getDescription() {
        return description;
    }

    public Object getLocation() {
        return location;
    }
}
