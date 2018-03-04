package com.example.jeff.yueli;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by XDDN2 on 2018/3/1.
 */

public class Map  extends Fragment {
    private AMap aMap;
    private ArrayList<MarkerOptions> markeroptions;
    private List<Marker> markers;
    private List<spot> spots; // 地图上心情的信息
    public Map() {

    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.map, container, false);
        try {
            markers = new ArrayList<>();
            // 获取心情信息
            spots = getSpots();
            initMap(view, savedInstanceState);
            Log.e("Map", "is OK");
            //心情添加到地图上的函数
            addMapPoint();

        }catch (Exception e) {
            Log.e("Map", "Wrong", e);
        }

        return view;
    }

    // 初始化地图以及获取定位信息
    private void initMap(View view, Bundle savedInstanceState) {
        MapView mapView = (MapView) view.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        aMap = mapView.getMap();
        //禁止地图缩放和移动
        aMap.getUiSettings().setScrollGesturesEnabled(false);
        aMap.getUiSettings().setZoomGesturesEnabled(false);
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

    private void addMapPoint() {
        for (spot s : spots) {
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.setFlat(true);
            markerOptions.anchor(0.5f,0.5f);
            BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(s.getImage());
            markerOptions.icon(bitmapDescriptor);
            markerOptions.position(new LatLng(s.getLatitude(), s.getLongtitude()));
            Marker marker = aMap.addMarker(markerOptions);
            markeroptions.add(markerOptions);
        }
        markers = aMap.addMarkers(markeroptions, true);

    }


    // 网络访问获取Spot相关信息
    // 写这个函数是方便我把逻辑写下去，网络访问该怎么样还是怎么样
    List<spot> getSpots() {
        List<spot> spots1 = new ArrayList<>();
        return spots1;
    }
}
