package com.example.jeff.yueli;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
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
    private int pinglunshu;
    TextView shownum;
    View leftLine;
    View rightLine;
   // ScrollView scrollView;
   SimpleDateFormat formatter   =   new   SimpleDateFormat   ("yyyy年MM月dd日");
    private Date findDate(String str) {
        Calendar calendar = Calendar.getInstance();
        Date date = new Date();
        try {
            date = formatter.parse(str);
            calendar.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.spot_detail);

        myApplication = (MyApplication)getApplication();
        initViews();
        final RecyclerView spotRecView = (RecyclerView)findViewById(R.id.left_recyclerview);
        final JourneyItemAdapter myAdapter2 = new JourneyItemAdapter(this, spotDatas);

        spotRecView.setLayoutManager(new LinearLayoutManager(this));
        spotRecView.setAdapter(myAdapter2);
        spotRecView.setFocusable(false);

        final RecyclerView myRecView = (RecyclerView) findViewById(R.id.right_recyclerview);
        final HomeAdapter myAdapter = new HomeAdapter();
        initDatas(myAdapter,myAdapter2);


        myRecView.setLayoutManager(new LinearLayoutManager(this));
        myRecView.setAdapter(myAdapter);
        myRecView.setVisibility(View.INVISIBLE);
        myRecView.setFocusable(false);
        Button comment = findViewById(R.id.comment_icon);
        Button back = findViewById(R.id.back);
        final Button like = findViewById(R.id.like);
        final TextView cnum = findViewById(R.id.comment_num);
        MyApplication application = (MyApplication) getApplication();
        OkHttpClient httpClient = application.gethttpclient();
        final User user = application.getUser();
        final int spotid = application.getSpots().get(application.getCurrentPos()).getID();
        shownum = (TextView)findViewById(R.id.star_num);
        final ImageView star1 = (ImageView) findViewById(R.id.mystar1);
        final ImageView star2 = (ImageView) findViewById(R.id.mystar2);
        final ImageView star3 = (ImageView) findViewById(R.id.mystar3);
        final ImageView star4 = (ImageView) findViewById(R.id.mystar4);
        final ImageView star5 = (ImageView) findViewById(R.id.mystar5);
        star1.setImageResource(R.drawable.star_empty);
        star2.setImageResource(R.drawable.star_empty);
        star3.setImageResource(R.drawable.star_empty);
        star4.setImageResource(R.drawable.star_empty);
        star5.setImageResource(R.drawable.star_empty);
        star1.setTag(R.drawable.star_empty);
        star2.setTag(R.drawable.star_empty);
        star3.setTag(R.drawable.star_empty);
        star4.setTag(R.drawable.star_empty);
        star5.setTag(R.drawable.star_empty);
        star1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                star1.setBackgroundResource(R.drawable.star_filled);
                star2.setBackgroundResource(R.drawable.star_empty);
                star3.setBackgroundResource(R.drawable.star_empty);
                star4.setBackgroundResource(R.drawable.star_empty);
                star5.setBackgroundResource(R.drawable.star_empty);
            }
        });
        star2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                star1.setBackgroundResource(R.drawable.star_filled);
                star2.setBackgroundResource(R.drawable.star_filled);
                star3.setBackgroundResource(R.drawable.star_empty);
                star4.setBackgroundResource(R.drawable.star_empty);
                star5.setBackgroundResource(R.drawable.star_empty);
            }
        });
        star3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                star1.setBackgroundResource(R.drawable.star_filled);
                star2.setBackgroundResource(R.drawable.star_filled);
                star3.setBackgroundResource(R.drawable.star_filled);
                star4.setBackgroundResource(R.drawable.star_empty);
                star5.setBackgroundResource(R.drawable.star_empty);
            }
        });
        star4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                star1.setBackgroundResource(R.drawable.star_filled);
                star2.setBackgroundResource(R.drawable.star_filled);
                star3.setBackgroundResource(R.drawable.star_filled);
                star4.setBackgroundResource(R.drawable.star_filled);
                star5.setBackgroundResource(R.drawable.star_empty);
            }
        });
        star5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                star1.setBackgroundResource(R.drawable.star_filled);
                star2.setBackgroundResource(R.drawable.star_filled);
                star3.setBackgroundResource(R.drawable.star_filled);
                star4.setBackgroundResource(R.drawable.star_filled);
                star5.setBackgroundResource(R.drawable.star_filled);
            }
        });



     //   cnum.setText(String.valueOf(myApplication.getSpots().get(myApplication.getCurrentPos()).getComment_count()));
        boolean favor = myApplication.getSpots().get(myApplication.getCurrentPos()).getFavorited();
        if (favor) {
            like.setBackgroundResource(R.drawable.heart_red);
        } else {
            like.setBackgroundResource(R.drawable.heart_gray_48px);
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setClass(SpotDetailActivity.this, MainActivity.class);
                //一定要指定是第几个pager，因为要跳到ThreeFragment，这里填写2
                i.putExtra("id", 0);
                startActivity(i);
            }
        });
        comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setClass(SpotDetailActivity.this, SpotCommentActivity.class);
                //一定要指定是第几个pager，因为要跳到ThreeFragment，这里填写2
                i.putExtra("spot_id", spotid);
                startActivity(i);
            }
        });

        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OkHttpClient httpClient = myApplication.gethttpclient();
                String url = "http://123.207.29.66:3009/api/spots/"+String.valueOf(spotid)+"/favorite";
                boolean favor = myApplication.getSpots().get(myApplication.getCurrentPos()).getFavorited();
                if (!favor) {
                    FormBody formBody = new FormBody
                            .Builder()
                            .add("spot_id",String.valueOf(spotid))
                            .build();
                    Request request = new Request.Builder().post(formBody).url(url).build();
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
                            final int rescode = response.code();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (rescode == 200) {
                                        Toast.makeText(getApplicationContext(), "成功收藏", Toast.LENGTH_SHORT).show();
                                        myApplication.getSpots().get(myApplication.getCurrentPos()).setFavorited(true);
                                        like.setBackgroundResource(R.drawable.heart_red);
                                    } else {
                                        Toast.makeText(getApplicationContext(), "收藏失败", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    });
                } else {
                    Request request = new Request.Builder()
                            .url(url)
                            .delete()
                            .build();
                    httpClient.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                        }
                        String string = null;

                        @Override
                        public void onResponse(Call call, final Response response) throws IOException {
                            //  Toast.makeText(getActivity().getApplicationContext(), "TestRes", Toast.LENGTH_SHORT).show();
                            try {
                                string = response.body().string();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            final int rescode = response.code();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (rescode == 200) {
                                        Toast.makeText(getApplicationContext(), "取消收藏", Toast.LENGTH_SHORT).show();
                                        myApplication.getSpots().get(myApplication.getCurrentPos()).setFavorited(false);
                                        like.setBackgroundResource(R.drawable.heart_gray_48px);
                                    } else {
                                        Toast.makeText(getApplicationContext(), "取消失败", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    });
                }
            }
        });
        String url = "http://123.207.29.66:3009/api/spots/"+String.valueOf(spotid);

        Request request = new Request.Builder().url(url).build();
            httpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {}
                String string = null;
                @Override
                public void onResponse(Call call, final Response response) throws IOException {
                    try {
                        string = response.body().string();
                        Gson gson = new Gson();
                        Type logintype = new TypeToken<Result<spot>>(){}.getType();
                        Result<spot> loginresult = gson.fromJson(string, logintype);
                        spot denglu = loginresult.data;
                        pinglunshu = denglu.getComment_count();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    final int rescode = response.code();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (rescode == 200) {

                                cnum.setText(String.valueOf(pinglunshu));
                            } else {
                                //Toast.makeText(getApplicationContext(), "获取评论数失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            });


    }

    private void initViews() {
//        scrollView = (ScrollView) findViewById(R.id.scrollview);
//        scrollView.scrollTo(0,0);
        TextView spotName = (TextView)findViewById(R.id.spot_name);
        TextView location = (TextView)findViewById(R.id.location);
        TextView descr = (TextView)findViewById(R.id.description);
        TextView leftTitle = (TextView)findViewById(R.id.left_title);
        TextView rightTitle = (TextView)findViewById(R.id.right_title);
        leftLine = (View) findViewById(R.id.left_line);
        rightLine = (View) findViewById(R.id.right_line);
        leftLine.setVisibility(View.VISIBLE);
        rightLine.setVisibility(View.INVISIBLE);

        leftTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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



    public void initDatas(final HomeAdapter a1, final JourneyItemAdapter a2){
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
                    temp.put("firstday", formatter.format(findDate(t.getFirst_day())));
                    temp.put("duration",  String.valueOf(t.getduration()) + "天");
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
        Map<String, String> temp = new LinkedHashMap<String, String>();
        temp.put("name", "星巴克");
        temp.put("description", "咖啡店");
        shopDatas.add(temp);
        temp = new LinkedHashMap<String, String>();
        temp.put("name", "绿野仙踪");
        temp.put("description", "西餐厅");
        shopDatas.add(temp);
        temp = new LinkedHashMap<String, String>();
        temp.put("name", "南京大牌档");
        temp.put("description", "中餐厅");
        shopDatas.add(temp);
        temp = new LinkedHashMap<String, String>();
        temp.put("name", "一点点");
        temp.put("description", "奶茶店");
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
