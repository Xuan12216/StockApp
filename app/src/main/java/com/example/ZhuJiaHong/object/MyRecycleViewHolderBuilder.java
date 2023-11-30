package com.example.ZhuJiaHong.object;

import android.content.Context;

import com.mdbs.base.view.domain.BaseStockData;
import com.mdbs.base.view.object.favorite.BaseRecycleLayout;
import com.mdbs.base.view.object.favorite.BaseRecycleViewHolderBuilder;
import com.mdbs.base.view.object.favorite.StockAdapter;

public class MyRecycleViewHolderBuilder implements BaseRecycleViewHolderBuilder {
    private Context mContext = null;

    public MyRecycleViewHolderBuilder(Context context){ mContext = context; }

    @Override
    public BaseRecycleLayout createViewHolder(int i) { return new MyViewHolder(mContext); }

    @Override
    public void onBindViewHolder(StockAdapter stockAdapter, BaseRecycleLayout baseRecycleLayout, BaseStockData baseStockData, int i) {

    }

    @Override
    public void onViewRecycled(BaseRecycleLayout baseRecycleLayout) {
        //取消訂閲個股
    }
}
