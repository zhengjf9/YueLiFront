package com.example.jeff.yueli;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.*;
import java.util.Map;

/**
 * Created by jeff on 18-3-8.
 */

class CommentItemAdapter extends RecyclerView.Adapter<CommentItemAdapter.MyViewHolder>
{

    private Context context;
    private List<java.util.Map<String, String>> mDatas;

    public CommentItemAdapter(Context context, List<java.util.Map<String, String>>  list) {
        this.context = context;
        mDatas = list;
    }

    public List<Map<String, String>> getmDatas() {
        return mDatas;
    }

    public void setmDatas(List<Map<String, String>> mDatas) {
        this.mDatas = mDatas;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        MyViewHolder holder = new MyViewHolder(
                LayoutInflater.from(context).inflate(R.layout.comment_item, parent,
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
        holder.name.setText(mDatas.get(position).get("name"));
        holder.date.setText(mDatas.get(position).get("date"));
        holder.content.setText(mDatas.get(position).get("content"));
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

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView name;
        TextView date;
        TextView content;

        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.avator){
                Intent intent = new Intent(v.getContext(), IndividualOtherActivity.class);
                intent.putExtra("user_id",Integer.parseInt(mDatas.get(getAdapterPosition()).get("user_id")));
                //intent.putExtra("favorited",Boolean.valueOf(mDatas.get(position).get("favorited")));
                v.getContext().startActivity(intent);
            }else{
                Toast.makeText(context,"item"+getAdapterPosition(),Toast.LENGTH_SHORT).show();
            }
        }

        public MyViewHolder(View view)
        {
            super(view);
            view.setOnClickListener(this);
            View button = view.findViewById(R.id.avator);
            button.setOnClickListener(this);
            name = (TextView) view.findViewById(R.id.name);
            date = (TextView) view.findViewById(R.id.date);
            content = (TextView) view.findViewById(R.id.content);
        }
    }
}
