package com.example.ZhuJiaHong.fragment.FragmentChooseStock;

import android.content.Intent;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Parcelable;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.ZhuJiaHong.R;
import com.example.ZhuJiaHong.Util.Data;
import com.example.ZhuJiaHong.Util.MyUtils1;
import com.example.ZhuJiaHong.activity.FragmentChooseStockActivity.ActivityAdvancedSettings;
import com.example.ZhuJiaHong.activity.FragmentChooseStockActivity.ActivityInfo;
import com.example.ZhuJiaHong.databinding.FragmentChooseStockBinding;
import com.example.ZhuJiaHong.model.Filter;
import com.example.ZhuJiaHong.object.favourite.MyDataSourceRequired;
import com.google.android.material.tabs.TabLayout;
import com.mdbs.base.view.domain.BaseStockData;
import com.mdbs.base.view.fragment.BaseFragment;
import com.mdbs.base.view.object.dialog.LoadingDialog;
import com.mdbs.base.view.utils.Utils;
import com.mdbs.basechart.client.WebsocketGetter;
import com.mdbs.basechart.view.ProgressUtil;
import com.mdbs.starwave_meta.network.rxhttp.method.TransformerHolder;
import com.mdbs.starwave_meta.tools.WhenDispose;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import io.reactivex.disposables.CompositeDisposable;

public class FragmentChooseStock extends BaseFragment implements MyUtils1.OnDataUpdateListener {
    private FragmentChooseStockBinding binding;
    private Data data = new Data();
    private FragmentChooseStockAdapter adapter;
    private List<String> symbolsList = new ArrayList<>();
    private HashMap<String,Double> priceFilterHaspMap = new HashMap<>();
    private MyUtils1 myUtils1 = new MyUtils1();
    private WebsocketGetter mWebsocketGetter;
    private LinearLayoutManager layoutManager;
    private Handler handler = new Handler();
    final CompositeDisposable mDataRequiredDisposes = new CompositeDisposable();
    final MyDataSourceRequired mDataRequired = new MyDataSourceRequired();
    private String[] sortStatus = {"false", "true", "no"};
    private static int indexZhang = -1, indexLiang = -1, indexQi = -1;
    private List<String> priceSymbols = new ArrayList<>();
    private static boolean img_tab_click_status = false, all_touch_status = false, is_trend_mode = true;//false 多， true 空, 全部觸及

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mWebsocketGetter = new WebsocketGetter(mContext, getViewLifecycleOwner());
        binding = FragmentChooseStockBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        layoutManager = new LinearLayoutManager(getActivity());
        binding.recyclerViewSelect.setLayoutManager(layoutManager);
        adapter = new FragmentChooseStockAdapter(mContext,mWebsocketGetter,getViewLifecycleOwner());
        binding.recyclerViewSelect.setAdapter(adapter);

        adjustUiSize();
        listenerFunc();
        MyUtils1.setOnDataUpdateListener(this);

