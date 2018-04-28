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
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.jeff.yueli.AddActivity.SELECT_PHOTO;

public class PostTrip extends AppCompatActivity {

    private ImageView imageView;
    private String imagePath;
    private List<DateInfo> dataInfoList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.journey_edit);
        Button s = findViewById(R.id.send);
        imageView = findViewById(R.id.pic);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(PostTrip.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(PostTrip.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE }, 1);
                } else {
                    openAlbum();
                }
            }
        });
        final EditText e = findViewById(R.id.title);
       // final String t = e.getText().toString();
        final EditText ed = findViewById(R.id.edit);//这里全部要去掉

        final CustomRecyclerView myRecView = (CustomRecyclerView) findViewById(R.id.outer_recyclerview);
        final DateInfoAdapter myAdapter = new DateInfoAdapter(this,dataInfoList);
        myAdapter.notifyDataSetChanged();
        myRecView.setLayoutManager(new LinearLayoutManager(this));
        myRecView.setAdapter(myAdapter);
        initData();//类似individualactovity,从本地读取

        s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final MyApplication application = (MyApplication) getApplication();
                final OkHttpClient httpClient = application.gethttpclient();
                final User user = application.getUser();
                int spotid = application.getSpots().get(application.getCurrentPos()).getID();
                String url = "http://123.207.29.66:3009/api/travels";
                String t = e.getText().toString();
                Request request;
                if (imagePath != "") {
                    MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
                    File file = new File(imagePath);
                    RequestBody body = RequestBody.create(MediaType.parse("image/*"), file);
                    requestBody.addFormDataPart("cover", file.getName(), body)
                            .addFormDataPart("title", t);
                    request = new Request.Builder().post(requestBody.build()).url(url).build();
                } else {
                    FormBody formBody = new FormBody.Builder().add("title", t).build();
                    request = new Request.Builder().url(url).build();
                }





                httpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                    }
                    String string=null;
                    @Override
                    public void onResponse(Call call, final Response response) throws IOException {
                        try {
                            string = response.body().string();
                            Gson gson = new Gson();
                            Type logintype = new TypeToken<Result<reviewback>>(){}.getType();
                            Result<reviewback> loginresult = gson.fromJson(string, logintype);
                            reviewback denglu = loginresult.data;
                            // Toast.makeText(LoginActivity.this, String.valueOf(loginresult.data.getuserid()), Toast.LENGTH_SHORT).show();
                            int rescode = response.code();
                            if (rescode == 200) {
                                application.eid=denglu.getComment_id();
                                String url = "http://123.207.29.66:3009/api/travels/"+String.valueOf(denglu.getComment_id())+"/travel-records";
                                FormBody formBody = new FormBody
                                        .Builder()
                                        .add("spot_id","1")//设置参数名称和参数值
                                        .add("content",ed.getText().toString())
                                        .build();
                                Request request = new Request.Builder().post(formBody).url(url).build();
                                httpClient.newCall(request).enqueue(new Callback() {
                                    @Override
                                    public void onFailure(Call call, IOException e) {
                                    }
                                    String string=null;
                                    @Override
                                    public void onResponse(Call call, final Response response) throws IOException {
                                        try {
                                            string = response.body().string();
                                            Gson gson = new Gson();
                                            Type logintype = new TypeToken<Result<reviewback>>(){}.getType();
                                            Result<reviewback> loginresult = gson.fromJson(string, logintype);
                                            final String m=loginresult.msg;
                                            reviewback denglu = loginresult.data;
                                            // Toast.makeText(LoginActivity.this, String.valueOf(loginresult.data.getuserid()), Toast.LENGTH_SHORT).show();

                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                int rescode = response.code();
                                                if (rescode == 200) {
                                                    //application.eid=denglu.getComment_id();
                                                    Toast.makeText(PostTrip.this, "发布游记成功", Toast.LENGTH_SHORT).show();

                                                    Intent i = new Intent();
                                                    i.setClass(PostTrip.this, MainActivity.class);
                                                    //一定要指定是第几个pager，因为要跳到ThreeFragment，这里填写2
                                                    i.putExtra("id", 4);
                                                    startActivity(i);
                                                } else {
                                                    Toast.makeText(PostTrip.this, String.valueOf(rescode), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }
                                });
                            } else {
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                            }
                        });
                    }
                });


            }
        });
    }
    void initData(){

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
                    Toast.makeText(PostTrip.this,"You denied the permission",Toast.LENGTH_SHORT).show();
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

        if (DocumentsContract.isDocumentUri(PostTrip.this,uri)) {
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

        displayImage(imagePath);
    }

    private void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri,null);
        displayImage(imagePath);
    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        Cursor cursor = getContentResolver().query(uri,null,selection,null,null);
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
            imageView.setImageBitmap(pic);
            TextView textView = findViewById(R.id.pic_hint);
            textView.setVisibility(View.GONE);
            Log.d("checkSelectPath",imagePath);
        } else {
            Toast.makeText(this,"failed to get image", Toast.LENGTH_SHORT).show();
        }
    }
}
