package com.example.ZhuJiaHong.object.strategy;

import android.content.Context;

import androidx.recyclerview.widget.LinearLayoutManager;

public class StrategyLinearLayoutManager extends LinearLayoutManager {
    private boolean isScrollEnabled = true;

    public StrategyLinearLayoutManager(Context context) {
        super(context, VERTICAL, false);
    }

    public void setScrollEnabled(boolean flag) {
        this.isScrollEnabled = flag;
    }

    @Override
    public boolean canScrollVertically() {
        return isScrollEnabled && super.canScrollVertically();
    }
}