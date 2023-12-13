package com.example.ZhuJiaHong.activity;

import android.content.Intent;
import com.google.gson.Gson;
import com.mdbs.base.view.domain.BaseStockData;
import com.mdbs.base.view.domain.Group;
import com.mdbs.base.view.utils.AppUtil;
import com.mdbs.base.view.utils.BaseUtil;
import com.mdbs.basechart.activity.ModelStockActivity;
import com.mdbs.basechart.view.StockSubView;
import com.mdbs.starwave_meta.common.stock.ProductSymbol;
import java.util.ArrayList;
import java.util.List;

public class ActivityStock extends ModelStockActivity {

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

        return null;
    }
}