package com.example.ZhuJiaHong.object.StockView;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.lifecycle.Lifecycle;
import com.example.ZhuJiaHong.R;
import com.example.ZhuJiaHong.databinding.ItemAnalysisBinding;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.formatter.DefaultValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.mdbs.basechart.base.BaseLinearLayout;
import com.mdbs.basechart.client.RxGatewayStarwave;
import com.mdbs.basechart.view.ProgressUtil;
import com.mdbs.basechart.view.StockSubView;
import com.mdbs.starwave_meta.common.stock.ProductSymbol;
import com.mdbs.starwave_meta.common.stock.TechIndexData;
import com.mdbs.starwave_meta.network.rxhttp.method.TransformerHolder;
import com.mdbs.starwave_meta.params.enums.CETick;
import com.mdbs.starwave_meta.tools.methods.Method0;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class AnalysisView extends BaseLinearLayout implements StockSubView{
    private Lifecycle mLifecycle;
    private ItemAnalysisBinding binding;
    private List<String> dateLabels = new ArrayList<>();
    SimpleDateFormat inputFormat = new SimpleDateFormat("yyyyMMdd");
    SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy/MM/dd");

    public AnalysisView(Context context, Lifecycle lifecycle) {
        super(context);

        mLifecycle = lifecycle;
    }

    @Override
    public void initView() {
        baseView.setBackgroundColor(getResources().getColor(R.color.background));
        baseView.setOrientation(VERTICAL);
        LinearLayout.LayoutParams layoutParams = new LinearLayoutCompat.LayoutParams(
                new LayoutParams(LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        baseView.addView(initCandleStickChart(), layoutParams);



    }

    @Override
    public void start(RxGatewayStarwave rxGatewayStarwave, ProductSymbol productSymbol) {
        if (rxGatewayStarwave != null) {
            rxGatewayStarwave.match_HistoryKB(CETick.TickDay, productSymbol)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe((disposable) -> { ProgressUtil.getInstance().show(this.mContext);})
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterNext((technicalIndexData) -> { ProgressUtil.getInstance().dismiss();})
                .doFinally(() -> { ProgressUtil.getInstance().dismiss();})
                .as(TransformerHolder.AutoSubscribeAndDispose(mLifecycle))
                .subscribe((data) -> {
                    if (data.getData() != null) setCandleStickChart(data);
                }, (e) -> {});
        }
    }

    @Override
    public void release() {}

    @Override
    public void setSubTabName(String s) {}

    @Override
    public boolean isScrollable() {return false;}

    @Override
    public Object getContainer() {return null;}

    @Override
    public void setStatusChangedListener(Method0<Void> method0) {}

    @Override
    public void setDefaultHeight(int contentHeight, int quotaViewHeight) {}

    //=====================================================

    private View initCandleStickChart() {
        // 在这里初始化 CandleStickChart
        LinearLayout.LayoutParams layoutParams = new LinearLayoutCompat.LayoutParams(
                new LayoutParams(LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        binding = ItemAnalysisBinding.inflate(LayoutInflater.from(mContext), null, false);
        LinearLayout linearLayout = new LinearLayout(mContext);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.addView(binding.getRoot(), layoutParams);

        // 配置 CandleStickChart 的一些属性
        binding.candleStickChart.setDragEnabled(true);  // 禁止拖拽
        binding.candleStickChart.setScaleEnabled(false);  // 禁止缩放
        binding.candleStickChart.setScaleXEnabled(true);
        binding.candleStickChart.setDoubleTapToZoomEnabled(false);
        binding.candleStickChart.setTouchEnabled(true);  // 禁止手势操作
        binding.candleStickChart.getLegend().setEnabled(false); // 禁用图例
        binding.candleStickChart.requestDisallowInterceptTouchEvent(true);
        binding.candleStickChart.setDescription(null);
        binding.candleStickChart.setPinchZoom(true);
        binding.candleStickChart.setAutoScaleMinMaxEnabled(true);
        binding.candleStickChart.setHighlightPerDragEnabled(false);
        binding.candleStickChart.setHighlightPerTapEnabled(false);

        // 配置 X 轴
        XAxis xAxis = binding.candleStickChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawLabels(true);
        xAxis.setDrawAxisLine(false);
        xAxis.setGranularityEnabled(true);
        xAxis.setGranularity(1.0f);
        xAxis.setLabelCount(3);
        xAxis.setAvoidFirstLastClipping(true);

        // 配置 Y 轴
        YAxis leftAxis = binding.candleStickChart.getAxisLeft();
        YAxis rightAxis = binding.candleStickChart.getAxisRight();
        leftAxis.setDrawLabels(false);
        leftAxis.setDrawAxisLine(false);
        leftAxis.setDrawGridLines(false);
        rightAxis.setTextColor(getResources().getColor(R.color.black1));
        rightAxis.setDrawLabels(true);
        rightAxis.setValueFormatter(new DefaultValueFormatter(2));
        rightAxis.setDrawAxisLine(false);
        rightAxis.setGridColor(getResources().getColor(R.color.matching1_2));
        rightAxis.setDrawGridLines(true);

        binding.candleStickChart.setOnChartGestureListener(new OnChartGestureListener() {
            @Override
            public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
            }

            @Override
            public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
            }

            @Override
            public void onChartLongPressed(MotionEvent me) {
            }

            @Override
            public void onChartDoubleTapped(MotionEvent me) {
            }

            @Override
            public void onChartSingleTapped(MotionEvent me) {
            }

            @Override
            public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {
            }

            @Override
            public void onChartScale(MotionEvent me, float scaleX, float scaleY) {
                float lowestVisibleX = binding.candleStickChart.getLowestVisibleX();
                float highestVisibleX = binding.candleStickChart.getHighestVisibleX();

                // 计算中间值
                float middleX = (lowestVisibleX + highestVisibleX) / 2.0f;

                // 更新左边、中间、右边的 X 轴值
                float leftX = lowestVisibleX;
                float rightX = highestVisibleX;

                // 然后您可以将这些值用于更新 UI 或执行其他操作
                updateTextViews(leftX, middleX, rightX);
            }

            @Override
            public void onChartTranslate(MotionEvent me, float dX, float dY) {
                float lowestVisibleX = binding.candleStickChart.getLowestVisibleX();
                float highestVisibleX = binding.candleStickChart.getHighestVisibleX();

                // 计算中间值
                float middleX = (lowestVisibleX + highestVisibleX) / 2.0f;

                // 更新左边、中间、右边的 X 轴值
                float leftX = lowestVisibleX;
                float rightX = highestVisibleX;

                // 然后您可以将这些值用于更新 UI 或执行其他操作
                updateTextViews(leftX, middleX, rightX);
            }
        });

        return linearLayout;
    }

    private void updateTextViews(float leftX, float middleX, float rightX) {
        // 获取 XAxis
        XAxis xAxis = binding.candleStickChart.getXAxis();

        // 获取对应 X 轴值的标签
        String leftLabel = xAxis.getValueFormatter().getFormattedValue(leftX);
        String middleLabel = xAxis.getValueFormatter().getFormattedValue(middleX);
        String rightLabel = xAxis.getValueFormatter().getFormattedValue(rightX);

        // 然后您可以使用这些标签进行其他操作，比如更新 TextView 的文本
        if (!leftLabel.isEmpty())binding.txtLeft.setText(leftLabel);
        if (!middleLabel.isEmpty())binding.txtMid.setText(middleLabel);
        if (!rightLabel.isEmpty())binding.txtRight.setText(rightLabel);
    }

    private void setCandleStickChart(TechIndexData.TechnicalIndexData data) {
        // Assuming data is a list of K-line data
        List<ArrayList<String>> kLineDataList = data.getData();
        // Prepare candle entries
        List<CandleEntry> entries = new ArrayList<>();
        dateLabels.clear();
        for (int i = 0; i < kLineDataList.size(); i++) {
            List<String> kLineData = kLineDataList.get(i);
            String dateStr = kLineData.get(0);
            convertToDate(dateStr);
            float high = Float.parseFloat(kLineData.get(2));
            float low = Float.parseFloat(kLineData.get(3));
            float open = Float.parseFloat(kLineData.get(1));
            float close = Float.parseFloat(kLineData.get(4));
            entries.add(new CandleEntry(i, high, low, open, close));

        }

        // 创建 CandleDataSet
        CandleDataSet dataSet = new CandleDataSet(entries, "K 线图");
        dataSet.setShadowColorSameAsCandle(true);
        dataSet.setShadowWidth(0.8f);
        dataSet.setDecreasingColor(getResources().getColor(R.color.market_green));
        dataSet.setDecreasingPaintStyle(Paint.Style.FILL);
        dataSet.setIncreasingColor(getResources().getColor(R.color.market_red));
        dataSet.setIncreasingPaintStyle(Paint.Style.FILL);
        dataSet.setNeutralColor(getResources().getColor(R.color.black1));
        dataSet.setDrawValues(false);

        // Create a CandleData and set it to the chart
        CandleData candleData = new CandleData(dataSet);
        binding.candleStickChart.setData(candleData);
        binding.candleStickChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(dateLabels));
        binding.candleStickChart.moveViewToX(entries.size());  // 将视图移动到最新的数据点
        binding.candleStickChart.setVisibleXRangeMaximum(45);
        binding.candleStickChart.getViewPortHandler().setMinimumScaleX(1f); // 最小缩放范围
        binding.candleStickChart.getViewPortHandler().setMaximumScaleX(200f); // 最大缩放范围

        binding.candleStickChart.invalidate();
    }

    private void convertToDate(String dateStr) {
        try {
            Date date = inputFormat.parse(dateStr);
            String formattedDate = outputFormat.format(date);
            dateLabels.add(formattedDate); // 保存格式化后的日期值
        } catch (ParseException e) {
            e.printStackTrace();
            dateLabels.add(dateStr); // 如果解析失败，仍然添加原始日期值
        }
    }
}