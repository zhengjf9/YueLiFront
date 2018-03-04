package com.example.jeff.yueli;

import java.io.Serializable;

/**
 * Created by xumuxin on 2018/3/3.
 */

public class User implements Serializable {
    private int id;

    private String nickname;

    public String getnickname() {
        return nickname;
    }
    public int getuserid() {
        return id;
    }
    public void setnickname(String name) {
        this.nickname = name;
    }
    public void setuserid(int id) {
        this.id = id;
    }
}
