package com.example.jeff.yueli;

/**
 * Created by xumuxin on 2018/4/30.
 */
import android.text.Editable;
import android.text.TextWatcher;


/**
 * Created by sxt on 2016/12/12.
 */
public class MyTextChangedListener implements TextWatcher {
    /**
     * 编辑框的内容发生改变之前的回调方法
     */
    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
       // LogUtils.showLog("MyEditTextChangeListener", "beforeTextChanged---" + charSequence.toString());
    }

    /**
     * 编辑框的内容正在发生改变时的回调方法 >>用户正在输入
     * 我们可以在这里实时地 通过搜索匹配用户的输入
     */
    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
       // LogUtils.showLog("MyEditTextChangeListener", "onTextChanged---" + charSequence.toString());
    }

    /**
     * 编辑框的内容改变以后,用户没有继续输入时 的回调方法
     */
    @Override
    public void afterTextChanged(Editable editable) {
       // LogUtils.showLog("MyEditTextChangeListener", "afterTextChanged---");
        
    }
}

