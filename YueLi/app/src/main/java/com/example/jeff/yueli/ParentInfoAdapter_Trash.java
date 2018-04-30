package com.example.jeff.yueli;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.List;

import static org.litepal.LitePalApplication.getContext;

/**
 * Created by jeff on 18-3-5.
 */

public class ParentInfoAdapter_Trash extends RecyclerView.Adapter<ParentInfoAdapter_Trash.ViewHolder> {

    private Context context;
    private List<ParentInfo> list;//父层列表 （里面是 text + 子List（子list是image+text））

    public ParentInfoAdapter_Trash(Context context, List<ParentInfo> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.trash_date_item,null);
        return new ViewHolder(view);

    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MyApplication application = (MyApplication)getContext().getApplicationContext();
        holder.day_num.setText(list.get(position).getDay_num());
        holder.date.setText(list.get(position).getDate());
        holder.week.setText(list.get(position).getWeek());
       // System.out.println("lallalalalallalalalala"+position);
       // System.out.println("lalalalallala"+list.get(position).getDate());
       // System.out.println(list.get(position).getDate() == "2018年05月01日");
        holder.add.setVisibility(View.INVISIBLE);
        if (list.get(position).getDate().equals(application.tmptoday) ) {
            holder.add.setVisibility(View.VISIBLE);
        }
        //把内层的RecyclerView 绑定在外层的onBindViewHolder
        // 先判断一下是不是已经设置了Adapter
        if (holder.mRecyclerView.getAdapter() == null) {
            holder.mRecyclerView.setAdapter(new ChildInfoAdapter_Trash(context, list.get(position).getItemList()));
        } else {
            holder.mRecyclerView.getAdapter().notifyDataSetChanged();
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    /**
     * static ViewHolder
     */
    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView day_num;
        TextView date;
        TextView week;
        RecyclerView mRecyclerView; // 父层的 RecyclerView
        ImageView add;

        public ViewHolder(final View itemView) {
            super(itemView);
            day_num = (TextView) itemView.findViewById(R.id.day_num);
            date = (TextView) itemView.findViewById(R.id.date);
            week = (TextView) itemView.findViewById(R.id.week);
            mRecyclerView = (RecyclerView) itemView.findViewById(R.id.inner_recyclerview);
            add = (ImageView)itemView.findViewById(R.id.add);
            /*
            add = (ImageView)itemView.findViewById(R.id.add);
            add.setVisibility(View.INVISIBLE);
            MyApplication application = (MyApplication)getContext().getApplicationContext();
            if (true) {
               add.setVisibility(View.VISIBLE);
            }*/

            RecyclerView.LayoutManager manager = new LinearLayoutManager(itemView.getContext());
            // 需要注意的是GridLayoutManager要设置setAutoMeasureEnabled(true)成自适应高度
            manager.setAutoMeasureEnabled(true);
            mRecyclerView.setLayoutManager(manager);
            add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        v.findViewById(R.id.title);
                        Intent intent = new Intent(itemView.getContext(), addDay.class);
                        itemView.getContext().startActivity(intent);
                    }
                });

        }
    }
}
