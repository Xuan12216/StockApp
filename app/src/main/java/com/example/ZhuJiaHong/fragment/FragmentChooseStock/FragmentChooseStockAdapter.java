package com.example.ZhuJiaHong.fragment.FragmentChooseStock;

import static com.example.ZhuJiaHong.AppApplication.mStockLoader;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ZhuJiaHong.AppApplication;
import com.example.ZhuJiaHong.R;
import com.example.ZhuJiaHong.Util.AppUtil;
import com.example.ZhuJiaHong.Util.Data;
import com.example.ZhuJiaHong.Util.Stock;
import com.example.ZhuJiaHong.Util.ValueParser;
import com.example.ZhuJiaHong.activity.ActivityStock;
import com.example.ZhuJiaHong.activity.ActivityStockIndustry;
import com.example.ZhuJiaHong.object.CustomPercentView.CustomPercentView;
import com.example.ZhuJiaHong.object.kf.PriceStyler;
import com.google.gson.Gson;
import com.mdbs.base.view.domain.BaseStockData;
import com.mdbs.base.view.domain.Group;
import com.mdbs.base.view.utils.BaseUtil;
import com.mdbs.basechart.client.RxPollings;
import com.mdbs.basechart.client.WebsocketGetter;
import com.mdbs.basechart.item.Constant;
import com.mdbs.starwave_meta.common.stock.ProductSymbol;
import com.mdbs.starwave_meta.common.stock.StockInfoLoader;
import com.mdbs.starwave_meta.network.rxhttp.method.TransformerHolder;
import com.mdbs.starwave_meta.params.RFMatchField;
import com.mdbs.starwave_meta.params.RFOwlData;
import com.mdbs.starwave_meta.params.RFStock0Data;
import com.mdbs.starwave_meta.params.RFSymbolName;
import com.mdbs.starwave_meta.tools.WhenDispose;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.functions.Functions;
import mdbs.basechart.view.realtime.ModelRealTimeLine2;

public class FragmentChooseStockAdapter extends RecyclerView.Adapter<FragmentChooseStockAdapter.ViewHolder> {
    private List<String> symbolsList = new ArrayList<>();
    private Map<String, ProductSymbol> stockMap = StockInfoLoader.getInstance().get();
    private HashMap<String, ViewHolder> viewHolderMap = new HashMap<>();
    private Context mContext;
    private WebsocketGetter mWebsocketGetter;
    private RFStock0Data mRFStock0Data;
    private List<String> listFuture = AppApplication.futureListStockNoOnly;
    private LifecycleOwner lifecycleOwner;

