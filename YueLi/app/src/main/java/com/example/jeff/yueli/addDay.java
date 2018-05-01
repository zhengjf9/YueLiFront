package com.example.jeff.yueli;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class addDay extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_day);
        Button sure = findViewById(R.id.sure);
        Button back = findViewById(R.id.back);
        final EditText edit = findViewById(R.id.edit);
        TextView DATX = findViewById(R.id.title);
        MyApplication application = (MyApplication) getApplication();
        DATX.setText("Day"+application.tmpdur);
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(addDay.this, PostTrip.class);
                intent.putExtra("From",1);
                intent.putExtra("locationForOneDay", "中国广州");
                intent.putExtra("textForOneDay",edit.getText().toString());
                startActivity(intent);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(addDay.this, PostTrip.class);
                intent.putExtra("From",3);
              //  intent.putExtra("locationForOneDay", "中国广州");
              //  intent.putExtra("textForOneDay",edit.getText());
                startActivity(intent);
            }
        });
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
