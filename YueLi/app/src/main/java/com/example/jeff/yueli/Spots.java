package com.example.jeff.yueli;

import java.util.List;

/**
 * Created by XDDN2 on 2018/3/7.
 */

public class Spots {
    private List<spot> data;
    private String msg;

    public Spots(List<spot> d, String m) {
        data = d;
        msg = m;
    }

    public List<spot> getData() {
        return data;
    }

    public String getMsg() {
        return msg;
    }
}

