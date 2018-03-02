package com.example.jeff.yueli;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.MyLocationStyle;

/**
 * Created by XDDN2 on 2018/3/1.
 */

public class Map  extends Fragment {
    private AMap aMap;

    public Map() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.map, container, false);
        try {
            MapView mapView = (MapView) view.findViewById(R.id.map);
            mapView.onCreate(savedInstanceState);// 此方法必须重写
            aMap = mapView.getMap();
//禁止地图缩放和移动
            aMap.getUiSettings().setScrollGesturesEnabled(false);
            aMap.getUiSettings().setZoomGesturesEnabled(false);
            //设置缩放比例
            aMap.moveCamera(CameraUpdateFactory.zoomTo(500));
            Log.e("enter", "map");
            // 实现定位
//            MyLocationStyle myLocationStyle;
//            myLocationStyle = new MyLocationStyle();
//
//            aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
            MyLocationStyle myLocationStyle;
            myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
            myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW) ;//连续定位、且将视角移动到地图中心点，定位蓝点跟随设备移动。（1秒1次定位）
            myLocationStyle.strokeColor(Color.argb(0,0,0,0));
            myLocationStyle.radiusFillColor(Color.argb(0,0,0,0));
            aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
//            aMap.getUiSettings().setMyLocationButtonEnabled(true); //设置默认定位按钮是否显示，非必需设置。
            aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        }catch (Exception e) {
            Log.e("Map", "Wrong", e);
        }

        return view;
    }


}
