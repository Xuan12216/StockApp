package com.example.ZhuJiaHong.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.ZhuJiaHong.R;
import com.mdbs.base.view.fragment.BaseFragment;
import com.mdbs.base.view.utils.BaseUtil;
import com.mdbs.basechart.activity.ActivityCalendar;
import com.mdbs.basechart.activity.ActivityNews;
import com.mdbs.basechart.activity.ActivityTwse;
import com.mdbs.basechart.client.RxGatewayStarwave;
import com.mdbs.basechart.setting.ModelResourceSetting;
import com.mdbs.starwave_meta.common.stock.StockInfoLoader;
import com.mdbs.starwave_meta.network.rxhttp.RxOwlHttpClient;
import com.mdbs.starwave_meta.params.RFStock0Data;
import com.mdbs.starwave_meta.params.RFUpDownCount;

import java.util.Locale;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.internal.functions.Functions;
import io.reactivex.schedulers.Schedulers;

public class FragmentTwse extends BaseFragment {

    private TwseView view;
    private RxGatewayStarwave rxGatewayStarwave;
    private CompositeDisposable disposes;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        LinearLayout layout = (LinearLayout) super.onCreateView(inflater, container, savedInstanceState);
        layout.addView(view = new TwseView(mContext));
        initTitleView();

        return layout;
    }

    @Override
    public void onResume() {

        super.onResume();
        initialize();
        connectWs();
    }

    @Override
    public void onPause() {

        super.onPause();

        disConnectWs();
    }

    @SuppressLint("AutoDispose")
    private void connectWs() {

        rxGatewayStarwave = new RxGatewayStarwave(mContext, null, mContext.getString(R.string.owl_api_future_socket_domain));

        disposes = new CompositeDisposable();

        // 報價
        disposes.add(rxGatewayStarwave.stock0(StockInfoLoader.SYMBOL_TWSE).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::setOpenPrice, Functions.ERROR_CONSUMER));
        disposes.add(rxGatewayStarwave.stock0(StockInfoLoader.SYMBOL_OTC).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(x -> view.otc.setValue(x), Functions.ERROR_CONSUMER));
        disposes.add(rxGatewayStarwave.stock0(StockInfoLoader.SYMBOL_TXF).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::setTxfPrice, Functions.ERROR_CONSUMER));

        // 即時圖
        view.trendView.connect(rxGatewayStarwave);

        // 加權即時、盤後
        view.taiexDataView.connect();

        // 台指盤後籌碼快訊
        view.txfDataView.connect();

        // 小台散戶多空比
        view.txfChartView.connect();

        // 加權指數排行
        view.taiexRankView.connect(rxGatewayStarwave);

        // 法人分佈
        view.corporationView.connect();

        // 產業分佈
        view.industryDistributionView.connect();

        // 漲跌家數
        view.upDownView.connect();

        //percentView
        view.percentView.connect();
    }

    private void disConnectWs() {

        view.trendView.disconnect();
        view.taiexDataView.disconnect();
        view.txfDataView.disConnect();
        view.txfChartView.disConnect();
        view.taiexRankView.disconnect();
        view.corporationView.disConnect();
        view.industryDistributionView.disConnect();
        view.upDownView.disconnect();
        view.percentView.disconnect();

        rxGatewayStarwave.release();
        rxGatewayStarwave = null;

        disposes.dispose();
        disposes = null;
    }

    @SuppressLint("DefaultLocale")
    private void setOpenPrice(RFStock0Data data) {

        if (data == null || !data.isValid()) return;

        try {
            view.taiexDataView.setUpdateText(data);
            view.taiexRankView.setUpdateText(data);
            view.industryDistributionView.setUpdateText(data);
            view.upDownView.setUpdateText(data);
            view.twse.setValue(data);
            view.trendView.setOpenPrice(data.ref, data.high, data.low, true);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("DefaultLocale")
    private void setOtcPrice(RFStock0Data data) {

        if (data == null || !data.isValid()) return;

        try {
            view.otc.setValue(data);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("DefaultLocale")
    private void setTxfPrice(RFStock0Data data) {

        if (data == null || !data.isValid()) return;

        try {
            view.tx.setValue(data);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initialize() {

        view.twse.setOnClickListener(view -> {
            Intent intent = new Intent(mContext, ActivityTwse.class);
            intent.putExtra("title", getResources().getString(R.string.taiex_taiex_title));
            mContext.startActivity(intent);
        });

        view.otc.setOnClickListener(view -> {
            Intent intent = new Intent(mContext, ActivityTwse.class);
            intent.putExtra("title", getResources().getString(R.string.taiex_otc_title));
            mContext.startActivity(intent);
        });

        view.tx.setOnClickListener(view -> {
            Intent intent = new Intent(mContext, ActivityTwse.class);
            intent.putExtra("title", getResources().getString(R.string.taiex_txf_title));
            mContext.startActivity(intent);
        });
    }

    private void initTitleView() {

        boolean isSubscribe = BaseUtil.isSubscribe(mContext);

        mTitleView.setTitle("大盤");
        mTitleView.setLeftImageButton(R.mipmap.favorite_search_btn, false, v -> {
            if (isSubscribe) {

                BaseUtil.showStockSearchDialog(mContext);
            }
            else {
                BaseUtil.popUpPurchasingDialog(mContext);
            }
        });
        mTitleView.setRightTextButton("快訊", false, v -> {

            if (isSubscribe) {

                mContext.startActivity(new Intent(mContext, ActivityNews.class));
            }
            else {
                BaseUtil.popUpPurchasingDialog(mContext);
            }
        });
        mTitleView.setRightCenterTextButton("曆", false, v -> {

            mContext.startActivity(new Intent(mContext, ActivityCalendar.class));
        });

        // 設定體驗模式的顯示icon
        mTitleView.initLeftButtonExperienceMode(false, R.mipmap.favorite_lock_search_btn);
        mTitleView.initRightButtonExperienceMode(false);
    }
}