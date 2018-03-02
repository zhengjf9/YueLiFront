package com.example.jeff.yueli;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by XDDN2 on 2018/3/2.
 */

public class IndividualActivity extends Fragment {
    public IndividualActivity() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_individual, container, false);
        return view;
    }
}
