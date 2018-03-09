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

import java.util.List;

/**
 * Created by jeff on 18-3-7.
 */

public class JourneyItemAdapter extends RecyclerView.Adapter<JourneyItemAdapter.MyViewHolder>
{

    private Context context;
    private List<java.util.Map<String, String>> mDatas;

    public JourneyItemAdapter(Context context, List<java.util.Map<String, String>>  list) {
        this.context = context;
        mDatas = list;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        MyViewHolder holder = new MyViewHolder(
                LayoutInflater.from(context).inflate(R.layout.journey_item, parent,
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

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView title;
        TextView firstday;
        TextView duration;
        TextView location;
        ImageView avator;
        TextView name;
        TextView like_num;
        TextView comment_num;

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
