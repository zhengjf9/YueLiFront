package com.example.jeff.yueli;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by jeff on 18-3-4.
 */

public class JourneyDetailActivity extends AppCompatActivity

{
    public List<java.util.Map<String, String>> dateDatas =
            new ArrayList<java.util.Map<String, String>>();//游记开头日期
    public List<java.util.Map<String, String>> contentDatas =
            new ArrayList<java.util.Map<String, String>>();//游记内容
    private List<ParentInfo> dataInfoList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.journey_detail);
        String t = (String)getIntent().getSerializableExtra("travel_id");

        initData(t);
        final RecyclerView myRecView = (RecyclerView) findViewById(R.id.outer_recyclerview);
        final ParentInfoAdapter myAdapter = new ParentInfoAdapter(this,dataInfoList);
        myRecView.setLayoutManager(new LinearLayoutManager(this));
        myRecView.setAdapter(myAdapter);
        Button back = (Button)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent();
                i.setClass(JourneyDetailActivity.this, MainActivity.class);
                //一定要指定是第几个pager，因为要跳到ThreeFragment，这里填写2
                i.putExtra("id",3);
                startActivity(i);
            }
        });
    }
    public void initData(String t){
//        MyApplication application = (MyApplication)getApplication();
//
//        OkHttpClient httpClient = application.gethttpclient();
//        User user = application.getUser();
//        getIntent().getSerializableExtra("book");
//        String url="http://123.207.29.66:3009/api/travels/"+t+"/travel-records";
//        Request request = new Request.Builder().url(url).build();
//
//        httpClient.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//            }
//            String string=null;
//            @Override
//            public void onResponse(Call call, final Response response) throws IOException {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        //  Toast.makeText(getActivity().getApplicationContext(), "TestRes", Toast.LENGTH_SHORT).show();
//                        try {
//                            string = response.body().string();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                        Gson gson = new Gson();
//                        record result = gson.fromJson(string,record.class);
//                        List<record.Rec> travellist =  result.gettrips();
//
//                        for (int i = 0; i < travellist.size(); i++) {
//                            record.Rec t = travellist.get(i);
//                            Map<String, String> temp = new LinkedHashMap<String, String>();
//                            temp.put("day_num", "Day"+String.valueOf(t.getday()));
//                            temp.put("date",t.gettime());
//                            SimpleDateFormat sdf =   new SimpleDateFormat( "yyyy-MM-dd" );
//                            Calendar calendar = Calendar.getInstance();
//                            try {
//                                Date date = sdf.parse(travellist.get(0).gettime().substring(0,10));
//                                calendar.setTime(date);
//                            } catch (ParseException e) {
//                                e.printStackTrace();
//                            }
//                            String[] dayofweek = new String[] { "周日", "周一", "周二", "周三", "周四",
//                                    "周五", "周六" };
//                            String wd=dayofweek[calendar.get(Calendar.DAY_OF_WEEK)-1];
//                            temp.put("week", wd);
//                            dateDatas.add(temp);
//
//                            Map<String, String> temp1 = new LinkedHashMap<String, String>();
//                            temp1.put("word", t.getcontent());
//                            temp1.put("location",t.getspotname());
//                            contentDatas.add(temp1);
//                        }
//
//                        int rescode = response.code();
//
//                        if (rescode == 200) {
//                             Toast.makeText(getApplicationContext(),result.getmsg() , Toast.LENGTH_SHORT).show();
//                        } else {
//                            Toast.makeText(getApplicationContext(), result.getmsg() , Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//            }
//        });
        //举个栗子
        for (int i = 0; i < 6; i++) {//比如总共6天
            ParentInfo parentInfo = new ParentInfo();//指的是某一天的标题，包括第几天，日期，星期。
            parentInfo.setDay_num("Day1");
            parentInfo.setDate("2018年1月1日");
            parentInfo.setWeek("周四");
            List<ChildInfo> childInfoList = new ArrayList<>();//指的是这一天所有的游记
            for (int j = 0; j < 3; j++) {//比如说，每一天都发了3条
                ChildInfo childInfo = new ChildInfo();
                childInfo.setWord("啦啦啦啦");//childInfo指一条游记
                childInfo.setLocation("上海，中国");
                childInfoList.add(childInfo);
            }
            parentInfo.setItemList(childInfoList);//将这一天的所有游记设置成标题的一个成员
            dataInfoList.add(parentInfo);//将一天一天的数据push进dataInfoList
                                         // dataInfoList就是最后要的数据
        }
    }

}
