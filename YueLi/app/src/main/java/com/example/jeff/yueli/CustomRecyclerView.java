package com.example.jeff.yueli;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by jeff on 18-3-9.
 */

public class CustomRecyclerView extends RecyclerView {

    public ArrayList<View> mHeaderViews = new ArrayList<>();
    public ArrayList<View> mFooterViews = new ArrayList<>();

    //添加Adapter
    public Adapter mAdapter;

    public CustomRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public CustomRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomRecyclerView(Context context) {
        super(context);
    }

    @Override
    public void setLayoutManager(LayoutManager layout) {
        super.setLayoutManager(layout);
    }

    public void addHeaderView(View view) {
        mHeaderViews.add(view);
        if (mAdapter != null) {
            if (!(mAdapter instanceof HeaderViewRecyclerAdapter)) {
                mAdapter = new HeaderViewRecyclerAdapter(mHeaderViews, mFooterViews, mAdapter,getLayoutManager());
            }
        }

    }

    public void addFooterView(View view) {
        mFooterViews.add(view);
        if (mAdapter != null) {
            if (!(mAdapter instanceof HeaderViewRecyclerAdapter)) {
                mAdapter = new HeaderViewRecyclerAdapter(mHeaderViews, mFooterViews, mAdapter,getLayoutManager());
            }
        }
    }

    public void setAdapter(Adapter adapter) {
        if (mHeaderViews.isEmpty() && mFooterViews.isEmpty()) {
            super.setAdapter(adapter);
        } else {
            adapter = new HeaderViewRecyclerAdapter(mHeaderViews, mFooterViews, adapter,getLayoutManager());
            super.setAdapter(adapter);
        }
        mAdapter = adapter;
    }

}