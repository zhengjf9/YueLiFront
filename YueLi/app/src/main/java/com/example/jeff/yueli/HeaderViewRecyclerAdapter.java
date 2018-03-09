package com.example.jeff.yueli;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by jeff on 18-3-9.
 */

public class HeaderViewRecyclerAdapter extends RecyclerView.Adapter {

    private final RecyclerView.Adapter mAdapter;

    // These two ArrayList are assumed to NOT be null.
    // They are indeed created when declared in ListView and then shared.
    ArrayList<View> mHeaderViews;
    ArrayList<View> mFooterViews;

    // Used as a placeholder in case the provided info views are indeed null.
    // Currently only used by some CTS tests, which may be removed.
    static final ArrayList<View> EMPTY_INFO_LIST = new ArrayList<>();
    private int headerPosition;
    private int footerPosition;

    public HeaderViewRecyclerAdapter(ArrayList<View> headerViews, ArrayList<View> footerViews,
                                     RecyclerView.Adapter adapter,
                                     RecyclerView.LayoutManager layoutManager) {
        if (null == adapter) {
            throw new NullPointerException("Adapter is not null");
        }
        this.mAdapter = adapter;
        if (headerViews == null) {
            this.mHeaderViews = EMPTY_INFO_LIST;
        } else {
            this.mHeaderViews = headerViews;
        }
        if (footerViews == null) {
            this.mFooterViews = EMPTY_INFO_LIST;
        } else {
            this.mFooterViews = footerViews;
        }
        setFullSpan(layoutManager);
    }

    private void setFullSpan(RecyclerView.LayoutManager layoutManager) {
        if (layoutManager instanceof GridLayoutManager) {
            ((GridLayoutManager) layoutManager).setSpanSizeLookup(
                    new HeaderSpanSizeLookup((GridLayoutManager) layoutManager));
        }
    }

    public int getHeadersCount() {
        return mHeaderViews.size();
    }

    public int getFootersCount() {
        return mFooterViews.size();
    }

    public boolean removeHeader(View v) {
        for (int i = 0; i < mHeaderViews.size(); i++) {
            View view = mHeaderViews.get(i);
            if (view == v) {
                mHeaderViews.remove(i);
                return true;
            }
        }
        return false;
    }

    public boolean removeFooter(View v) {
        for (int i = 0; i < mFooterViews.size(); i++) {
            View view = mFooterViews.get(i);
            if (view == v) {
                mFooterViews.remove(i);
                return true;
            }
        }
        return false;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == RecyclerView.INVALID_TYPE - 1) {
            return new HeaderViewHolder(mHeaderViews.get(headerPosition++));
        } else if (viewType == RecyclerView.INVALID_TYPE - 2) {
            return new HeaderViewHolder(mFooterViews.get(0));
        }
        return mAdapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int numHeaders = getHeadersCount();
        if (position < numHeaders) {
            return;
        }
        int adjPosition = position - numHeaders;
        int adapterCount;
        if (mAdapter != null) {
            adapterCount = mAdapter.getItemCount();
            if (adjPosition < adapterCount) {
                mAdapter.onBindViewHolder(holder, adjPosition);
            }
        }

    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        int numHeaders = getHeadersCount();
        int numItems = mAdapter.getItemCount();
        ViewGroup.LayoutParams lparams = holder.itemView.getLayoutParams();
        if (lparams != null
                && lparams instanceof StaggeredGridLayoutManager.LayoutParams
                && (holder.getLayoutPosition() < numHeaders || holder.getLayoutPosition() >= numItems + numHeaders)) {
            StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lparams;
            p.setFullSpan(true);
        }
    }

    @Override
    public int getItemCount() {
        if (mAdapter != null) {
            return getHeadersCount() + getFootersCount() + mAdapter.getItemCount();
        } else {
            return getHeadersCount() + getFootersCount();
        }
    }

    @Override
    public int getItemViewType(int position) {
        int numHeaders = getHeadersCount();
        if (position < numHeaders) {
            return RecyclerView.INVALID_TYPE - 1;
        } else if (position >= numHeaders + mAdapter.getItemCount()) {
            return RecyclerView.INVALID_TYPE - 2;
        } else {
            int adjPosition = position - numHeaders;
            return mAdapter.getItemViewType(adjPosition);
        }
    }

    @Override
    public long getItemId(int position) {
        int numHeaders = getHeadersCount();
        if (mAdapter != null && position >= numHeaders) {
            int adjPosition = position - numHeaders;
            int adapterCount = mAdapter.getItemCount();
            if (adjPosition < adapterCount) {
                return mAdapter.getItemId(adjPosition);
            }
        }
        return -1;
    }

    public RecyclerView.Adapter getRecyclerAdapter() {
        return mAdapter;
    }


    @Override
    public void unregisterAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
        super.unregisterAdapterDataObserver(observer);
    }

    @Override
    public void registerAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
        super.registerAdapterDataObserver(observer);
    }

    private class HeaderViewHolder extends RecyclerView.ViewHolder {
        public HeaderViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class HeaderSpanSizeLookup extends GridLayoutManager.SpanSizeLookup {

        private final GridLayoutManager layoutManager;

        public HeaderSpanSizeLookup(GridLayoutManager layoutManager) {
            this.layoutManager = layoutManager;
        }

        @Override
        public int getSpanSize(int position) {
            position = position < mHeaderViews.size() ||
                    position > getItemCount() - mHeaderViews.size() ? layoutManager.getSpanCount() :
                    1;
            return position;
        }

    }
}