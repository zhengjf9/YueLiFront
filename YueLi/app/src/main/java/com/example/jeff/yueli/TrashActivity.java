package com.example.jeff.yueli;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.*;

/**
 * Created by jeff on 18-4-28.
 */

public class TrashActivity extends AppCompatActivity {
    public List<java.util.Map<String, String>> mDatas =
            new ArrayList<java.util.Map<String, String>>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.journey_trash);

        final RecyclerView myRecView = (RecyclerView)findViewById(R.id.my_recyclerview);
        final JourneyItemAdapter myAdapter = new JourneyItemAdapter(this, mDatas);
        initData(myAdapter);
        myAdapter.notifyDataSetChanged();
        myRecView.setLayoutManager(new LinearLayoutManager(this));
        myRecView.setAdapter(myAdapter);
    }
    public void initData(final JourneyItemAdapter adapter){
        //跟TripActivity.java类似，但是从本地读取数据
    }
}
