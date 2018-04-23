package com.example.jeff.yueli;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.location.Location;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by XDDN2 on 2018/3/1.
 */

public class Map  extends Fragment implements AMap.OnMarkerClickListener {
    private AMap aMap;
    private ArrayList<MarkerOptions> markeroptions;
    private List<Marker> markers;
    private MapView mapView;
    private List<Bitmap>imageLists;
    private List<Feelings> feelings; // 地图上心情的信息
    private AMapLocation mapLocation;
    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明定位回调监听器
    public AMapLocationListener mLocationListener = new MyAMapLocationListener();
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;
    private int pos;
    private int size;
    private View totalView;
    private MyApplication myApplication;
    private AMapLocation lastLocation;
    private BitmapDescriptor bitmapDescriptor;
    private Boolean isUpdate;
    public Map() {

    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        totalView = inflater.inflate(R.layout.map, container, false);
        try {
            pos = -1;
            size = 0;
            isUpdate =false;
            markers = new ArrayList<>();
            initImageList();
            myApplication = (MyApplication) getActivity().getApplication();
            ConstraintLayout c = (ConstraintLayout)(totalView.findViewById(R.id.info));
            c.setVisibility(View.GONE);
            c.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 跳转心情的详情页面
                }
            });
            initMap(totalView, savedInstanceState);
            Log.e("Map", "is OK");
            //心情添加到地图上的函数
            // 获取心情信息
            init();
        }catch (Exception e) {
            Log.e("Map", "Wrong", e);
        }

        return totalView;
    }



    // 初始化地图以及获取定位信息
    private void initMap(View view, Bundle savedInstanceState) {
        mapView = (MapView) view.findViewById(R.id.map);

        mapView.onCreate(savedInstanceState);// 此方法必须重写
        aMap = mapView.getMap();
        aMap.setOnMarkerClickListener(this);
        aMap.getUiSettings().setZoomControlsEnabled(false);
        //禁止地图缩放和移动
        aMap.getUiSettings().setScrollGesturesEnabled(false);
//        aMap.getUiSettings().setZoomGesturesEnabled(false);
        //设置缩放比例
        aMap.moveCamera(CameraUpdateFactory.zoomTo(15));
        // 实现定位

        MyLocationStyle myLocationStyle;
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW) ;//连续定位、且将视角移动到地图中心点，定位蓝点跟随设备移动。（1秒1次定位）
        myLocationStyle.strokeColor(Color.argb(0,0,0,0));
        myLocationStyle.radiusFillColor(Color.argb(0,0,0,0));
        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
