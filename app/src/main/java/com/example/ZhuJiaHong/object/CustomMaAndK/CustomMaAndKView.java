package com.example.ZhuJiaHong.object.CustomMaAndK;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import com.example.ZhuJiaHong.R;
import com.example.ZhuJiaHong.Util.MAData;
import com.github.mikephil.charting.charts.CandleStickChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.DefaultValueFormatter;
import com.mdbs.starwave_meta.common.stock.ProductSymbol;
import com.mdbs.starwave_meta.params.RFOwlData;

import java.util.ArrayList;
import java.util.List;

public class CustomMaAndKView extends FrameLayout {

    private CandleStickChart candleStickChart;
    private LineChart lineChart;
    private float density, sizePx;
    private boolean isDataReady = false, isMaDataReady = false;
    public CustomMaAndKView(Context context) {
        super(context);
        init();
    }

    public CustomMaAndKView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        density = getResources().getDisplayMetrics().density;
        sizePx = density * 0.6f;
        initCandleStickChart();
        initLineChart();
    }

    private void initLineChart() {
        lineChart = new LineChart(getContext());
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        lineChart.setLayoutParams(layoutParams);

        lineChart.setDragEnabled(false);
        lineChart.setScaleEnabled(false);  // 禁止缩放
        lineChart.setTouchEnabled(false);  // 禁止手势操作
        lineChart.getLegend().setEnabled(false); // 禁用图例
        lineChart.requestDisallowInterceptTouchEvent(true);
        lineChart.setDescription(null);

        // 配置 X 轴
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawLabels(false);
        xAxis.setDrawAxisLine(false);
        // 配置 Y 轴
        YAxis leftAxis = lineChart.getAxisLeft();
        YAxis rightAxis = lineChart.getAxisRight();
        leftAxis.setDrawLabels(false);
        leftAxis.setDrawAxisLine(false);
        leftAxis.setDrawGridLines(false);
        rightAxis.setTextColor(getResources().getColor(R.color.black1));
        rightAxis.setDrawLabels(true);
        rightAxis.setLabelCount(5,true);
        rightAxis.setValueFormatter(new DefaultValueFormatter(2));
        rightAxis.setDrawAxisLine(false);
        rightAxis.setGridColor(getResources().getColor(R.color.matching1_2));
        rightAxis.setDrawGridLines(false);

        addView(lineChart);
    }

    private void initCandleStickChart() {
        // 在这里初始化 CandleStickChart
        candleStickChart = new CandleStickChart(getContext());
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        candleStickChart.setLayoutParams(layoutParams);

        // 配置 CandleStickChart 的一些属性
        candleStickChart.setDragEnabled(false);  // 禁止拖拽
        candleStickChart.setScaleEnabled(false);  // 禁止缩放
        candleStickChart.setTouchEnabled(false);  // 禁止手势操作
        candleStickChart.getLegend().setEnabled(false); // 禁用图例
        candleStickChart.requestDisallowInterceptTouchEvent(true);
        candleStickChart.setDescription(null);

        // 配置 X 轴
        XAxis xAxis = candleStickChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawLabels(false);
        xAxis.setDrawAxisLine(false);
        // 配置 Y 轴
        YAxis leftAxis = candleStickChart.getAxisLeft();
        YAxis rightAxis = candleStickChart.getAxisRight();
        leftAxis.setDrawLabels(false);
        leftAxis.setDrawAxisLine(false);
        leftAxis.setDrawGridLines(false);
        rightAxis.setTextColor(getResources().getColor(R.color.black1));
        rightAxis.setDrawLabels(true);
        rightAxis.setLabelCount(5,true);
        rightAxis.setValueFormatter(new DefaultValueFormatter(2));
        rightAxis.setDrawAxisLine(false);
        rightAxis.setGridColor(getResources().getColor(R.color.matching1_2));
        rightAxis.setDrawGridLines(true);

        addView(candleStickChart);
    }

    public void setData(List<MAData> listMA8, List<MAData> listMA22, RFOwlData list) {
        // 将 list 转换为 CandleEntry 列表
        List<ArrayList<String>> last25Entries = list.getData();
        ArrayList<CandleEntry> entries = new ArrayList<>();

        float minValue = Float.MAX_VALUE;
        float maxValue = Float.MIN_VALUE;

        for (int i = last25Entries.size() - 1; i >= 0; i--) {
            ArrayList<String> data = last25Entries.get(i);
            float open = Float.parseFloat(data.get(3));  // 开盘价
            float high = Float.parseFloat(data.get(4));  // 最高价
            float low = Float.parseFloat(data.get(5));   // 最低价
            float close = Float.parseFloat(data.get(6)); // 收盘价

            entries.add(new CandleEntry(last25Entries.size() - 1 - i, high, low, open, close));

            // 记录最高和最低价
            minValue = Math.min(minValue, low);
            maxValue = Math.max(maxValue, high);
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

        // 创建 CandleData，注意这里传入的是一个包含 dataSet 的列表
        CandleData candleData = new CandleData(dataSet);

        //==========================================

        // 处理 MA8 数据
        List<Entry> entriesMA8 = new ArrayList<>();
        for (int i = 0; i < listMA8.size(); i++) {
            MAData maData = listMA8.get(i);
            float maValue = Float.parseFloat(maData.getMA());
            entriesMA8.add(new Entry(i, maValue));

            // 记录最高和最低价
            minValue = Math.min(minValue, maValue);
            maxValue = Math.max(maxValue, maValue);
        }

        // 处理 MA22 数据
        List<Entry> entriesMA22 = new ArrayList<>();
        for (int i = 0; i < listMA22.size(); i++) {
            MAData maData = listMA22.get(i);
            float maValue = Float.parseFloat(maData.getMA());
            entriesMA22.add(new Entry(i, maValue));

            // 记录最高和最低价
            minValue = Math.min(minValue, maValue);
            maxValue = Math.max(maxValue, maValue);
        }

        // 创建 LineDataSet for MA8
        LineDataSet lineDataSetMA8 = new LineDataSet(entriesMA8, "MA8");
        lineDataSetMA8.setColor(Color.parseColor("#E659A1"));
        lineDataSetMA8.setDrawCircles(false);
        lineDataSetMA8.setDrawValues(false);
        lineDataSetMA8.setLineWidth(sizePx);

        // 创建 LineDataSet for MA22
        LineDataSet lineDataSetMA22 = new LineDataSet(entriesMA22, "MA22");
        lineDataSetMA22.setColor(Color.parseColor("#0099AD"));
        lineDataSetMA22.setDrawCircles(false);
        lineDataSetMA22.setDrawValues(false);
        lineDataSetMA22.setLineWidth(sizePx);

        // 创建 LineData
        LineData lineData = new LineData(lineDataSetMA8, lineDataSetMA22);

        //==========================================

        candleStickChart.getAxisLeft().setAxisMaximum(maxValue);
        candleStickChart.getAxisLeft().setAxisMinimum(minValue);
        candleStickChart.getAxisRight().setAxisMaximum(maxValue);
        candleStickChart.getAxisRight().setAxisMinimum(minValue);

        lineChart.getAxisLeft().setAxisMaximum(maxValue);
        lineChart.getAxisLeft().setAxisMinimum(minValue);
        lineChart.getAxisRight().setAxisMaximum(maxValue);
        lineChart.getAxisRight().setAxisMinimum(minValue);

        // 设置数据到 CandleStickChart,设置数据到 LineChart
        candleStickChart.setData(candleData);
        lineChart.setData(lineData);

        candleStickChart.invalidate();
        lineChart.invalidate();
    }
}
