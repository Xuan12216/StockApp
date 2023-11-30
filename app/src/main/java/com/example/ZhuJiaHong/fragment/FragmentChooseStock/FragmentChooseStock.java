package com.example.ZhuJiaHong.fragment.FragmentChooseStock;

import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
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
import com.example.ZhuJiaHong.databinding.FragmentChooseStockBinding;
import com.example.ZhuJiaHong.model.Filter;
import com.example.ZhuJiaHong.object.favourite.MyDataSourceRequired;
import com.google.android.material.tabs.TabLayout;
import com.mdbs.base.view.domain.BaseStockData;
import com.mdbs.base.view.fragment.BaseFragment;
import com.mdbs.base.view.object.dialog.LoadingDialog;
import com.mdbs.basechart.client.WebsocketGetter;
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
    private String[] tabUpItem = {"波段","長抱","盤中強勢","盤中排行","一點鐘"};
    private String[] tabMidItem = {"頭高底高","回後進場"};
    private String[] tabDownItem = {"全部","低價","中價","高價","超高"};
    private LinearLayoutManager layoutManager;
    private Handler handler = new Handler();
    private static final long REFRESH_INTERVAL = 60000; // 毫秒
    private static Parcelable recyclerViewState;
    final CompositeDisposable mDataRequiredDisposes = new CompositeDisposable();
    final MyDataSourceRequired mDataRequired = new MyDataSourceRequired();
    private String[] isZhang = {"false", "true", "no"};
    private String[] isLiang = {"false", "true", "no"};
    private String[] isQi = {"false", "true", "no"};
    private static int indexZhang = -1, indexLiang = -1, indexQi = -1;
    private List<String> priceSymbols = new ArrayList<>();
    private boolean img_tab_click_status = false;
    private com.mdbs.base.view.object.dialog.LoadingDialog loadingDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mWebsocketGetter = new WebsocketGetter(mContext, getViewLifecycleOwner());
        binding = FragmentChooseStockBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        layoutManager = new LinearLayoutManager(getActivity());
        binding.recyclerViewSelect.setLayoutManager(layoutManager);

        //listener

        binding.imgTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img_tab_click_status = !img_tab_click_status;

                if (img_tab_click_status) binding.imgTab.setImageResource(R.mipmap.img_tab_short);
                else binding.imgTab.setImageResource(R.mipmap.img_tab_long);
            }
        });

        View.OnClickListener clickListener1 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> tempList = new ArrayList<>();

                if ("全部".equals(data.getPriceFilter())) tempList.addAll(symbolsList);
                else tempList.addAll(priceSymbols);

                indexZhang++;
                getStockListByFilterClick(Filter.漲跌, tempList, isZhang[indexZhang]);
                if (indexZhang >= 2) indexZhang = -1;
            }
        };

        View.OnClickListener clickListener2 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> tempList = new ArrayList<>();

                if ("全部".equals(data.getPriceFilter())) tempList.addAll(symbolsList);
                else tempList.addAll(priceSymbols);

                indexLiang++;
                getStockListByFilterClick(Filter.量, tempList, isLiang[indexLiang]);
                if (indexLiang >= 2) indexLiang = -1;
            }
        };

        View.OnClickListener clickListener3 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> tempList = new ArrayList<>();

                if ("全部".equals(data.getPriceFilter())) tempList.addAll(symbolsList);
                else tempList.addAll(priceSymbols);

                indexQi++;
                getStockListByFilterClick(Filter.期, tempList, isQi[indexQi]);
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
                        data.setStrategy("長抱");
                        checkTimeAndShowData(data.getStrategy(),"");
                        binding.tableLayoutMiddle.setVisibility(View.GONE);
                        break;
                    case 2:
                        data.setStrategy("盤中強勢");
                        checkTimeAndShowData(data.getStrategy(),"");
                        binding.tableLayoutMiddle.setVisibility(View.GONE);
                        break;
                    case 3:
                        data.setStrategy("盤中排行");
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
                        data.setStrategy_1("頭高底高");
                        checkTimeAndShowData(data.getStrategy(), data.getStrategy_1());
                        break;
                    case 1:
                        data.setStrategy_1("回後進場");
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

        adjustUiSize();
        MyUtils1.setOnDataUpdateListener(this);

        return view;
    }

    //========================================================

    private void sortPriceFilter(String priceFilter) {
        priceSymbols = new ArrayList<>();
        if ("全部".equals(priceFilter) && adapter != null) {
            binding.imageViewCloseTIme.setVisibility(symbolsList.isEmpty() ? View.VISIBLE : View.GONE);
            binding.textViewCloseTime.setVisibility(symbolsList.isEmpty() ? View.VISIBLE : View.GONE);
            binding.textViewCloseTime.setText("您設定的篩選條件，\n目前無符合項目");
            if (checkFilterSort()) adapter.updateSortList(symbolsList);
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

            if (priceSymbols.isEmpty()){
                binding.imageViewCloseTIme.setVisibility(View.VISIBLE);
                binding.textViewCloseTime.setVisibility(View.VISIBLE);
                binding.textViewCloseTime.setText("您設定的篩選條件，\n目前無符合項目");
            }
            else{
                binding.imageViewCloseTIme.setVisibility(View.GONE);
                binding.textViewCloseTime.setVisibility(View.GONE);
            }

            if (adapter != null) {
                if (checkFilterSort()) adapter.updateSortList(priceSymbols);
                else sortFilter(priceSymbols);
            }
        }
    }

    //==================================================

    private void sortFilter(List<String> symbolsList) {
        if (indexZhang != -1) getStockListByFilterClick(Filter.漲跌,symbolsList,isZhang[indexZhang]);
        else if (indexLiang != -1) getStockListByFilterClick(Filter.量,symbolsList,isLiang[indexLiang]);
        else if (indexQi != -1) getStockListByFilterClick(Filter.期,symbolsList,isQi[indexQi]);
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

        binding.textViewCloseTime.setTextSize(TypedValue.COMPLEX_UNIT_PX,textSize2);
    }

    //==========================================================

    public void getStockListByFilterClick(String name, List<String> list, String isAscending) {
        changeArrowImage(name,isAscending);

        if ("no".equals(isAscending)) {
            List<String> tempList = new ArrayList<>();
            if ("全部".equals(data.getPriceFilter())) tempList.addAll(symbolsList);
            else tempList.addAll(priceSymbols);

            if (adapter != null) adapter.updateSortList(tempList);
        }
        else {
            boolean isTrue = false;
            if ("true".equals(isAscending)) isTrue = true;

            mDataRequiredDisposes.clear();
            mDataRequiredDisposes.add(mDataRequired.getAutoSortedStream(myUtils1.ConvertToBaseStockDataList(list), isTrue, name)
                    .compose(TransformerHolder.applySingleScheduler())
                    .as(WhenDispose.onDestroy(this))
                    .subscribe(this::setStockListByFilterClick)
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

    private void setStockListByFilterClick(List<BaseStockData> sortList, Throwable object1) {

        if (sortList != null &&!sortList.isEmpty()){
            List<String> newSymbolList = new ArrayList<>();

            for (int i = 0; i < sortList.size(); i++){
                String data = sortList.get(i).stock_no;
                newSymbolList.add(data);
            }

            if (adapter != null) adapter.updateSortList(newSymbolList);
        }
    }

    //==========================================================
    //檢查現在時間和去Api拿取資料
    private void checkTimeAndShowData(String strategy,String strategy_1) {
        myUtils1.callStrategyApi(strategy,strategy_1,getActivity());
    }

    //==========================================================
    //讀取策略token
    @Override
    public void getTokenComplete() {
        myUtils1.callStrategyApi(data.getStrategy(), data.getStrategy_1(), getActivity());
    }

    //==========================================================
    //更新recyclerViewAdapter顯示內容,股名，股號，廠商，期貨icon，櫃買icon
    @Override
    public void updateRecyclerViewData(List<String> symbolsList) {
        if (!symbolsList.isEmpty()){
            binding.imageViewCloseTIme.setVisibility(View.GONE);
            binding.textViewCloseTime.setVisibility(View.GONE);

            this.symbolsList = symbolsList;
            binding.recyclerViewSelect.setVisibility(View.VISIBLE);
            adapter = new FragmentChooseStockAdapter(symbolsList,mContext,mWebsocketGetter,getViewLifecycleOwner());

            if (!checkFilterSort())sortFilter(symbolsList);
            binding.recyclerViewSelect.setAdapter(adapter);

            if (recyclerViewState != null) {
                layoutManager.onRestoreInstanceState(recyclerViewState);
            }

            String priceFilter = data.getPriceFilter();
            if (!"全部".equals(priceFilter)) sortPriceFilter(priceFilter);

            refreshRecyclerViewData();
        }
        else{
            this.symbolsList = new ArrayList<>();
            binding.recyclerViewSelect.setVisibility(View.GONE);
            // 創建台灣的Calendar實例，並設置時區為台灣
            Calendar taiwanCalendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Taipei"));
            int hourOfDay = taiwanCalendar.get(Calendar.HOUR_OF_DAY);
            int minute = taiwanCalendar.get(Calendar.MINUTE);
            int dayOfWeek = taiwanCalendar.get(Calendar.DAY_OF_WEEK);
            String strategy = data.getStrategy();

            if ("盤中強勢".equals(strategy) && dayOfWeek != Calendar.SATURDAY && dayOfWeek != Calendar.SUNDAY && (hourOfDay < 9 || (hourOfDay == 9 && minute < 30))){
                binding.imageViewCloseTIme.setVisibility(View.VISIBLE);
                binding.textViewCloseTime.setVisibility(View.VISIBLE);
                binding.textViewCloseTime.setText("盤中強勢策略\n啓動時間\n09:30~13:30");
            }
            else if ("盤中排行".equals(strategy) && dayOfWeek != Calendar.SATURDAY && dayOfWeek != Calendar.SUNDAY && (hourOfDay < 9 )){
                binding.imageViewCloseTIme.setVisibility(View.VISIBLE);
                binding.textViewCloseTime.setVisibility(View.VISIBLE);
                binding.textViewCloseTime.setText("盤中排行策略\n啓動時間\n09:00~13:30");
            }
            else if("一點鐘".equals(strategy) && dayOfWeek != Calendar.SATURDAY && dayOfWeek != Calendar.SUNDAY && (hourOfDay < 12 || (hourOfDay == 12 && minute < 30))){
                binding.imageViewCloseTIme.setVisibility(View.VISIBLE);
                binding.textViewCloseTime.setVisibility(View.VISIBLE);
                binding.textViewCloseTime.setText("一點鐘策略\n啓動時間\n12:30~13:30");
            }
            else {
                binding.imageViewCloseTIme.setVisibility(View.VISIBLE);
                binding.textViewCloseTime.setVisibility(View.VISIBLE);
                binding.textViewCloseTime.setText("您設定的篩選條件，\n目前無符合項目");
            }
        }

        if (loadingDialog != null) {
            loadingDialog.hideDialog();
        }
    }

    //==========================================================
    //recyclerView顯示內容刷新Timer
    private void refreshRecyclerViewData() {
        synchronized (this) {
            if (handler != null) {
                handler.removeCallbacksAndMessages(null);
                handler = null;
            }

            if (handler == null) {
                handler = new Handler();
            }

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    String strategy = data.getStrategy();
                    String strategy_1 = data.getStrategy_1();

                    if (!"波段".equals(strategy) && !"長抱".equals(strategy)) {
                        System.out.println("TestXuan:Refresh");
                        checkTimeAndShowData(strategy, strategy_1);
                    }

                    handler.postDelayed(this, REFRESH_INTERVAL);
                }
            }, REFRESH_INTERVAL);
        }
    }

    //==========================================================
    //價格篩選
    @Override
    public void updatePriceFilter(HashMap<String, Double> priceFilter) {
        this.priceFilterHaspMap = priceFilter;
    }

    //==========================================================

    @Override
    public void onPause() {
        super.onPause();
        cancelTimerAndOther();
        recyclerViewState = layoutManager.onSaveInstanceState();
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

        loadingDialog = new LoadingDialog(mContext);

        if (loadingDialog != null && !loadingDialog.isShowing()) loadingDialog.showDialog();

        if (data.getTokenStrategy().isEmpty()) myUtils1.getStrategyToken(mContext);
        else myUtils1.callStrategyApi(data.getStrategy(), data.getStrategy_1(), getActivity());

        int index = myUtils1.findTabItemIndex(data.getStrategy(),tabUpItem);
        TabLayout.Tab tabUp = binding.tabLayoutUp.getTabAt(index);
        tabUp.select();

        index = myUtils1.findTabItemIndex(data.getStrategy_1(),tabMidItem);
        TabLayout.Tab tabMid = binding.tableLayoutMiddle.getTabAt(index);
        tabMid.select();

        index = myUtils1.findTabItemIndex(data.getPriceFilter(),tabDownItem);
        TabLayout.Tab tabDown = binding.tabLayoutDown.getTabAt(index);
        tabDown.select();
    }
}