package com.example.jeff.yueli;

import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.media.Image;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private AddActivity addActivity;
    private IndividualActivity individualActivity;
    private RecommendActivity recommendActivity;
    private TripActivity tripActivity;
    private Map map;
    private ViewPager vp;
    private List<Fragment> fragmentList = new ArrayList<>();
    private FragmentAdapter fragmentAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            Log.e("getIntoMain", "Successfully");
            initViews();

            fragmentAdapter = new FragmentAdapter(this.getSupportFragmentManager(), fragmentList);
            vp.setOffscreenPageLimit(5);
            vp.setAdapter(fragmentAdapter);
            vp.setCurrentItem(2);

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

        fragmentList.add(addActivity);
        fragmentList.add(individualActivity);
        fragmentList.add(recommendActivity);
        fragmentList.add(tripActivity);
        fragmentList.add(map);
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
            case 0:
                add.setImageResource(R.drawable.plus_cover);
                break;
            case 1:
                individual.setImageResource(R.drawable.male_user_96px_cover);
                break;
            case 2:
                recommend.setImageResource(R.drawable.map_marker_96px_cover);
                break;
            case 3:
                trip.setImageResource(R.drawable.photo_gallery_96px_cover);
                break;
            case 4:
                meet.setImageResource(R.drawable.map_marker_96px_cover);
                break;
        }
    }

    /**
     * 点击底部Text 动态修改ViewPager的内容
     */
    @Override
    public void onClick(View v) {
        try {
            switch (v.getId()) {
                case R.id.plus:
                    vp.setCurrentItem(0, true);
                    break;
                case R.id.individual:
                    vp.setCurrentItem(1, true);
                    break;
                case R.id.recommend:
                    vp.setCurrentItem(2, true);
                    break;
                case R.id.trip:
                    vp.setCurrentItem(3, true);
                    break;
                case R.id.meet:
                    vp.setCurrentItem(4, true);
                    break;
            }
        } catch (Exception e) {
            Log.e("jump", "wrong", e);
        }
    }

}
