package com.example.ZhuJiaHong.object.favourite;

import static com.mdbs.starwave_meta.params.RFMatchField.MatchField.TOTAL;
import static com.mdbs.starwave_meta.params.RFStock0Data.Stock0Field.DF_RATE;

import com.example.ZhuJiaHong.AppApplication;
import com.example.ZhuJiaHong.domain.Future;
import com.example.ZhuJiaHong.model.Filter;
import com.mdbs.base.view.domain.BaseStockData;
import com.mdbs.basechart.view.chip.converts.SymbolNoConverter;
import com.mdbs.starwave_meta.network.rxhttp.RxOwlHttpClient;
import com.mdbs.starwave_meta.params.RFQuoteField;
import com.mdbs.starwave_meta.params.RFStock0Data;
import com.mdbs.starwave_meta.tools.functions.data_required.AbsDataSourceRequired;
import com.mdbs.starwave_meta.tools.functions.data_required.AbsSortTask;
import com.mdbs.starwave_meta.tools.methods.Method1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.functions.Function;

public class MyDataSourceRequired extends AbsDataSourceRequired {

    private List<RFStock0Data> dataSave = new ArrayList<>();
    private List<String> futureStockNoList = AppApplication.futureListStockNoOnly;
    private List<RFStock0Data> matchingFutureStockNoList = new ArrayList<>();
    private List<RFStock0Data> nonMatchingFutureStockNoList = new ArrayList<>();

    public MyDataSourceRequired() {
        super(new SymbolNoConverter());
    }

    public <E> Single<List<E>> getAutoSortedStream(List<E> source,Boolean isAscending,String sortField) {

        String mSortField = sortField;
        Boolean ASC = isAscending;
        dataSave.clear();

        Single<List<RFStock0Data>> mGetFactorStream;

        switch (mSortField) {
            // 須額外加載報價 API 的排序欄位
            case Filter.個股漲跌:
            case Filter.漲跌:
            case Filter.成交量:
            case Filter.量: {

                mGetFactorStream = (null != ASC)
                        ? RxOwlHttpClient.getInstance().getMetaApi().getLastPrice(getSymbols(source))
                        .doOnSuccess(dataSave::addAll)
                        : Single.just(new ArrayList<>());
                break;
            }
            case Filter.期: {

                matchingFutureStockNoList.clear();
                nonMatchingFutureStockNoList.clear();
                saveDataToList(getSymbols(source));//把資料儲存進dataSaveList
                mGetFactorStream = Single.just(dataSave);
                break;
            }
            // 不須額外加載其他資料的排序欄位
            default: {
                mGetFactorStream = Single.just(new ArrayList<>());
                break;
            }
        }
        // 加載額外資料後，再進行排序
        Single<List<E>> mSortedStream = mGetFactorStream.map(getSortedMethod1(source, mSortField, ASC));

        return mSortedStream;
    }

    //========================================

    private void saveDataToList(String[] symbols) {

        if (symbols.length > 0){

            dataSave.clear();

            for (int i = 0; i < symbols.length; i++){
                RFStock0Data rfStock0Data = new RFStock0Data();
                rfStock0Data.stockNo = symbols[i];
                dataSave.add(rfStock0Data);
            }
        }
    }

    //================================

    public <E> Function<List<RFStock0Data>, List<E>> getSortedMethod1(List<E> source, Object mSortField, Boolean ASC) {
        return (rfStock0Data) -> {
            this.InsertRFStock0Data(rfStock0Data);
            return sortMySelf(source, mSortField, ASC);
        };
    }

    //========================================

    private <E> List<E> sortMySelf(List<E> source, Object mSortField, Boolean asc) {

        List<E> newList = new ArrayList<>();

        if (mSortField instanceof String && !dataSave.isEmpty()) {
            String sortField = (String) mSortField;

            if (Filter.個股漲跌.equals(sortField) || Filter.漲跌.equals(sortField)) {
                // 按照 DF_RATE 属性排序
                Collections.sort(dataSave, (item1, item2) -> {
                    if (item1 != null && item2 != null) {

                        // 根据 asc 参数决定升序还是降序
                        int result = Float.compare(item1.dfRate, item2.dfRate);
                        return asc ? result : -result;
                    }
                    return 0;
                });
            }
            else if (Filter.成交量.equals(sortField) || Filter.量.equals(sortField)) {

                // 按照 volume 属性排序
                Collections.sort(dataSave, (item1, item2) -> {
                    if (item1 != null && item2 != null) {

                        // 根据 asc 参数决定升序还是降序
                        int result = Float.compare(item1.total, item2.total);
                        return asc ? result : -result;
                    }
                    return 0;
                });
            }
            else if (Filter.期.equals(sortField) && !futureStockNoList.isEmpty()) {
                for (int i = 0; i < dataSave.size(); i++){
                    String stockNo = dataSave.get(i).stockNo;
                    RFStock0Data rfStock0Data = new RFStock0Data();
                    rfStock0Data.stockNo = stockNo;

                    if (futureStockNoList.contains(stockNo)) matchingFutureStockNoList.add(rfStock0Data);
                    else nonMatchingFutureStockNoList.add(rfStock0Data);
                }

                dataSave.clear();
                dataSave.addAll(!asc ? matchingFutureStockNoList : nonMatchingFutureStockNoList);
                dataSave.addAll(asc ? matchingFutureStockNoList : nonMatchingFutureStockNoList);
            }

            for (RFStock0Data item : dataSave) {
                BaseStockData baseStockData = new BaseStockData();
                baseStockData.stock_no = item.stockNo;
                newList.add((E) baseStockData);
            }
        }
        return newList;
    }

    //========================================

    @Override
    protected float safeGetValue(Object item, String symbolNo, Object mSortField, Boolean ASC) {

        switch (((String) mSortField)) {

            case Filter.個股漲跌: case Filter.漲跌: return super.safeGetValue(item, symbolNo, DF_RATE, ASC);
            case Filter.成交量: case Filter.量: return super.safeGetValue(item, symbolNo, TOTAL, ASC);
            default: return super.safeGetValue(item, symbolNo, mSortField, ASC);
        }
    }

    //========================================

    public String[] getSymbols(List<?> source) {
        String[] symbols = new String[source.size()];

        for (int i = 0; i < source.size(); i++) {
            symbols[i] = ((BaseStockData) source.get(i)).stock_no;
        }
        return symbols;
    }
}
