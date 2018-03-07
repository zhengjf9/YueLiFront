package com.example.jeff.yueli;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

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
        }
    }
}
