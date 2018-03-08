package com.example.jeff.yueli;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by jeff on 18-3-5.
 */

public class JourneyCommentActivity extends AppCompatActivity {
    public List<java.util.Map<String, String>> mDatas =
            new ArrayList<java.util.Map<String, String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.journey_comment);
        try {
            final String t = (String) getIntent().getSerializableExtra("travel_id");

            initData(t);
            Button back = findViewById(R.id.back);
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            Button send = (Button)findViewById(R.id.sendcomment);
            send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Intent i=new Intent();
                    //i.setClass(JourneyDetailActivity.this, JourneyCommentActivity.class);
                    //一定要指定是第几个pager，因为要跳到ThreeFragment，这里填写2
                    // i.putExtra("travel_id",t);
                    //startActivity(i);
                    EditText review = (EditText)findViewById(R.id.name_input);
                    final String pinglun = review.getText().toString();
                    MyApplication application = (MyApplication)getApplication();
                    OkHttpClient httpClient = application.gethttpclient();

                    String url = "http://123.207.29.66:3009/api/travels/"+t+"/comments";
                /*FormBody formBody = new FormBody
                        .Builder()
                        .add("reply_to_id",null)//设置参数名称和参数值
                        .add("content",pinglun)
                        .build();*/
                    //String tmp= "{\"reply_to_id\": null,\"content\":"+pinglun+"}";
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("reply_to_id", JSONObject.NULL);
                        jsonObject.put("content", pinglun);
                        // jsonObject.put("anyKey", "anyValue");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                    RequestBody body = RequestBody.create(JSON, jsonObject.toString());
                    Request request = new Request.Builder().url(url)
                            .post(body)
                            .build();;
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
                                        Gson gson = new Gson();
                                        Type commenttype = new TypeToken<Result<reviewback>>(){}.getType();
                                        Result<reviewback> commentresult = gson.fromJson(string, commenttype);
                                        int rescode = response.code();
                                        if (rescode == 200) {
                                            Toast.makeText(getApplicationContext(),String.valueOf(commentresult.msg)   , Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(getApplicationContext(), commentresult.msg , Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }


                                }
                            });
                        }
                    });
                }
            });

            final RecyclerView myRecView = (RecyclerView) findViewById(R.id.my_recyclerview);
            final CommentItemAdapter myAdapter = new CommentItemAdapter(this, mDatas);
            myRecView.setLayoutManager(new LinearLayoutManager(this));
            myRecView.setAdapter(myAdapter);
        } catch (Exception e) {
            Log.e("Comment Activity", "Bug", e);
        }

    }

    public void initData(String t) {
        mDatas.clear();
        MyApplication application = (MyApplication)getApplication();
        OkHttpClient httpClient = application.gethttpclient();
        User user = application.getUser();

        String url="http://123.207.29.66:3009/api/travels/"+t+"/comments";
        Request request = new Request.Builder().url(url).build();

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
                            Gson gson = new Gson();
                            Comment result = gson.fromJson(string,Comment.class);
                            //record result = gson.fromJson(string,record.class);
                            List<Comment.review> reviewlist =  result.getreviews();

                            for (int i = 0; i < reviewlist.size(); i++) {
                                Comment.review t = reviewlist.get(i);
                                Map<String, String> temp = new LinkedHashMap<String, String>();

                                temp.put("name", t.getnickname());
                                temp.put("date",t.gettime());
                            /*
                            SimpleDateFormat sdf =   new SimpleDateFormat( "yyyy-MM-dd" );
                            Calendar calendar = Calendar.getInstance();
                            try {
                                Date date = sdf.parse(travellist.get(0).gettime().substring(0,10));
                                calendar.setTime(date);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            String[] dayofweek = new String[] { "周日", "周一", "周二", "周三", "周四",
                                    "周五", "周六" };
                            String wd=dayofweek[calendar.get(Calendar.DAY_OF_WEEK)-1];
                            temp.put("week", wd);*/
                                temp.put("content",t.getcontent());
                                mDatas.add(temp);


                            }

                            int rescode = response.code();

                            if (rescode == 200) {
                               // Toast.makeText(getApplicationContext(),String.valueOf(reviewlist.size())   , Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "fail" , Toast.LENGTH_SHORT).show();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                    }
                });
            }
        });



    }
}
