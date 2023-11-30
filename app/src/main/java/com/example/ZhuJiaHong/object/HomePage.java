package com.example.ZhuJiaHong.object;

import android.content.Context;

import com.example.ZhuJiaHong.fragment.AppFragmentFactory;
import com.mdbs.base.view.fragment.FragmentFactory;
import com.mdbs.base.view.object.homepage.BaseHomePage;

public class HomePage extends BaseHomePage {

    public HomePage(Context context) {
        super(context);
    }

    @Override
    public FragmentFactory createFragmentFactory() {
        return new AppFragmentFactory();
    }
}
