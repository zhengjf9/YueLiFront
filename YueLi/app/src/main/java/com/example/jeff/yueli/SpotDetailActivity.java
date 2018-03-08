package com.example.jeff.yueli;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.*;
import java.util.Map;

/**
 * Created by jeff on 18-3-8.
 */

public class SpotDetailActivity extends AppCompatActivity {
    public List<java.util.Map<String, String>> spotDatas =
            new ArrayList<java.util.Map<String, String>>();
    public List<java.util.Map<String, String>> shopDatas =
            new ArrayList<java.util.Map<String, String>>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.spot_detail);
        initDatas();
        final RecyclerView spotRecView = (RecyclerView)findViewById(R.id.left_recyclerview);
        final JourneyItemAdapter myAdapter2 = new JourneyItemAdapter(this, spotDatas);
        spotRecView.setLayoutManager(new LinearLayoutManager(this));
        spotRecView.setAdapter(myAdapter2);
        spotRecView.setVisibility(View.INVISIBLE);

        final RecyclerView myRecView = (RecyclerView) findViewById(R.id.right_recyclerview);
        final HomeAdapter myAdapter = new HomeAdapter();
        myRecView.setLayoutManager(new LinearLayoutManager(this));
        myRecView.setAdapter(myAdapter);

    }
    public void initDatas(){
        Map<String, String> temp1 = new LinkedHashMap<String, String>();
        temp1.put("title", "上海:梦中城");
        temp1.put("firstday", "2018-3-7");
        temp1.put("duration",  "3天");
        temp1.put("location", "上海");
        temp1.put("name", "旅行者");
        temp1.put("like_num", "99");
        temp1.put("comment_num", "99");
        spotDatas.add(temp1);
        spotDatas.add(temp1);

        Map<String, String> temp = new LinkedHashMap<String, String>();
        temp.put("name", "星巴克");
        temp.put("description", "咖啡店咖啡店");
        shopDatas.add(temp);
        shopDatas.add(temp);
        shopDatas.add(temp);
    }
    class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> {

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                    SpotDetailActivity.this).inflate(R.layout.shop_item, parent,
                    false));
            return holder;
        }

        private OnItemClickLitener mOnItemClickLitener;

        public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
            this.mOnItemClickLitener = mOnItemClickLitener;
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position) {
            holder.name.setText(shopDatas.get(position).get("name"));
            holder.description.setText(shopDatas.get(position).get("description"));
            if (mOnItemClickLitener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = holder.getLayoutPosition();
                        mOnItemClickLitener.onItemClick(holder.itemView, pos);
                    }
                });

                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        int pos = holder.getLayoutPosition();
                        mOnItemClickLitener.onItemLongClick(holder.itemView, pos);
                        return false;
                    }
                });
            }
        }

        public void removeData(int position) {
            shopDatas.remove(position);
            notifyItemRemoved(position);
        }

        @Override
        public int getItemCount() {
            return shopDatas.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {

            TextView name;
            TextView description;

            public MyViewHolder(View view) {
                super(view);
                name = (TextView) view.findViewById(R.id.name);
                description = (TextView) view.findViewById(R.id.description);
            }
        }
    }
}
