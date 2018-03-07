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

public class AttentionCollectActivity extends AppCompatActivity {
    public List<java.util.Map<String, String>> mDatas =
            new ArrayList<java.util.Map<String, String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.attention_and_collect);
        final String t = (String) getIntent().getSerializableExtra("travel_id");

        initData();
        final RecyclerView myRecView = (RecyclerView) findViewById(R.id.my_recyclerview);
        final HomeAdapter myAdapter = new HomeAdapter();
        myRecView.setLayoutManager(new LinearLayoutManager(this));
        myRecView.setAdapter(myAdapter);

    }
     public void initData(){
         Map<String, String> temp = new LinkedHashMap<String, String>();
         temp.put("name", "旅行者");
         temp.put("signature", "我是个性签名");
         mDatas.add(temp);
         mDatas.add(temp);
     }

    class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> {

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                    AttentionCollectActivity.this).inflate(R.layout.attention_person_item, parent,
                    false));
            return holder;
        }

        private OnItemClickLitener mOnItemClickLitener;

        public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
            this.mOnItemClickLitener = mOnItemClickLitener;
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position) {
            holder.name.setText(mDatas.get(position).get("name"));
            holder.signature.setText(mDatas.get(position).get("signature"));
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

            TextView name;
            TextView signature;

            public MyViewHolder(View view) {
                super(view);
                name = (TextView) view.findViewById(R.id.name);
                signature = (TextView) view.findViewById(R.id.signature);
            }
        }
    }
}
