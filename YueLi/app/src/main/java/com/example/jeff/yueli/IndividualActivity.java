package com.example.jeff.yueli;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by XDDN2 on 2018/3/2.
 */

public class IndividualActivity extends Fragment {
    private List<DateInfo> dataInfoList = new ArrayList<>();
    public IndividualActivity() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_individual, container, false);
        initDatas();
        final RecyclerView myRecView = (RecyclerView) view.findViewById(R.id.outer_recyclerview);
        final DateInfoAdapter myAdapter = new DateInfoAdapter(getContext(),dataInfoList);
        myRecView.setLayoutManager(new LinearLayoutManager(getContext()));
        myRecView.setAdapter(myAdapter);
        return view;
    }
    public void initDatas(){
        MyApplication application = (MyApplication) getActivity().getApplication();

        OkHttpClient httpClient = application.gethttpclient();
        final User user = application.getUser();

        String url = "http://123.207.29.66:3009/api/feelings?user_id="+String.valueOf(user.getuserid()) ;
        Request request = new Request.Builder().url(url).build();


        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            String string = null;

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            string = response.body().string();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        int rescode = response.code();
                        if (rescode == 200) {
                            Toast.makeText(getActivity().getApplicationContext(), string, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity().getApplicationContext(),
                                    "获取失败失败啊啊"+ String.valueOf(user.getuserid()), Toast.LENGTH_SHORT).show();
                        }

                        Gson gson = new Gson();
                        Mood result = gson.fromJson(string, Mood.class);
                        List<Mood.xinqing> moodlist = result.getdata();

                        for (int i = 0; i < moodlist.size(); ) {
                            Mood.xinqing t = moodlist.get(i);
                            DateInfo dateInfo = new DateInfo();//指的是某一天的标题，包括第几天，日期，星期。

                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            Calendar calendar = Calendar.getInstance();
                            Date date = null;
                            try {
                                date = sdf.parse(t.gettime().substring(0, 10));
                                calendar.setTime(date);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            dateInfo.setDate(t.gettime().substring(0, 10));
                            List<ContentInfo> contentInfoList = new ArrayList<>();//指的是这一天所有的心情
                            Date tmpdate = date;
                            while (tmpdate == date) {
                                ContentInfo contentInfo = new ContentInfo();
                                String location = String.valueOf(t.getlatitude()) + ", " + String.valueOf(t.getlongitude());
                                contentInfo.setLocation(location);//contentInfo指一条心情
                                contentInfo.setComment(t.getcontent());
                                contentInfoList.add(contentInfo);
                                i++;
                                if (i < moodlist.size()) {
                                    t = moodlist.get(i);
                                } else break;
                            }

                            dateInfo.setContentInfoList(contentInfoList);//将这一天的所有游记设置成标题的一个成员
                            dataInfoList.add(dateInfo);;//将一天一天的数据push进dataInfoList
                            // dataInfoList就是最后要的数据
                        }


                    }
                });
            }
        });
/*
        for (int i = 0; i < 6; i++) {//比如总共6天
            DateInfo dateInfo = new DateInfo();//指的是某一天的标题，包括日期。
            dateInfo.setDate("一月二十四日");
            List<ContentInfo> contentInfoList = new ArrayList<>();//指的是这一天所有的心情
            for (int j = 0; j < 3; j++) {//比如说，每一天都发了3条
                ContentInfo contentInfo = new ContentInfo();
                contentInfo.setLocation("上海，中国");//contentInfo指一条心情
                contentInfo.setComment("啦啦啦啦");
                contentInfoList.add(contentInfo);
            }
            dateInfo.setContentInfoList(contentInfoList);//将这一天的所有心情设置成标题的一个成员
            dataInfoList.add(dateInfo);//将一天一天的数据push进dataInfoList
            // dataInfoList就是最后要的数据
        }*/
    }
}
