package com.example.ZhuJiaHong;

import com.example.ZhuJiaHong.domain.Future;
import com.example.ZhuJiaHong.object.MyMarboView;
import com.google.firebase.FirebaseApp;
import com.mdbs.base.view.application.BaseAppApplication;
import com.mdbs.base.view.setting.GalaxySetting;
import com.mdbs.basechart.client.OwlRealTimeApi;
import com.mdbs.basechart.setting.ModelSetting;
import com.mdbs.starwave_meta.MetaPlugin;
import com.mdbs.starwave_meta.common.stock.ProductSymbol;
import com.mdbs.starwave_meta.common.stock.StockInfoLoader;
import com.mdbs.starwave_meta.params.enums.CEExchange;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import io.reactivex.internal.disposables.DisposableContainer;

public class AppApplication extends BaseAppApplication {
    public static List<Future> futureList = new CopyOnWriteArrayList<>();
    public static List<String> futureListStockNoOnly = new CopyOnWriteArrayList<>();
    private boolean isLight = true;//是否爲淺色底app
    private boolean isBold = true;//是否為整體粗體app
    public static StockInfoLoader mStockLoader;
    public static OwlRealTimeApi mRealTimeApi;

    static {
        System.loadLibrary("marboapp");
    }

    public static boolean isOTC(ProductSymbol symbol) {
        return symbol != null && symbol.exchangeNo.equals(CEExchange.上櫃);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseApp.initializeApp(this);//Firebase初始化
        MetaPlugin.initialize(this);//連綫初始化
        mStockLoader = StockInfoLoader.getInstance();//個股清單初始化
        mRealTimeApi = new OwlRealTimeApi();
        //設定公版模組的MarboView
        //MarboUtil.initMarboView(new MyMarboView(getApplicationContext(), null));
    }

    @Override
    protected void initGalaxySetting() {
        // TODO 自定義資源工廠
        GalaxySetting.init(isLight, isBold, new MyGalaxyResourceFactory(getApplicationContext()));
    }

    @Override
    protected void initStockModel() {
        // TODO 自定義資源工廠
        ModelSetting.init(isLight, isBold,  new MyResourceFactory(getApplicationContext()));
    }
}
