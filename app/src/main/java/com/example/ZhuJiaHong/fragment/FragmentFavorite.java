package com.example.ZhuJiaHong.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import static com.example.ZhuJiaHong.AppApplication.mStockLoader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.example.ZhuJiaHong.R;
import com.example.ZhuJiaHong.Util.AppUtil;
import com.example.ZhuJiaHong.Util.MyUtils1;
import com.example.ZhuJiaHong.model.Filter;
import com.example.ZhuJiaHong.object.favourite.FavoriteViewHolder;
import com.example.ZhuJiaHong.object.favourite.MyBaseFavoriteFragment;
import com.example.ZhuJiaHong.object.favourite.MyDataSourceRequired;
import com.mdbs.base.view.domain.BaseStockData;
import com.mdbs.base.view.fragment.BaseFavoriteFragment;
import com.mdbs.base.view.object.favorite.BaseRecycleLayout;
import com.mdbs.base.view.object.favorite.BaseRecycleViewHolderBuilder;
import com.mdbs.base.view.object.favorite.StockAdapter;
import com.mdbs.base.view.utils.BaseUtil;
import com.mdbs.basechart.activity.ActivityTwse;
import com.mdbs.basechart.base.OverviewItemView;
import com.mdbs.basechart.base.Utils;
import com.mdbs.basechart.client.RxGatewayStarwave;
import com.mdbs.basechart.client.RxPollings;
import com.mdbs.basechart.client.WebsocketGetter;
import com.mdbs.starwave_meta.common.stock.ProductSymbol;
import com.mdbs.starwave_meta.common.stock.StockInfoLoader;
import com.mdbs.starwave_meta.network.rxhttp.RxOwlHttpClient;
import com.mdbs.starwave_meta.network.rxhttp.method.TransformerHolder;
import com.mdbs.starwave_meta.params.RFOwlData;
import com.mdbs.starwave_meta.params.RFStock0Data;
import com.mdbs.starwave_meta.tools.WhenDispose;
import com.mdbs.starwave_meta.tools.methods.Method0;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.functions.Functions;
import io.reactivex.schedulers.Schedulers;

@interface DisplayMode {

    String NORMAL = "一般模式";
    String TREND = "小報價模式";
}
public class FragmentFavorite extends MyBaseFavoriteFragment {

    // Fields
    private WebsocketGetter mWebsocketGetter;
    private OverviewItemView twseView;
    private OverviewItemView otcView;
    private OverviewItemView txView;
    private RxGatewayStarwave rxGatewayStarwave;
    private CompositeDisposable disposes;
    public StockAdapter mAdapter;
    private RFStock0Data mRFStock0Data;
    private final LinkedList<String> mDisplayModes = new LinkedList<>(Arrays.asList(DisplayMode.NORMAL, DisplayMode.TREND));
    final CompositeDisposable mDataRequiredDisposes = new CompositeDisposable();
    final MyDataSourceRequired mDataRequired = new MyDataSourceRequired();
    private MyUtils1 myUtils1 = new MyUtils1();

    //================================================

    @Override
    public BaseRecycleViewHolderBuilder createViewHolderBuilder() {

        return new BaseRecycleViewHolderBuilder() {
            @Override
            public BaseRecycleLayout createViewHolder(int currentType) {

                switch (mDisplayModes.getFirst()) {

                    case DisplayMode.TREND:
                    case DisplayMode.NORMAL:
                    default: {

                        return new FavoriteViewHolder(mContext) {
                            @Override
                            public boolean isTrendMode() {
                                return true;
                            }
                        };
                    }
                }
            }

            @SuppressLint("ClickableViewAccessibility")
            @Override
            public void onBindViewHolder(StockAdapter stockAdapter, BaseRecycleLayout baseRecycleLayout, BaseStockData stockData, int i) {

                mAdapter = stockAdapter;
                ProductSymbol productSymbol = mStockLoader.safeGet(stockData.stock_no);
                FavoriteViewHolder viewHolder = (FavoriteViewHolder) baseRecycleLayout;
                viewHolder.disposes.clear();

                topicStock(productSymbol, viewHolder);
                viewHolder.initValue(productSymbol);
            }

            @Override
            public void onViewRecycled(BaseRecycleLayout baseRecycleLayout) {
                FavoriteViewHolder viewHolder = (FavoriteViewHolder) baseRecycleLayout;

                if (null != viewHolder && null != viewHolder.disposes) {

                    viewHolder.disposes.clear();
                }
            }
        };
    }

    //================================================

