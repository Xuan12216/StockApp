package com.example.ZhuJiaHong;

import android.content.Context;
import android.graphics.Color;

import com.mdbs.basechart.base.Utils;
import com.mdbs.basechart.setting.TabBarSetting;
import com.mdbs.basechart.setting.factory.ResourceFactory;

public class MyResourceFactory extends ResourceFactory{

    public MyResourceFactory(Context context) {super(context);}

    @Override
    public void set指數內頁Res() {
        super.set指數內頁Res();
        setTabBar價量技術();
        setTabBar分日週月();
        setTabBar分KTabBar();
        setTabBar附圖TabBar();
    }

    @Override
    public void set個股頁Res() {
        super.set個股頁Res();
        set個股_功能列設定();
    }

    @Override
    public void set個股頁即時Res(){
        super.set個股頁即時Res();
        set個股即時_五檔買賣力TabBar設定();
    }

    @Override
    public void set個股價量Res(){
        super.set個股價量Res();
        set個股價量_功能列TabBar設定();
        set個股價量_累N日列TabBar設定();
    }

    @Override
    public void set個股K線Res(){
        super.set個股K線Res();
        set個股K線_分日週月TabBar設定();
        set個股K線_附圖TabBar設定();
    }

    //====================================================================

    private void set個股K線_附圖TabBar設定() {
        TabBarSetting tabBarSetting = new TabBarSetting();
        tabBarSetting.單一背景色 = Color.parseColor("#DAD0C1");
        tabBarSetting.未選取文字色 = Color.parseColor("#3F3F3F");
        tabBarSetting.選取文字色 = Color.parseColor("#866C45");
        tabBarSetting.底線色 = Color.parseColor("#866C45");
        this.addResource("個股K線_附圖TabBar設定_o", tabBarSetting);
    }

    private void set個股K線_分日週月TabBar設定() {
        TabBarSetting tabBarSetting = new TabBarSetting();
        tabBarSetting.未選取文字色 = Color.parseColor("#B6B6B6");
        tabBarSetting.選取文字色 = Color.parseColor("#866C45");
        this.addResource("個股K線_分日週月TabBar設定_o", tabBarSetting);
    }

    private void set個股價量_累N日列TabBar設定() {
        TabBarSetting tabBarSetting = new TabBarSetting();
        tabBarSetting.未選取文字色 = Color.parseColor("#B6B6B6");
        tabBarSetting.選取文字色 = Color.parseColor("#866C45");
        this.addResource("個股價量_累N日列TabBar設定_o", tabBarSetting);
    }

    private void set個股價量_功能列TabBar設定() {
        TabBarSetting tabBarSetting = new TabBarSetting();
        tabBarSetting.未選取文字色 = Color.parseColor("#EDE7DD");
        tabBarSetting.選取文字色 = Color.parseColor("#FFFFFF");
        tabBarSetting.未選取背景色 = Color.parseColor("#C7B89F");
        tabBarSetting.選取背景色 = Color.parseColor("#866C45");
        this.addResource("個股價量_功能列TabBar設定_o", tabBarSetting);
    }

    private void set個股即時_五檔買賣力TabBar設定() {
        TabBarSetting tabBarSetting = new TabBarSetting();
        tabBarSetting.未選取文字色 = Color.parseColor("#E6DFD3");
        tabBarSetting.選取文字色 = Color.parseColor("#FFFFFF");
        tabBarSetting.未選取背景色 = Color.parseColor("#C7B89F");
        tabBarSetting.選取背景色 = Color.parseColor("#866C45");
        this.addResource("個股即時_五檔買賣力TabBar設定_o", tabBarSetting);
    }

    private void set個股_功能列設定() {
        TabBarSetting tabBarSetting = new TabBarSetting();
        tabBarSetting.未選取文字色 = Color.parseColor("#EDE7DD");
        tabBarSetting.選取文字色 = Color.parseColor("#FFFFFF");
        tabBarSetting.未選取背景色 = Color.parseColor("#C7B89F");
        tabBarSetting.選取背景色 = Color.parseColor("#866C45");
        tabBarSetting.邊框顏色 = Color.parseColor("#866C45");
        tabBarSetting.四邊圓角角度 = Utils.dp2px(appContext, 8);
        tabBarSetting.邊框粗度 = Utils.dp2px(appContext, 1);
        this.addKeepResource("個股_功能列設定_o", tabBarSetting);
    }

    private void setTabBar附圖TabBar() {
        TabBarSetting tabBarSetting = new TabBarSetting();
        tabBarSetting.未選取文字色 = Color.parseColor("#3F3F3F");
        tabBarSetting.選取文字色 = Color.parseColor("#866C45");
        tabBarSetting.單一背景色 = Color.parseColor("#DAD0C1");
        tabBarSetting.寬度是否長滿畫面 = true;
        tabBarSetting.底線色 = Color.parseColor("#866C45");
        this.addResource("大盤指數頁_附圖TabBar設定_o", tabBarSetting);
    }

    private void setTabBar分KTabBar() {
        TabBarSetting tabBarSetting = new TabBarSetting();
        tabBarSetting.未選取文字色 = Color.parseColor("#3F3F3F");
        tabBarSetting.選取文字色 = Color.parseColor("#866C45");
        tabBarSetting.未選取背景色 = Color.parseColor("#DAD0C1");
        tabBarSetting.選取背景色 = Color.parseColor("#DAD0C1");
        this.addResource("大盤指數頁_分KTabBar設定_o", tabBarSetting);
    }

    private void setTabBar分日週月() {
        TabBarSetting tabBarSetting = new TabBarSetting();
        tabBarSetting.未選取文字色 = Color.parseColor("#FFFFFF");
        tabBarSetting.選取文字色 = Color.parseColor("#FFFFFF");
        tabBarSetting.未選取背景色 = Color.parseColor("#C7B89F");
        tabBarSetting.選取背景色 = Color.parseColor("#866C45");
        tabBarSetting.邊框顏色 = Color.parseColor("#866C45");
        tabBarSetting.四邊圓角角度 = Utils.dp2px(appContext, 8);
        tabBarSetting.邊框粗度 = Utils.dp2px(appContext, 1);
        this.addResource("大盤指數頁_分日週月TabBar設定_o", tabBarSetting);
    }

    private void setTabBar價量技術() {
        TabBarSetting tabBarSetting = new TabBarSetting();
        tabBarSetting.未選取文字色 = Color.parseColor("#FFFFFF");
        tabBarSetting.選取文字色 = Color.parseColor("#FFFFFF");
        tabBarSetting.選取背景色 = Color.parseColor("#866C45");
        tabBarSetting.未選取背景色 = Color.parseColor("#C7B89F");
        this.addResource("大盤指數頁_價量技術TabBar設定_o", tabBarSetting);
    }
}
