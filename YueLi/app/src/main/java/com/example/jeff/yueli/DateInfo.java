package com.example.jeff.yueli;

import java.util.List;

/**
 * Created by jeff on 18-3-6.
 */

public class DateInfo {
    private String date;
    private List<ContentInfo> contentInfoList;

    public String getDate(){return date;}
    public List<ContentInfo> getContentInfoList(){return contentInfoList;}
    public void setDate(String Date){this.date = Date;}
    public void setContentInfoList(List<ContentInfo> C){this.contentInfoList = C;}
}
