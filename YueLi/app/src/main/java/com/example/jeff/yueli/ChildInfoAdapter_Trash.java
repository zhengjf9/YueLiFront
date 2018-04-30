package com.example.jeff.yueli;

import android.content.Context;
import android.content.Intent;
import android.graphics.ColorSpace;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.widget.RecyclerView;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.util.List;

/**
 * Created by jeff on 18-3-5.
 */

public class ChildInfoAdapter_Trash extends RecyclerView.Adapter<ChildInfoAdapter_Trash.ViewHolder> {

    private Context context;
    private List<ChildInfo> listchild;

    /**
     * 构造函数
     * @param context
     * @param list
     */
    public ChildInfoAdapter_Trash(Context context, List<ChildInfo> list) {
        this.context = context;
        this.listchild = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.trash_journey_content, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        ChildInfo info = listchild.get(position);
        holder.word.setText(info.getWord());
        holder.location.setText(info.getLocation());
        holder.id = info.getid();
        final int tid= holder.id;
        holder.del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listchild.size() == 1) {
                    Snackbar.make(v, "此条目不能删除", Snackbar.LENGTH_SHORT).show();
                } else {
                    MyApplication application = (MyApplication)v.getContext().getApplicationContext();
                    application.curTrashRecords.remove(tid);
                    removeData(position);
                }
            }
        });
    }
    public void removeData(int position) {

        listchild.remove(position);
        //删除动画
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return listchild.size();
    }

    /**
     * static ViewHolder
     */
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView word;
        TextView location;
        ImageView edit;
        ImageView trash;
        Button del;
        int id;


        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.trash){
              // MyApplication application =(MyApplication)v.getContext().getApplicationContext();
                listchild.remove(getAdapterPosition());
                notifyItemRemoved(getAdapterPosition());
               // application.curTrashRecords.remove(id);
               //
                Log.i("id is: ", "id is: "+String.valueOf(id));
            }else{
                // Toast.makeText(context,"item"+getAdapterPosition(),Toast.LENGTH_SHORT).show();
            }
        }

        public ViewHolder(View itemView) {
            super(itemView);
            word = (TextView) itemView.findViewById(R.id.word);
            location = (TextView) itemView.findViewById(R.id.location);
            del = (Button) itemView.findViewById(R.id.trash);


        }
    }


}