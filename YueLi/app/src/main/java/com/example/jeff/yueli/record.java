package com.example.jeff.yueli;

/**
 * Created by xumuxin on 2018/3/3.
 */

public class record {
    private int travel_record_id;
    private int spot_id;
    private String spot_name;
    private String content;
    private String time;

    public int gettravelrecordid() {return travel_record_id;}
    public int getspotid() {
        return spot_id;
    }
    public String getspotname() {return spot_name;}
    public String getcontent() {return content;}
    public String gettime() {return time;}

    public void settravelrecordid(int id) {travel_record_id = id;}
    public void setspotid(int id) {spot_id = id;}

    public void setspotname(String name) {spot_name = name;}
    public void setContent(String t) {content = t;}
    public void settime(String t) {time = t;}
}
