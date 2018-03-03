package com.example.jeff.yueli;

/**
 * Created by xumuxin on 2018/3/3.
 */

public class Travel {
    private int travel_id;
    private int user_id;
    private String nickname;
    private String title;
    private String first_day;
    private int favorite_count;
    private boolean favorited;
    private int comment_count;
    private String location;
    private int duration;

    public int gettravelid() {return travel_id;}
    public int getuserid() {
        return user_id;
    }
    public String getnickname() {return nickname;}
    public String gettitle() {return title;}
    public String getFirst_day() {return first_day;}
    public int getfavoritecount() {return favorite_count;}
    public boolean getfavorited() {return favorited;}
    public int getComment_count() {return comment_count;}
    public String getlocation() {return location;}
    public int getduration() {return duration;}

    public void settravelid(int id) {travel_id = id;}
    public void setuserid(int id) {user_id = id;}


    public void setnickname(String name) {nickname = name;}
    public void settitle(String t) {title = t;}
    public void setfirstday(String day) {first_day = day;}

    public void setfavoritecount(int count) {favorite_count = count;}
    public void setfavorited(boolean favor) {favorited = favor;}
    public void setcommentcount(int count) {comment_count = count;}
    public void setlocation(String locate) {location = locate;}
    public void setduration(int d) {duration = d;}

}
