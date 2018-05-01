package com.example.jeff.yueli;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map;

/**
 * Created by jeff on 18-4-28.
 */

public class TrashActivity extends AppCompatActivity {
    public List<java.util.Map<String, String>> mDatas =
            new ArrayList<java.util.Map<String, String>>();
    SimpleDateFormat formatter   =   new   SimpleDateFormat   ("yyyy年MM月dd日");
    private Date findDate(String str) {
        Calendar calendar = Calendar.getInstance();
        Date date = new Date();
        try {
            date = formatter.parse(str);
            calendar.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.journey_trash);


       // trashJournalItem test = new trashJournalItem();
       // test.save();

        final RecyclerView myRecView = (RecyclerView)findViewById(R.id.my_recyclerview);

        final TrashItemAdapter myAdapter = new TrashItemAdapter(this, mDatas);
        initData(myAdapter);
        myAdapter.notifyDataSetChanged();
        myRecView.setLayoutManager(new LinearLayoutManager(this));
        myRecView.setAdapter(myAdapter);

        myAdapter.setOnItemClickLitener(new OnItemClickLitener()
        {

            @Override
            public void onItemClick(View view, int position)
            {
                Intent intent = new Intent(TrashActivity.this, PostTrip.class);
                intent.putExtra("From",2);
                intent.putExtra("travel_id",mDatas.get(position).get("travel_id"));
                startActivity(intent);
            }
            @Override
            public void onItemLongClick(View view, int position)
            {
                //Todo
                //myAdapter.removeData(position);
            }
        });

        Button edit = (Button)findViewById(R.id.plus_icon);
        Button back = (Button)findViewById(R.id.back);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(getActivity(), PostTrip.class);
                Intent intent = new Intent(TrashActivity.this, PostTrip.class);
                intent.putExtra("From",0);
                //intent.putExtra("trashItemId",1);
                startActivity(intent);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setClass(TrashActivity.this, MainActivity.class);
                //一定要指定是第几个pager，因为要跳到ThreeFragment，这里填写2
                i.putExtra("id", 3);
                startActivity(i);
            }
        });
    }
    public void initData(final TrashItemAdapter adapter){
        //跟TripActivity.java类似，但是从本地读取数据
        //四个数据，title，firstday， duration， location
        MyApplication application = (MyApplication)getApplication();
        Connector.getDatabase();
        List<trashJournalItem> trashItem = DataSupport.where("user_id = ?",
                String.valueOf(application.getUser().getuserid())).find(trashJournalItem.class);
        for (int i = 0; i < trashItem.size(); i++) {
            Map<String, String> temp = new LinkedHashMap<String, String>();
            trashJournalItem t = trashItem.get(i);
            temp.put("user_id",String.valueOf(t.getUser_id()));
            temp.put("title", t.gettitle());
            temp.put("firstday", formatter.format(findDate(t.getFirst_day())));
            temp.put("duration",  String.valueOf(t.getduration()) + "天");
            temp.put("location", t.getlocation());
            temp.put("travel_id",String.valueOf(t.getid()));
            mDatas.add(temp);
        }

        /*Map<String, String> temp = new LinkedHashMap<String, String>();
       // temp.put("user_id",String.valueOf(t.getUser_id()));
        temp.put("title", "testing");
        temp.put("firstday", "testing");
        temp.put("duration",  String.valueOf(1));
        temp.put("location", "GZ");
     //   temp.put("travel_id",String.valueOf(t.getid()));
        mDatas.add(temp);*/


    }
}
