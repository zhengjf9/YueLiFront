package com.example.jeff.yueli;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.*;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import rx.*;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by jeff on 18-3-8.
 */

public class MapMoodActivity extends AppCompatActivity {
    public List<java.util.Map<String, String>> mDatas =
            new ArrayList<java.util.Map<String, String>>();
    private List<Comment.review> comments;
    private MyApplication myApplication;
    private Feelings cFeelings;
    private CommentItemAdapter myAdapter;
    private int pos;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_mood);
        try {
            myApplication = (MyApplication)getApplication();
            cFeelings = myApplication.getCurrentFeelings();
            pos = 0;

            initViews();
            final RecyclerView myRecView = (RecyclerView) findViewById(R.id.my_recyclerview);

            final CommentItemAdapter myAdapter = new CommentItemAdapter(this, mDatas);
            myAdapter.notifyDataSetChanged();



            myRecView.setLayoutManager(new LinearLayoutManager(this));
            myRecView.setAdapter(myAdapter);
        } catch (Exception e) {
            Log.e("map_mood", "Wrong!", e);
        }
    }





    private void initViews() {
        TextView name = (TextView)findViewById(R.id.name);
        TextView time = (TextView)findViewById(R.id.date);
        TextView desc = (TextView)findViewById(R.id.description);
        ImageView back = (ImageView)findViewById(R.id.back);
        ImageView pic = (ImageView)findViewById(R.id.pic);
        TextView loca = (TextView)findViewById(R.id.location);
        Button sendComment = (Button)findViewById(R.id.sendcomment);
        name.setText(cFeelings.getNickname());
        time.setText(cFeelings.getTime());
        desc.setText(cFeelings.getContent());
        loca.setText(myApplication.getaMapLocation().getAddress());
        pic.setImageBitmap(cFeelings.getImage());
        sendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OkHttpClient okHttpClient = myApplication.gethttpclient();
                String url = "http://123.207.29.66:3009/api/feelings/" + cFeelings.getFeeling_id() + "/comments";
                EditText nameInput = (EditText)findViewById(R.id.name_input);
                FormBody formBody = new FormBody.Builder()
                        .add("content", nameInput.getText().toString()).build();
                Log.e("send feeling comment", "start " + url);
                final Request request = new Request.Builder().post(formBody).url(url).build();
                okHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        final boolean isOk;
                        String s = response.body().string();
                        if (response.code() == 200) {
                            Log.e("sendFComments", "success" + " " + s);
                            isOk = true;

                        } else {
                            Log.e("sendFComments", "fail" + " " + s);

                            isOk = false;
                        }
                        Observable<String> observable = Observable.create(new Observable.OnSubscribe<String>() {
                            @Override
                            public void call(Subscriber<? super String> subscriber) {
                                subscriber.onCompleted();
                            }
                        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread());
                        Observer<String> observer = new Observer<String>() {
                            @Override
                            public void onCompleted() {
                                if (isOk) {
                                    Toast.makeText(MapMoodActivity.this, "评论成功", Toast.LENGTH_SHORT).show();
                                    TextView nameInput = (TextView)findViewById(R.id.name_input);
                                    nameInput.setText("");
                                } else {
                                    Toast.makeText(MapMoodActivity.this, "请稍后重新尝试", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onNext(String s) {

                            }
                        };
                        observable.subscribe(observer);
                    }
                });
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        Observable<String> observable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                while (true) {
                    try {
                        OkHttpClient okHttpClient = myApplication.gethttpclient();
                        String url = "http://123.207.29.66:3009/api/feelings/" + cFeelings.getFeeling_id() + "/comments";
                        final Request request = new Request.Builder().url(url).build();
                        okHttpClient.newCall(request).enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {

                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                try {
                                    String s = response.body().string();
                                    Gson gson = new Gson();
                                    Comment comment = gson.fromJson(s, Comment.class);
                                    List<Comment.review> temp = comment.getreviews();
                                    if (isCommentsUpdate(temp)) {
                                        convertCommentToAdapter();
                                    }
                                } catch (Exception e) {

                                }
                            }
                        });
                        Thread.sleep(2000);
                    } catch (Exception e) {
                        Log.e("get mood comments", "wrong", e);
                    }
                }
            }
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread());
        Observer<String> observer = new Observer<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {

            }
        };
        observable.subscribe(observer);

    }

    private boolean isCommentsUpdate(List<Comment.review> c) {
        if (comments == null) {
            comments = new ArrayList<>();
        }
        int size = comments.size();
        if (c.size() == 0) {
            return false;
        }
        boolean isChanged = false;
        boolean isSame;
        for (int i = 0; i < c.size(); ++i) {
            isSame = false;
            for (int j = 0; j < size; ++i) {
                if (c.get(i).getcommentid() == comments.get(j).getcommentid()) {
                    isSame = true;
                    break;
                }
            }
            if (!isSame) {
                comments.add(c.get(i));
                isChanged = true;
            }
        }
        return isChanged;
    }

    void convertCommentToAdapter() {
        List<java.util.Map<String, String>> datas = myAdapter.getmDatas();

        for (int i = pos; i < comments.size(); ++i) {
            java.util.Map<String, String> temp = new LinkedHashMap<>();
            temp.put("name", comments.get(i).getnickname());
            temp.put("date", comments.get(i).gettime());
            temp.put("content", comments.get(i).getcontent());
            datas.add(temp);
        }
        myAdapter.setmDatas(datas);
        myAdapter.notifyDataSetChanged();
    }

    public void initDatas(){
        java.util.Map<String, String> temp = new LinkedHashMap<String, String>();
        temp.put("name", "小明");
        temp.put("date", "2017-8-9");
        temp.put("content", "写得好好");
        mDatas.add(temp);
        mDatas.add(temp);
        mDatas.add(temp);
        mDatas.add(temp);
        mDatas.add(temp);
        mDatas.add(temp);
    }



}
