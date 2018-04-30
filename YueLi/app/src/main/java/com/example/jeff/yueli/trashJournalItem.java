package com.example.jeff.yueli;

import org.litepal.crud.DataSupport;

/**
 * Created by xumuxin on 2018/4/28.
 */

public class trashJournalItem extends DataSupport {
    private long id;
    private int user_id;
    private int travel_id;
    private String title;
    private String first_day;
    private String location;
    private int duration;
    //private int user_id;
    //private String nickname;
   // private int favorite_count;
   //private boolean favorited;
   // private int comment_count;
    public long getid() {return id;}
    public int getUser_id() {
        return user_id;
    }
    public int gettravelid() {
        return travel_id;
    }
    public String gettitle() {
        return title;
    }
    public String getFirst_day() {
        return first_day;
    }
    public String getlocation() {
        return location;
    }
    public int getduration() {
        return duration;
    }
    public void setuser_id(int id) {
        user_id = id;
    }
    public void settravelid(int id) {
        travel_id = id;
    }
    public void settitle(String t) {
        title = t;
    }
    public void setfirstday(String day) {
        first_day = day;
    }
    public void setlocation(String locate) {
        location = locate;
    }

    public void setduration(int d) {
        duration = d;
    }

   // public int getuserid() {return user_id;}

  /*  public String getnickname() {
        return nickname;
    }





    public int getfavoritecount() {
        return favorite_count;
    }

    public boolean getfavorited() {
        return favorited;
    }

    public int getComment_count() {
        return comment_count;
    }

    */



/*    public void setuserid(int id) {user_id = id;}


    public void setnickname(String name) {
        nickname = name;
    }



    public void setfavoritecount(int count) {
        favorite_count = count;
    }

    public void setfavorited(boolean favor) {
        favorited = favor;
    }

    public void setcommentcount(int count) {
        comment_count = count;
    }
*/

}
