package com.example.jeff.yueli;

import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by XDDN2 on 2018/3/2.
 */

public class RecommendActivity extends Fragment {
    private int which;
    private int verticalMinistance = 50;
    private int minVelocity = 10;
    private GestureDetector gestureDetector;
    private View totalView;
    private MyApplication myApplication;
    private ConstraintLayout first, second, third;
    private List<spot> spots;
    private int times;
    private TextView firstDest, secondDest, thirdDest, firstDesc, secondDesc, thirdDesc, local;

    public RecommendActivity() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        totalView = inflater.inflate(R.layout.activity_recommend, container, false);

        which = 0;
        times = 0;
        myApplication = (MyApplication)getActivity().getApplication();
        myApplication.setCurrentPos(which);
        getRecommandedSpot();
        initViews();
        return totalView;
    }


    private void initViews() {
        gestureDetector = new GestureDetector(getActivity(), new LearnGestureListener());
        first = totalView.findViewById(R.id.firstSpot);

        second = totalView.findViewById(R.id.secondSpot);
        third = totalView.findViewById(R.id.thirdSpot);
        firstDest = (TextView)first.findViewById(R.id.firstDestination);
        firstDesc = first.findViewById(R.id.firstDescription);
        secondDest = (TextView)second.findViewById(R.id.secondDestination);
        secondDesc = second.findViewById(R.id.secondDescription);
        thirdDest = third.findViewById(R.id.thirdDestination);
        thirdDesc = third.findViewById(R.id.thirdDescription);
        local = totalView.findViewById(R.id.location);
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
        public boolean onSingleTapUp(MotionEvent e) {
            Log.e("getDownTime", e.getEventTime() + " " + e.getDownTime());

            Intent intent = new Intent(getActivity(), SpotDetailActivity.class);

            startActivity(intent);
            return super.onSingleTapUp(e);
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (e1.getY() - e2.getY() > verticalMinistance && Math.abs(velocityY) > minVelocity) {
                Log.e("movement", "shanghua");
                if (which == 0) {
                    first.setVisibility(View.GONE);

                    secondDest.setText(spots.get(1).getName());
                    secondDesc.setText(spots.get(1).getDescription());
                    local.setText(spots.get(1).getCity());
                    second.setVisibility(View.VISIBLE);
                    first.setAnimation(AnimationUtil.upoutside());
                    second.setAnimation(AnimationUtil.upinto());
                    which = 1;

                } else if (which == 1) {
                    second.setVisibility(View.GONE);
                    third.setVisibility(View.VISIBLE);

                    thirdDest.setText(spots.get(2).getName());

                    thirdDesc.setText(spots.get(2).getDescription());
                    local.setText(spots.get(2).getCity());
                    second.setAnimation(AnimationUtil.upoutside());
                    third.setAnimation(AnimationUtil.upinto());
                    which = 2;
                }
                myApplication.setCurrentPos(which);
                times = times + 1;
            } else if (e2.getY() - e1.getY() > verticalMinistance && Math.abs(velocityY) > minVelocity) {
                Log.e("movement", "xiahua");
                if (which == 1) {
                    first.setVisibility(View.VISIBLE);

                    firstDest.setText(spots.get(0).getName());
                    firstDesc.setText(spots.get(0).getDescription());
                    local.setText(spots.get(0).getCity());
                    first.setAnimation(AnimationUtil.downinto());
                    second.setAnimation(AnimationUtil.downoutside());
                    second.setVisibility(View.GONE);
                    which = 0;

                } else if (which == 2) {
                    second.setVisibility(View.VISIBLE);

                    secondDest.setText(spots.get(1).getName());

                    secondDesc.setText(spots.get(1).getDescription());
                    local.setText(spots.get(1).getCity());
                    second.setAnimation(AnimationUtil.downinto());
                    third.setAnimation(AnimationUtil.downoutside());
                    third.setVisibility(View.GONE);
                    which = 1;
                }  else {
                    Log.e("xiahua","update spots");
                    getRecommandedSpot();
                }
            }
//            if (times == 2) {
//                getRecommandedSpot();
//                times = 0;
//            }
            return false;
        }

        //此方法必须重写且返回真，否则onFling不起效
        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }
    }

    // 网络访问部分


    // 景点图片获取未完成

    // 获取景点介绍和文字!->
    // 网络访问获取景点列表
    void getRecommandedSpot() {
        String url="http://123.207.29.66:3009/api/spots";
        OkHttpClient okHttpClient = myApplication.gethttpclient();
        Request request = new Request.Builder().url(url).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    String string = response.body().string();
                    Gson gson = new Gson();
                    Spots spotsInfo = gson.fromJson(string, Spots.class);
                    spots = spotsInfo.getData();
                    if (spots.size() < 3) {
                        spots.add(spots.get(1));
                    }
                    myApplication.setSpots(spots);
                    int rescode = response.code();
                    if (rescode == 200) {
                        Log.e("first spot", spots.get(0).getName());
                        Observable<String> observable = Observable.create(new Observable.OnSubscribe<String>() {
                            @Override
                            public void call(Subscriber<? super String> subscriber) {

                                subscriber.onNext("");
                                subscriber.onCompleted();
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
                                firstDest.setText(spots.get(0).getName());

                                firstDesc.setText(spots.get(0).getDescription());
                                local.setText(spots.get(0).getCity());
//                                second.setAnimation(AnimationUtil.downoutside());
                                first.setAnimation(AnimationUtil.downinto());
                            }
                        };
                        observable.subscribe(observer);
                    } else {

                    }
                } catch (Exception e) {
                    Log.e("get Spots", "Wrong", e);
                }

            }
        });
    }
}
