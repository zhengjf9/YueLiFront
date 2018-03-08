package com.example.jeff.yueli;

/**
 * Created by xumuxin on 2018/3/3.
 */
import android.app.Application;

import com.amap.api.location.AMapLocation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class MyApplication extends Application {
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

    private AMapLocation aMapLocation;

    public void setaMapLocation(AMapLocation aMapLocation) {
        this.aMapLocation = aMapLocation;
    }

    public AMapLocation getaMapLocation() {
        return aMapLocation;
    }

    public User getUser() {return user;}
    public void setUser(User u) {user = u;}
    public OkHttpClient gethttpclient() {return httpClient;}

}
