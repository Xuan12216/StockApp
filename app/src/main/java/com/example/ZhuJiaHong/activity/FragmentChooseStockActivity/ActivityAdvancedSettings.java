package com.example.ZhuJiaHong.activity.FragmentChooseStockActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ZhuJiaHong.R;
import com.example.ZhuJiaHong.Util.Data;
import com.example.ZhuJiaHong.databinding.ActivityAdvancedSettingsBinding;
import com.mdbs.base.view.utils.Utils;

public class ActivityAdvancedSettings extends AppCompatActivity {
    private String[] item = {"策略名稱1","策略名2","策略名稱3","策略名名稱4","策略5","策6","策略名稱稱稱7"};
    private Context context;
    private Data data = new Data();

    private ActivityAdvancedSettingsBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdvancedSettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        context = getApplicationContext();

        //listener
        binding.advancedBackIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.advancedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkFlexClickStatus();
                finish();
            }
        });

        //Method
        adjustSize();
        setFlexBoxValue();
    }

    //============================================

    private void checkFlexClickStatus() {
        for (int i = 0; i < binding.flexboxLayoutFinancial.getChildCount(); i++) {
            View childView = binding.flexboxLayoutFinancial.getChildAt(i);
            if (childView instanceof TextView) {
                TextView textView = (TextView) childView;
                if (textView.getTag() != null && (boolean) textView.getTag()) {
                    System.out.println("TestXuan:TextView in flexboxLayoutFinancial is clicked: " + textView.getText());
                }
            }
        }

        for (int i = 0; i < binding.flexboxLayoutChips.getChildCount(); i++) {
            View childView = binding.flexboxLayoutChips.getChildAt(i);
            if (childView instanceof TextView) {
                TextView textView = (TextView) childView;
                if (textView.getTag() != null && (boolean) textView.getTag()) {
                    System.out.println("TestXuan:TextView in flexboxLayoutChips is clicked: " + textView.getText());
                }
            }
        }

        for (int i = 0; i < binding.flexboxLayoutIndex.getChildCount(); i++) {
            View childView = binding.flexboxLayoutIndex.getChildAt(i);
            if (childView instanceof TextView) {
                TextView textView = (TextView) childView;
                if (textView.getTag() != null && (boolean) textView.getTag()) {
                    System.out.println("TestXuan:TextView in flexboxLayoutIndex is clicked: " + textView.getText());
                }
            }
        }

        for (int i = 0; i < binding.flexboxLayoutGrowth.getChildCount(); i++) {
            View childView = binding.flexboxLayoutGrowth.getChildAt(i);
            if (childView instanceof TextView) {
                TextView textView = (TextView) childView;
                if (textView.getTag() != null && (boolean) textView.getTag()) {
                    System.out.println("TestXuan:TextView in flexboxLayoutGrowth is clicked: " + textView.getText());
                }
            }
        }
    }

    //============================================

    private void adjustSize() {
        binding.advancedTv.setTextSize(TypedValue.COMPLEX_UNIT_PX,Utils.dp2px(context,22));
        binding.financialReportTv.setTextSize(TypedValue.COMPLEX_UNIT_PX,Utils.dp2px(context,20));
        binding.chipsTv.setTextSize(TypedValue.COMPLEX_UNIT_PX,Utils.dp2px(context,20));
        binding.indexTv.setTextSize(TypedValue.COMPLEX_UNIT_PX,Utils.dp2px(context,20));
        binding.growthAmountTv.setTextSize(TypedValue.COMPLEX_UNIT_PX,Utils.dp2px(context,20));
        binding.advancedBtn.setTextSize(TypedValue.COMPLEX_UNIT_PX,Utils.dp2px(context,18));
    }

    //============================================

    private void setFlexBoxValue() {
        for (String strategyName : item) {
            binding.flexboxLayoutFinancial.addView(createTextView(strategyName, 10, "flexboxLayoutFinancial"));
            binding.flexboxLayoutChips.addView(createTextView(strategyName, 10, "flexboxLayoutChips"));
            binding.flexboxLayoutIndex.addView(createTextView(strategyName, 10, "flexboxLayoutIndex"));
            binding.flexboxLayoutGrowth.addView(createTextView(strategyName, 10, "flexboxLayoutGrowth"));
        }
    }

    //============================================

    private TextView createTextView(String text, int margin, String flexBoxId) {
        ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(this, R.style.FlexBoxTvUnClickStyle);
        margin = Utils.dp2px(context, margin);

        final TextView textView = new TextView(contextThemeWrapper);
        textView.setText(text);

        // 从 SharedPreferences 中加载点击状态
        boolean isClicked = data.loadClickStatus(this, flexBoxId, text);
        textView.setTag(isClicked);
        updateTextViewBackground(textView, isClicked);

        ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(margin, margin, 0, 0);
        textView.setLayoutParams(params);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, Utils.dp2px(context, 18));

        textViewListener(textView, flexBoxId, text);
        return textView;
    }

    //============================================

    private void textViewListener(TextView textView, String flexBoxId, String text) {
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isClicked = !(boolean) textView.getTag();
                textView.setTag(isClicked);
                updateTextViewBackground(textView, isClicked);

                // 保存点击状态到 SharedPreferences
                data.saveClickStatus(ActivityAdvancedSettings.this, flexBoxId, text, isClicked);
            }
        });
    }

    //============================================

    private void updateTextViewBackground(TextView textView, boolean isClicked) {
        if (isClicked) {
            textView.setBackground(getDrawable(R.drawable.flexbox_click_bg));
        } else {
            textView.setBackground(getDrawable(R.drawable.flexbox_unclick_bg));
        }
    }
}