package com.example.ZhuJiaHong.object.twse;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.text.Spanned;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.ZhuJiaHong.R;
import com.example.ZhuJiaHong.Util.AppUtil;
import com.mdbs.basechart.base.BaseLinearLayout;
import com.mdbs.basechart.base.Utils;

public class TaiexView extends BaseLinearLayout {

    // Fields
    private TextView titleView;
    private TextView lampText;
    private TextView priceText;
    private TextView diffText;


    // Constructors
    public TaiexView(Context context) {

        super(context);

        this.setOrientation(VERTICAL);

        initView();
    }


    // Methods
    private void initView() {

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.weight = 1;
        this.setLayoutParams(params);

        LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout titleLayout = new LinearLayout(mContext);
        titleLayout.setOrientation(HORIZONTAL);
        this.addView(titleLayout, titleParams);

        titleView = new TextView(mContext);
        LinearLayout.LayoutParams txtP = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
        titleView.setGravity(Gravity.TOP);
        titleView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        titleView.setTextColor(Color.WHITE);
        titleView.setText("加權");
        titleLayout.addView(titleView, txtP);

        lampText = new TextView(mContext);
        LinearLayout.LayoutParams lampParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
        lampParams.setMargins(Utils.convertDpToPixel(5, mContext), 0, 0, 0);
        lampText.setGravity(Gravity.TOP);
        lampText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        lampText.setTextColor(Color.WHITE);
        lampText.setText("");
        titleLayout.addView(lampText, lampParams);

        priceText = new TextView(mContext);
        LinearLayout.LayoutParams priceParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        priceParams.weight = 1;
        priceText.setGravity(Gravity.LEFT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            priceText.setLetterSpacing(0.05f);
        }
        priceText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        priceText.setTextColor(Color.WHITE);
        this.addView(priceText, priceParams);

        diffText = new TextView(mContext);
        LinearLayout.LayoutParams diffParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        diffParams.weight = 1;
        diffText.setGravity(Gravity.LEFT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            priceText.setLetterSpacing(0.05f);
        }
        diffText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        diffText.setTextColor(Color.WHITE);
        this.addView(diffText, diffParams);
    }

    public void setTitle(String title) {

        titleView.setText(title);
    }

    public void setPrice(String price) {

        priceText.setText(price);
    }

    public void setDiffPrice(float diffPrice, float dfRate) {

        diffText.setText(AppUtil.getDiffString(diffPrice, dfRate));

        if (diffPrice > 0) {
            priceText.setTextColor(ContextCompat.getColor(mContext, R.color.stock_price_rise));
            diffText.setTextColor(ContextCompat.getColor(mContext, R.color.stock_price_rise));
        }
        else if (diffPrice < 0) {

            priceText.setTextColor(ContextCompat.getColor(mContext, R.color.stock_price_green));
            diffText.setTextColor(ContextCompat.getColor(mContext, R.color.stock_price_green));
        }
        else {
            priceText.setTextColor(ContextCompat.getColor(mContext, R.color.stock_price_plate));
            diffText.setTextColor(ContextCompat.getColor(mContext, R.color.stock_price_plate));

        }
    }

    public void setDiff(String diff) {

        diffText.setText(diff);
    }

    public void setLamp(Spanned lamp) {

        lampText.setText(lamp);
    }
}