package com.example.jeff.yueli;

import java.util.List;

/**
 * Created by xumuxin on 2018/3/3.
 */

public class record {
    private List<Rec> data;
    private String msg;

    public void setmsg(String msg) {
        this.msg = msg;
    }

    public void setdata(List<Rec> recordinfo) {
        this.data = recordinfo;
    }

    public String getmsg() {
        return msg;
    }

    public List<Rec> gettrips() {
        return data;
    }

    public static class Rec {
        private int travel_record_id;
        private int spot_id;
        private String spot_name;
        private String content;
        private String time;
        private int day;

        public int gettravelrecordid() {return travel_record_id;}
        public int getspotid() {
            return spot_id;
        }
        public String getspotname() {return spot_name;}
        public String getcontent() {return content;}
        public String gettime() {return time;}
        public int getday() {return day;}

        public void settravelrecordid(int id) {travel_record_id = id;}
        public void setspotid(int id) {spot_id = id;}

        public void setspotname(String name) {spot_name = name;}
        public void setContent(String t) {content = t;}
        public void settime(String t) {time = t;}
        public void setday(int d) {day=d;}
    }

}
