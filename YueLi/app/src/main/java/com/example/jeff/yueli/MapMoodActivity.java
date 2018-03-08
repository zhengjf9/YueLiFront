package com.example.jeff.yueli;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.*;

/**
 * Created by jeff on 18-3-8.
 */

public class MapMoodActivity extends AppCompatActivity {
    public List<java.util.Map<String, String>> mDatas =
            new ArrayList<java.util.Map<String, String>>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_mood);
        initDatas();

        final RecyclerView myRecView = (RecyclerView) findViewById(R.id.my_recyclerview);
        final CommentItemAdapter myAdapter = new CommentItemAdapter(this, mDatas);
        myRecView.setLayoutManager(new LinearLayoutManager(this));
        myRecView.setAdapter(myAdapter);
    }
    public void initDatas(){
        java.util.Map<String, String> temp = new LinkedHashMap<String, String>();
        temp.put("name", "小明");
        temp.put("date", "2017-8-9");
        temp.put("content", "写得好好");
        mDatas.add(temp);
        mDatas.add(temp);
        mDatas.add(temp);
        mDatas.add(temp);
        mDatas.add(temp);
        mDatas.add(temp);
    }
}
