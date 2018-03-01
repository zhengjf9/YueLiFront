package com.example.jeff.yueli;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button login = (Button)findViewById(R.id.log_button);
        Button forget = (Button)findViewById(R.id.forget_button);
        Button regist = (Button)findViewById(R.id.register_button);
        // 注册、登录、忘记密码按钮绑定
        regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ForgetActivity.class);
                startActivity(intent);
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText userName = (EditText)findViewById(R.id.name_input);
                EditText passWord = (EditText)findViewById(R.id.psw_input);
                String username = userName.getText().toString();
                String password = passWord.getText().toString();
                //for test
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);

                if (isUserExist(username)) {
                    if (password.equals(getPassWord(username))) {
                        // 跳转主页面->!
                    } else {
                        Toast.makeText(LoginActivity.this, "密码错误", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "用户名不存在", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }


    // 网络访问模块

    // 获取用户名对应的密码
    String getPassWord(String usesrName) {
        String res = "";
        return res;
    }

    // 判断用户是否存在
    boolean isUserExist(String userName) {

        return true;
    }
}
