package com.example.jeff.yueli;

/**
 * Created by xumuxin on 2018/3/3.
 */
import android.app.Application;

import com.amap.api.location.AMapLocation;

import org.litepal.LitePalApplication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class MyApplication extends LitePalApplication {
    private OkHttpClient httpClient = new OkHttpClient.Builder()
            .cookieJar(new CookieJar() {
        private final HashMap<String, List<Cookie>> cookieStore = new HashMap<>();
        //Tip：這裡key必須是String
        @Override
        public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
            cookieStore.put(url.host(), cookies);
        }
        @Override
        public List<Cookie> loadForRequest(HttpUrl url) {
            List<Cookie> cookies = cookieStore.get(url.host());
            return cookies != null ? cookies : new ArrayList<Cookie>();
        }
    })
            .addInterceptor(new HttpLoggingInterceptor()).build();
    private User user;

    private List<spot> spots;

    private int currentPos;
    private Feelings currentFeelings;

    private AMapLocation aMapLocation;

    public void setCurrentFeelings(Feelings currentFeelings) {
        this.currentFeelings = currentFeelings;
    }

    public Feelings getCurrentFeelings() {
        return currentFeelings;
    }

    public void setaMapLocation(AMapLocation aMapLocation) {
        this.aMapLocation = aMapLocation;
    }

    public void setCurrentPos(int currentPos) {
        this.currentPos = currentPos;
    }

    public int getCurrentPos() {
        return currentPos;
    }

    public void setSpots(List<spot> spots) {
        this.spots = spots;
    }

    public List<spot> getSpots() {
        return spots;
    }

    public AMapLocation getaMapLocation() {
        return aMapLocation;
    }
    public int eid;
    public User getUser() {return user;}
    public void setUser(User u) {user = u;}
    public OkHttpClient gethttpclient() {return httpClient;}

}
