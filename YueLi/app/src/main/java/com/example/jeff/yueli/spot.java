package com.example.jeff.yueli;

import android.graphics.Bitmap;

/**
 * Created by XDDN2 on 2018/3/1.
 */

public class spot {
    private Bitmap image;
    private String description;
    private String local;
    public spot(Bitmap img, String des, String l) {
        image = img;
        description = des;
        local = l;
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
}
