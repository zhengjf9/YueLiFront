package com.example.jeff.yueli;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jeff on 18-3-9.
 */

public class IndividualOtherActivity extends AppCompatActivity {
    private List<DateInfo> dataInfoList = new ArrayList<>();//心情的数据
    private List<java.util.Map<String, String>> trashDatas =
            new ArrayList<java.util.Map<String, String>>();//草稿箱的数据
    private List<java.util.Map<String, String>> launchDatas =
            new ArrayList<java.util.Map<String, String>>();//已发布的数据

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.individual_other);
        initDatas();

        final RecyclerView myRecView = (RecyclerView) findViewById(R.id.outer_recyclerview);
        final DateInfoAdapter myAdapter = new DateInfoAdapter(this,dataInfoList);
        myRecView.setLayoutManager(new LinearLayoutManager(this));
        myRecView.setAdapter(myAdapter);

        final RecyclerView trashRecView = (RecyclerView)findViewById(R.id.trash_recyclerview);
        final JourneyItemAdapter trashAdapter = new JourneyItemAdapter(this, trashDatas);
        trashRecView.setLayoutManager(new LinearLayoutManager(this));
        trashRecView.setAdapter(trashAdapter);

        final RecyclerView launchRecView = (RecyclerView)findViewById(R.id.launch_recyclerview);
        final JourneyItemAdapter launchAdapter = new JourneyItemAdapter(this, launchDatas);
        launchRecView.setLayoutManager(new LinearLayoutManager(this));
        launchRecView.setAdapter(launchAdapter);
    }
    public void initDatas(){
        //与individualactivity一样
    }

}