    public FragmentChooseStockAdapter(List<String> symbolsList, Context context, WebsocketGetter websocketGetter, LifecycleOwner lifecycleOwner) {
        this.symbolsList = new ArrayList<>(symbolsList);
        this.viewHolderMap = new HashMap<>();
        this.mContext = context;
        this.mWebsocketGetter = websocketGetter;
        this.lifecycleOwner = lifecycleOwner;

        for (int i = 0; i < symbolsList.size(); i++) {
            String stockNo = symbolsList.get(i).toString();
            viewHolderMap.put(stockNo, new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_stock_favor, null)));
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_stock_favor, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (position >=0 && position < symbolsList.size()){

            String stockNo = symbolsList.get(position).toString();
            String stockName = stockMap.get(stockNo).name;
            ProductSymbol productSymbol = mStockLoader.safeGet(stockNo);

            // 更新 ViewHolder HashMap
            viewHolderMap.put(stockNo, holder);

            topicStock(productSymbol);

            //stockNo,stockName,industryName
            holder.stockNoTextView.setText(stockNo);
            holder.stockNameTextView.setText(stockName);
            holder.yuan_jian.setText(productSymbol.industryName);

            holder.yuan_jian.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!TextUtils.isEmpty(productSymbol.industryName)) {

                        if (BaseUtil.isSubscribe(mContext)) {

                            Intent intent = new Intent(mContext, ActivityStockIndustry.class);
                            intent.putExtra("isMain", false);
                            intent.putExtra("stockNo", productSymbol.no);
                            intent.putExtra("industryName", mStockLoader.safeGet(productSymbol.no).industryName);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                            mContext.startActivity(intent);

                        }
                        else BaseUtil.popUpPurchasingDialog(mContext);
                    }
                }
            });

            //icon_gui
            holder.imageViewGui.setVisibility(AppApplication.isOTC(productSymbol) ? View.VISIBLE : View.GONE);

            //icon_qi
            // 在合適的地方初始化哈希集合，例如在適配器的構造函數中
            if (listFuture != null && !listFuture .isEmpty()){
                if (listFuture.contains(stockNo)) holder.imageViewQi.setVisibility(View.VISIBLE);
                else holder.imageViewQi.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onViewRecycled(@NonNull ViewHolder holder) {
        super.onViewRecycled(holder);
        // 調用 ViewHolder 的 onViewRecycled 方法
        if (null != holder && null != holder.disposes){
            holder.onViewRecycled();
        }
    }

    private void topicStock(ProductSymbol symbol) {
        if (mWebsocketGetter == null) return;
        ViewHolder viewHolder = viewHolderMap.get(symbol.no);

        if (viewHolder != null){
            viewHolder.disposes.addAll(
                    mWebsocketGetter.StarwaveServiceLatest(service -> service.stock0(symbol))
                            .compose(TransformerHolder.applyFlowableScheduler())
                            .as(WhenDispose.autoFocus(lifecycleOwner))
                            .subscribe(stock0 -> {
                                updateData(stock0);

                                if (stock0.試撮) {

                                    viewHolder.trend_view.setPrices(0, 0, 0, 0);

                                } else {

                                    viewHolder.trend_view.setPrices(stock0.open, stock0.ref, stock0.high, stock0.low);
                                }

                                viewHolder.trend_view.postInvalidate();

                                if (stock0.清盤) {

                                    viewHolder.customPercentView.setVisibility(View.INVISIBLE);
                                }

                            }, Functions.ERROR_CONSUMER), //即時k
                    RxPollings.sellbuy(symbol)
                            .compose(TransformerHolder.applyFlowableScheduler())
                            .as(WhenDispose.autoFocus(lifecycleOwner))
                            .subscribe(sellBuy -> {

                                if (null != mRFStock0Data && mRFStock0Data.清盤) {

                                    viewHolder.customPercentView.setVisibility(View.INVISIBLE);

                                } else {

                                    if (sellBuy.buy == 0 && sellBuy.sell == 0) return;
                                    float percent = (float) sellBuy.sell / (sellBuy.buy + sellBuy.sell) * 100.f;
                                    viewHolder.customPercentView.setBackgroundColor(mContext.getResources().getColor(R.color.market_green), mContext.getResources().getColor(R.color.market_red));
                                    viewHolder.customPercentView.setValue(percent);
                                    viewHolder.customPercentView.setVisibility(View.VISIBLE);
                                }

                            }, Throwable::printStackTrace)
            );

            viewHolder.disposes.addAll(
                    RxPollings.trend(symbol)
                            .map(payload -> AppUtil.export(payload, "日期", "股票代號", "開盤價", "最高價", "最低價", "收盤價", "成交量", "平均價"))
                            .compose(TransformerHolder.applyFlowableScheduler())
                            .as(WhenDispose.autoFocus(lifecycleOwner))
                            .subscribe(new Consumer<RFOwlData>() {
                                @Override
                                public void accept(RFOwlData rfOwlData) throws Exception {

                                    if (null != mRFStock0Data && mRFStock0Data.試撮) {

                                        viewHolder.trend_view.setItemOwlData(null);

                                    } else {

                                        viewHolder.trend_view.setItemOwlData(rfOwlData);
                                    }
                                }
                            })
            );
        }
    }

    private void updateData(RFStock0Data data) {
        ViewHolder holder = viewHolderMap.get(data.stockNo);

        if (holder != null) {
            // 检查 ViewHolder 是否已被回收
            if (holder.getAdapterPosition() != RecyclerView.NO_POSITION) {
                PriceStyler priceStyler = new PriceStyler();
                priceStyler.invoke(holder.price_container, holder.baojia, data, RFMatchField.MatchField.PRICE, true);
                // 下面这一行修改字符串后没有赋值回去，我添加了赋值语句
                holder.baojia.setText(holder.baojia.getText().toString().replace("+0.00 (+0.00%)", "0.00 (0.00%)"));
                holder.baojiaSmall.setTextColor(holder.baojia.getCurrentTextColor());
                holder.baojiaSmall.setText(data.清盤 ? null : data.get(RFStock0Data.Stock0Field.DF_INFO).replace("+0.00 (+0.00%)", "0.00 (0.00%)"));

                //量
                holder.liang.setText(String.valueOf(data.total));

                long total = data.total;
                String amount = String.valueOf(total);

                if (total > 10000) {
                    amount = ValueParser.roundDouble(total / 1000d, 1) + "K";
                }

                holder.liang.setText(amount);
            }
        }
    }

    @Override
    public int getItemCount() {return symbolsList.size();}

    public void updateSortList(List<String> symbolsList) {
        this.symbolsList = new ArrayList<>(symbolsList);
        this.viewHolderMap = new HashMap<>();

        for (int i = 0; i < symbolsList.size(); i++) {
            String stockNo = symbolsList.get(i).toString();
            viewHolderMap.put(stockNo, new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_stock_favor, null)));
        }
        notifyDataSetChanged();
    }

    // ViewHolder 類，用於保存視圖元素的引用
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView stockNameTextView, baojia, baojiaSmall, liang, stockNoTextView, yuan_jian;
        public ImageView imageViewGui, imageViewQi;
        public CustomPercentView customPercentView;
        public RelativeLayout price_container;
        public ModelRealTimeLine2 trend_view;
        public CompositeDisposable disposes = new CompositeDisposable();

        public ViewHolder(View view) {
            super(view);
            stockNameTextView = view.findViewById(R.id.stockNameTv);
            stockNoTextView = view.findViewById(R.id.stockNoTv);
            imageViewGui = view.findViewById(R.id.stock_type_tv2);
            imageViewQi = view.findViewById(R.id.stock_type_tv);
            baojia = view.findViewById(R.id.price_tv);
            baojiaSmall = view.findViewById(R.id.percent_tv);
            liang = view.findViewById(R.id.amount_tv);
            yuan_jian = view.findViewById(R.id.industry_type_tv);
            customPercentView = view.findViewById(R.id.customPercentView);
            price_container = view.findViewById(R.id.price_container);
            trend_view = view.findViewById(R.id.trend_view);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAbsoluteAdapterPosition();
                    if (position >= 0 && position < symbolsList.size()) {

                        Group strategyGroup = new Group();
                        strategyGroup.stockList = new ArrayList<>();
                        for (String symbol : symbolsList) {
                            strategyGroup.stockList.add(new BaseStockData(symbol));
                        }

                        BaseStockData baseStockData = new BaseStockData(symbolsList.get(position));
                        Intent intent = new Intent(mContext, ActivityStock.class);
                        intent.putExtra(Constant.PAGE_CATEGORY, new Gson().toJson(strategyGroup));
                        intent.putExtra("stockData", baseStockData);
                        mContext.startActivity(intent);
                    }
                }
            });
        }

        public void onViewRecycled() {
            // 在這裡取消訂閱或做其他清理工作
            disposes.clear();
        }
    }
}
