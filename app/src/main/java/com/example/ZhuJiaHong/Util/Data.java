package com.example.ZhuJiaHong.Util;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Parcelable;

import androidx.lifecycle.LifecycleOwner;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import okhttp3.WebSocket;

public class Data {
    public static String appId_strategy = "1660268702927";
    public static String appSecret_strategy = "6263cdb019e011ed81b24ae7daaa2639";
    private static String tokenStrategy = "";
    private static String strategy = "波段";
    private static String strategy_1 = "頭高底高";
    private static String priceFilter = "全部";
    private static String lock_strategy = "波段";
    private static String lock_priceFilter = "全部";
    private static final String PREF_NAME = "FlexBoxClickStatus";
    private static final String KEY_IS_CLICKED_PREFIX = "isClicked_";
    private static boolean choose_img_tab_click_status = false, choose_all_touch_status = false, choose_is_trend_mode = true;//false 多， true 空, 全部觸及
    private static boolean lock_img_tab_click_status = false, lock_is_trend_mode = true;//false 多， true 空
    private static Parcelable recyclerViewStateLock, recyclerViewStateChoose;


    //==============================================================================


    public void setAppId_strategy(String appId_strategy) {
        Data.appId_strategy = appId_strategy;
    }

    public Parcelable getRecyclerViewStateLock() {
        return recyclerViewStateLock;
    }

    public void setRecyclerViewStateLock(Parcelable recyclerViewStateLock) {
        Data.recyclerViewStateLock = recyclerViewStateLock;
    }

    public Parcelable getRecyclerViewStateChoose() {
        return recyclerViewStateChoose;
    }

    public static void setRecyclerViewStateChoose(Parcelable recyclerViewStateChoose) {
        Data.recyclerViewStateChoose = recyclerViewStateChoose;
    }

    public boolean isLock_img_tab_click_status() {
        return lock_img_tab_click_status;
    }

    public void setLock_img_tab_click_status(boolean lock_img_tab_click_status) {
        Data.lock_img_tab_click_status = lock_img_tab_click_status;
    }

    public boolean isLock_is_trend_mode() {
        return lock_is_trend_mode;
    }

    public void setLock_is_trend_mode(boolean lock_is_trend_mode) {
        Data.lock_is_trend_mode = lock_is_trend_mode;
    }

    public boolean isChoose_img_tab_click_status() {
        return choose_img_tab_click_status;
    }

    public void setChoose_img_tab_click_status(boolean choose_img_tab_click_status) {
        Data.choose_img_tab_click_status = choose_img_tab_click_status;
    }

    public boolean isChoose_all_touch_status() {
        return choose_all_touch_status;
    }

    public void setChoose_all_touch_status(boolean choose_all_touch_status) {
        Data.choose_all_touch_status = choose_all_touch_status;
    }

    public boolean isChoose_is_trend_mode() {
        return choose_is_trend_mode;
    }

    public void setChoose_is_trend_mode(boolean choose_is_trend_mode) {
        Data.choose_is_trend_mode = choose_is_trend_mode;
    }

    public String getLock_strategy() {
        return lock_strategy;
    }

    public void setLock_strategy(String lock_strategy) {
        Data.lock_strategy = lock_strategy;
    }

    public String getLock_priceFilter() {
        return lock_priceFilter;
    }

    public void setLock_priceFilter(String lock_priceFilter) {
        Data.lock_priceFilter = lock_priceFilter;
    }

    public void saveClickStatus(Context context, String flexBoxId, String textViewId, boolean isClicked) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(getPrefKey(flexBoxId, textViewId), isClicked);
        editor.apply();
    }

    public boolean loadClickStatus(Context context, String flexBoxId, String textViewId) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getBoolean(getPrefKey(flexBoxId, textViewId), false);
    }

    private String getPrefKey(String flexBoxId, String textViewId) {
        return KEY_IS_CLICKED_PREFIX + flexBoxId + "_" + textViewId;
    }

    public void resetData(Context context) {

        tokenStrategy = "";
        strategy = "波段";
        lock_strategy = "等突破";
        lock_priceFilter = "全部";
        strategy_1 = "頭高底高";
        priceFilter = "全部";
        choose_img_tab_click_status = false;
        choose_all_touch_status = false;
        choose_is_trend_mode = true;
        lock_img_tab_click_status = false;
        lock_is_trend_mode = true;
        recyclerViewStateLock = null;
        recyclerViewStateChoose = null;

        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }

    public String getStrategy() {
        return strategy;
    }

    public void setStrategy(String strategy) {
        Data.strategy = strategy;
    }

    public String getStrategy_1() {
        return strategy_1;
    }

    public void setStrategy_1(String strategy_1) {
        Data.strategy_1 = strategy_1;
    }

    public String getPriceFilter() {
        return priceFilter;
    }

    public void setPriceFilter(String priceFilter) {
        Data.priceFilter = priceFilter;
    }

    public String getTokenStrategy() {
        return tokenStrategy;
    }

    public void setTokenStrategy(String tokenStrategy) {
        Data.tokenStrategy = tokenStrategy;
    }

    public String getAppId_strategy() {
        return appId_strategy;
    }

    public String getAppSecret_strategy() {
        return appSecret_strategy;
    }
}
