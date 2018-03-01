package com.example.jeff.yueli;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private float y1;
    private float y2;
    ImageView recommend, meet, trip, individual;
    private int pos;
    private ArrayList<spot> spots;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        spots = new ArrayList<>();
        pos = 0;
        recommend = (ImageView)findViewById(R.id.recommend);
        meet = (ImageView)findViewById(R.id.meet);
        trip = (ImageView)findViewById(R.id.trip);
        individual = (ImageView)findViewById(R.id.individual);
        meet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Map.class);

                pos = 2;
                startActivity(intent);
            }
        });
    }

    private void changeItemColor(int p) {
        
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //继承了Activity的onTouchEvent方法，直接监听点击事件
        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            //当手指按下的时候
            y1 = event.getY();
        }
        if(event.getAction() == MotionEvent.ACTION_UP) {
            //当手指离开的时候
            y2 = event.getY();
            if(y1 - y2 > 50) {
                Toast.makeText(MainActivity.this, "向上滑", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "向下滑", Toast.LENGTH_SHORT).show();
            }
        }
        return super.onTouchEvent(event);
    }

    // 网络访问部分

    // 获取景点介绍和文字!->


}
