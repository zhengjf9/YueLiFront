package com.example.jeff.yueli;

import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import rx.*;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.app.Activity.RESULT_OK;
import static com.example.jeff.yueli.AddActivity.SELECT_PHOTO;

/**
 * Created by XDDN2 on 2018/3/2.
 */

public class IndividualActivity extends Fragment {
    private List<DateInfo> dataInfoList = new ArrayList<>();
    private List<java.util.Map<String, String>> trashDatas =
            new ArrayList<java.util.Map<String, String>>();//草稿箱的数据
    private List<java.util.Map<String, String>> launchDatas =
            new ArrayList<java.util.Map<String, String>>();//已发布的数据
    private int mnum;
    private int tripnum;
    private int fannum;
    private String imagePath;
    private ImageView image;
    private ImageView bg;
    private User user;
    private int which = 0;


    public IndividualActivity() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_individual, container, false);
        MyApplication application = (MyApplication) getActivity().getApplication();
        OkHttpClient httpClient = application.gethttpclient();
        user = application.getUser();
        final CustomRecyclerView myRecView = (CustomRecyclerView) view.findViewById(R.id.outer_recyclerview);
        final DateInfoAdapter myAdapter = new DateInfoAdapter(getContext(),dataInfoList);
        myAdapter.notifyDataSetChanged();
        myRecView.setLayoutManager(new LinearLayoutManager(getContext()));
        myRecView.setAdapter(myAdapter);
       // myRecView.setVisibility(View.INVISIBLE);//心情页面暂时设置不可见
        Button mood = view.findViewById(R.id.mood_title);
        Button journey = view.findViewById(R.id.journey_title);
        TextView name = view.findViewById(R.id.name);
        final Button trash = view.findViewById(R.id.trash);
        final Button launch = view.findViewById(R.id.launch);
        Button like = view.findViewById(R.id.like);
        Button letter = view.findViewById(R.id.letter);
        Button menu = view.findViewById(R.id.menu);
        final TextView moodnum = view.findViewById(R.id.mood_num);
        TextView tnum = view.findViewById(R.id.journey_num);
        TextView fans = view.findViewById(R.id.fans_num);
        TextView sig = view.findViewById(R.id.signaure);
        bg = view.findViewById(R.id.background);
        bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                which = 1;
                if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE }, 1);
                } else {
                    openAlbum();
                }
            }
        });
        image = view.findViewById(R.id.avator);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                which = 0;
                if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE }, 1);
                } else {
                    openAlbum();
                }
            }
        });
        sig.setText(user.getSignature());
        name.setText(user.getnickname());


        trash.setVisibility(View.INVISIBLE);
        launch.setVisibility(View.INVISIBLE);
        final CustomRecyclerView trashRecView = (CustomRecyclerView)view.findViewById(R.id.trash_recyclerview);
        final JourneyItemAdapter trashAdapter = new JourneyItemAdapter(getContext(), trashDatas);
       trashRecView.setLayoutManager(new LinearLayoutManager(getContext()));


        //trashRecView.addHeaderView(headerView);
        trashRecView.setAdapter(trashAdapter);
        trashRecView.setVisibility(View.INVISIBLE);
        final CustomRecyclerView launchRecView = (CustomRecyclerView)view.findViewById(R.id.launch_recyclerview);
        final JourneyItemAdapter launchAdapter = new JourneyItemAdapter(getContext(), launchDatas);
        initDatas(moodnum,fans,tnum,launchAdapter,trashAdapter,myAdapter);
        launchAdapter.setOnItemClickLitener(new OnItemClickLitener()
        {

            @Override
            public void onItemClick(View view, int position)
            {
                Intent intent = new Intent(getActivity(), JourneyDetailActivity.class);
                intent.putExtra("travel_id",launchDatas.get(position).get("travel_id"));
                intent.putExtra("favorited",Boolean.valueOf(launchDatas.get(position).get("favorited")));
                startActivity(intent);
            }
            @Override
            public void onItemLongClick(View view, int position)
            {
                //Todo
                //myAdapter.removeData(position);
            }
        });
        launchRecView.setLayoutManager(new LinearLayoutManager(getContext()));
        launchRecView.setAdapter(launchAdapter);
        launchRecView.setVisibility(View.INVISIBLE);
        //launchRecView.addHeaderView(headerView);

        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AttentionCollectActivity.class);
                startActivity(intent);

            }
        });
        letter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), InfoActivity.class);
                startActivity(intent);

            }
        });
