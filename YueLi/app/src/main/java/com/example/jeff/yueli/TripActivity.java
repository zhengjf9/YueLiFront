package com.example.jeff.yueli;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.*;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by XDDN2 on 2018/3/2.
 */

public class TripActivity extends Fragment {
    public List<java.util.Map<String, String>> mDatas =
            new ArrayList<java.util.Map<String, String>>();
    public TripActivity() {


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_trip, container,false);
        mDatas.clear();


        final RecyclerView myRecView = (RecyclerView)view.findViewById(R.id.my_recyclerview);
        final JourneyItemAdapter myAdapter = new JourneyItemAdapter(getContext(), mDatas);
        initData(myAdapter);
        myAdapter.notifyDataSetChanged();
        myRecView.setLayoutManager(new LinearLayoutManager(getActivity()));
        myRecView.setAdapter(myAdapter);
        //添加游记按钮
        Button edit = view.findViewById(R.id.plus_icon);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(getActivity(), PostTrip.class);
                Intent intent = new Intent(getActivity(), TrashActivity.class);
                startActivity(intent);
            }
        });
        myAdapter.setOnItemClickLitener(new OnItemClickLitener()
        {

            @Override
            public void onItemClick(View view, int position)
            {
                Intent intent = new Intent(getActivity(), JourneyDetailActivity.class);
                intent.putExtra("travel_id",mDatas.get(position).get("travel_id"));
                intent.putExtra("favorited",Boolean.valueOf(mDatas.get(position).get("favorited")));
                startActivity(intent);
            }
            @Override
            public void onItemLongClick(View view, int position)
            {
               //Todo
                //myAdapter.removeData(position);
            }
        });
        return view;
    }
    public void initData(final JourneyItemAdapter adapter){
        //获取网络数据
        MyApplication application = (MyApplication)getActivity().getApplication();
        OkHttpClient httpClient = application.gethttpclient();
        User user = application.getUser();
      //  Toast.makeText(getActivity().getApplicationContext(),String.valueOf(user.getuserid())  , Toast.LENGTH_LONG).show();
        String url="http://123.207.29.66:3009/api/travels";//?user_id="+String.valueOf(user.getuserid());
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
                Travel result = gson.fromJson(string,Travel.class);
                List<Travel.trip> travellist =  result.gettrips();

                for (int i = 0; i < travellist.size(); i++) {
                    Travel.trip t = travellist.get(i);
                    Map<String, String> temp = new LinkedHashMap<String, String>();
                    temp.put("user_id",String.valueOf(t.getuserid()));
                    temp.put("title", t.gettitle());
                    temp.put("firstday", t.getFirst_day());
                    temp.put("duration",  String.valueOf(t.getduration()));
                    temp.put("location", t.getlocation());
                    temp.put("name", t.getnickname());

                    temp.put("like_num", String.valueOf(t.getfavoritecount()));
                    temp.put("comment_num", String.valueOf(t.getComment_count()));

                    temp.put("travel_id",String.valueOf(t.gettravelid()));
                    temp.put("favorited",String.valueOf(t.getfavorited()));
                    mDatas.add(temp);

                }

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                      //  Toast.makeText(getActivity().getApplicationContext(), "TestRes", Toast.LENGTH_SHORT).show();
                        int rescode = response.code();
                        if (rescode == 200) {
                            adapter.notifyDataSetChanged();
                           // Toast.makeText(getActivity().getApplicationContext(),"travel_id is " + String.valueOf(travellist.get(0).gettravelid())  , Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity().getApplicationContext(), "try", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }
}