        return view;
    }

    public void setTabItem() {
        binding.tabLayoutUp.removeAllTabs();
        binding.tableLayoutMiddle.removeAllTabs();

        String[] tempTabUpItem = getResources().getStringArray(R.array.tabUpItem);
        String[] tempTabMediumItem = getResources().getStringArray(R.array.tabMidItem);

        if (img_tab_click_status) {
            tempTabUpItem = getResources().getStringArray(R.array.tabUpItem1);
            tempTabMediumItem = getResources().getStringArray(R.array.tabMidItem1);
        }

        replaceTabItem("tabUp",tempTabUpItem);
        replaceTabItem("tabMid",tempTabMediumItem);
    }

    public void replaceTabItem(String tab, String[] tempTabItem) {
        if ("tabUp".equals(tab)) {
            String tempFindItem = data.getStrategy();
            binding.tabLayoutUp.removeAllTabs();

            for (int i = 0; i < tempTabItem.length; i++) {
                TabLayout.Tab newTab = binding.tabLayoutUp.newTab();
                newTab.setText(tempTabItem[i]);
                binding.tabLayoutUp.addTab(newTab);
            }

            refreshTabPosition(binding.tabLayoutUp, tempTabItem, tempFindItem);
        }
        else if ("tabMid".equals(tab)){
            String tempFindItem = data.getStrategy_1();
            binding.tableLayoutMiddle.removeAllTabs();

            for (int i = 0; i < tempTabItem.length; i++) {
                TabLayout.Tab newTab = binding.tableLayoutMiddle.newTab();
                newTab.setText(tempTabItem[i]);
                binding.tableLayoutMiddle.addTab(newTab);
            }
            refreshTabPosition(binding.tableLayoutMiddle, tempTabItem, tempFindItem);
        }
    }


    private void listenerFunc() {
        binding.pageBtnOnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                is_trend_mode = !is_trend_mode;
                data.setChoose_is_trend_mode(is_trend_mode);
                checkShortOrLongAndAllTouch();
                if (null != adapter) adapter.isShowTrendMode(is_trend_mode);
            }
        });
        binding.allTouchTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                all_touch_status = !all_touch_status;
                data.setChoose_all_touch_status(all_touch_status);
                checkShortOrLongAndAllTouch();
            }
        });
        binding.iconInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, ActivityInfo.class));
            }
        });

        binding.iconAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, ActivityAdvancedSettings.class));
            }
        });

        binding.imgTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img_tab_click_status = !img_tab_click_status;
                data.setChoose_img_tab_click_status(img_tab_click_status);
                checkShortOrLongAndAllTouch();
                setTabItem();
            }
        });

        View.OnClickListener clickListener1 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> tempList = new ArrayList<>();
                if (null != layoutManager)layoutManager.smoothScrollToPosition(binding.recyclerViewSelect, new RecyclerView.State(),0);

                if ("全部".equals(data.getPriceFilter())) tempList.addAll(symbolsList);
                else tempList.addAll(priceSymbols);

                indexZhang++;
                getStockListByFilterClick(Filter.漲跌, tempList, sortStatus[indexZhang]);
                if (indexZhang >= 2) indexZhang = -1;
            }
        };

        View.OnClickListener clickListener2 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> tempList = new ArrayList<>();
                if (null != layoutManager)layoutManager.smoothScrollToPosition(binding.recyclerViewSelect, new RecyclerView.State(),0);

                if ("全部".equals(data.getPriceFilter())) tempList.addAll(symbolsList);
                else tempList.addAll(priceSymbols);

                indexLiang++;
                getStockListByFilterClick(Filter.量, tempList, sortStatus[indexLiang]);
                if (indexLiang >= 2) indexLiang = -1;
            }
        };

        View.OnClickListener clickListener3 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> tempList = new ArrayList<>();
                if (null != layoutManager)layoutManager.smoothScrollToPosition(binding.recyclerViewSelect, new RecyclerView.State(),0);

                if ("全部".equals(data.getPriceFilter())) tempList.addAll(symbolsList);
                else tempList.addAll(priceSymbols);

                indexQi++;
                getStockListByFilterClick(Filter.期, tempList, sortStatus[indexQi]);
                if (indexQi >= 2) indexQi = -1;
            }
        };

        binding.textViewZhangDie.setOnClickListener(clickListener1);
        binding.imageViewZhangUp.setOnClickListener(clickListener1);
        binding.imageViewZhangDown.setOnClickListener(clickListener1);

        binding.textViewLiang.setOnClickListener(clickListener2);
        binding.imageViewLiangUp.setOnClickListener(clickListener2);
        binding.imageViewLiangDown.setOnClickListener(clickListener2);

        binding.textViewQi.setOnClickListener(clickListener3);
        binding.imageViewQiUp.setOnClickListener(clickListener3);
        binding.imageViewQiDown.setOnClickListener(clickListener3);


        binding.tabLayoutUp.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                String strategy_1 = data.getStrategy_1();
                int position = tab.getPosition();
                switch (position){
                    case 0:
                        data.setStrategy("波段");
                        checkTimeAndShowData(data.getStrategy(),strategy_1);
                        binding.tableLayoutMiddle.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        data.setStrategy(!img_tab_click_status ? "長抱" : "盤中弱勢");
                        checkTimeAndShowData(data.getStrategy(),"");
                        binding.tableLayoutMiddle.setVisibility(View.GONE);
                        break;
                    case 2:
                        data.setStrategy(!img_tab_click_status ? "盤中強勢" : "盤中排行");
                        checkTimeAndShowData(data.getStrategy(),"");
                        binding.tableLayoutMiddle.setVisibility(View.GONE);
                        break;
                    case 3:
                        data.setStrategy(!img_tab_click_status ? "盤中排行" : "一點鐘");
                        checkTimeAndShowData(data.getStrategy(),"");
                        binding.tableLayoutMiddle.setVisibility(View.GONE);
                        break;
                    case 4:
                        data.setStrategy("一點鐘");
                        checkTimeAndShowData(data.getStrategy(),"");
                        binding.tableLayoutMiddle.setVisibility(View.GONE);
                        break;
                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}
            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        binding.tableLayoutMiddle.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();

                switch (position){
                    case 0:
                        data.setStrategy_1(!img_tab_click_status ? "頭高底高" : "頭低底低");
                        checkTimeAndShowData(data.getStrategy(), data.getStrategy_1());
                        break;
                    case 1:
                        data.setStrategy_1(!img_tab_click_status ? "回後準進場" : "彈後準進場");
                        checkTimeAndShowData(data.getStrategy(), data.getStrategy_1());
                        break;
                    case 2:
                        data.setStrategy_1(!img_tab_click_status ? "起漲策略" : "起跌策略");
                        checkTimeAndShowData(data.getStrategy(), data.getStrategy_1());
                        break;
                    case 3:
                        data.setStrategy_1(!img_tab_click_status ? "雙線黃金交叉" : "雙線死亡交叉");
                        checkTimeAndShowData(data.getStrategy(), data.getStrategy_1());
                        break;
                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}
            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        binding.tabLayoutDown.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();

                switch (position){
                    case 0:
                        data.setPriceFilter("全部");
                        sortPriceFilter(data.getPriceFilter());
                        break;
                    case 1:
                        data.setPriceFilter("低價");
                        sortPriceFilter(data.getPriceFilter());
                        break;
                    case 2:
                        data.setPriceFilter("中價");
                        sortPriceFilter(data.getPriceFilter());
                        break;
                    case 3:
                        data.setPriceFilter("高價");
                        sortPriceFilter(data.getPriceFilter());
                        break;
                    case 4:
                        data.setPriceFilter("超高");
                        sortPriceFilter(data.getPriceFilter());
                        break;
                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}
            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

    }

    private void checkShortOrLongAndAllTouch() {
        binding.imgTab.setImageResource(img_tab_click_status ? R.mipmap.img_tab_short : R.mipmap.img_tab_long);
        binding.allTouchTv.setBackgroundColor(all_touch_status ? getResources().getColor(R.color.primary1) : getResources().getColor(R.color.matching1_2));
        binding.pageBtnOnOff.setImageResource(is_trend_mode ? R.mipmap.page_btn_up_on : R.mipmap.page_btn_up);
    }

    //========================================================

    private void sortPriceFilter(String priceFilter) {
        priceSymbols = new ArrayList<>();
        if ("全部".equals(priceFilter) && adapter != null) {

            if (!symbolsList.isEmpty()) setRecyclerViewVisible(true);
            else setRecyclerViewVisible(false);

            if (checkFilterSort()) updateSortList(symbolsList);
            else sortFilter(symbolsList);
        }
        else {
            for (Map.Entry<String, Double> entry : priceFilterHaspMap.entrySet()) {
                String symbol = entry.getKey();
                double price = entry.getValue();

                if ("低價".equals(priceFilter))if (price >= 0 && price < 50) { priceSymbols.add(symbol); }
                if ("中價".equals(priceFilter))if (price >= 50 && price < 100) { priceSymbols.add(symbol); }
                if ("高價".equals(priceFilter))if (price >= 100 && price < 250) { priceSymbols.add(symbol); }
                if ("超高".equals(priceFilter))if (price >= 250) { priceSymbols.add(symbol); }
            }

            if (!priceSymbols.isEmpty()) setRecyclerViewVisible(true);
            else setRecyclerViewVisible(false);

            if (adapter != null) {
                if (checkFilterSort()) updateSortList(priceSymbols);
                else sortFilter(priceSymbols);
            }
        }
    }

    //==================================================

    private void setRecyclerViewVisible(boolean isShow){
        binding.recyclerViewSelect.setVisibility(isShow ? View.VISIBLE : View.GONE);
        binding.imageViewCloseTIme.setVisibility(isShow ? View.GONE : View.VISIBLE);
        binding.textViewCloseTime.setVisibility(isShow ? View.GONE : View.VISIBLE);
        binding.textViewCloseTime.setText("您設定的篩選條件，\n目前無符合項目");
    }

    //==================================================
    private void sortFilter(List<String> symbolsList) {
        if (indexZhang != -1) getStockListByFilterClick(Filter.漲跌,symbolsList,sortStatus[indexZhang]);
        else if (indexLiang != -1) getStockListByFilterClick(Filter.量,symbolsList,sortStatus[indexLiang]);
        else if (indexQi != -1) getStockListByFilterClick(Filter.期,symbolsList,sortStatus[indexQi]);
    }

    //==================================================

    private boolean checkFilterSort() {
        if (indexZhang == -1 && indexLiang == -1 && indexQi == -1) return true;
        else return false;
    }

    //==========================================================

    private void adjustUiSize() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int screenWidth = displayMetrics.widthPixels;
        float textSize2 = (float) screenWidth / 25;

        // Resize TextViews
        binding.textViewCloseTime.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize2);
        binding.textViewSelect.setTextSize(TypedValue.COMPLEX_UNIT_PX, Utils.dp2px(mContext, 22));
        ViewGroup.LayoutParams layoutParams = binding.imageView1.getLayoutParams();
        layoutParams.height = Utils.dp2px(mContext,60);
        binding.imageView1.setLayoutParams(layoutParams);
    }

    //==========================================================

    public void getStockListByFilterClick(String name, List<String> list, String isAscending) {
        changeArrowImage(name,isAscending);

        if ("no".equals(isAscending)) {
            List<String> tempList = new ArrayList<>();
            if ("全部".equals(data.getPriceFilter())) tempList.addAll(symbolsList);
            else tempList.addAll(priceSymbols);

            updateSortList(tempList);
        }
        else {
            boolean isTrue = false;
            if ("true".equals(isAscending)) isTrue = true;

            mDataRequiredDisposes.clear();
            mDataRequiredDisposes.add(mDataRequired.getAutoSortedStream(myUtils1.ConvertToBaseStockDataList(list), isTrue, name)
                    .compose(TransformerHolder.applySingleScheduler())
                    .as(WhenDispose.onDestroy(this))
                    .subscribe(dataList -> updateSortList(myUtils1.ConvertToStringList(dataList)))
            );
        }
    }

    //========================================================

    private void changeArrowImage(String name, String isAscending) {
        binding.imageViewZhangUp.setImageResource(R.mipmap.icon_up_off);
        binding.imageViewZhangDown.setImageResource(R.mipmap.icon_down_off);
        binding.imageViewLiangUp.setImageResource(R.mipmap.icon_up_off);
        binding.imageViewLiangDown.setImageResource(R.mipmap.icon_down_off);
        binding.imageViewQiUp.setImageResource(R.mipmap.icon_up_off);
        binding.imageViewQiDown.setImageResource(R.mipmap.icon_down_off);

        if ("漲跌".equals(name)){
            indexLiang = -1;
            indexQi = -1;
            if ("true".equals(isAscending)) binding.imageViewZhangUp.setImageResource(R.mipmap.icon_up_on);
            else if ("false".equals(isAscending)) binding.imageViewZhangDown.setImageResource(R.mipmap.icon_down_on);
        }
        else if ("量".equals(name)){
            indexZhang = -1;
            indexQi = -1;
            if ("true".equals(isAscending)) binding.imageViewLiangUp.setImageResource(R.mipmap.icon_up_on);
            else if ("false".equals(isAscending)) binding.imageViewLiangDown.setImageResource(R.mipmap.icon_down_on);
        }
        else if ("期".equals(name)){
            indexZhang = -1;
            indexLiang = -1;
            if ("true".equals(isAscending)) binding.imageViewQiUp.setImageResource(R.mipmap.icon_up_on);
            else if ("false".equals(isAscending)) binding.imageViewQiDown.setImageResource(R.mipmap.icon_down_on);
        }
    }

    //==========================================================

    private void updateSortList(List<String> updateList) {
        if (null != updateList && !updateList.isEmpty() && null != adapter){
            adapter.updateSortList(updateList, is_trend_mode);
        }
    }

    //==========================================================
    //檢查現在時間和去Api拿取資料
    private void checkTimeAndShowData(String strategy,String strategy_1) {

        if ("起漲策略".equals(strategy_1) || "雙線黃金交叉".equals(strategy_1) || "盤中強勢".equals(strategy) || "盤中排行".equals(strategy) || "一點鐘".equals(strategy)
        || "起跌策略".equals(strategy_1) || "雙線死亡交叉".equals(strategy_1) || "盤中弱勢".equals(strategy)) binding.allTouchTv.setVisibility(View.VISIBLE);
        else binding.allTouchTv.setVisibility(View.GONE);

        myUtils1.callStrategyApi(img_tab_click_status, strategy, strategy_1, getActivity());
    }

    //==========================================================
    //讀取策略token
    @Override
    public void getTokenComplete() {
        myUtils1.callStrategyApi(img_tab_click_status, data.getStrategy(), data.getStrategy_1(), getActivity());
    }

    //==========================================================
    //更新recyclerViewAdapter顯示內容,股名，股號，廠商，期貨icon，櫃買icon
    @Override
    public void updateRecyclerViewData(List<String> symbolsList) {
        if (!symbolsList.isEmpty()){
            setRecyclerViewVisible(true);

            this.symbolsList = symbolsList;
            updateSortList(symbolsList);

            if (!checkFilterSort())sortFilter(symbolsList);

            if (data.getRecyclerViewStateChoose() != null) layoutManager.onRestoreInstanceState(data.getRecyclerViewStateChoose());

            String priceFilter = data.getPriceFilter();
            if (!"全部".equals(priceFilter)) sortPriceFilter(priceFilter);

            refreshRecyclerViewData();
        }
        else{
            this.symbolsList = new ArrayList<>();
            setRecyclerViewVisible(false);
            // 創建台灣的Calendar實例，並設置時區為台灣
            Calendar taiwanCalendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Taipei"));
            int hourOfDay = taiwanCalendar.get(Calendar.HOUR_OF_DAY);
            int minute = taiwanCalendar.get(Calendar.MINUTE);
            int dayOfWeek = taiwanCalendar.get(Calendar.DAY_OF_WEEK);
            String strategy = data.getStrategy();

            if (("盤中強勢".equals(strategy) || "盤中弱勢".equals(strategy)) && dayOfWeek != Calendar.SATURDAY && dayOfWeek != Calendar.SUNDAY && (hourOfDay < 9 || (hourOfDay == 9 && minute < 30))){
                binding.textViewCloseTime.setText("盤中強勢策略\n啓動時間\n09:30~13:30");
            }
            else if ("盤中排行".equals(strategy) && dayOfWeek != Calendar.SATURDAY && dayOfWeek != Calendar.SUNDAY && (hourOfDay < 9 )){
                binding.textViewCloseTime.setText("盤中排行策略\n啓動時間\n09:00~13:30");
            }
            else if("一點鐘".equals(strategy) && dayOfWeek != Calendar.SATURDAY && dayOfWeek != Calendar.SUNDAY && (hourOfDay < 12 || (hourOfDay == 12 && minute < 30))){
                binding.textViewCloseTime.setText("一點鐘策略\n啓動時間\n12:30~13:30");
            }
            else binding.textViewCloseTime.setText("您設定的篩選條件，\n目前無符合項目");
        }

        ProgressUtil.getInstance().dismiss();
    }

    //==========================================================
    //recyclerView顯示內容刷新Timer
    private void refreshRecyclerViewData() {
        synchronized (this) {
            if (handler != null) {
                handler.removeCallbacksAndMessages(null);
                handler = null;
            }

            if (handler == null) handler = new Handler();

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    String strategy = data.getStrategy();
                    String strategy_1 = data.getStrategy_1();

                    if (!"波段".equals(strategy) && !"長抱".equals(strategy)) {
                        System.out.println("TestXuan:Refresh");
                        checkTimeAndShowData(strategy, strategy_1);
                    }
                    handler.postDelayed(this, 60000);
                }
            }, 60000);
        }
    }

    //==========================================================
    //價格篩選
    @Override
    public void updatePriceFilter(HashMap<String, Double> priceFilter) { this.priceFilterHaspMap = priceFilter; }

    //==========================================================

    @Override
    public void onPause() {
        super.onPause();
        cancelTimerAndOther();
        data.setRecyclerViewStateChoose(layoutManager.onSaveInstanceState());
    }

    //==========================================================

    private void cancelTimerAndOther() {

        if (null != mDataRequiredDisposes) mDataRequiredDisposes.clear();

        synchronized (this){
            if (handler != null) {
                handler.removeCallbacksAndMessages(null);
                handler = null;
            }
        }
        myUtils1.cancelDisposable();
    }

    //========================================================

    @Override
    public void onResume() {
        super.onResume();

        img_tab_click_status = data.isChoose_img_tab_click_status();
        is_trend_mode = data.isChoose_is_trend_mode();
        all_touch_status = data.isChoose_all_touch_status();

        checkShortOrLongAndAllTouch();
        setTabItem();
        refreshTabPosition(binding.tabLayoutDown, getResources().getStringArray(R.array.tabDownItem), data.getPriceFilter());//tabDown

        ProgressUtil.getInstance().show(mContext);

        if (data.getTokenStrategy().isEmpty()) myUtils1.getStrategyToken(mContext);
    }

    private void refreshTabPosition(TabLayout tabLayout, String[] tabItem, String findItem) {
        int index = myUtils1.findTabItemIndex(findItem, tabItem);
        TabLayout.Tab tab = tabLayout.getTabAt(index);
        tab.select();
    }
}