    private void topicStock(ProductSymbol symbol, BaseRecycleLayout holder) {

        if (mWebsocketGetter == null) return;

        FavoriteViewHolder viewHolder = (FavoriteViewHolder) holder;

        viewHolder.disposes.addAll(
                mWebsocketGetter.StarwaveServiceLatest(service -> service.stock0(symbol))
                        .compose(TransformerHolder.applyFlowableScheduler())
                        .as(WhenDispose.autoFocus(getViewLifecycleOwner()))
                        .subscribe(stock0 -> {

                            mRFStock0Data = stock0;

                            viewHolder.setValue(stock0);

                            if (stock0.試撮) {

                                viewHolder.binding.trendView.setPrices(0, 0, 0, 0);

                            } else {
                                viewHolder.binding.trendView.setPrices(stock0.open, stock0.ref, stock0.high, stock0.low);
                            }

                            viewHolder.binding.trendView.postInvalidate();

                            if (stock0.清盤) {

                                viewHolder.binding.customPercentView.setVisibility(View.INVISIBLE);
                            }

                        }, Functions.ERROR_CONSUMER), //即時k
                RxPollings.sellbuy(symbol)
                        .compose(TransformerHolder.applyFlowableScheduler())
                        .as(WhenDispose.autoFocus(this))
                        .subscribe(sellBuy -> {

                            if (null != mRFStock0Data && mRFStock0Data.清盤) {

                                viewHolder.binding.customPercentView.setVisibility(View.INVISIBLE);

                            } else {

                                if (sellBuy.buy == 0 && sellBuy.sell == 0) return;
                                float percent = (float) sellBuy.sell / (sellBuy.buy + sellBuy.sell) * 100.f;
                                viewHolder.binding.customPercentView.setBackgroundColor(mContext.getResources().getColor(R.color.market_green), mContext.getResources().getColor(R.color.market_red));
                                viewHolder.binding.customPercentView.setValue(percent);
                                viewHolder.binding.customPercentView.setVisibility(View.VISIBLE);
                            }

                        }, Throwable::printStackTrace)
        );

        if (viewHolder.isTrendMode()) {

            viewHolder.disposes.addAll(
                    RxPollings.trend(symbol)
                            .map(payload -> AppUtil.export(payload, "日期", "股票代號", "開盤價", "最高價", "最低價", "收盤價", "成交量", "平均價"))
                            .compose(TransformerHolder.applyFlowableScheduler())
                            .as(WhenDispose.autoFocus(getViewLifecycleOwner()))
                            .subscribe(new Consumer<RFOwlData>() {
                                @Override
                                public void accept(RFOwlData rfOwlData) throws Exception {

                                    if (null != mRFStock0Data && mRFStock0Data.試撮) {

                                        viewHolder.binding.trendView.setItemOwlData(null);

                                    } else {

                                        viewHolder.binding.trendView.setItemOwlData(rfOwlData);
                                    }
                                }
                            })
            );
        }
    }

