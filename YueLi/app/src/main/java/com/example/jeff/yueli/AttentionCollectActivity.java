package com.example.jeff.yueli;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

public class AttentionCollectActivity extends AppCompatActivity {
    public List<java.util.Map<String, String>> mDatas =
            new ArrayList<java.util.Map<String, String>>();//关注的数据
    public List<java.util.Map<String, String>> collectDatas =
            new ArrayList<java.util.Map<String, String>>();//收藏的数据

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.attention_and_collect);
        final String t = (String) getIntent().getSerializableExtra("travel_id");

        initData();
        final RecyclerView attentionRecView = (RecyclerView) findViewById(R.id.attention_recyclerview);
        final HomeAdapter myAdapter = new HomeAdapter();
        attentionRecView.setLayoutManager(new LinearLayoutManager(this));
        attentionRecView.setAdapter(myAdapter);
        attentionRecView.setVisibility(View.INVISIBLE);

        final RecyclerView collectRecView = (RecyclerView)findViewById(R.id.collect_recyclerview);
        final JourneyItemAdapter myAdapter2 = new JourneyItemAdapter(this, collectDatas);
        collectRecView.setLayoutManager(new LinearLayoutManager(this));
        collectRecView.setAdapter(myAdapter2);

        Button attention = findViewById(R.id.attention);
        Button collect = findViewById(R.id.collect);
        attention.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attentionRecView.setVisibility(View.VISIBLE);
                collectRecView.setVisibility(View.INVISIBLE);
            }
        });
        collect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attentionRecView.setVisibility(View.INVISIBLE);
                collectRecView.setVisibility(View.VISIBLE);
            }
        });
    }
     public void initData(){

         MyApplication application = (MyApplication) getApplication();

         OkHttpClient httpClient = application.gethttpclient();
         final User user = application.getUser();

         String url = "http://123.207.29.66:3009/api/users/"+String.valueOf(user.getuserid())+"/followers";
         Request request = new Request.Builder().url(url).build();

         httpClient.newCall(request).enqueue(new Callback() {
             @Override
             public void onFailure(Call call, IOException e) {
             }

             String string = null;
             @Override
             public void onResponse(Call call, final Response response) throws IOException {
                 runOnUiThread(new Runnable() {
                     @Override
                     public void run() {
                         try {
                             string = response.body().string();
                         } catch (IOException e) {
                             e.printStackTrace();
                         }

                         Gson gson = new Gson();
                         Followers result = gson.fromJson(string, Followers.class);
                         List<Followers.follow> followerlist = result.getdata();

                         for (int i = 0; i < followerlist.size(); ) {
                             Followers.follow t = followerlist.get(i);
                             Map<String, String> temp = new LinkedHashMap<String, String>();
                             temp.put("name", t.getnickname());
                             temp.put("signature",t.getsignature());
                             mDatas.add(temp);
                         }
                         int rescode = response.code();
                         if (rescode == 200) {
                             Toast.makeText(getApplicationContext(), result.getmsg(), Toast.LENGTH_SHORT).show();
                         } else {
                             Toast.makeText(getApplicationContext(),
                                     result.getmsg(), Toast.LENGTH_SHORT).show();
                         }
                     }
                 });
             }
         });

         url="http://123.207.29.66:3009/api/travels?user_id="+String.valueOf(user.getuserid());
         request = new Request.Builder().url(url).build();

         httpClient.newCall(request).enqueue(new Callback() {
             @Override
             public void onFailure(Call call, IOException e) {
             }
             String string=null;
             @Override
             public void onResponse(Call call, final Response response) throws IOException {
                runOnUiThread(new Runnable() {
                     @Override
                     public void run() {
                         //  Toast.makeText(getActivity().getApplicationContext(), "TestRes", Toast.LENGTH_SHORT).show();
                         try {
                             string = response.body().string();
                         } catch (IOException e) {
                             e.printStackTrace();
                         }
                         Gson gson = new Gson();
                         Travel result = gson.fromJson(string,Travel.class);
                         List<Travel.trip> travellist =  result.gettrips();

                         for (int i = 0; i < travellist.size(); i++) {
                             Travel.trip t = travellist.get(i);
                             Map<String, String> temp = new LinkedHashMap<String, String>();
                             temp.put("title", t.gettitle());
                             temp.put("firstday", t.getFirst_day());
                             temp.put("duration",  String.valueOf(t.getduration()));
                             temp.put("location", t.getlocation());
                             temp.put("name", t.getnickname());
                             temp.put("like_num", String.valueOf(t.getfavoritecount()));
                             temp.put("comment_num", String.valueOf(t.getComment_count()));
                             temp.put("travel_id",String.valueOf(t.gettravelid()));
                             temp.put("favorited",String.valueOf(t.getfavorited()));
                             if (Boolean.valueOf(temp.get("favorited"))) {
                                 collectDatas.add(temp);
                             }

                         }
                         int rescode = response.code();
                         if (rescode == 200) {
                             //Toast.makeText(getActivity().getApplicationContext(),"travel_id is " + String.valueOf(travellist.get(0).gettravelid())  , Toast.LENGTH_SHORT).show();
                         } else {
                             Toast.makeText(getApplicationContext(), result.getmsg(), Toast.LENGTH_SHORT).show();
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
