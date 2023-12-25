package com.example.ZhuJiaHong.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.ZhuJiaHong.object.StockView.AnalysisView;
import com.google.gson.Gson;
import com.mdbs.base.view.domain.BaseStockData;
import com.mdbs.base.view.domain.Group;
import com.mdbs.base.view.utils.AppUtil;
import com.mdbs.base.view.utils.BaseUtil;
import com.mdbs.basechart.StockContainer;
import com.mdbs.basechart.activity.ModelStockActivity;
import com.mdbs.basechart.client.RxGatewayStarwave;
import com.mdbs.basechart.view.StockSubView;
import com.mdbs.starwave_meta.common.stock.ProductSymbol;
import java.util.ArrayList;
import java.util.List;

public class ActivityStock extends ModelStockActivity{

    private AnalysisView mAnalysisView;
    private RxGatewayStarwave mRxGatewayStarwave = new RxGatewayStarwave(mContext);
    @Override
    protected void goStockAdd(ProductSymbol productSymbol) {
        try {
            BaseUtil.goStockAdd(mContext, new BaseStockData(productSymbol.no));
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TODO 設定TabBar顯示項目條件
        /*setShowTabBarList = strings -> {
            if (productSymbol.no.startsWith("2")) { // 依此範例2開頭個股，不會出現上述三個TabBar，ex:2330
                strings.remove("核心");
                strings.remove("評價");
                strings.remove("策略");
            }

            return strings;
        };
         */
    }

    @Override
    protected void initTitleView() {
        // TODO 要客製個股標題時使用
        //  titleView.setRightCenterButtonImage(R.mipmap.auth_icon);
    }

    @Override
    protected void initContentView() {

        // TODO
        // contentView.showDotViewVisible(true, true);
    }

    @Override
    protected String parseStockWithBaseStockData(Intent intent) {

        BaseStockData baseStockData = (BaseStockData) getIntent().getSerializableExtra("stockData");
        return baseStockData.stock_no;
    }

    @Override
    protected List<String> parseGroupWithBaseStockData(Intent intent) {

        Group currentGroup = new Gson().fromJson(PAGE_CATEGORY, Group.class);
        List<String> result = new ArrayList<>();

        if (currentGroup != null && !AppUtil.isEmpty(currentGroup.stockList)) {
            for (int i = 0; i < currentGroup.stockList.size(); i++) {
                result.add(currentGroup.stockList.get(i).stock_no);
            }
        }

        return result;
    }

    //======================================================
    //Tag

    @Override
    protected String getFocusTagName() {

        // TODO return "籌碼";
        return null;
    }

    @Override
    protected String getFocusSubTagName() {

        // TODO return "分點";
        return null;
    }

    @Override
    protected StockSubView createCustomStockSubView(String tag) {

        System.out.println("TestXuan:"+tag);
        //標籤按下後呼叫
        switch (tag) {

            case "分析": {
                mAnalysisView = new AnalysisView(mContext, getLifecycle());
                return mAnalysisView;
            }
            default: {
                break;
            }
        }
        return null;
    }
}