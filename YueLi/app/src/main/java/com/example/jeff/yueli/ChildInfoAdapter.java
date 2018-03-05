package com.example.jeff.yueli;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.widget.RecyclerView;

import java.util.List;

/**
 * Created by jeff on 18-3-5.
 */

public class ChildInfoAdapter extends RecyclerView.Adapter<ChildInfoAdapter.ViewHolder> {

    private Context context;
    private List<ChildInfo> list;

    /**
     * 构造函数
     * @param context
     * @param list
     */
    public ChildInfoAdapter(Context context, List<ChildInfo> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.journey_content, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ChildInfo info = list.get(position);
        holder.word.setText(info.getWord());
        holder.location.setText(info.getLocation());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    /**
     * static ViewHolder
     */
    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView word;
        TextView location;

        public ViewHolder(View itemView) {
            super(itemView);
            word = (TextView) itemView.findViewById(R.id.word);
            location = (TextView) itemView.findViewById(R.id.location);
        }
    }


}