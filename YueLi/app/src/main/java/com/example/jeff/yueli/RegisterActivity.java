package com.example.jeff.yueli;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Button regist = (Button)findViewById(R.id.log_button);
        Button back = (Button)findViewById(R.id.back_to_login);
        // 注册绑定
        regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText userName = (EditText)findViewById(R.id.name_input);
                EditText passWord = (EditText)findViewById(R.id.psw_input);
                final String username = userName.getText().toString();
                final String password = passWord.getText().toString();
                String url="http://123.207.29.66:3009/api/users";
                OkHttpClient httpClient = new OkHttpClient.Builder()
                        .cookieJar(new CookieJar() {
                            private final java.util.Map<String, List<Cookie>> cookiesMap = new HashMap<String, List<Cookie>>();
                            @Override
                            public void saveFromResponse(HttpUrl arg0, List<Cookie> arg1) {
                                // TODO Auto-generated method stub
                                //移除相同的url的Cookie
                                String host = arg0.host();
                                List<Cookie> cookiesList = cookiesMap.get(host);
                                if (cookiesList != null){
                                    cookiesMap.remove(host);
                                }
                                //再重新天添加
                                cookiesMap.put(host, arg1);
                            }
                            @Override
                            public List<Cookie> loadForRequest(HttpUrl arg0) {
                                // TODO Auto-generated method stub
                                List<Cookie> cookiesList = cookiesMap.get(arg0.host());
                                //注：这里不能返回null，否则会报NULLException的错误。
                                //原因：当Request 连接到网络的时候，OkHttp会调用loadForRequest()
                                return cookiesList != null ? cookiesList : new ArrayList<Cookie>();
                            }
                        })
                        .addInterceptor(new HttpLoggingInterceptor()).build();
                FormBody formBody = new FormBody
                        .Builder()
                        .add("username",username)//设置参数名称和参数值
                        .add("password",password)
                        .build();
                Request request = new Request.Builder().post(formBody).url(url).build();
                httpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                    }
                    String string=null;
                    @Override
                    public void onResponse(Call call, final Response response) throws IOException {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    string = response.body().string();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                Gson gson = new Gson();
                                Type regtype = new TypeToken<Result<signup>>(){}.getType();
                                Result<signup> regresult = gson.fromJson(string, regtype);
                                signup zhuce = regresult.data;

                                int rescode = response.code();
                                if (rescode == 200) {
                                  //  Toast.makeText(RegisterActivity.this, regresult.msg, Toast.LENGTH_SHORT).show();
                                    User yonghu=new User();
                                    yonghu.setusername(username);
                                    yonghu.setuserid(zhuce.getuserid());
                                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                    intent.putExtra("user",yonghu);
                                    startActivity(intent);

                                } else {

                                    Toast.makeText(RegisterActivity.this, regresult.msg, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });

            }
        });
        // 返回按钮绑定
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }


}