//        aMap.getUiSettings().setMyLocationButtonEnabled(true); //设置默认定位按钮是否显示，非必需设置。
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
    }

    private void addMapPoint(int p) {
        Log.e("getInto", "addMapPoint" + p);
        Log.e("feelings' sizes", feelings.size() + "");

        View circleImage = LayoutInflater.from(getActivity()).inflate(R.layout.circleimage, null);
        CircleImageView circleImageView = circleImage.findViewById(R.id.circle);
        if (p >= feelings.size()) {
            p = feelings.size() - 1;
        }
        circleImageView.setImageBitmap(feelings.get(p).getImage());

        Log.e("finish", "add image");
        bitmapDescriptor = BitmapDescriptorFactory.fromView(circleImage);
        try {


            for (int i = 0; i < feelings.size(); ++i) {
                if (i == 0) {
                    i = p;
                }
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.setFlat(true);
                markerOptions.anchor(0.5f,0.5f);
                Log.e("order", i + "");
                markerOptions.icon(bitmapDescriptor);
                markerOptions.position(new LatLng(feelings.get(i).getLatitude(), feelings.get(i).getLongtitude()));
                Log.e("marks' La and Lo", feelings.get(i).getLatitude() + " " + feelings.get(i).getLongtitude());
                markers.add(aMap.addMarker(markerOptions));
                markeroptions.add(markerOptions);
                Log.e("order", i + "");
                ++i;
            }


        } catch (Exception e) {
            Log.e("marks' sizes", markeroptions.size() + "", e);
        }
        Log.e("marks' sizes", markeroptions.size() + "");
    }

    // 初始化定位
    private void init() {


        //初始化定位
        mLocationClient = new AMapLocationClient(getActivity().getApplicationContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);
        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //获取一次定位结果：
        //该方法默认为false。
        mLocationOption.setOnceLocation(false);

        //获取最近3s内精度最高的一次定位结果：
        //设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
        mLocationOption.setOnceLocationLatest(true);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);
        //关闭缓存机制
        mLocationOption.setLocationCacheEnable(false);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();



    }

    int findPosInMarkers(Marker marker) {
        for (int i = 0; i < markers.size(); ++i) {
            if (marker.equals(markers.get(i))) {
                return i;
            }
        }
        return -1;
    }

    void changeInfo(int p) {
        final int fpos = p;
        ConstraintLayout c = (ConstraintLayout)totalView.findViewById(R.id.info);
        c.setVisibility(View.VISIBLE);
        ImageView imageView = (ImageView)totalView.findViewById(R.id.markerImg);
        imageView.setImageBitmap(feelings.get(p).getImage());

        ImageView userImage = (ImageView)c.findViewById(R.id.user_image);
        TextView username = (TextView)c.findViewById(R.id.user_name);
        username.setText(feelings.get(p).getNickname());
        TextView content = (TextView)c.findViewById(R.id.descriptionOfMark);
        content.setText(feelings.get(p).getContent());
        c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MapMoodActivity.class);
                myApplication.setCurrentFeelings(feelings.get(fpos));
                startActivity(intent);
            }
        });
    }

    // 地图点击事件


    // 覆盖物点击事件
    @Override
    public boolean onMarkerClick(Marker marker) {
        // TODO Auto-generated method stub
        int p = findPosInMarkers(marker);
        changeInfo(p);

        return true;
    }


    private class MyAMapLocationListener implements AMapLocationListener {

        @Override
        public void onLocationChanged(final AMapLocation aMapLocation) {
            if (aMapLocation != null) {
                if (aMapLocation.getErrorCode() == 0) {
                    Log.e("位置：", aMapLocation.getLatitude() + " " + aMapLocation.getLongitude());
                    try {

                        getFeelings(aMapLocation);
                        if (isUpdate) {
                            Observable<AMapLocation> observable = Observable.create(new Observable.OnSubscribe<AMapLocation>() {
                                @Override
                                public void call(Subscriber<? super AMapLocation> subscriber) {
                                    Log.e("readdy for update", ""+isUpdate);
                                    if (pos == size) {

                                        isUpdate = false;
                                        Log.e("update", "OK");
                                    }
                                    while (pos < size) {
                                        pos = pos + 1;
                                        Log.e("marker", feelings.size() + " " + pos);


                                        addMapPoint(pos);
                                        Log.e("endadd", "infor");
                                        subscriber.onCompleted();
                                    }
                                }
                            }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread());
                            Observer<AMapLocation> observer = new Observer<AMapLocation>() {
                                @Override
                                public void onCompleted() {


                                    Log.e("process image", "OK");
                                }

                                @Override
                                public void onError(Throwable e) {

                                }

                                @Override
                                public void onNext(AMapLocation a) {

                                }
                            };
                            observable.subscribe(observer);

                            Log.e("getInto", "success");

                        }
                        lastLocation = aMapLocation;
                        myApplication.setaMapLocation(lastLocation);
                    } catch (Exception e) {
                        Log.e("getFeelings", "wrong", e);
                    }



                } else {
                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                    Log.e("AmapError", "location Error, ErrCode:"
                            + aMapLocation.getErrorCode() + ", errInfo:"
                            + aMapLocation.getErrorInfo());
                }
            }
        }
    }

    private boolean addTheDifferentOne(List<Feelings> t) {
        if (t == null)
            return false;
        if (feelings == null) {
            feelings = new ArrayList<>();
        }
        int size = feelings.size();
        if (size == 0) {
            feelings = t;
            Log.e("add feelings first", "success");
            return true;
        }
        boolean isSame;
        for (int i = 0; i < t.size(); ++i) {
            isSame = false;
            for (int j = 0; j < size; ++j) {
                if (t.get(i).getFeeling_id() == feelings.get(j).getFeeling_id()) {
                    isSame = true;
                    break;
                }
            }
            if (!isSame) {
                feelings.add(t.get(i));
                Log.e("add_feelings", "success");
            }
            isSame = false;
        }
        this.size = feelings.size();
        Log.e("after_adding_feelings", "" + this.size);
        return !(size == feelings.size());
    }

    void initImageList() {
        imageLists = new ArrayList<>();
//        imageLists.add(BitmapFactory.decodeResource(getResources(), R.drawable.r1));
        imageLists.add(BitmapFactory.decodeResource(getResources(), R.drawable.r2));
        imageLists.add(BitmapFactory.decodeResource(getResources(), R.drawable.r3));
        imageLists.add(BitmapFactory.decodeResource(getResources(), R.drawable.r4));
//        imageLists.add(BitmapFactory.decodeResource(getResources(), R.drawable.r5));
    }

    // 网络访问获取Spot相关信息

    List<Feelings> getFeelings(AMapLocation myLocation) {
        List<Feelings> feelings1 = new ArrayList<>();

        OkHttpClient okHttpClient = myApplication.gethttpclient();
        String url = "http://123.207.29.66:3009/api/feelings?longitude=[" + (myLocation.getLongitude()-0.002) + "," + (myLocation.getLongitude()+0.002)
                + "]&latitude=[" + (myLocation.getLatitude()-0.002) + "," + (myLocation.getLatitude()+0.002) + "]";
        Log.e("url", url);
        Request request = new Request.Builder().url(url).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    String s = response.body().string();
                    InputStream in = response.body().byteStream();
//                    Bitmap bitmap = BitmapFactory.decodeStream(in);
                    Gson gson = new Gson();

                    AllFeelings allFeelings = gson.fromJson(s, AllFeelings.class);
                    allFeelings.setImages(imageLists);
                    List<Feelings> temp = allFeelings.getFeelingsList();
                    if (temp == null) {
                        Log.e("update_info", s);
                        return;
                    }
                    Log.e("update_infos", s + " " + temp.size());

                    if(addTheDifferentOne(temp)) {
                        isUpdate = true;
                    }

                } catch (Exception e) {
                    Log.e("get mood", "wrong", e);
                }
            }
        });
        Log.e("start getFeelings", "success");
        return feelings1;
    }
}
