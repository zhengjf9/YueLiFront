package com.example.jeff.yueli;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by jeff on 18-3-7.
 */

public class DateInfoAdapter extends RecyclerView.Adapter<DateInfoAdapter.ViewHolder> {

    private Context context;
    private List<DateInfo> list;//父层列表 （里面是 text + 子List（子list是image+text））

    public DateInfoAdapter(Context context, List<DateInfo> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.individual_mood_date, parent, false);
        return new ViewHolder(view);

    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.date.setText(list.get(position).getDate());
        //把内层的RecyclerView 绑定在外层的onBindViewHolder
        // 先判断一下是不是已经设置了Adapter
        if (holder.mRecyclerView.getAdapter() == null) {
            holder.mRecyclerView.setAdapter(new ContentInfoAdapter(context, list.get(position).getContentInfoList()));
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

        TextView date;
        RecyclerView mRecyclerView; // 父层的 RecyclerView

        public ViewHolder(View itemView) {
            super(itemView);
            date = (TextView) itemView.findViewById(R.id.date);
            mRecyclerView = (RecyclerView) itemView.findViewById(R.id.inner_recyclerview);
            RecyclerView.LayoutManager manager = new LinearLayoutManager(itemView.getContext());
            // 需要注意的是GridLayoutManager要设置setAutoMeasureEnabled(true)成自适应高度
            manager.setAutoMeasureEnabled(true);
            mRecyclerView.setLayoutManager(manager);
            mRecyclerView.setFocusable(false);
        }
    }
}