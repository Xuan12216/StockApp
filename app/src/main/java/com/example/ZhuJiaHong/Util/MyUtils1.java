package com.example.ZhuJiaHong.Util;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Base64;
import androidx.fragment.app.FragmentActivity;
import com.example.ZhuJiaHong.domain.RxStrategyHttpClient.MyApi;
import com.example.ZhuJiaHong.domain.RxStrategyHttpClient.RxStrategyHttpClient;
import com.mdbs.base.view.domain.BaseStockData;
import com.mdbs.starwave_meta.common.stock.ProductSymbol;
import com.mdbs.starwave_meta.common.stock.StockInfoLoader;
import com.mdbs.starwave_meta.network.rxhttp.RxOwlHttpClient;
import com.mdbs.starwave_meta.params.RFQuoteField;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.apache.commons.text.StringEscapeUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

public class MyUtils1{

    private static MyUtils1.OnDataUpdateListener listener;
    private Data data = new Data();
    private Handler timer2Handler;
    private final Object timer2Lock = new Object();
    private Handler uiHandler;
    private Context context;
    private Disposable disposable;

    //=====================================================
    //listener

    public static void setOnDataUpdateListener(MyUtils1.OnDataUpdateListener listener) {MyUtils1.listener = listener;}

    public interface OnDataUpdateListener {

        void getTokenComplete();

        void updateRecyclerViewData(List<String> symbolsList);

        void updatePriceFilter(HashMap<String, Double> priceFilter);
    }

    //================================================
    //StrategyApi

    public void callStrategyApi(String strategy, String strategy_1, FragmentActivity activity) {

        String temp = "頭高底高";
        MyApi strategyApi = RxStrategyHttpClient.getInstance().getStrategyApi();

        if ("波段".equals(strategy)) {
            if ("頭高底高".equals(strategy_1)) temp = "頭高底高";
            else if ("回後進場".equals(strategy_1)) temp = "回後進場";
        }
        else if ("長抱".equals(strategy)) temp = "長抱";
        else if ("盤中強勢".equals(strategy)) temp = "盤中強勢";
        else if ("盤中排行".equals(strategy)) temp = "盤中排行";
        else if ("一點鐘".equals(strategy)) temp = "一點鐘";

        if (disposable != null && !disposable.isDisposed())disposable.dispose();

        disposable = getStrategyCall(strategyApi, temp, "Bearer "+data.getTokenStrategy())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .retry()
                .subscribe(
                        response -> {
                            String responseData = response.string();
                            String utf8String = StringEscapeUtils.unescapeJava(responseData);
                            getSymbolData(utf8String,activity);
                        }
                );
    }

    private Single<ResponseBody> getStrategyCall(MyApi strategyApi, String strategy, String authorization) {
        // 構建請求體
        RequestBody requestBody = RequestBody.create(null, new byte[0]); // 如果不需要請求體，可以使用這種方式

        switch (strategy) {
            case "頭高底高":
                return strategyApi.頭高底高(requestBody,"*/*",authorization);
            case "回後進場":
                return strategyApi.回後進場(requestBody,"*/*",authorization);
            case "長抱":
                return strategyApi.長抱(requestBody,"*/*",authorization);
            case "盤中強勢":
                return strategyApi.盤中強勢(requestBody,"*/*",authorization);
            case "盤中排行":
                return strategyApi.盤中排行(requestBody,"*/*",authorization);
            case "一點鐘":
                return strategyApi.一點鐘(requestBody,"*/*",authorization);
            default:
                return Single.error(new IllegalArgumentException("Invalid strategy URL"));
        }
    }

    //======================================================
    //getSymbolData

