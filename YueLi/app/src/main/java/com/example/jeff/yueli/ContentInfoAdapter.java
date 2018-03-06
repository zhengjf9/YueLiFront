package com.example.jeff.yueli;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by jeff on 18-3-7.
 */

public class ContentInfoAdapter extends RecyclerView.Adapter<ContentInfoAdapter.ViewHolder> {

    private Context context;
    private List<ContentInfo> list;

    /**
     * 构造函数
     * @param context
     * @param list
     */
    public ContentInfoAdapter(Context context, List<ContentInfo> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.individual_mood_content, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ContentInfo info = list.get(position);
        holder.location.setText(info.getLocation());
        holder.comment.setText(info.getComment());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    /**
     * static ViewHolder
     */
    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView location;
        TextView comment;

        public ViewHolder(View itemView) {
            super(itemView);
            location = (TextView) itemView.findViewById(R.id.location);
            comment = (TextView) itemView.findViewById(R.id.comment);
        }
    }


}
