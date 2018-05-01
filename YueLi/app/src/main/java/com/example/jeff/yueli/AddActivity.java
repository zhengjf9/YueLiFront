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
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
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

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * Created by XDDN2 on 2018/3/2.
 */

public class AddActivity extends Fragment {
    private View totalView;
    public static final int SELECT_PHOTO = 2;
    private ImageView image, addPic;
    private MyApplication myApplication;
    private String imagePath = "";
    public AddActivity() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        totalView = inflater.inflate(R.layout.activity_addmood, container, false);
        myApplication = (MyApplication)getActivity().getApplication();
        initViews();
        MainActivity.MyTouchListener myTouchListener = new MainActivity.MyTouchListener() {
            @Override
            public void onTouchEvent(MotionEvent event) {
                // 处理手势事件
                if(null != getActivity().getCurrentFocus()){
                    /**
                     * 点击空白位置 隐藏软键盘
                     */
                    InputMethodManager mInputMethodManager = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
                     mInputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                     return;
                }
                getActivity().onTouchEvent(event);
                return;
            }
        };

        // 将myTouchListener注册到分发列表
        ((MainActivity)this.getActivity()).registerMyTouchListener(myTouchListener);
        return totalView;
    }

    private void initViews() {
        addPic = (ImageView) totalView.findViewById(R.id.add);
        image = (ImageView)totalView.findViewById(R.id.add);

        Button send = (Button)totalView.findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    OkHttpClient okHttpClient = myApplication.gethttpclient();
                    String url = "http://123.207.29.66:3009/api/feelings";
                    EditText con = (EditText) totalView.findViewById(R.id.edit);

                    if (con.getText().toString().equals("") || imagePath.equals("")) {
                        Toast.makeText(getActivity(), "内容或图片不能为空", Toast.LENGTH_SHORT).show();
                    }

                    MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
                    File file = new File(imagePath);
                    RequestBody body = RequestBody.create(MediaType.parse("image/*"), file);
                    requestBody.addFormDataPart("photo", file.getName(), body);

                    requestBody.addFormDataPart("longitude", (myApplication.getaMapLocation().getLongitude() + Math.random() / 500) + "")
                            .addFormDataPart("latitude", (myApplication.getaMapLocation().getLatitude() + Math.random() / 500) + "")
                            .addFormDataPart("content", con.getText().toString());
                    Request request = new Request.Builder().url(url).post(requestBody.build()).build();
                    okHttpClient.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {

                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            try {
                                if (response.code() == 200) {
                                    Toast.makeText(getContext(), response.message(), Toast.LENGTH_SHORT).show();
                                    String s = response.body().string();
                                    Gson gson = new Gson();
                                    Feelings f = gson.fromJson(s, Feelings.class);
                                    Log.e("id", f.getFeeling_id()+ " " + s);
                                    Intent intent = new Intent(getActivity(), MainActivity.class);
                                    startActivity(intent);
                                }

                            } catch (Exception e) {
                                Log.e("responce", "Wrong", e);
                            }
                        }
                    });
                } catch (Exception e) {
                    Log.e("update feelings", "wrong");
                }
            }
        });
        final TextView location = (TextView)totalView.findViewById(R.id.location);
        Observable<String> observable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                while (true) {
                    if (myApplication.getaMapLocation() != null) {
                        subscriber.onNext(myApplication.getaMapLocation().getAddress());
                    }
                    try {
                        if (myApplication.getaMapLocation() != null) {
                            subscriber.onNext(myApplication.getaMapLocation().getAddress());
                        }
                        Thread.sleep(2000);
                    } catch (Exception e) {
                        Log.e("sleep wrong", "!", e);
                    }

                }
                
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
                location.setText(s);
            }
        };
        observable.subscribe(observer);

        addPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE }, 1);
                } else {
                    openAlbum();
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
            image.setImageBitmap(pic);
            Log.d("checkSelectPath",imagePath);
        } else {
            Toast.makeText(getActivity(),"failed to get image", Toast.LENGTH_SHORT).show();
        }
    }









}
