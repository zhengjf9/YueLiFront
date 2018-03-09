package com.example.jeff.yueli;

import android.graphics.Bitmap;

/**
 * Created by XDDN2 on 2018/3/1.
 */

public class spot {
    private Bitmap image;
    private int spot_id;
    private String name;
    private String city;
    private String description;
    private Object location;
    private int rank;
    private boolean favorited;
    private int comment_count;

    public spot(int id, String n, String descri, String c, Object l, int r) {
        spot_id = id;
        name = n;
        city = c;
        description = descri;
        location = l;
        rank = r;
    }


    public void setFavorited(boolean favorited) {
        this.favorited = favorited;
    }

    public boolean getFavorited() {
        return favorited;
    }
    
    public void setImage(Bitmap image) {
        this.image = image;
    }

    public void setComment_count(int c) {comment_count = c;}


    public int getID() {
        return spot_id;
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

    public int getComment_count() {return comment_count;}
}
