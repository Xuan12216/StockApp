package com.example.ZhuJiaHong.object.kf;

import static com.mdbs.starwave_meta.params.RFMatchField.MatchField.PRICE;
import static com.mdbs.starwave_meta.params.RFMatchField.MatchField.TOTAL;
import static com.mdbs.starwave_meta.params.RFMatchField.MatchField.VOLUME;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.ZhuJiaHong.R;
import com.mdbs.starwave_meta.params.RFMatchField;
import com.mdbs.starwave_meta.params.RFStock0Data;

import kotlin.jvm.functions.Function5;

public class PriceStyler implements Function5<View, TextView, RFStock0Data, RFMatchField.MatchField, Boolean, Void> {

    // 集合競價 表示符
//    public final String MARK_AUCTION = com.mdbs.starwave_meta.tools.H5.FontStyle("#FFFF00", "*");
    public final String MARK_AUCTION = "";
    // 漲跌停 res
    public int MARK_MAX = R.drawable.max_price_bg, MARK_MIN = R.drawable.min_price_bg;

    public Object getFieldObject(String field) {
        Object object = RFMatchField.MatchField.valueOf(field);
        if (object != null) object = RFStock0Data.Stock0Field.valueOf(field);
        return object;
    }

    public void invoke(View mBackground, TextView textView, RFStock0Data stock0, RFStock0Data.Stock0Field field, Boolean isMarkMaxMin) {

        Context context = textView.getContext();
        textView.setTextColor(ContextCompat.getColor(context, R.color.grey1));
        textView.setBackgroundColor(Color.TRANSPARENT);
        if (mBackground != null) mBackground.setBackgroundColor(Color.TRANSPARENT);

        String value;
        if (stock0 == null || field == null || stock0.清盤 || "0.00".equals(value = stock0.get(field))) {
            textView.setText("--");
            return;
        }

        textView.setText(Html.fromHtml(value));
        switch (stock0.getTrend(value)) {
            case LIMIT_RISE:
                if (!isMarkMaxMin) textView.setTextColor(ContextCompat.getColor(context, R.color.market_red));
                else if (mBackground != null) mBackground.setBackgroundResource(MARK_MAX);
                else textView.setBackgroundResource(MARK_MAX);
                break;
            case LIMIT_FALL:
                if (!isMarkMaxMin) textView.setTextColor(ContextCompat.getColor(context, R.color.market_green));
                else if (mBackground != null) mBackground.setBackgroundResource(MARK_MIN);
                else textView.setBackgroundResource(MARK_MIN);
                break;
            case RISE:
                textView.setTextColor(ContextCompat.getColor(context, R.color.market_red));
                break;
            case FALL:
                textView.setTextColor(ContextCompat.getColor(context, R.color.market_green));
                break;
        }
    }

    @Override
    public Void invoke(View mBackground, TextView textView, RFStock0Data stock0, RFMatchField.MatchField field, Boolean isMarkMaxMin) {

        Context context = textView.getContext();
        textView.setTextColor(ContextCompat.getColor(context, R.color.grey1));
        textView.setBackgroundColor(Color.TRANSPARENT);
        if (mBackground != null) mBackground.setBackgroundColor(Color.TRANSPARENT);

        String value;
        if (stock0 == null || field == null || stock0.清盤
                || "0".equals(value = stock0.get(field)) || "0.00".equals(value)) {
            textView.setText("--");
            return null;
        }

        boolean delayfactor = PRICE.equals(field) || VOLUME.equals(field);
        String delaySign = delayfactor && stock0.試撮 ? MARK_AUCTION : "";
        textView.setText(Html.fromHtml(value + delaySign));

        if (VOLUME == field) {
            setVolumeStyle(textView, stock0);
            return null;
        }

        if (TOTAL == field) {
            return null;
        }

        switch (stock0.getTrend(value)) {
            case LIMIT_RISE:
                if (!isMarkMaxMin) textView.setTextColor(ContextCompat.getColor(context, R.color.market_red));
                else if (mBackground != null) {
                    mBackground.setBackgroundResource(MARK_MAX);
                    textView.setTextColor(ContextCompat.getColor(context, R.color.white));
                }
                else {
                    textView.setBackgroundResource(MARK_MAX);
                    textView.setTextColor(ContextCompat.getColor(context, R.color.white));
                }
                break;
            case LIMIT_FALL:
                if (!isMarkMaxMin) textView.setTextColor(ContextCompat.getColor(context, R.color.market_green));
                else if (mBackground != null) {
                    mBackground.setBackgroundResource(MARK_MIN);
                    textView.setTextColor(ContextCompat.getColor(context, R.color.white));
                }
                else {
                    textView.setBackgroundResource(MARK_MIN);
                    textView.setTextColor(ContextCompat.getColor(context, R.color.white));
                }
                break;
            case RISE:
                textView.setTextColor(ContextCompat.getColor(context, R.color.market_red));
                break;
            case FALL:
                textView.setTextColor(ContextCompat.getColor(context, R.color.market_green));
                break;
        }

        return null;
    }

    public void setVolumeStyle(TextView textView, RFStock0Data stock0) {

        Context context = textView.getContext();
        int factor = Float.compare(
                Math.abs(stock0.price - stock0.bestSell),
                Math.abs(stock0.price - stock0.bestBuy));
        switch (factor) {
            case -1 : textView.setTextColor(ContextCompat.getColor(context, R.color.market_red)); break;
            case 0 : textView.setTextColor(ContextCompat.getColor(context, R.color.grey1)); break;
            case 1: textView.setTextColor(ContextCompat.getColor(context, R.color.market_green)); break;
        }
    }

}