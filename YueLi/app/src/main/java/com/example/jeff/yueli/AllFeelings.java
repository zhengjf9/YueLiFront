package com.example.jeff.yueli;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.List;

/**
 * Created by XDDN2 on 2018/3/7.
 */

public class AllFeelings {
    private List<Feelings> data;
    private String msg;
    public AllFeelings(List<Feelings> f, String m) {
        data = f;
        msg = m;
    }

    public String getMsg() {
        return msg;
    }

    public void setImages(List<Bitmap> images) {
        for (int i = 0; i < data.size() && i < images.size(); ++i) {
            data.get(i).setImage(images.get(i));
        }
    }

    public List<Feelings> getFeelingsList() {
        return data;
    }
}
