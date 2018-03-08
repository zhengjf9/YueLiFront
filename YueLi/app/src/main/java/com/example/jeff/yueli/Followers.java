package com.example.jeff.yueli;

import java.util.List;

/**
 * Created by xumuxin on 2018/3/7.
 */

public class Followers {
    private List<follow> data;
    private String msg;

    public void setmsg(String msg) {
        this.msg = msg;
    }

    public void setdata(List<follow> recordinfo) {
        this.data = recordinfo;
    }

    public String getmsg() {
        return msg;
    }

    public List<follow> getdata() {
        return data;
    }

    public static class follow {
        private int user_id;
        private String nickname;
        private String signature;

        public int getuser_id() {return user_id;}
        public String getnickname() {return nickname;}
        public String getsignature() {return signature;}


        public void setuser_id(int id) {user_id = id;}
        public void setSignature(String t) {signature = t;}
        public void setNickname(String t) {nickname = t;}

    }
}
