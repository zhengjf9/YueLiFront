package com.example.jeff.yueli;

import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by XDDN2 on 2018/3/2.
 */

public class RecommendActivity extends Fragment {
    public RecommendActivity() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_recommend, container, false);
        return view;
    }

    // 网络访问部分

    // 获取景点介绍和文字!->
}
