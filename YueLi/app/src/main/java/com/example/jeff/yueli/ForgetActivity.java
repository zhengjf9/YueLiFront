package com.example.jeff.yueli;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class ForgetActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);
        Button forget = (Button)findViewById(R.id.log_button);
        Button back = (Button)findViewById(R.id.back_to_login);
        //绑定忘记密码和返回事件
        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText userName = (EditText)findViewById(R.id.name_input);
                EditText passWord = (EditText)findViewById(R.id.psw_input);
                final String username = userName.getText().toString();
                final String password = passWord.getText().toString();
                String url="http://123.207.29.66:3009/api/users/login";
                final MyApplication application = (MyApplication)getApplication();

                OkHttpClient httpClient = application.gethttpclient();
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
                                Type forgettype = new TypeToken<Result<forget>>(){}.getType();
                                Result<forget> forgetresult = gson.fromJson(string, forgettype);
                                forget wangji = forgetresult.data;

                                int rescode = response.code();
                                if (rescode == 200) {
                                    //Toast.makeText(ForgetActivity.this, "密码更改成功", Toast.LENGTH_SHORT).show();
                                    //application.setuserid(wangji.getuserid());
                                    //application.setusernickname(wangji.getnickname());
                                    User u = new User();
                                    u.setuserid(wangji.getuserid());
                                    u.setnickname(wangji.getnickname());
                                    MyApplication application = (MyApplication)getApplication();
                                    application.setUser(u);
                                    Intent intent = new Intent(ForgetActivity.this, MainActivity.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(ForgetActivity.this, forgetresult.msg, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });

            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForgetActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

}
