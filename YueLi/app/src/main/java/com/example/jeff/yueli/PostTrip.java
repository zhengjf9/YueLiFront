package com.example.jeff.yueli;

import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ColorSpace;
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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

import static android.widget.PopupMenu.*;
import static com.example.jeff.yueli.AddActivity.SELECT_PHOTO;

public class PostTrip extends AppCompatActivity {
    private final long ONE_DAY_MS=24*60*60*1000;
    private ImageView imageView;
    private String imagePath;
    public List<java.util.Map<String, String>> dateDatas =
            new ArrayList<java.util.Map<String, String>>();//游记开头日期
    public List<java.util.Map<String, String>> contentDatas =
            new ArrayList<java.util.Map<String, String>>();//游记内容
    Button btn;
    private List<ParentInfo> dataInfoList = new ArrayList<>();
    SimpleDateFormat   formatter   =   new   SimpleDateFormat   ("yyyy年MM月dd日");
    Date curDate =  new Date(System.currentTimeMillis());
    Calendar calendar = Calendar.getInstance();

    String testDate = "2018年5月1日";
    /*假装今天是几号*/
    //Date curDate = findDate(testDate);


    Date firstDate;
    final String  strDate   =   formatter.format(curDate);

    EditText e;
    private int daynum;
    TextView first, dur;
    // trashItem被Click的时候不是-1，
    // application.trashItemId;
    // 退出时设为-1
    private Date findDate(String str) {
        Calendar calendar = Calendar.getInstance();
        Date date = new Date();
        try {
            date = formatter.parse(str);
            calendar.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
    private String findWeekX(String tmp) {
        Calendar calendar = Calendar.getInstance();
        try {
            Date date = formatter.parse(tmp);
            calendar.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String[] dayofweek = new String[]{"周日", "周一", "周二", "周三", "周四",
                "周五", "周六"};
        String wd = dayofweek[calendar.get(Calendar.DAY_OF_WEEK) - 1];
        return wd;
    }
    private void saveTrip() {
        MyApplication application = (MyApplication) getApplication();
        //当前的内容未被归到一个已有的游记中，需要先创建一个游记
        if (application.getTrashItemId() == -1) {
            trashJournalItem journal  = new trashJournalItem();
            journal.setduration(Integer.parseInt(application.tmpdur));
            journal.setfirstday(strDate);
            journal.setlocation(application.curTrashRecords.get(0).getlocation());

            String title = e.getText().toString()==""?"未命名":e.getText().toString();
            journal.settitle(application.tmptitle);
            journal.settravelid(23333);
            journal.setuser_id(application.getUser().getuserid());
            journal.save();

            List<trashJournalItem> savedJ = DataSupport.where("travel_id = ?", "23333").find(trashJournalItem.class);
            trashJournalItem J = savedJ.get(0);
            int t = (int)J.getid();
            application.setTrashItemId(t);

            String tmp = String.valueOf(t);
            J.settravelid(t);
            J.save();

            for (int i = 0; i < application.curTrashRecords.size(); i++) {
                application.curTrashRecords.get(i).settravelid(t);
                application.curTrashRecords.get(i).save();
            }


        } else {
            trashJournalItem thisJournal = DataSupport.find(trashJournalItem.class,application.getTrashItemId() );
            thisJournal.settitle(application.tmptitle);
            thisJournal.setduration(Integer.parseInt(application.tmpdur));
            thisJournal.save();
            List<trashRecord> newrecord = new ArrayList<>();
            for (int i = 0; i < application.curTrashRecords.size(); i++) {
                trashRecord n = new trashRecord();
                trashRecord old = application.curTrashRecords.get(i);

                n.setrecord_id(old.getrecord_id());

                n.settravelid(application.getTrashItemId());
                n.setday(old.getday());
                n.setduration(old.getduration());
                n.setlocation(old.getlocation());
                n.settext(old.gettext());
                newrecord.add(n);

            }
            System.out.println(application.getTrashItemId());
            int yuanben = DataSupport.where("travel_id = ?", String.valueOf(application.getTrashItemId())).find(trashRecord.class).size();
            System.out.println("原本有："+yuanben);
            int delnum = DataSupport.deleteAll(trashRecord.class,"travel_id = ?",String.valueOf(application.getTrashItemId()) );
            System.out.println("删除了："+delnum);
            application.curTrashRecords.clear();
            System.out.println("还有："+newrecord.size());
            for (int i = 0; i < newrecord.size(); i++) {
                newrecord.get(i).save();
            }
            int gengxin = DataSupport.where("travel_id = ?", String.valueOf(application.getTrashItemId())).find(trashRecord.class).size();
            System.out.println("更新后有："+gengxin);

            /*

            int t  = DataSupport.deleteAll(trashRecord.class,"travel_id = ?", String.valueOf(-1));
            System.out.println(t);
            for (int i = 0; i < newrecord.size(); i++) {
                newrecord.get(i).save();
            }
            System.out.println(DataSupport.where("travel_id = ?",String.valueOf(application.getTrashItemId())).find(trashRecord.class).size());
        */
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Connector.getDatabase();
        setContentView(R.layout.journey_edit);
        final int from;
        final int trashItemId;
        final String locationForOneDay, textForOneDay;

        from = (int) getIntent().getSerializableExtra("From");
        e = findViewById(R.id.title);
        first = findViewById(R.id.firstday);
        dur = findViewById(R.id.duration);

        if (from == 0) {
            // 从草稿箱点+号，新建
            MyApplication application = (MyApplication) getApplication();
            application.curTrashRecords.clear();
            application.tmptitle = "";
            application.setTrashItemId(-1);

            application.tmpfirst = (strDate);
            application.tmpdur = "1";
            application.tmptoday=strDate;

        } else if (from == 1){
            // 新增了day之后，回到草稿箱
            MyApplication application = (MyApplication) getApplication();
            locationForOneDay = (String)getIntent().getSerializableExtra("locationForOneDay");
            textForOneDay = (String)getIntent().getSerializableExtra("textForOneDay");

            trashRecord tmp = new trashRecord();
            tmp.settext(textForOneDay);
            tmp.setday(strDate);
            //String duration = dur.getText().toString();
            //Toast.makeText(PostTrip.this, String.valueOf(Integer.parseInt(duration.substring(0,duration.length()-1))+1), Toast.LENGTH_LONG).show();
            tmp.setduration(Integer.parseInt(application.tmpdur));
            tmp.setrecord_id(application.curTrashRecords.size());
            tmp.setlocation(locationForOneDay);
            tmp.settravelid(application.getTrashItemId());
            tmp.setrecord_id(application.curTrashRecords.size());

            application.curTrashRecords.add(tmp);

            //Toast.makeText(PostTrip.this, String.valueOf(application.curTrashRecords.size()), Toast.LENGTH_SHORT).show();
            //

        } else if (from == 2) {
            // 从已有草稿进入
            MyApplication application = (MyApplication) getApplication();
            String t = (String)getIntent().getSerializableExtra("travel_id");
            System.out.println("传入的ID:"+t);
           // Toast.makeText(PostTrip.this, t, Toast.LENGTH_SHORT).show();
            int i = Integer.parseInt(t);
            application.setTrashItemId(i);
            trashJournalItem journeltofindtitle = DataSupport.find(trashJournalItem.class,i);
            application.tmptitle = journeltofindtitle.gettitle();

            application.curTrashRecords = DataSupport.where("travel_id = ?", String.valueOf(i)).find(trashRecord.class);

            application.tmpfirst=(journeltofindtitle.getFirst_day());
            try {firstDate = formatter.parse(journeltofindtitle.getFirst_day());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Calendar fromCalendar = Calendar.getInstance();
            fromCalendar.setTime(firstDate);
            fromCalendar.set(Calendar.HOUR_OF_DAY, 0);
            fromCalendar.set(Calendar.MINUTE, 0);
            fromCalendar.set(Calendar.SECOND, 0);
            fromCalendar.set(Calendar.MILLISECOND, 0);
            Calendar toCalendar = Calendar.getInstance();
            toCalendar.setTime(curDate);
            toCalendar.set(Calendar.HOUR_OF_DAY, 0);
            toCalendar.set(Calendar.MINUTE, 0);
            toCalendar.set(Calendar.SECOND, 0);
            toCalendar.set(Calendar.MILLISECOND, 0);

            int s = (int) ((toCalendar.getTimeInMillis() - fromCalendar.getTimeInMillis())/ (ONE_DAY_MS));

            int days= (int)s;
            application.tmpdur =(String.valueOf(days+1));
            application.tmptoday=strDate;
           // Toast.makeText(PostTrip.this, application.tmptoday, Toast.LENGTH_SHORT).show();

        }
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

        Button back = findViewById(R.id.back);
        e.addTextChangedListener(new TextWatcher() {
            private CharSequence temp;
            private int editStart;
            private int editEnd;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                MyApplication application = (MyApplication) getApplication();
                application.tmptitle = e.getText().toString();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //清空这次编辑
                MyApplication application = (MyApplication) getApplication();
               // Toast.makeText(PostTrip.this, String.valueOf(application.curTrashRecords.size()), Toast.LENGTH_LONG).show();
                if (application.curTrashRecords.size()!=0) {
                    saveTrip();
                }

                application.tmptitle = "";
                application.setTrashItemId(-1);
                Intent intent = new Intent(PostTrip.this, TrashActivity.class);
                startActivity(intent);
            }
        });


        final RecyclerView myRecView = (RecyclerView) findViewById(R.id.outer_recyclerview);

        final ParentInfoAdapter_Trash myAdapter = new ParentInfoAdapter_Trash(this, dataInfoList);
        initData();

        LinearLayoutManager m = new LinearLayoutManager(this);
        myRecView.setLayoutManager(m);

        int firstItemPosition = m.findFirstVisibleItemPosition();

        myRecView.setAdapter(myAdapter);
        int count = myAdapter.getItemCount();

        MyApplication application = (MyApplication)getApplication();

        int fir = m.findFirstVisibleItemPosition();
        System.out.println("初始位置"+fir);
        int c = myAdapter.getItemCount();
        System.out.println("共有："+c);
        for (int position = 0; position<c; position++) {
            System.out.println("位置"+position);
            ParentInfoAdapter_Trash.ViewHolder myviewholde = (ParentInfoAdapter_Trash.ViewHolder) myRecView.findViewHolderForAdapterPosition(position);
//            System.out.println(myviewholde.date);
           /*
            if (myviewholde!=null) {
                System.out.println("位置"+position+"不空");
                myviewholde.add.setVisibility(View.INVISIBLE);
            }*/


        }

       // final String t = e.getText().toString();
        //final EditText ed = findViewById(R.id.edit);//这里全部要去掉


        //加号的点击事件已经放到ParentInfoAdapter_Trash.java里面
        //草稿箱的编辑和点击按钮写在ChildInfoAdapter_Trash里面
//        final Button addday = (Button)findViewById(R.id.add);
//        addday.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(PostTrip.this, addDay.class);
//                startActivity(intent);
//            }
//        });

        s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final MyApplication application = (MyApplication) getApplication();
                saveTrip();
                application.tmptitle = e.getText().toString();
                application.setTrashItemId(-1);


                final OkHttpClient httpClient = application.gethttpclient();
                final User user = application.getUser();
                int spotid = application.getSpots().get(application.getCurrentPos()).getID();
                String url = "http://123.207.29.66:3009/api/travels";
                String t = application.tmptitle;
                Request request;
                //if (imagePath != "") {
                  if (false) {
                    MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
                    File file = new File(imagePath);
                    RequestBody body = RequestBody.create(MediaType.parse("image/*"), file);
                    requestBody.addFormDataPart("cover", file.getName(), body)
                            .addFormDataPart("title", t);
                    request = new Request.Builder().post(requestBody.build()).url(url).build();
                } else {
                    FormBody formBody = new FormBody.Builder().add("title", t).build();
                    request = new Request.Builder().url(url).post(formBody).build();
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
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        System.out.println("哈哈哈哈哈"+string);
                        Gson gson = new Gson();
                        Type logintype = new TypeToken<Result<reviewback>>(){}.getType();
                        Result<reviewback> loginresult = gson.fromJson(string, logintype);
                        reviewback rb = loginresult.data;

                        // Toast.makeText(LoginActivity.this, String.valueOf(loginresult.data.getuserid()), Toast.LENGTH_SHORT).show();
                        int rescode = response.code();
                        List<trashRecord> trashlists = application.curTrashRecords;
                        int count = trashlists.size();
                        if (rescode == 200) {
                            application.eid=rb.getComment_id();
                            String url = "http://123.207.29.66:3009/api/travels/"+String.valueOf(rb.getComment_id())+"/travel-records";
                            for (int i = 0; i < count ;i++) {
                                FormBody formBody = new FormBody
                                        .Builder()
                                        .add("spot_id","1")//设置参数名称和参数值
                                        .add("content",trashlists.get(i).gettext())
                                        .build();
                                Request request = new Request.Builder().post(formBody).url(url).build();
                                httpClient.newCall(request).enqueue(new Callback() {
                                    @Override
                                    public void onFailure(Call call, IOException e) {}
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
                                                   // Toast.makeText(PostTrip.this, "发布游记记录成功", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    //Toast.makeText(PostTrip.this, String.valueOf(rescode), Toast.LENGTH_SHORT).show();
                                                }
                                            }

                                        });
                                    }
                                });
                            }
                        } else {}
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(PostTrip.this, JourneyDetailActivity.class);
                                intent.putExtra("From",1);
                                intent.putExtra("travel_id",String.valueOf(application.eid));
                                intent.putExtra("favorited",false);
                                startActivity(intent);
                            }
                        });
                    }
                });


            }
        });
    }
    void initData(){
        MyApplication application = (MyApplication) getApplication();
        e.setText(application.tmptitle);
        dur.setText(application.tmpdur);
        first.setText(application.tmpfirst);

        int writingday=-1;
        List<trashRecord> all;
        all=application.curTrashRecords;
        /*
        if (application.getTrashItemId()==-1) {
            Toast.makeText(PostTrip.this, "No travel_id now!", Toast.LENGTH_SHORT).show();

            all = application.curTrashRecords;
        } else {
            Toast.makeText(PostTrip.this, String.valueOf(application.getTrashItemId()), Toast.LENGTH_SHORT).show();
            all = DataSupport.where("travel_id = ?",
                    String.valueOf(application.getTrashItemId()) ).find(trashRecord.class);

        }*/

//模仿journeydetail.activity

       // Toast.makeText(PostTrip.this, String.valueOf(all.size()), Toast.LENGTH_SHORT).show();
        boolean newday = true;

        for (int i = 0; i < all.size();) {
            trashRecord tmp = all.get(i);
            if (writingday != tmp.getduration()) {
                writingday = tmp.getduration();
            }
            if (writingday == Integer.parseInt(application.tmpdur)) {
                System.out.println(writingday);
                newday = false;
            }
            ParentInfo parentInfo = new ParentInfo();
            parentInfo.setDay_num("Day"+String.valueOf(writingday));
          //  Toast.makeText(PostTrip.this, "Day"+String.valueOf(writingday),Toast.LENGTH_SHORT).show();
           // Toast.makeText(PostTrip.this, String.valueOf(tmp.gettravelid()),Toast.LENGTH_LONG).show();
            parentInfo.setDate(tmp.getday());
            parentInfo.setWeek(findWeekX(tmp.getday()));
            List<ChildInfo> childInfoList = new ArrayList<>();
            while (writingday == tmp.getduration()) {
                parentInfo.setItemList(childInfoList);
                ChildInfo childInfo = new ChildInfo();
                childInfo.setWord(tmp.gettext());
                childInfo.setLocation(tmp.getlocation());
                childInfo.setrecordid(i);

                Log.i("recoidid",String.valueOf(i));
                childInfoList.add(childInfo);
                i++;
                if (i < all.size()) {
                    tmp = all.get(i);
                } else {
                    break;
                }
            }
            parentInfo.setItemList(childInfoList);
            dataInfoList.add(parentInfo);


        }

        if (newday) {
            ParentInfo parentInfo = new ParentInfo();
            parentInfo.setDay_num("Day"+application.tmpdur);
            parentInfo.setDate(strDate);
            parentInfo.setWeek(findWeekX(strDate));

            List<ChildInfo> childInfoList = new ArrayList<>();//指的是这一天所有的游记,最开始为空
            parentInfo.setItemList(childInfoList);//将这一天的所有游记设置成标题的一个成员
            ChildInfo childInfo = new ChildInfo();
            childInfo.setWord("又是美好的一天");//childInfo指一条游记
            childInfo.setLocation("中国广州");

           // childInfoList.add(childInfo);
            parentInfo.setItemList(childInfoList);//将这一天的所有游记设置成标题的一个成员
            dataInfoList.add(parentInfo);//将一天一天的数据push进dataInfoList

        }





        //指的是这一天所有的游记,最开始为空
        //将这一天的所有游记设置成标题的一个成员

        //childInfo指一条游记


        //将这一天的所有游记设置成标题的一个成员
        //将一天一天的数据push进dataInfoList
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
                   // Toast.makeText(PostTrip.this,"You denied the permission",Toast.LENGTH_SHORT).show();
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
          //  Toast.makeText(this,"failed to get image", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(null != this.getCurrentFocus()){
            /**
             * 点击空白位置 隐藏软键盘
             */
            InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            return mInputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
        }
        return super.onTouchEvent(event);
    }

}
