package com.example.jeff.yueli;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.ColorSpace;
import android.os.Build;
import android.os.PersistableBundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.media.Image;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private AddActivity addActivity;
    private IndividualActivity individualActivity;
    private RecommendActivity recommendActivity;
    private TripActivity tripActivity;
    private Map map;
    private ViewPager vp;
    private List<Fragment> fragmentList = new ArrayList<>();
    private FragmentAdapter fragmentAdapter;
    public ViewPager getVp() {return vp;}



    @Override
    public void onCreate(Bundle savedInstanceState) {

        try {
            super.onCreate(savedInstanceState);
            Connector.getDatabase();
            /*清空数据库
            DataSupport.deleteAll(trashRecord.class);
            DataSupport.deleteAll(trashJournalItem.class);
            */
            MyApplication application = (MyApplication)getApplication();
            OkHttpClient httpClient = application.gethttpclient();
            User user = application.getUser();
            String url = "http://123.207.29.66:3009/api/users/"+String.valueOf(user.getuserid());
            Request request = new Request.Builder().url(url).build();
            httpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {}
                String string=null;
                @Override
                public void onResponse(Call call, final Response response) throws IOException {

                    try {
                        string = response.body().string();
                        Gson gson = new Gson();
                        Type Usertype = new TypeToken<Result<User>>(){}.getType();
                        Result<User> userresult = gson.fromJson(string, Usertype);
                        User u = userresult.data;
                        // Toast.makeText(LoginActivity.this, String.valueOf(loginresult.data.getuserid()), Toast.LENGTH_SHORT).show();
                        int rescode = response.code();
                        if (rescode == 200) {

                            MyApplication application = (MyApplication)getApplication();
                            User nu = new User();
                            nu.setuserid(application.getUser().getuserid());
                            nu.setnickname(application.getUser().getnickname());
                            nu.setSignature(u.getSignature());
                            application.setUser(nu);

                        } else {

                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }
            });

            int i = getIntent().getIntExtra("id", -1);
            setContentView(R.layout.activity_main);
            Log.e("getIntoMain", "Successfully");
            initViews();
            verifyPermission(this);
            fragmentAdapter = new FragmentAdapter(this.getSupportFragmentManager(), fragmentList);
            vp.setOffscreenPageLimit(5);
            vp.setAdapter(fragmentAdapter);
            if (i == -1) {
                i = 1;
            }
            vp.setCurrentItem(i);
            changeTextColor(i);
            //ViewPager的监听事件
            vp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                /*此方法在页面被选中时调用*/
                    changeTextColor(position);
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                /*此方法是在状态改变的时候调用，其中arg0这个参数有三种状态（0，1，2）。
                arg0 ==1的时辰默示正在滑动，
                arg0==2的时辰默示滑动完毕了，
                arg0==0的时辰默示什么都没做。*/
                }
            });
        } catch (Exception e) {
            Log.e("main", "Wrong", e);
        }

    }

    private void initViews() {
        ImageView recommend = (ImageView) findViewById(R.id.recommend);
        ImageView add = (ImageView) findViewById(R.id.plus);
        ImageView individual = (ImageView) findViewById(R.id.individual);
        ImageView trip = (ImageView) findViewById(R.id.trip);
        ImageView meet = (ImageView) findViewById(R.id.meet);

        recommend.setOnClickListener(this);
        add.setOnClickListener(this);
        individual.setOnClickListener(this);
        trip.setOnClickListener(this);
        meet.setOnClickListener(this);

        vp = (ViewPager)findViewById(R.id.viewPager);
        addActivity = new AddActivity();
        individualActivity = new IndividualActivity();
        recommendActivity = new RecommendActivity();
        tripActivity = new TripActivity();
        map = new Map();

        fragmentList.add(recommendActivity);
        fragmentList.add(map);
        fragmentList.add(addActivity);
        fragmentList.add(tripActivity);
        fragmentList.add(individualActivity);
    }




    public class FragmentAdapter extends FragmentPagerAdapter {

        List<Fragment> fragmentList = new ArrayList<Fragment>();

        public FragmentAdapter(FragmentManager fm, List<Fragment> fragmentList) {
            super(fm);
            this.fragmentList = fragmentList;
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

    }

    void changeTextColor(int pos) {
        ImageView recommend = (ImageView) findViewById(R.id.recommend);
        ImageView add = (ImageView) findViewById(R.id.plus);
        ImageView individual = (ImageView) findViewById(R.id.individual);
        ImageView trip = (ImageView) findViewById(R.id.trip);
        ImageView meet = (ImageView) findViewById(R.id.meet);
        recommend.setImageResource(R.drawable.marker_96px);
        add.setImageResource(R.drawable.plus);
        individual.setImageResource(R.drawable.male_user_96px);
        trip.setImageResource(R.drawable.photo_gallery_96px);
        meet.setImageResource(R.drawable.map_marker_96px);
        switch (pos) {
            case 2:
                add.setImageResource(R.drawable.plus_cover);

                break;
            case 4:
                individual.setImageResource(R.drawable.male_user_96px_cover);
                break;
            case 0:
                recommend.setImageResource(R.drawable.marker_96px_cover);
                break;
            case 3:
                trip.setImageResource(R.drawable.photo_gallery_96px_cover);
                break;
            case 1:
                meet.setImageResource(R.drawable.map_marker_96px_cover);
                break;
        }
    }

    private static void verifyPermission(Activity activity) {
        List<String> permissions = new ArrayList<>();
        permissions.add("android.permission.INTERNET");
        permissions.add("android.permission.WRITE_EXTERNAL_STORAGE");
        permissions.add("android.permission.ACCESS_NETWORK_STATE");
        permissions.add("android.permission.ACCESS_WIFI_STATE");
        permissions.add("android.permission.READ_PHONE_STATE");
        permissions.add("android.permission.ACCESS_COARSE_LOCATION");
        for (String s : permissions) {
            int p = ActivityCompat.checkSelfPermission(activity, s);
            if (p != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, new String[] {s}, PackageManager.PERMISSION_GRANTED);
            }
        }
    }

    /**
     * 点击底部Text 动态修改ViewPager的内容
     */
    @Override
    public void onClick(View v) {
        // 测试按钮
        int p = 0;
        try {
            switch (v.getId()) {
                case R.id.plus:
                    vp.setCurrentItem(2, true);
                    p = 2;
                    break;
                case R.id.individual:
                    vp.setCurrentItem(4, true);
                    p = 4;
                    break;
                case R.id.recommend:
                    vp.setCurrentItem(0, true);
                    p = 0;
                    break;
                case R.id.trip:
                    vp.setCurrentItem(3, true);
                    p = 3;
                    break;
                case R.id.meet:
                    vp.setCurrentItem(1, true);
                    p = 1;
                    break;
            }
        } catch (Exception e) {
            Log.e("jump", "wrong", e);
        }
//        Log.e("click function",String.valueOf(p));
    }

}
