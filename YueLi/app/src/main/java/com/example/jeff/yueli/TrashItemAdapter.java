package com.example.jeff.yueli;

import android.content.Context;
import android.content.Intent;
import android.graphics.ColorSpace;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.util.List;

/**
 * Created by jeff on 18-3-7.
 */

public class TrashItemAdapter extends RecyclerView.Adapter<TrashItemAdapter.MyViewHolder>
{

    private Context context;
    private List<java.util.Map<String, String>> mDatas;

    public TrashItemAdapter(Context context, List<java.util.Map<String, String>>  list) {
        this.context = context;
        mDatas = list;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        MyViewHolder holder = new MyViewHolder(
                LayoutInflater.from(context).inflate(R.layout.trash_item, parent,
                        false));
        return holder;
    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener)
    {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    public void removeData(int position, int id) {
        Connector.getDatabase();
        DataSupport.delete(trashJournalItem.class, id);
        mDatas.remove(position);
        //删除动画
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position)
    {
        holder.title.setText(mDatas.get(position).get("title"));
        holder.firstday.setText(mDatas.get(position).get("firstday"));
        holder.duration.setText(mDatas.get(position).get("duration"));
        holder.location.setText(mDatas.get(position).get("location"));
        holder.id = Integer.parseInt(mDatas.get(position).get("travel_id"));
        final int tid = holder.id;
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
        holder.del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDatas.size() == -1) {
                    Snackbar.make(v, "此条目不能删除", Snackbar.LENGTH_SHORT).show();
                } else {

                    removeData(position, tid);
                }
            }
        });
    }



    @Override
    public int getItemCount()
    {
        return mDatas.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder  {

        TextView title;
        TextView firstday;
        TextView duration;
        TextView location;
        int id;
        Button del;
        /*
        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.trash){
                mDatas.remove(getAdapterPosition());
                notifyItemRemoved(getAdapterPosition());
            }else{
                // Toast.makeText(context,"item"+getAdapterPosition(),Toast.LENGTH_SHORT).show();
            }
        }*/


        public MyViewHolder(View view)
        {
            super(view);
           // view.setOnClickListener(this);
          //  View button = view.findViewById(R.id.edit);
          //  button.setOnClickListener(this);
          //  View btn = view.findViewById(R.id.trash);
          //  btn.setOnClickListener(this);
            title = (TextView) view.findViewById(R.id.title);
            firstday = (TextView) view.findViewById(R.id.firstday);
            duration = (TextView) view.findViewById(R.id.duration);
            location = (TextView) view.findViewById(R.id.location);
            del =(Button)view.findViewById(R.id.trash);


        }
    }
}
