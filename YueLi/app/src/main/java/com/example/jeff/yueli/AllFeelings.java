package com.example.jeff.yueli;

import java.util.List;

/**
 * Created by XDDN2 on 2018/3/7.
 */

public class AllFeelings {
    private List<Feelings> feelingsList;
    private String msg;
    public AllFeelings(List<Feelings> f, String m) {
        feelingsList = f;
        msg = m;
    }

    public String getMsg() {
        return msg;
    }

    public List<Feelings> getFeelingsList() {
        return feelingsList;
    }
}
