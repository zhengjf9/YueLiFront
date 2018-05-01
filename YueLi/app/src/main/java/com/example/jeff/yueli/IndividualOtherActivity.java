package com.example.jeff.yueli;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by jeff on 18-3-9.
 */

public class IndividualOtherActivity extends AppCompatActivity {
    private List<DateInfo> dataInfoList = new ArrayList<>();//心情的数据
    private List<java.util.Map<String, String>> trashDatas =
            new ArrayList<java.util.Map<String, String>>();//草稿箱的数据
    private List<java.util.Map<String, String>> launchDatas =
            new ArrayList<java.util.Map<String, String>>();//已发布的数据
    private int mnum;
    private int tripnum;
    private int fannum;
    private MyApplication application;
    private OkHttpClient httpClient;
    private User user;
    private String otherid;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.individual_other);
        application = (MyApplication)getApplication();
        httpClient= application.gethttpclient();
        user = application.getUser();
        int tmp = (int)getIntent().getSerializableExtra("user_id");
        otherid = String.valueOf(tmp);
        if (tmp == user.getuserid()) {
            Intent i = new Intent();
            i.setClass(IndividualOtherActivity.this, MainActivity.class);
            i.putExtra("id", 4);
            startActivity(i);
        }

       // Toast.makeText(getApplicationContext(), otherid , Toast.LENGTH_SHORT).show();
       // initDatas();

        final RecyclerView myRecView = (RecyclerView) findViewById(R.id.outer_recyclerview);
        final DateInfoAdapter myAdapter = new DateInfoAdapter(this,dataInfoList);
        myAdapter.notifyDataSetChanged();
        myRecView.setLayoutManager(new LinearLayoutManager(this));
        myRecView.setAdapter(myAdapter);

        Button mood = findViewById(R.id.mood_title);
        Button journey = findViewById(R.id.journey_title);
        final TextView name = findViewById(R.id.name);
        final Button trash = findViewById(R.id.trash);
        final Button launch = findViewById(R.id.launch);
        Button like =findViewById(R.id.like);
        Button letter = findViewById(R.id.letter);
       // Button menu =findViewById(R.id.menu);
        final TextView moodnum =findViewById(R.id.mood_num);
        TextView tnum =findViewById(R.id.journey_num);
        TextView fans = findViewById(R.id.fans_num);
        final TextView sig = findViewById(R.id.signaure);
        final Button addattention = findViewById(R.id.add_attention);
        String url="http://123.207.29.66:3009/api/users/"+otherid;
        Request request = new Request.Builder().url(url).build();
        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }
            String string=null;
            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                try {
                    string = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Gson gson = new Gson();
                Type usertype = new TypeToken<Result<User>>(){}.getType();
                Result<User> userresult = gson.fromJson(string, usertype);
                final User other = userresult.data;
                final int rescode = response.code();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //  Toast.makeText(getActivity().getApplicationContext(), "TestRes", Toast.LENGTH_SHORT).show();
                        if (rescode == 200) {
                            sig.setText(other.getSignature());
                            name.setText(other.getnickname());
                            if (other.getfollowd()) {
                                addattention.setVisibility(View.INVISIBLE);
                            } else {
                                addattention.setVisibility(View.VISIBLE);
                            }
                            // Toast.makeText(getActivity().getApplicationContext(),"travel_id is " + String.valueOf(travellist.get(0).gettravelid())  , Toast.LENGTH_SHORT).show();
                        } else {
                           // Toast.makeText(getApplicationContext(), "try", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });


        trash.setVisibility(View.INVISIBLE);
        launch.setVisibility(View.INVISIBLE);

        final RecyclerView trashRecView = (RecyclerView)findViewById(R.id.trash_recyclerview);
        final JourneyItemAdapter trashAdapter = new JourneyItemAdapter(this, trashDatas);
        trashRecView.setLayoutManager(new LinearLayoutManager(this));
        trashRecView.setAdapter(trashAdapter);
        trashRecView.setVisibility(View.INVISIBLE);

        final RecyclerView launchRecView = (RecyclerView)findViewById(R.id.launch_recyclerview);
        final JourneyItemAdapter launchAdapter = new JourneyItemAdapter(this, launchDatas);
        initDatas(moodnum,fans,tnum,launchAdapter,trashAdapter,myAdapter);
        launchAdapter.setOnItemClickLitener(new OnItemClickLitener()
        {

            @Override
            public void onItemClick(View view, int position)
            {
                Intent intent = new Intent(IndividualOtherActivity.this, JourneyDetailActivity.class);
                intent.putExtra("From",0);
                intent.putExtra("travel_id",launchDatas.get(position).get("travel_id"));
                intent.putExtra("favorited",Boolean.valueOf(launchDatas.get(position).get("favorited")));
                startActivity(intent);
            }
            @Override
            public void onItemLongClick(View view, int position)
            {
                //Todo
                //myAdapter.removeData(position);
            }
        });
        launchRecView.setLayoutManager(new LinearLayoutManager(this));
        launchRecView.setAdapter(launchAdapter);
        launchRecView.setVisibility(View.INVISIBLE);

        addattention.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://123.207.29.66:3009/api/users/"+otherid+"/followers";
                FormBody formBody = new FormBody
                        .Builder()
                        .add("user_id",otherid)//设置参数名称和参数值
                        .build();
                Request request = new Request.Builder().post(formBody).url(url).build();
                httpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                    }
                    String string=null;
                    @Override
                    public void onResponse(Call call, final Response response) throws IOException {
                        try {
                            string = response.body().string();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        final int rescode = response.code();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //  Toast.makeText(getActivity().getApplicationContext(), "TestRes", Toast.LENGTH_SHORT).show();
                                if (rescode == 200) {
                                    addattention.setVisibility(View.INVISIBLE);
                                    //Toast.makeText(getActivity().getApplicationContext(),"travel_id is " + String.valueOf(travellist.get(0).gettravelid())  , Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), "关注失败", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });

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






    }
    public void initDatas(final TextView m, final TextView f, final TextView t,
                          final JourneyItemAdapter a, final JourneyItemAdapter b,final DateInfoAdapter c){


        String url = "http://123.207.29.66:3009/api/feelings?user_id="+String.valueOf(otherid) ;
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
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        c.notifyDataSetChanged();
                        int rescode = response.code();
                        if (rescode == 200) {

                            m.setText(String.valueOf(mnum));
                        }
                        // Toast.makeText(getActivity().getApplicationContext(), "心情"+moodlist.size(), Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });

        url="http://123.207.29.66:3009/api/travels?user_id="+String.valueOf(otherid);
        request = new Request.Builder().url(url).build();
        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }
            String string=null;
            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                try {
                    string = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Gson gson = new Gson();
                Travel result = gson.fromJson(string,Travel.class);
                final List<Travel.trip> travellist =  result.gettrips();

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
                final int rescode = response.code();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        a.notifyDataSetChanged();
                        //  Toast.makeText(getActivity().getApplicationContext(), "TestRes", Toast.LENGTH_SHORT).show();
                        if (rescode == 200) {
                            tripnum = travellist.size();
                            t.setText(String.valueOf(tripnum));
                            // Toast.makeText(getActivity().getApplicationContext(),"travel_id is " + String.valueOf(travellist.get(0).gettravelid())  , Toast.LENGTH_SHORT).show();
                        } else {
                          //  Toast.makeText(getApplicationContext(), "try", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        //获取粉丝数量
        url = "http://123.207.29.66:3009/api/users/"+String.valueOf(otherid)+"/followers";
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
                runOnUiThread(new Runnable() {
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
        b.notifyDataSetChanged();

    }


}
