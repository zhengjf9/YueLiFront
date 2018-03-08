package com.example.jeff.yueli;

import com.google.gson.annotations.SerializedName;

/**
 * Created by xumuxin on 2018/3/5.
 */

public class reviewback {
    @SerializedName(value = "travel_id",alternate = {"travel_record_id"})
    private int travel_id;

    public void setComment_id(int i) {
        travel_id = i;
    }
    public int getComment_id() {
        return travel_id;
    }
}
