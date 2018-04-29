package com.example.jeff.yueli;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import org.litepal.LitePal;
import org.litepal.tablemanager.Connector;

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
        //LitePal.initialize(this);
        Connector.getDatabase();
       // trashJournalItem test = new trashJournalItem();
       // test.save();

        final RecyclerView myRecView = (RecyclerView)findViewById(R.id.my_recyclerview);
        final TrashItemAdapter myAdapter = new TrashItemAdapter(this, mDatas);
        initData(myAdapter);
        myAdapter.notifyDataSetChanged();
        myRecView.setLayoutManager(new LinearLayoutManager(this));
        myRecView.setAdapter(myAdapter);

        Button edit = (Button)findViewById(R.id.plus_icon);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(getActivity(), PostTrip.class);
                Intent intent = new Intent(TrashActivity.this, PostTrip.class);
                startActivity(intent);
            }
        });
    }
    public void initData(final TrashItemAdapter adapter){
        //跟TripActivity.java类似，但是从本地读取数据
        //四个数据，title，firstday， duration， location

    }
}
