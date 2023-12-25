package com.example.ZhuJiaHong.activity;

import android.content.Intent;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.mdbs.base.view.domain.BaseStockData;
import com.mdbs.base.view.domain.Group;
import com.mdbs.base.view.utils.BaseUtil;
import com.mdbs.base.view.utils.Constant;
import com.mdbs.basechart.base.Utils;
import com.mdbs.basechart.view.updown.UpDownActivityView;
import com.mdbs.starwave_meta.params.RFSymbolName;

import java.util.ArrayList;

public class ActivityUpDown extends com.mdbs.basechart.activity.ActivityUpDown {

    private UpDownActivityView.UpDownViewHolderHelper helper = new UpDownActivityView.UpDownViewHolderHelper() {
        @Override
        public RecyclerView.ViewHolder createViewHolder() {
            return new AppViewHolder(new LinearLayout(mContext));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, RFSymbolName rfSymbolName, int position) {

            AppViewHolder viewHolder = (AppViewHolder) holder;
            viewHolder.textView.setText(String.format("%s %s", rfSymbolName.symbol, rfSymbolName.name));
            viewHolder.textView.setOnClickListener(v -> {
                if (BaseUtil.isSubscribe(mContext)) {
                    Group strategyGroup = new Group();
                    strategyGroup.stockList = new ArrayList<>();
                    for (RFSymbolName symbolName : view.upDownAdapter.getCurrentList()) {

                        strategyGroup.stockList.add(new BaseStockData(symbolName.symbol));
                    }

                    BaseStockData baseStockData = new BaseStockData(rfSymbolName.symbol);
                    Intent intent = new Intent(mContext, ActivityStock.class);
                    intent.putExtra(Constant.PAGE_CATEGORY, new Gson().toJson(strategyGroup));
                    intent.putExtra("stockData", baseStockData);
                    mContext.startActivity(intent);
                }
                else {

                    BaseUtil.popUpPurchasingDialog(mContext);
                }
            });
        }
    };

    @Override
    protected UpDownActivityView.UpDownViewHolderHelper createHelper(){ return helper; }

    // TODO 繼承UpDownViewHolder，實作各自viewHolder畫面
    public class AppViewHolder extends RecyclerView.ViewHolder {

        TextView textView;

        public AppViewHolder(@NonNull View itemView) {

            super(itemView);

            initView((LinearLayout) itemView);
        }

        private void initView(LinearLayout itemView) {

            textView = new TextView(mContext);
            textView.setPadding(Utils.convertDpToPixel(10, mContext), Utils.convertDpToPixel(10, mContext), Utils.convertDpToPixel(10, mContext), Utils.convertDpToPixel(10, mContext));
            textView.setTextColor(Color.BLACK);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);

            itemView.addView(textView);
        }
    }
}