package com.example.jeff.yueli;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.*;
import java.util.Map;

/**
 * Created by jeff on 18-3-4.
 */

public class JourneyDetailActivity extends AppCompatActivity

{
    public List<java.util.Map<String, String>> dateDatas =
            new ArrayList<java.util.Map<String, String>>();//游记开头日期
    public List<java.util.Map<String, String>> contentDatas =
            new ArrayList<java.util.Map<String, String>>();//游记内容
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.journey_detail);
        initData();
    }
    public void initData(){
        //获取网络数据,按照注释中的格式
        //java.util.Map<String, String> temp = new LinkedHashMap<String, String>();
        //temp.put("day_num", "Day1");
        //temp.put("date","2018年3月1日");
        //temp.put("week", "周四");
        //dateDatas.add(temp);
        //Map<String, String> temp1 = new LinkedHashMap<String, String>();
        //temp1.put("word", "啦啦啦啦啦啦");
       // temp1.put("location","上海，中国");
        //contentDatas.add(temp1);
    }

}
