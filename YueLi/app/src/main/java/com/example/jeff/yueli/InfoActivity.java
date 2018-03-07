package com.example.jeff.yueli;

import android.os.Bundle;
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
 * Created by jeff on 18-3-7.
 */

public class InfoActivity extends AppCompatActivity {
    public List<java.util.Map<String, String>> mDatas =
            new ArrayList<java.util.Map<String, String>>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.individual_info);
        initDatas();
        final RecyclerView myRecView = (RecyclerView) findViewById(R.id.my_recyclerview);
        final HomeAdapter myAdapter = new HomeAdapter();
        myRecView.setLayoutManager(new LinearLayoutManager(this));
        myRecView.setAdapter(myAdapter);
    }
    public void initDatas(){
        Map<String, String> temp = new LinkedHashMap<String, String>();
        temp.put("info", "您被某人");
        mDatas.add(temp);
        mDatas.add(temp);
    }
    class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> {

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                    InfoActivity.this).inflate(R.layout.info_item, parent,
                    false));
            return holder;
        }

        private OnItemClickLitener mOnItemClickLitener;

        public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
            this.mOnItemClickLitener = mOnItemClickLitener;
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position) {
            holder.info.setText(mDatas.get(position).get("info"));
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
            mDatas.remove(position);
            notifyItemRemoved(position);
        }

        @Override
        public int getItemCount() {
            return mDatas.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {

            TextView info;

            public MyViewHolder(View view) {
                super(view);
                info = (TextView) view.findViewById(R.id.info);
            }
        }
    }
}
