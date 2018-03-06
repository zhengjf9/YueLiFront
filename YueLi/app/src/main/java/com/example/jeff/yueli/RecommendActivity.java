package com.example.jeff.yueli;

import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
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
    private int which;
    private int verticalMinistance = 50;
    private int minVelocity = 10;
    private GestureDetector gestureDetector;
    private View totalView;
    private ConstraintLayout first, second;
    public RecommendActivity() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        totalView = inflater.inflate(R.layout.activity_recommend, container, false);
        which = 0;
        initViews();
        return totalView;
    }

    private void initViews() {
        gestureDetector = new GestureDetector(getActivity(), new LearnGestureListener());
        first = totalView.findViewById(R.id.firstSpot);
        second = totalView.findViewById(R.id.secondSpot);
        totalView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        });
    }

    //设置手势识别监听器
    public class LearnGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (e1.getX() - e2.getX() > verticalMinistance && Math.abs(velocityX) > minVelocity) {

            } else if (e2.getX() - e1.getX() > verticalMinistance && Math.abs(velocityX) > minVelocity) {

            } else if (e1.getY() - e2.getY() > verticalMinistance && Math.abs(velocityY) > minVelocity) {

            } else if (e2.getY() - e1.getY() > verticalMinistance && Math.abs(velocityY) > minVelocity) {
                if (which == 0) {
                    first.setVisibility(View.GONE);
                    second.setVisibility(View.VISIBLE);
                    first.setAnimation(AnimationUtil.moveToViewBottom());
                    second.setAnimation(AnimationUtil.moveToViewLocation());
                    which = 1;
                } else {
                    second.setVisibility(View.GONE);
                    first.setVisibility(View.VISIBLE);
                    second.setAnimation(AnimationUtil.moveToViewBottom());
                    first.setAnimation(AnimationUtil.moveToViewLocation());
                    which = 0;
                }
            }
            return false;
        }

        //此方法必须重写且返回真，否则onFling不起效
        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }
    }

    // 网络访问部分

    // 获取景点介绍和文字!->
}
