package com.example.jeff.yueli;

import android.content.Intent;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.lang.reflect.Modifier;
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
 * Created by XDDN2 on 2018/3/2.
 */

public class IndividualActivity extends Fragment {
    private List<DateInfo> dataInfoList = new ArrayList<>();
    private List<java.util.Map<String, String>> trashDatas =
            new ArrayList<java.util.Map<String, String>>();//草稿箱的数据
    private List<java.util.Map<String, String>> launchDatas =
            new ArrayList<java.util.Map<String, String>>();//已发布的数据
    private int mnum;
    private int tripnum;
    private int fannum;


    public IndividualActivity() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_individual, container, false);

        MyApplication application = (MyApplication) getActivity().getApplication();
        OkHttpClient httpClient = application.gethttpclient();
        final User user = application.getUser();
        final RecyclerView myRecView = (RecyclerView) view.findViewById(R.id.outer_recyclerview);
        final DateInfoAdapter myAdapter = new DateInfoAdapter(getContext(),dataInfoList);
        myRecView.setLayoutManager(new LinearLayoutManager(getContext()));
        myRecView.setAdapter(myAdapter);
       // myRecView.setVisibility(View.INVISIBLE);//心情页面暂时设置不可见
        Button mood = view.findViewById(R.id.mood_title);
        Button journey = view.findViewById(R.id.journey_title);
        TextView name = view.findViewById(R.id.name);
        final Button trash = view.findViewById(R.id.trash);
        final Button launch = view.findViewById(R.id.launch);
        Button like = view.findViewById(R.id.like);
        Button letter = view.findViewById(R.id.letter);
        Button menu = view.findViewById(R.id.menu);
        final TextView moodnum = view.findViewById(R.id.mood_num);
        TextView tnum = view.findViewById(R.id.journey_num);
        TextView fans = view.findViewById(R.id.fans_num);
        TextView sig = view.findViewById(R.id.signaure);
        sig.setText(user.getSignature());
        name.setText(user.getnickname());
        initDatas(moodnum,fans,tnum);

        trash.setVisibility(View.INVISIBLE);
        launch.setVisibility(View.INVISIBLE);
        final RecyclerView trashRecView = (RecyclerView)view.findViewById(R.id.trash_recyclerview);
        final JourneyItemAdapter trashAdapter = new JourneyItemAdapter(getContext(), trashDatas);
        trashRecView.setLayoutManager(new LinearLayoutManager(getContext()));
        trashRecView.setAdapter(trashAdapter);
        trashRecView.setVisibility(View.INVISIBLE);

        final RecyclerView launchRecView = (RecyclerView)view.findViewById(R.id.launch_recyclerview);
        final JourneyItemAdapter launchAdapter = new JourneyItemAdapter(getContext(), launchDatas);
        launchRecView.setLayoutManager(new LinearLayoutManager(getContext()));
        launchRecView.setAdapter(launchAdapter);
        launchRecView.setVisibility(View.INVISIBLE);

        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AttentionCollectActivity.class);
                startActivity(intent);

            }
        });
        letter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), InfoActivity.class);
                startActivity(intent);

            }
        });


        mood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trashRecView.setVisibility(View.INVISIBLE);
                launchRecView.setVisibility(View.INVISIBLE);
                myRecView.setVisibility(View.VISIBLE);
                trash.setVisibility(View.INVISIBLE);
                launch.setVisibility(View.INVISIBLE);

            }
        });
        journey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myRecView.setVisibility(View.INVISIBLE);
                trashRecView.setVisibility(View.INVISIBLE);
                launchRecView.setVisibility(View.VISIBLE);
                trash.setVisibility(View.VISIBLE);
                launch.setVisibility(View.VISIBLE);
            }
        });
        launch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myRecView.setVisibility(View.INVISIBLE);
                trashRecView.setVisibility(View.INVISIBLE);
                launchRecView.setVisibility(View.VISIBLE);
            }
        });
        trash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myRecView.setVisibility(View.INVISIBLE);
                launchRecView.setVisibility(View.INVISIBLE);
                trashRecView.setVisibility(View.VISIBLE);
            }
        });

        return view;
    }
    public void initDatas(final TextView m, final TextView f, final TextView t){
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
                                                    try {
                                                        string = response.body().string();
                                                    } catch (IOException e) {
                                                        e.printStackTrace();
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
                                                        if (response.code() == 200)
                                                            mnum = moodlist.size();

                                                        dateInfo.setContentInfoList(contentInfoList);//将这一天的所有游记设置成标题的一个成员
                                                        dataInfoList.add(dateInfo);
                                                        ;//将一天一天的数据push进dataInfoList
                                                        // dataInfoList就是最后要的数据
                                                    }
                                                    getActivity().runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {

                                                            int rescode = response.code();
                                                            if (rescode == 200) {

                                                                m.setText(String.valueOf(mnum));
                                                            }
                                                            // Toast.makeText(getActivity().getApplicationContext(), "心情"+moodlist.size(), Toast.LENGTH_SHORT).show();

                                                        }
                                                    });
                                                }
                                            });

        url="http://123.207.29.66:3009/api/travels?user_id="+String.valueOf(user.getuserid());
        request = new Request.Builder().url(url).build();
        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }
            String string=null;
            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //  Toast.makeText(getActivity().getApplicationContext(), "TestRes", Toast.LENGTH_SHORT).show();
                        try {
                            string = response.body().string();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Gson gson = new Gson();
                        Travel result = gson.fromJson(string,Travel.class);
                        List<Travel.trip> travellist =  result.gettrips();

                        for (int i = 0; i < travellist.size(); i++) {
                            Travel.trip t = travellist.get(i);
                            Map<String, String> temp = new LinkedHashMap<String, String>();
                            temp.put("title", t.gettitle());
                            temp.put("firstday", t.getFirst_day());
                            temp.put("duration",  String.valueOf(t.getduration()));
                            temp.put("location", t.getlocation());
                            temp.put("name", t.getnickname());

                            temp.put("like_num", String.valueOf(t.getfavoritecount()));
                            temp.put("comment_num", String.valueOf(t.getComment_count()));

                            temp.put("travel_id",String.valueOf(t.gettravelid()));
                            temp.put("favorited",String.valueOf(t.getfavorited()));
                            launchDatas.add(temp);
                        }
                        int rescode = response.code();
                        if (rescode == 200) {
                            tripnum = travellist.size();
                            t.setText(String.valueOf(tripnum));
                            // Toast.makeText(getActivity().getApplicationContext(),"travel_id is " + String.valueOf(travellist.get(0).gettravelid())  , Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity().getApplicationContext(), "try", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        //获取粉丝数量
        url = "http://123.207.29.66:3009/api/users/"+String.valueOf(user.getuserid())+"/followers";
        request = new Request.Builder().url(url).build();
        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            String string = null;

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                try {
                    string = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Gson gson = new Gson();
                        Followers result = gson.fromJson(string, Followers.class);
                        List<Followers.follow> followerlist = result.getdata();
                        fannum = followerlist.size();
                        f.setText(String.valueOf(fannum));
                    }
                });
            }
        });
        // 草稿箱
        java.util.Map<String, String> temp1 = new LinkedHashMap<String, String>();
        temp1.put("title", "上海:梦中城");
        temp1.put("firstday", "2018-3-7");
        temp1.put("duration",  "3天");
        temp1.put("location", "上海");
        temp1.put("name", "旅行者");
        temp1.put("like_num", "99");
        temp1.put("comment_num", "99");
        trashDatas.add(temp1);
        trashDatas.add(temp1);

    }
    public void showDialog(View view){
        final BottomSheetDialog dialog=new BottomSheetDialog(getContext());
        View dialogView= LayoutInflater.from(getContext())
                .inflate(R.layout.logout,null);
        TextView logout= (TextView) dialogView.findViewById(R.id.logout);
        TextView cancel= (TextView) dialogView.findViewById(R.id.cancel);

        logout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
        {
            Intent intent = new Intent(getContext(), LoginActivity.class);
            startActivity(intent);
            dialog.dismiss();
        }
        });
        cancel.setOnClickListener(new View.OnClickListener()
        {
            @Override public void onClick(View v)
        {
            dialog.dismiss();
        }
        }
        );
        dialog.setContentView(dialogView);
        dialog.show();
    }
}
