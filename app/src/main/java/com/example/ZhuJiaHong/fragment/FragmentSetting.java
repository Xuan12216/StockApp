package com.example.ZhuJiaHong.fragment;

import com.example.ZhuJiaHong.Util.Data;
import com.mdbs.base.view.fragment.BaseSettingFragment;

public class FragmentSetting extends BaseSettingFragment {
    private Data data = new Data();
    @Override
    protected void logout() {
        super.logout();
        data.resetData(mContext);
    }
}
