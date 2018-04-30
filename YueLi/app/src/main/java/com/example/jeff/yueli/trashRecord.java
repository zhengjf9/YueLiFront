package com.example.jeff.yueli;

import org.litepal.crud.DataSupport;

/**
 * Created by xumuxin on 2018/4/30.
 */

public class trashRecord extends DataSupport {
    private int id;
    private int record_id;
    private int travel_id;
    private String text;
    private String day;
    private String location;
    private int duration;
    //private int user_id;
    //private String nickname;
    // private int favorite_count;
    //private boolean favorited;
    // private int comment_count;
    public int getId() {return id;}
    public int getrecord_id() {
        return record_id;
    }
    public int gettravelid() {
        return travel_id;
    }
    public String gettext() {
        return text;
    }
    public String getday() {
        return day;
    }
    public String getlocation() {
        return location;
    }
    public int getduration() {
        return duration;
    }

    public void setrecord_id(int id) {
        record_id = id;
    }
    public void settravelid(int id) {
        travel_id = id;
    }

    public void settext(String t) {
        text = t;
    }
    public void setday(String d) {
        day = d;
    }
    public void setlocation(String locate) {
        location = locate;
    }

    public void setduration(int d) {
        duration = d;
    }
}
