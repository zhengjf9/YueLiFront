package com.example.jeff.yueli;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
                String username = userName.getText().toString();
                String password = passWord.getText().toString();
                if (!isUserExist(username)) {
                    sendNewUser(username, password);
                    // 跳转到主页面->!
                } else {
                    Toast.makeText(RegisterActivity.this, "用户名已存在", Toast.LENGTH_SHORT);
                }
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

    // 网络访问部分

    // 判断用户名是否被注册
    boolean isUserExist(String userName) {
        return true;
    }

    // 提交注册
    void sendNewUser(String userName, String pwd) {

    }
}
