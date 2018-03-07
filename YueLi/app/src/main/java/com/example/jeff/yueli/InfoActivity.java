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
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.*;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

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
        MyApplication application = (MyApplication) getApplication();
        OkHttpClient httpClient = application.gethttpclient();
        final User user = application.getUser();

        String url = "http://123.207.29.66:3009/api/notifications";
        Request request = new Request.Builder().url(url).build();

        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            String string = null;
            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                try {
                    string = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Gson gson = new Gson();
                final Notifications result = gson.fromJson(string, Notifications.class);
                 List<Notifications.tongzhi> tongzhilist = result.getdata();
                 int c = 0;
                for (int i = 0; i < tongzhilist.size(); i++) {
                    Notifications.tongzhi t = tongzhilist.get(i);
                    Map<String, String> temp = new LinkedHashMap<String, String>();
                    String xinxi = "";
                    if (t.gettype().equals("follow-user") ) {
                        xinxi = "您被" + t.getcontent().getSender() + "关注了";
                        c++;
                    }
                    else if (t.gettype().equals( "favorite-travel")) {
                        xinxi = "您的 " + String.valueOf(t.getcontent().getid()) +" 游记被" +t.getcontent().getSender() + "收藏了";
                        c++;
                    }
                    else if (t.gettype().equals("comment-travel")) {
                        xinxi = "您的 " + t.getcontent().getname() +" 游记被" +t.getcontent().getSender() + "评论了";
                        c++;
                    }
                    else if (t.gettype().equals("comment-feeling")) {
                        xinxi = "您的 " + t.getcontent().getname() +" 心情被" +t.getcontent().getSender() + "评论了";
                        c++;
                    }
                    else if (t.gettype().equals("post-feeling")) {
                        xinxi = "您的关注对象 " + t.getcontent().getSender() +" 发布了新的心情 " +t.getcontent().getname();
                        c++;
                    }
                    else if (t.gettype().equals("post-travel")) {
                        xinxi = "您的关注对象 " + t.getcontent().getSender() +" 发布了新的游记 " +t.getcontent().getname();
                        c++;
                    }
                    else xinxi = t.gettype();

                    temp.put("info", xinxi);
                    mDatas.add(temp);


                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {



/*


                        }*/
                        int rescode = response.code();
                        if (rescode == 200) {
                            Toast.makeText(getApplicationContext(),String.valueOf(result.getdata().size())  , Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(),
                                    result.getmsg(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });


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
