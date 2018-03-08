package com.example.jeff.yueli;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by xumuxin on 2018/3/7.
 */

public class Notifications {
    private List<tongzhi> data;
    private String msg;

    public void setmsg(String msg) {
        this.msg = msg;
    }

    public void setdata(List<tongzhi> recordinfo) {
        this.data = recordinfo;
    }

    public String getmsg() {
        return msg;
    }

    public List<tongzhi> getdata() {
        return data;
    }

    public static class tongzhi {
        private int notification_id;
        private String type;
        private String  time;
        private Content content;
        private boolean is_read;


        public static class Content {
           @SerializedName(value = "travel_id", alternate = {"feeling_id","user_id"})
            private int travel_id;
           @SerializedName(value = "title", alternate = {"content"})
            private String title;

            private int sender_id;

            private String sender;


            public int getid() {return travel_id;}
            public int getsenderid() {return sender_id;}
            public String getname() {return title;}
            public String getSender() {return sender;}

            public void setid(int i) {travel_id = i;}
            public void setSender_id(int i) {sender_id = i;}
            public void setname(String s) {title = s;}
            public  void setSender(String s) {sender = s;}

        }
        public int getnotification_id() {return notification_id;}
        public String gettype() {return type;}
        public Content getcontent() {return content;}
        public String gettime() {return time;}
        public Boolean getread() {return is_read;}
        public void setnotification_id(int id) {notification_id = id;}
        public void settype(String name) {type = name;}
        public void setContent(Content t) {content = t;}
        public void settime(String t) {time = t;}
        public void setread(Boolean b) {is_read = b;}
    }
}