//        getBaseData();
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
        mood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trashRecView.setVisibility(View.INVISIBLE);
                launchRecView.setVisibility(View.INVISIBLE);
                myRecView.setVisibility(View.VISIBLE);
                trash.setVisibility(View.INVISIBLE);
                launch.setVisibility(View.INVISIBLE);

            }
        });
        journey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myRecView.setVisibility(View.INVISIBLE);
                trashRecView.setVisibility(View.INVISIBLE);
                launchRecView.setVisibility(View.VISIBLE);
                trash.setVisibility(View.VISIBLE);
                launch.setVisibility(View.VISIBLE);
            }
        });
        launch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myRecView.setVisibility(View.INVISIBLE);
                trashRecView.setVisibility(View.INVISIBLE);
                launchRecView.setVisibility(View.VISIBLE);
            }
        });
        trash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myRecView.setVisibility(View.INVISIBLE);
                launchRecView.setVisibility(View.INVISIBLE);
                trashRecView.setVisibility(View.VISIBLE);
            }
        });

        return view;
    }
    private void getBaseData() {
        MyApplication myApplication = (MyApplication)getActivity().getApplication();
        OkHttpClient okHttpClient = myApplication.gethttpclient();
        String url = "http://123.207.29.66:3009/api/users/" + user.getuserid() + "?photo=%60avatar%60";
        Request request = new Request.Builder().url(url).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.code() == 200) {
                    final byte[] inputStream = response.body().bytes();

                    rx.Observable<Bitmap> observable = Observable.create(new Observable.OnSubscribe<Bitmap>() {
                        @Override
                        public void call(Subscriber<? super Bitmap> subscriber) {
                            Bitmap bitmap = BitmapFactory.decodeByteArray(inputStream, 0, inputStream.length);
                            Log.e("setAvator", "start");
                            subscriber.onNext(bitmap);
                            subscriber.onCompleted();
                        }
                    }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread());
                    Observer<Bitmap> observer = new Observer<Bitmap>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(Bitmap bitmap) {
                            image.setImageBitmap(bitmap);
                            Log.e("setAvator", "success");
                        }
                    };
                    observable.subscribe(observer);
                    Log.e("get_user_avator", "OK!");
                } else {
                    Log.e("get_user_avator", "Fail!");
                }
            }
        });
        url = "http://123.207.29.66:3009/api/users/" + user.getuserid() + "?photo=%60bg%60";
        request = new Request.Builder().url(url).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.code() == 200) {
                    final byte[] inputStream = response.body().bytes();

                    rx.Observable<Bitmap> observable = Observable.create(new Observable.OnSubscribe<Bitmap>() {
                        @Override
                        public void call(Subscriber<? super Bitmap> subscriber) {
                            Bitmap bitmap = BitmapFactory.decodeByteArray(inputStream, 0, inputStream.length);
                            subscriber.onNext(bitmap);
                            subscriber.onCompleted();
                        }
                    }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread());
                    Observer<Bitmap> observer = new Observer<Bitmap>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(Bitmap bitmap) {
                            bg.setImageBitmap(bitmap);
                        }
                    };
                    observable.subscribe(observer);
                    Log.e("get_user_avator", "OK!");
                } else {
                    Log.e("get_user_avator", "Fail!");
                }
            }
        });
    }

    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent,SELECT_PHOTO);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch(requestCode) {
            case 1:
                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlbum();
                } else {
                    Toast.makeText(getActivity(),"You denied the permission",Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }

    @Override
    public  void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode, data);
        switch (requestCode) {
            case SELECT_PHOTO:
                if (resultCode == RESULT_OK) {
                    if (Build.VERSION.SDK_INT >= 19) {
                        handleImageOnKitKat(data);
                    } else {
                        handleImageBeforeKitKat(data);
                    }
                }
        }
    }
    private void handleImageOnKitKat(Intent data) {

        Uri uri = data.getData();

        if (DocumentsContract.isDocumentUri(getActivity(),uri)) {
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];
                String selecttion = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,selecttion);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),Long.valueOf(docId));
                imagePath = getImagePath(contentUri,null);
            }

        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            imagePath = getImagePath(uri,null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            imagePath = uri.getPath();
        }
        MyApplication myApplication = (MyApplication)getActivity().getApplication();
        OkHttpClient okHttpClient = myApplication.gethttpclient();
        String url = "http://123.207.29.66:3009/api/users/" + user.getuserid();
        MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
        File file = new File(imagePath);
        RequestBody body = RequestBody.create(MediaType.parse("image/*"), file);
        if (which == 0) {
            requestBody.addFormDataPart("avatar", file.getName(), body);
        } else {
            requestBody.addFormDataPart("bg", file.getName(), body);
        }
        Request request = new Request.Builder().url(url).patch(requestBody.build()).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                if (response.code() == 200) {
                    Log.e("change_user_Image", "OK!");
                } else {
                    Log.e("change_user_Image", "Fail!");
                }
            }
        });
        displayImage(imagePath);
    }

    private void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri,null);
        displayImage(imagePath);
    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        Cursor cursor = getActivity().getContentResolver().query(uri,null,selection,null,null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    private void displayImage(String imagePath) {
        if (imagePath != null) {
//            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 4;
            Bitmap pic = BitmapFactory.decodeFile(imagePath, options);
            if (which == 0) {
                image.setImageBitmap(pic);
            } else {
                bg.setImageBitmap(pic);
            }
            Log.d("checkSelectPath",imagePath);
        } else {
            Toast.makeText(getActivity(),"failed to get image", Toast.LENGTH_SHORT).show();
        }
    }

    public void initDatas(final TextView m, final TextView f, final TextView t,
    final JourneyItemAdapter a, final JourneyItemAdapter b,final DateInfoAdapter c) {
        MyApplication application = (MyApplication) getActivity().getApplication();
        OkHttpClient httpClient = application.gethttpclient();
        final User user = application.getUser();
        String url = "http://123.207.29.66:3009/api/feelings?user_id="+String.valueOf(user.getuserid()) ;
        Request request = new Request.Builder().url(url).build();


        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            String string = null;

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                try {
                    string = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Gson gson = new Gson();
                Mood result = gson.fromJson(string, Mood.class);
                List<Mood.xinqing> moodlist = result.getdata();


                for (int i = 0; i < moodlist.size(); ) {
                    Mood.xinqing t = moodlist.get(i);
                    DateInfo dateInfo = new DateInfo();//指的是某一天的标题，包括第几天，日期，星期。

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Calendar calendar = Calendar.getInstance();
                    Date date = null;
                    try {
                        date = sdf.parse(t.gettime().substring(0, 10));
                        calendar.setTime(date);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    dateInfo.setDate(t.gettime().substring(0, 10));
                    List<ContentInfo> contentInfoList = new ArrayList<>();//指的是这一天所有的心情
                    Date tmpdate = date;
                    while (tmpdate == date) {
                        ContentInfo contentInfo = new ContentInfo();
                        String location = String.valueOf(t.getlatitude()) + ", " + String.valueOf(t.getlongitude());
                        contentInfo.setLocation(location);//contentInfo指一条心情
                        contentInfo.setComment(t.getcontent());
                        contentInfoList.add(contentInfo);
                        i++;
                        if (i < moodlist.size()) {
                            t = moodlist.get(i);
                        } else break;
                    }
                    if (response.code() == 200)
                        mnum = moodlist.size();

                    dateInfo.setContentInfoList(contentInfoList);//将这一天的所有游记设置成标题的一个成员
                    dataInfoList.add(dateInfo);

                    ;//将一天一天的数据push进dataInfoList
                    // dataInfoList就是最后要的数据
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        c.notifyDataSetChanged();
                        int rescode = response.code();
                        if (rescode == 200) {

                            m.setText(String.valueOf(mnum));
                        }
                        // Toast.makeText(getActivity().getApplicationContext(), "心情"+moodlist.size(), Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });

        url="http://123.207.29.66:3009/api/travels?user_id="+String.valueOf(user.getuserid());
        request = new Request.Builder().url(url).build();
        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }
            String string=null;
            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                try {
                    string = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Gson gson = new Gson();
                Travel result = gson.fromJson(string,Travel.class);
                final List<Travel.trip> travellist =  result.gettrips();

                for (int i = 0; i < travellist.size(); i++) {
                    Travel.trip t = travellist.get(i);
                    Map<String, String> temp = new LinkedHashMap<String, String>();
                    temp.put("user_id", String.valueOf(t.getuserid()));
                    temp.put("title", t.gettitle());
                    temp.put("firstday", t.getFirst_day());
                    temp.put("duration",  String.valueOf(t.getduration()));
                    temp.put("location", t.getlocation());
                    temp.put("name", t.getnickname());

                    temp.put("like_num", String.valueOf(t.getfavoritecount()));
                    temp.put("comment_num", String.valueOf(t.getComment_count()));

                    temp.put("travel_id",String.valueOf(t.gettravelid()));
                    temp.put("favorited",String.valueOf(t.getfavorited()));
                    launchDatas.add(temp);

                }
                final int rescode = response.code();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        a.notifyDataSetChanged();
                        //  Toast.makeText(getActivity().getApplicationContext(), "TestRes", Toast.LENGTH_SHORT).show();
                        if (rescode == 200) {
                            tripnum = travellist.size();
                            t.setText(String.valueOf(tripnum));
                            // Toast.makeText(getActivity().getApplicationContext(),"travel_id is " + String.valueOf(travellist.get(0).gettravelid())  , Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity().getApplicationContext(), "try", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        //获取粉丝数量
        url = "http://123.207.29.66:3009/api/users/"+String.valueOf(user.getuserid())+"/followers";
        request = new Request.Builder().url(url).build();
        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            String string = null;

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                try {
                    string = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Gson gson = new Gson();
                        Followers result = gson.fromJson(string, Followers.class);
                        List<Followers.follow> followerlist = result.getdata();
                        fannum = followerlist.size();
                        f.setText(String.valueOf(fannum));
                    }
                });
            }
        });
        // 草稿箱
        java.util.Map<String, String> temp1 = new LinkedHashMap<String, String>();
        temp1.put("title", "上海:梦中城");
        temp1.put("firstday", "2018-3-7");
        temp1.put("duration",  "3天");
        temp1.put("location", "上海");
        temp1.put("name", "旅行者");
        temp1.put("like_num", "99");
        temp1.put("comment_num", "99");
        trashDatas.add(temp1);
        trashDatas.add(temp1);
        b.notifyDataSetChanged();

    }
    public void showDialog(){
        final BottomSheetDialog dialog=new BottomSheetDialog(getContext());
        View dialogView= LayoutInflater.from(getContext())
                .inflate(R.layout.logout,null);
        TextView logout= (TextView) dialogView.findViewById(R.id.logout);
        TextView cancel= (TextView) dialogView.findViewById(R.id.cancel);
        dialog.setContentView(dialogView);
        dialog.show();
        logout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
        {

            String url="http://123.207.29.66:3009/api/users/logout";

            MyApplication application = (MyApplication)getActivity().getApplication();
            OkHttpClient httpClient = application.gethttpclient();
            FormBody formBody = new FormBody
                    .Builder()
                    .build();
            Request request = new Request.Builder().post(formBody).url(url).build();
            httpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                }
                String string=null;
                @Override
                public void onResponse(Call call, final Response response) throws IOException {

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //  Toast.makeText(getActivity().getApplicationContext(), "TestRes", Toast.LENGTH_SHORT).show();
                            if (response.code() == 200) {
                                Toast.makeText(getActivity(), "退出成功", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getContext(), LoginActivity.class);
                                startActivity(intent);
                                dialog.dismiss();
                            } else {}
                        }
                    });

                }
            });


        }
        });
        cancel.setOnClickListener(new View.OnClickListener()
        {
            @Override public void onClick(View v)
        {
            dialog.dismiss();
        }
        }
        );

    }
}
