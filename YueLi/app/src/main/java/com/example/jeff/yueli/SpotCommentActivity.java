package com.example.jeff.yueli;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by jeff on 18-3-9.
 */

public class SpotCommentActivity extends AppCompatActivity{
    public List<java.util.Map<String, String>> mDatas =
            new ArrayList<java.util.Map<String, String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);
            setContentView(R.layout.spot_comment);

            final RecyclerView myRecView = (RecyclerView) findViewById(R.id.my_recyclerview);
            final CommentItemAdapter myAdapter = new CommentItemAdapter(this, mDatas);
            myRecView.setLayoutManager(new LinearLayoutManager(this));
            myRecView.setAdapter(myAdapter);
            initData();

    }

    public void initData() {

    }
}
