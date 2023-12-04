package com.example.ZhuJiaHong.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ZhuJiaHong.R;
import com.example.ZhuJiaHong.activity.FragmentChooseStockActivity.ActivityAdvancedSettings;
import com.example.ZhuJiaHong.activity.FragmentChooseStockActivity.ActivityInfo;
import com.example.ZhuJiaHong.databinding.FragmentChooseStockBinding;
import com.example.ZhuJiaHong.fragment.FragmentChooseStock.FragmentChooseStock;
import com.mdbs.base.view.fragment.BaseFragment;
import com.mdbs.base.view.object.dialog.LoadingDialog;
import com.mdbs.base.view.utils.Utils;

public class FragmentLockStock extends FragmentChooseStock {
    private String[] tabUpItem = {"等突破","高檔等回檔","回檔等上漲"};
    private String[] tabUpItem1 = {"等跌破","低檔等反彈","反彈等下跌"};
    private static boolean img_tab_click_status = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}