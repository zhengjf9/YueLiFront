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

import java.lang.reflect.Array;
import java.util.*;
import java.util.Map;

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
        initData();
        final RecyclerView myRecView = (RecyclerView)view.findViewById(R.id.my_recyclerview);
        final HomeAdapter myAdapter = new HomeAdapter();
        myAdapter.setOnItemClickLitener(new OnItemClickLitener()
        {

            @Override
            public void onItemClick(View view, int position)
            {
                //Todo
            }

            @Override
            public void onItemLongClick(View view, int position)
            {
               //Todo
                //myAdapter.removeData(position);
            }
        });
        myRecView.setLayoutManager(new LinearLayoutManager(getActivity()));
        myRecView.setAdapter(myAdapter);
        return view;
    }
    public void initData(){
        //获取网络数据
        Map<String, String> temp = new LinkedHashMap<String, String>();
        temp.put("title", "上海:梦中城");
        temp.put("firstday","2018.3.1");
        temp.put("duration", "3天");
        temp.put("location", "中国，上海");
        temp.put("name", "旅行者");
        temp.put("like_num", "99");
        temp.put("comment_num", "99");
        mDatas.add(temp);
        Map<String, String> temp1 = new LinkedHashMap<String, String>();
        temp1.put("title", "广州:羊城");
        temp1.put("firstday","2018.3.1");
        temp1.put("duration","4天");
        temp1.put("location", "中国，广州");
        temp1.put("name", "旅行者");
        temp1.put("like_num", "99");
        temp1.put("comment_num", "99");
        mDatas.add(temp1);
    }



    class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder>
    {

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            MyViewHolder holder = new MyViewHolder(
                    LayoutInflater.from(getActivity()).inflate(R.layout.journey_item, parent,
                    false));
            return holder;
        }

        private OnItemClickLitener mOnItemClickLitener;

        public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener)
        {
            this.mOnItemClickLitener = mOnItemClickLitener;
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position)
        {
            holder.title.setText(mDatas.get(position).get("title"));
            holder.firstday.setText(mDatas.get(position).get("firstday"));
            holder.duration.setText(mDatas.get(position).get("duration"));
            holder.location.setText(mDatas.get(position).get("location"));
            holder.name.setText(mDatas.get(position).get("name"));
            holder.like_num.setText(mDatas.get(position).get("like_num"));
            holder.comment_num.setText(mDatas.get(position).get("comment_num"));
            if (mOnItemClickLitener != null)
            {
                holder.itemView.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        int pos = holder.getLayoutPosition();
                        mOnItemClickLitener.onItemClick(holder.itemView, pos);
                    }
                });

                holder.itemView.setOnLongClickListener(new View.OnLongClickListener()
                {
                    @Override
                    public boolean onLongClick(View v)
                    {
                        int pos = holder.getLayoutPosition();
                        mOnItemClickLitener.onItemLongClick(holder.itemView, pos);
                        return false;
                    }
                });
            }
        }

        public void removeData(int position) {
            mDatas.remove(position);
            notifyItemRemoved(position);
        }

        @Override
        public int getItemCount()
        {
            return mDatas.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder
        {

            TextView title;
            TextView firstday;
            TextView duration;
            TextView location;
            ImageView avator;
            TextView name;
            TextView like_num;
            TextView comment_num;

            public MyViewHolder(View view)
            {
                super(view);
                title = (TextView) view.findViewById(R.id.title);
                firstday = (TextView) view.findViewById(R.id.firstday);
                duration = (TextView) view.findViewById(R.id.duration);
                location = (TextView) view.findViewById(R.id.location);
                avator = (ImageView) view.findViewById(R.id.avator);
                name = (TextView) view.findViewById(R.id.name);
                like_num = (TextView) view.findViewById(R.id.like_num);
                comment_num = (TextView) view.findViewById(R.id.comment_num);
            }
        }
    }

}
