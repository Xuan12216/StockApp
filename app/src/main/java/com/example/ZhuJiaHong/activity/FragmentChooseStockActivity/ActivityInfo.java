package com.example.ZhuJiaHong.activity.FragmentChooseStockActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;

import com.example.ZhuJiaHong.R;
import com.example.ZhuJiaHong.databinding.ActivityInfoBinding;
import com.mdbs.base.view.utils.Utils;

public class ActivityInfo extends AppCompatActivity {
    private ActivityInfoBinding binding;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        context = getApplicationContext();

        binding.infoBackIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        adjustSize();
    }

    private void adjustSize() {
        binding.infoTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, Utils.dp2px(context,22));
    }
}