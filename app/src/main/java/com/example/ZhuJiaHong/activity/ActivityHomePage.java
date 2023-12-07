package com.example.ZhuJiaHong.activity;

import android.view.KeyEvent;
import android.view.View;

import androidx.lifecycle.Lifecycle;

import com.example.ZhuJiaHong.AppApplication;
import com.example.ZhuJiaHong.Util.Data;
import com.example.ZhuJiaHong.Util.MyUtils1;
import com.example.ZhuJiaHong.domain.Future;
import com.example.ZhuJiaHong.object.HomePage;
import com.mdbs.base.view.activity.ActivityHomePageBase;
import com.mdbs.base.view.object.homepage.BaseHomePage;
import com.mdbs.base.view.utils.AlertDialogUtil;
import com.mdbs.starwave_meta.network.rxhttp.RxOwlHttpClient;
import com.mdbs.starwave_meta.network.rxhttp.method.TransformerHolder;
import com.mdbs.starwave_meta.params.enums.CEOwlTable;
import com.uber.autodispose.AutoDispose;
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider;

import java.util.List;

public class ActivityHomePage extends ActivityHomePageBase {
    private MyUtils1 myUtils1 = new MyUtils1();
    private Data data = new Data();

    @Override
    protected BaseHomePage createHomePage() {

        //期貨
        callFuture();
        if (data.getTokenStrategy().isEmpty()) myUtils1.getStrategyToken(getApplicationContext());

        return new HomePage(this);
    }

    private void callFuture() {

        RxOwlHttpClient.getInstance().getMetaApi().getTable(CEOwlTable.期貨商品基本資訊.tableId)
                .compose(TransformerHolder.applySingleScheduler())
                .as(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(this, Lifecycle.Event.ON_DESTROY)))
                .subscribe(rfOwlData -> {

                    for (List<String> dataList : rfOwlData.getData()) {
                        Future future = new Future();
                        future.type = dataList.get(0);
                        future.stockNo = dataList.get(1);
                        future.stockName = dataList.get(2);
                        AppApplication.futureList.add(future);
                        AppApplication.futureListStockNoOnly.add(future.stockNo);
                    }
                });
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            AlertDialogUtil.showMessageAlert(this, "確定要離開 ?", true, v -> {
                myUtils1.cancelTimer();
                myUtils1.cancelDisposable();
                finish();
            });
        }
        return false;
    }
}
