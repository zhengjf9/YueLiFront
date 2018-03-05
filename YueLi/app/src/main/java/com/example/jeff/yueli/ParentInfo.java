package com.example.jeff.yueli;

import java.util.List;

/**
 * Created by jeff on 18-3-5.
 */

public class ParentInfo {
    private String day_num;
    private String date;
    private String week;

    private List<ChildInfo> itemList;

    public String getDay_num() {
        return day_num;
    }
    public String getDate() {
        return date;
    }
    public String getWeek() {
        return week;
    }
    public List<ChildInfo> getItemList() {
        return itemList;
    }

    public void setDay_num(String Day_num){this.day_num = Day_num;}
    public void setDate(String Date){this.date = Date;}
    public void setWeek(String Week){this.week = Week;}
    public void setItemList(List<ChildInfo> ItemList) {
        this.itemList = ItemList;
    }
}