    private void getSymbolData(String utf8String,FragmentActivity activity) {
        List<String> symbolsList = new ArrayList<>();

        if (!utf8String.isEmpty()){
            try {
                Map<String, ProductSymbol> stockMap = StockInfoLoader.getInstance().get();

                if (stockMap != null && !stockMap.isEmpty()){
                    JSONArray jsonArray = new JSONArray(utf8String);

                    for (int i=0;i<jsonArray.length();i++){
                        String symbol = jsonArray.getJSONObject(i).getString("symbol");
                        symbolsList.add(symbol);
                    }

                    JSONArray jsonArray1 = new JSONArray(symbolsList);

                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("symbols", jsonArray1);

                    //7.價格篩選
                    getPriceFilter(symbolsList,jsonObject,activity);
                }

            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
        else{
            if (listener!=null && activity!=null){
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        listener.updateRecyclerViewData(symbolsList);
                    }
                });
            }
        }
    }

    //======================================================
    //Get PriceFilter

    //7.價格篩選
    private void getPriceFilter(List<String> symbolsList,JSONObject jsonObject,FragmentActivity activity) {
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody requestBody = RequestBody.create(mediaType, jsonObject.toString());

        if (disposable != null && !disposable.isDisposed()) disposable.dispose();

        disposable = RxOwlHttpClient.getInstance().getApi().getQuote(requestBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .retry()
                .subscribe(newList -> handleSuccess(newList,activity,symbolsList),throwable -> {System.out.println("PriceFilter:Error");});
    }

    private void handleSuccess(List<RFQuoteField> data, FragmentActivity activity, List<String> symbolsList) {
        HashMap<String, Double> priceFilter = new HashMap<String, Double>();

        for (RFQuoteField rfQuoteField : data){
            priceFilter.put(rfQuoteField.symbol, Double.valueOf(rfQuoteField.close));
        }
        if (listener != null && activity !=null) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    listener.updatePriceFilter(priceFilter);
                    listener.updateRecyclerViewData(symbolsList);
                }
            });
        }
    }

    //===========================================================
    //find Tab Index

    public int findTabItemIndex(String categories,String[] tabItem) {
        for (int i = 0; i < tabItem.length; i++){
            if (categories.equals(tabItem[i])) return i;
        }
        return 0;
    }

    //============================================================
    //Token Strategy

    private void decryptToken(String token,String timer) {
        String[] parts = token.split("\\.");
        if (parts.length == 3) {
            String payload = new String(Base64.decode(parts[1], Base64.DEFAULT));

            long expSecond = 0;
            try {
                JSONObject jsonObject = new JSONObject(payload);
                expSecond = jsonObject.getLong("exp");

                long currentTimestamp = System.currentTimeMillis() / 1000; // 當前時間的秒數
                long remainingSeconds = expSecond - currentTimestamp;

                setTimerToGetToken(remainingSeconds,timer);
            }
            catch (JSONException e) {throw new RuntimeException(e);}
        }
    }

    private void setTimerToGetToken(long remainingSeconds, String timer) {
        long initialDelay = (long) ((remainingSeconds * 0.9) * 1000);

        Runnable tokenRunnable;
        if ("timer2".equals(timer)) {
            tokenRunnable = () -> {
                uiHandler.post(() -> getStrategyToken(context));
            };
        } else {
            return; // Handle unsupported timer case or remove this line if not needed
        }

        if ("timer2".equals(timer)) {
            synchronized (timer2Lock) {
                if (timer2Handler != null) {
                    timer2Handler.removeCallbacksAndMessages(null);
                    timer2Handler = null;
                }

                timer2Handler = new Handler(Looper.getMainLooper());
                timer2Handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        tokenRunnable.run();
                        timer2Handler.postDelayed(this, initialDelay);
                    }
                }, initialDelay);
            }
        }
    }

    public void getStrategyToken(Context context) {
        if (context != null){
            this.context = context;
            uiHandler = new Handler(context.getMainLooper());
            String appId = data.getAppId_strategy();
            String appSecret = data.getAppSecret_strategy();

            // 取消之前的订阅
            if (disposable != null && !disposable.isDisposed()) disposable.dispose();

            disposable = RxStrategyHttpClient.getInstance().getStrategyApi()
                    .getStrategyToken(appId, appSecret)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .retry()
                    .subscribe(
                            tokenResponse -> {
                                // Token 获取成功
                                data.setTokenStrategy(tokenResponse.getToken());
                                if (!data.getTokenStrategy().isEmpty()) {
                                    System.out.println("tokenStrategy: " + data.getTokenStrategy() + "\n=================================== ");
                                    if (listener != null) listener.getTokenComplete();
                                    decryptToken(data.getTokenStrategy(), "timer2");
                                }
                            },
                            throwable -> {System.out.println("tokenStrategyError");}
                    );
        }
    }

    //=======================================================

    public List<BaseStockData> ConvertToBaseStockDataList(List<String> symbolsList) {
        List<BaseStockData> convertList = new ArrayList<>();

        for (String data : symbolsList){
            convertList.add(new BaseStockData(data));
        }

        return  convertList;
    }


    //===================================================
    //Handler And Disposable Cancel

    public void cancelTimer() {
        synchronized (timer2Lock){
            if (timer2Handler != null){
                timer2Handler.removeCallbacksAndMessages(null);
                timer2Handler = null;
            }
        }

        data.setTokenStrategy("");
    }

    public void cancelDisposable(){
        if (disposable != null && !disposable.isDisposed()) disposable.dispose();
    }
}
