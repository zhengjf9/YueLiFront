package com.example.jeff.yueli;

import java.io.Serializable;

/**
 * Created by xumuxin on 2018/3/3.
 */

public class User implements Serializable {
    private int id;

    private String name;

    public String getusername() {
        return name;
    }
    public int getuserid() {
        return id;
    }
    public void setusername(String name) {
        this.name = name;
    }
    public void setuserid(int id) {
        this.id = id;
    }
}
