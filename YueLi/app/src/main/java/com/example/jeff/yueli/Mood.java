package com.example.jeff.yueli;

import java.util.List;

/**
 * Created by xumuxin on 2018/3/7.
 */

public class Mood {
    private List<xinqing> data;
    private String msg;

    public void setmsg(String msg) {
        this.msg = msg;
    }

    public void setdata(List<xinqing> recordinfo) {
        this.data = recordinfo;
    }

    public String getmsg() {
        return msg;
    }

    public List<xinqing> getdata() {
        return data;
    }

    public static class xinqing {
        private int feeling_id;
        private int user_id;
        private String nickname;
        private String content;
        private float longitude;
        private float latitude;
        private String  time;

        public int getfeeling_id() {return feeling_id;}
        public int getuser_id() {
            return user_id;
        }
        public String getnickname() {return nickname;}
        public String getcontent() {return content;}

        public String gettime() {return time;}
        public float getlongitude() {return longitude;}
        public float getlatitude() {return latitude;}

        public void setfeeling_idd(int id) {feeling_id = id;}
        public void setuser_id(int id) {user_id = id;}

        public void setspotname(String name) {nickname = name;}
        public void setContent(String t) {content = t;}
        public void settime(String t) {time = t;}
        public void setlongitude(float d) {longitude=d;}
        public void setlatitude(float d) {latitude=d;}
    }

}
