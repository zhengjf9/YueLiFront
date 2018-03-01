package com.example.jeff.yueli;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
                String username = userName.getText().toString();
                String password = passWord.getText().toString();
                if (isUserExist(username)) {
                    sendChangedUser(username, password);
                    Toast.makeText(ForgetActivity.this, "密码更改成功", Toast.LENGTH_SHORT);
                    // 跳转主页面->!
                } else {
                    Toast.makeText(ForgetActivity.this, "用户名不存在", Toast.LENGTH_SHORT);
                }
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

    // 判断用户名是否被注册
    boolean isUserExist(String userName) {
        return true;
    }

    // 提交更改
    void sendChangedUser(String userName, String pwd) {

    }
}
