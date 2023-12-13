package com.example.ZhuJiaHong.fragment;

import android.content.OperationApplicationException;

import androidx.fragment.app.Fragment;

import com.example.ZhuJiaHong.fragment.FragmentChooseStock.FragmentChooseStock;
import com.mdbs.base.view.fragment.BaseInformationFragment;
import com.mdbs.base.view.fragment.BaseSettingFragment;
import com.mdbs.basechart.view.chip.DefaultChipFragment;

public class AppFragmentFactory implements com.mdbs.base.view.fragment.FragmentFactory {
    @Override
    public Fragment createFragment(String name) throws OperationApplicationException {
        // TODO 依照項目名稱回傳對應Fragment
        switch (name) {
            case "自選":
                return new FragmentFavorite();
            case "大盤":
                return new FragmentTwse();
            case "選股":
                return new FragmentChooseStock();
            case "鎖股":
                return new FragmentLockStock();
            case "分點":
                return new DefaultChipFragment();
            case "情報":
                return new BaseInformationFragment();
            case "設定":
                return new FragmentSetting();
            default:
                throw new OperationApplicationException("undefined fragment");
        }
    }
}