    @Override
    public View createOverview() {

        // 大盤區
        LinearLayout overViewLayout = new LinearLayout(mContext);

        // 加權
        twseView = new OverviewItemView(mContext, "加權");
        overViewLayout.addView(twseView);
        View divider = new View(mContext);
        LinearLayout.LayoutParams dividerParams = new LinearLayout.LayoutParams(Utils.convertDpToPixel(1, mContext), ViewGroup.LayoutParams.MATCH_PARENT);
        divider.setBackgroundColor(Color.GRAY);
        dividerParams.setMargins(0, Utils.convertDpToPixel(10, mContext), 0, Utils.convertDpToPixel(10, mContext));
        overViewLayout.addView(divider, dividerParams);

        // 櫃買
        otcView = new OverviewItemView(mContext, "櫃買");
        overViewLayout.addView(otcView);
        View divider2 = new View(mContext);
        divider2.setBackgroundColor(Color.GRAY);
        overViewLayout.addView(divider2, dividerParams);

        // 台指近
        txView = new OverviewItemView(mContext, "台指近");
        overViewLayout.addView(txView);

        twseView.setOnClickListener(view -> {
            if (super.isEditor()) return;
            Intent intent = new Intent(mContext, ActivityTwse.class);
            intent.putExtra("title", getResources().getString(R.string.taiex_taiex_title));
            mContext.startActivity(intent);
        });

        otcView.setOnClickListener(view -> {
            if (super.isEditor()) return;
            Intent intent = new Intent(mContext, ActivityTwse.class);
            intent.putExtra("title", getResources().getString(R.string.taiex_otc_title));
            mContext.startActivity(intent);
        });

        txView.setOnClickListener(view -> {
            if (super.isEditor()) return;
            Intent intent = new Intent(mContext, ActivityTwse.class);
            intent.putExtra("title", getResources().getString(R.string.taiex_txf_title));
            mContext.startActivity(intent);
        });

        return overViewLayout;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {

        mWebsocketGetter = new WebsocketGetter(mContext, getViewLifecycleOwner());
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onResume() {

        super.onResume();
        initView();
        connect();
    }

    @Override
    public void onPause() {

        super.onPause();
        disconnect();
    }

    @Override
    public void getStockListByFilterClick(int position, String name, List<BaseStockData> list, boolean isAscending) {

        mDataRequiredDisposes.clear();
        mDataRequiredDisposes.add(mDataRequired.getAutoSortedStream(list, isAscending, name)
                .compose(TransformerHolder.applySingleScheduler())
                .as(WhenDispose.onDestroy(this))
                .subscribe(this::setStockListByFilterClick)
        );
    }

    // TODO 自選半頁模式才需時做，參考FragmentFavorite2
    @Override
    public View createSubView() {
        return null;
    }

    // TODO 自選半頁模式才需時做，參考FragmentFavorite2
    @Override
    public BaseFavoriteFragment.OpenSubViewClickListener initOpenSubViewClickListener() {
        return null;
    }

    private void connect(){

        rxGatewayStarwave = new RxGatewayStarwave(mContext, null, mContext.getString(R.string.owl_api_future_socket_domain));

        disposes = new CompositeDisposable();

        disposes.add(rxGatewayStarwave.stock0(StockInfoLoader.SYMBOL_OTC).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).as(TransformerHolder.AutoSubscribeAndDispose((LifecycleOwner) mContext))
                .subscribe(this::setOtcPrice, Functions.ERROR_CONSUMER));
        disposes.add(rxGatewayStarwave.stock0(StockInfoLoader.SYMBOL_TXF).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).as(TransformerHolder.AutoSubscribeAndDispose((LifecycleOwner) mContext))
                .subscribe(this::setTxfPrice, Functions.ERROR_CONSUMER));
        disposes.add(rxGatewayStarwave.stock0(StockInfoLoader.SYMBOL_TWSE).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).as(TransformerHolder.AutoSubscribeAndDispose((LifecycleOwner) mContext))
                .subscribe(this::setOpenPrice, Functions.ERROR_CONSUMER));
    }

    private void disconnect(){

        disposes.dispose();
        disposes = null;

        if (null != mDataRequiredDisposes) {

            mDataRequiredDisposes.clear();
        }
    }

    @SuppressLint("DefaultLocale")
    private void setOpenPrice(RFStock0Data data) {

        if (data == null || !data.isValid()) return;

        try {
            twseView.setValue(data);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("DefaultLocale")
    private void setOtcPrice(RFStock0Data data) {

        if (data == null || !data.isValid()) return;

        try {
            otcView.setValue(data);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("DefaultLocale")
    private void setTxfPrice(RFStock0Data data) {

        if (data == null || !data.isValid()) return;

        try {
            txView.setValue(data);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initView() {

        //產業
        mTitleView.setRightCenterImageButton(R.mipmap.industry_btn, Utils.convertDpToPixel(48,mContext), Utils.convertDpToPixel(30,mContext), false, new com.mdbs.basechart.base.DelayClickListener(1000) {
            @Override
            public void onNClick(View view) {
                if (BaseUtil.isSubscribe(mContext)) {
                    switchToTeacherMode(!isTeacherMode());
                }
                else {
                    switchToTeacherMode(!isTeacherMode());
                    //BaseUtil.popUpPurchasingDialog(mContext);
                }
            }
        });

        //產業locker
        mTitleView.initRightCenterButtonExperienceMode(false, R.mipmap.industry_locker);

        showSortButton(getDisplayModeIconMethod.invoke(), v -> {

            if (null == mAdapter) return;

            mDisplayModes.addLast(mDisplayModes.pollFirst());
            mAdapter.notifyDataSetChanged();
            if (v instanceof ImageView) ((ImageView) v).setImageResource(getDisplayModeIconMethod.invoke());
        });
    }

    Method0<Integer> getDisplayModeIconMethod = () -> {

        switch (mDisplayModes.getFirst()) {

            case DisplayMode.TREND: {

                return R.mipmap.page_btn_up;
            }
            default: {

                return R.mipmap.page_btn_up_on;
            }
        }
    };
}