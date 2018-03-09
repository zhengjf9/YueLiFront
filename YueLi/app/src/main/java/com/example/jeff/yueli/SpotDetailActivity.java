package com.example.jeff.yueli;

import android.os.Bundle;
import android.support.annotation.Nullable;
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
 * Created by jeff on 18-3-8.
 */

public class SpotDetailActivity extends AppCompatActivity {
    public List<java.util.Map<String, String>> spotDatas =
            new ArrayList<java.util.Map<String, String>>();
    public List<java.util.Map<String, String>> shopDatas =
            new ArrayList<java.util.Map<String, String>>();
    private spot currentSpot;
    private MyApplication myApplication;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.spot_detail);
        initDatas();
        myApplication = (MyApplication)getApplication();
        initViews();
        final RecyclerView spotRecView = (RecyclerView)findViewById(R.id.left_recyclerview);
        final JourneyItemAdapter myAdapter2 = new JourneyItemAdapter(this, spotDatas);
        spotRecView.setLayoutManager(new LinearLayoutManager(this));
        spotRecView.setAdapter(myAdapter2);
        spotRecView.setVisibility(View.INVISIBLE);

        final RecyclerView myRecView = (RecyclerView) findViewById(R.id.right_recyclerview);
        final HomeAdapter myAdapter = new HomeAdapter();
        myRecView.setLayoutManager(new LinearLayoutManager(this));
        myRecView.setAdapter(myAdapter);

    }

    private void initViews() {
        TextView spotName = (TextView)findViewById(R.id.spot_name);
        TextView location = (TextView)findViewById(R.id.location);
        TextView descr = (TextView)findViewById(R.id.description);
        TextView leftTitle = (TextView)findViewById(R.id.left_title);
        TextView rightTitle = (TextView)findViewById(R.id.right_title);

        leftTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View leftLine = (View)findViewById(R.id.left_line);
                View rightLine = (View) findViewById(R.id.right_line);
                RecyclerView leftRecyc = (RecyclerView)findViewById(R.id.left_recyclerview);
                RecyclerView rightRecyc = (RecyclerView)findViewById(R.id.right_recyclerview);
                leftLine.setVisibility(View.VISIBLE);
                rightLine.setVisibility(View.GONE);
                leftRecyc.setVisibility(View.VISIBLE);
                rightRecyc.setVisibility(View.GONE);
            }
        });
        rightTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View leftLine = (View)findViewById(R.id.left_line);
                View rightLine = (View) findViewById(R.id.right_line);
                RecyclerView leftRecyc = (RecyclerView)findViewById(R.id.left_recyclerview);
                RecyclerView rightRecyc = (RecyclerView)findViewById(R.id.right_recyclerview);
                leftLine.setVisibility(View.GONE);
                rightLine.setVisibility(View.VISIBLE);
                leftRecyc.setVisibility(View.GONE);
                rightRecyc.setVisibility(View.VISIBLE);
            }
        });
        currentSpot = myApplication.getSpots().get(myApplication.getCurrentPos());
        spotName.setText(currentSpot.getName());
        location.setText(currentSpot.getCity());
        descr.setText(currentSpot.getDescription());
    }



    public void initDatas(){
        MyApplication application = (MyApplication) getApplication();
        OkHttpClient httpClient = application.gethttpclient();
        final User user = application.getUser();
        int spotid = application.getSpots().get(application.getCurrentPos()).getID();
        String url = "http://123.207.29.66:3009/api/travels?spot_id="+String.valueOf(spotid) ;
        Request request = new Request.Builder().url(url).build();
        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }
            String string=null;
            @Override
            public void onResponse(Call call, final Response response) throws IOException {
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
                    temp.put("user_id",String.valueOf(t.getuserid()));
                    temp.put("title", t.gettitle());
                    temp.put("firstday", t.getFirst_day());
                    temp.put("duration",  String.valueOf(t.getduration()));
                    temp.put("location", t.getlocation());
                    temp.put("name", t.getnickname());
                    temp.put("like_num", String.valueOf(t.getfavoritecount()));
                    temp.put("comment_num", String.valueOf(t.getComment_count()));
                    temp.put("travel_id",String.valueOf(t.gettravelid()));
                    temp.put("favorited",String.valueOf(t.getfavorited()));
                    spotDatas.add(temp);
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int rescode = response.code();
                        if (rescode == 200) {
                            // Toast.makeText(getActivity().getApplicationContext(),"travel_id is " + String.valueOf(travellist.get(0).gettravelid())  , Toast.LENGTH_SHORT).show();
                        } else {
                           // Toast.makeText(getActivity().getApplicationContext(), "try", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        /*
        Map<String, String> temp1 = new LinkedHashMap<String, String>();
        temp1.put("title", "上海:梦中城");
        temp1.put("firstday", "2018-3-7");
        temp1.put("duration",  "3天");
        temp1.put("location", "上海");
        temp1.put("name", "旅行者");
        temp1.put("like_num", "99");
        temp1.put("comment_num", "99");
        spotDatas.add(temp1);
        spotDatas.add(temp1);
*/
        Map<String, String> temp = new LinkedHashMap<String, String>();
        temp.put("name", "星巴克");
        temp.put("description", "咖啡店咖啡店");
        shopDatas.add(temp);
        shopDatas.add(temp);
        shopDatas.add(temp);
    }
    class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> {

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                    SpotDetailActivity.this).inflate(R.layout.shop_item, parent,
                    false));
            return holder;
        }

        private OnItemClickLitener mOnItemClickLitener;

        public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
            this.mOnItemClickLitener = mOnItemClickLitener;
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position) {
            holder.name.setText(shopDatas.get(position).get("name"));
            holder.description.setText(shopDatas.get(position).get("description"));
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
            shopDatas.remove(position);
            notifyItemRemoved(position);
        }

        @Override
        public int getItemCount() {
            return shopDatas.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {

            TextView name;
            TextView description;

            public MyViewHolder(View view) {
                super(view);
                name = (TextView) view.findViewById(R.id.name);
                description = (TextView) view.findViewById(R.id.description);
            }
        }
    }


    // 判断经典是否被收藏
    void getInfoAboutFavorite() {
        OkHttpClient okHttpClient = myApplication.gethttpclient();
        String url = "http://123.207.29.66:3009/api/spots/" + currentSpot.getID();
    }
}
