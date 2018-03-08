package com.example.jeff.yueli;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PostTrip extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.journey_edit);
        Button s = findViewById(R.id.send);
        EditText e = findViewById(R.id.title);
        final String t = e.getText().toString();
        final EditText ed = findViewById(R.id.edit);
        s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final MyApplication application = (MyApplication) getApplication();
                final OkHttpClient httpClient = application.gethttpclient();
                final User user = application.getUser();
                int spotid = application.getSpots().get(application.getCurrentPos()).getID();
                String url = "http://123.207.29.66:3009/api/travels";
                FormBody formBody = new FormBody
                        .Builder()
                        .add("title",t)//设置参数名称和参数值
                        .build();
                Request request = new Request.Builder().post(formBody).url(url).build();

                httpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                    }
                    String string=null;
                    @Override
                    public void onResponse(Call call, final Response response) throws IOException {
                        try {
                            string = response.body().string();
                            Gson gson = new Gson();
                            Type logintype = new TypeToken<Result<reviewback>>(){}.getType();
                            Result<reviewback> loginresult = gson.fromJson(string, logintype);
                            reviewback denglu = loginresult.data;
                            // Toast.makeText(LoginActivity.this, String.valueOf(loginresult.data.getuserid()), Toast.LENGTH_SHORT).show();
                            int rescode = response.code();
                            if (rescode == 200) {
                                application.eid=denglu.getComment_id();
                                String url = "http://123.207.29.66:3009/api/travels/"+String.valueOf(denglu.getComment_id())+"/travel-records";
                                FormBody formBody = new FormBody
                                        .Builder()
                                        .add("spot_id","1")//设置参数名称和参数值
                                        .add("content",ed.getText().toString())
                                        .build();
                                Request request = new Request.Builder().post(formBody).url(url).build();
                                httpClient.newCall(request).enqueue(new Callback() {
                                    @Override
                                    public void onFailure(Call call, IOException e) {
                                    }
                                    String string=null;
                                    @Override
                                    public void onResponse(Call call, final Response response) throws IOException {
                                        try {
                                            string = response.body().string();
                                            Gson gson = new Gson();
                                            Type logintype = new TypeToken<Result<reviewback>>(){}.getType();
                                            Result<reviewback> loginresult = gson.fromJson(string, logintype);
                                            final String m=loginresult.msg;
                                            reviewback denglu = loginresult.data;
                                            // Toast.makeText(LoginActivity.this, String.valueOf(loginresult.data.getuserid()), Toast.LENGTH_SHORT).show();

                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                int rescode = response.code();
                                                if (rescode == 200) {
                                                    //application.eid=denglu.getComment_id();
                                                    Toast.makeText(PostTrip.this, String.valueOf(rescode), Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(PostTrip.this, String.valueOf(rescode), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }
                                });
                            } else {
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                            }
                        });
                    }
                });


            }
        });
    }
}
