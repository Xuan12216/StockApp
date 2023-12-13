package com.example.ZhuJiaHong.fragment;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.content.ContextCompat;

import com.example.ZhuJiaHong.object.CustomPercentView.CustomPercentView;
import com.google.android.material.appbar.AppBarLayout;
import com.mdbs.base.view.object.dialog.LoadingDialog;
import com.mdbs.basechart.base.BaseLinearLayout;
import com.mdbs.basechart.base.OverviewItemView;
import com.mdbs.basechart.base.Utils;
import com.mdbs.basechart.view.PercentView;
import com.mdbs.basechart.view.chip.DefaultChipFragment;
import com.mdbs.basechart.view.corporation.CorporationView;
import com.mdbs.basechart.view.industrydistribution.IndustryDistributionView;
import com.mdbs.basechart.view.taiex.TaiexDataView;
import com.mdbs.basechart.view.taiex.TaiexRankView;
import com.mdbs.basechart.view.taiex.TaiexTrendView;
import com.mdbs.basechart.view.taiex.UpDownView;
import com.mdbs.basechart.view.txf.TxfChartView;
import com.example.ZhuJiaHong.R;
import com.mdbs.basechart.view.txf.TxfDataView;

public class TwseView extends BaseLinearLayout {

    // Fields
    private LinearLayout contentLayout;
    public OverviewItemView twse;
    public OverviewItemView otc;
    public OverviewItemView tx;
    public TaiexTrendView trendView;
    public TaiexRankView taiexRankView;
    public UpDownView upDownView;
    public TaiexDataView taiexDataView;
    public IndustryDistributionView industryDistributionView;
    public TxfDataView txfDataView;
    public TxfChartView txfChartView;
    public CorporationView corporationView;
    public CustomPercentView percentView;

    // Constructors
    public TwseView(Context context) {

        super(context);

        baseView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.content_bg));
        baseView.setOrientation(VERTICAL);
        baseView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        initScrollView();
        setPercentView();
        setTrendView();
        setDataView();
        setTxfDataView();
        setTxfChartView();
        setTaiexRankView();
        setCorporationView();
        setIndustryDistributionView();
        setUpDownView();
    }

    // Methods
    private void initScrollView() {

        setOverViewLayout();

        ScrollView scrollView = new ScrollView(mContext);
        scrollView.setVerticalScrollBarEnabled(true);
        baseView.addView(scrollView, new ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT));

        contentLayout = new LinearLayout(mContext);
        contentLayout.setOrientation(LinearLayout.VERTICAL);
        LayoutParams viewParams = new LayoutParams(MATCH_PARENT, WRAP_CONTENT);
        scrollView.addView(contentLayout, viewParams);
    }

    private void setPercentView() {
        int height = Utils.convertDpToPixel(20,mContext);

        LayoutParams params = new LinearLayoutCompat.LayoutParams(MATCH_PARENT, height);
        params.setMargins(Utils.convertDpToPixel(10, mContext), Utils.convertDpToPixel(10, mContext), Utils.convertDpToPixel(10, mContext), 0);

        percentView = new CustomPercentView(mContext, height);
        percentView.setLayoutParams(params);
        contentLayout.addView(percentView);
    }

    private void setTrendView() {

        // 即時圖
        LayoutParams params = new LinearLayoutCompat.LayoutParams(MATCH_PARENT, mResizeUtil.scaleX(240));
        params.setMargins(Utils.convertDpToPixel(10, mContext), Utils.convertDpToPixel(10, mContext), Utils.convertDpToPixel(10, mContext), 0);
        trendView = new TaiexTrendView(mContext);
        trendView.setLayoutParams(params);
        contentLayout.addView(trendView);
        contentLayout.addView(createDivider());
    }

    private void setDataView() {

        // 三大法人
        LayoutParams dataParams = new LayoutParams(MATCH_PARENT, WRAP_CONTENT);
        dataParams.setMargins(com.mdbs.base.view.utils.Utils.convertDpToPixel(10, mContext), 0, com.mdbs.base.view.utils.Utils.convertDpToPixel(10, mContext), 0);
        taiexDataView = new TaiexDataView(mContext);
        taiexDataView.setCorporationImageResource(R.mipmap.twse_corporation_img, R.mipmap.twse_corporation_img_lock);
        contentLayout.addView(taiexDataView, dataParams);
        contentLayout.addView(createDivider());
    }

    private void setTxfDataView() {

        // 台指盤後籌碼快訊
        LayoutParams dataParams = new LayoutParams(MATCH_PARENT, WRAP_CONTENT);
        txfDataView = new TxfDataView(mContext);

        contentLayout.addView(txfDataView, dataParams);
        contentLayout.addView(createDivider());
    }

    private void setTxfChartView() {

        // 小台散戶多空比
        LayoutParams dataParams = new LayoutParams(MATCH_PARENT, WRAP_CONTENT);
        txfChartView = new TxfChartView(mContext);

        contentLayout.addView(txfChartView, dataParams);
        contentLayout.addView(createDivider());
    }

    private void setTaiexRankView() {

        // 加權指數排行
        LayoutParams rankParams = new LayoutParams(MATCH_PARENT, WRAP_CONTENT);
        rankParams.setMargins(0, 0,0, Utils.convertDpToPixel(10, mContext));

        taiexRankView = new TaiexRankView(mContext);
        contentLayout.addView(taiexRankView, rankParams);
        contentLayout.addView(createDivider());
    }

    private void setCorporationView() {

        // 法人佈局
        LayoutParams params = new LayoutParams(MATCH_PARENT, WRAP_CONTENT);
        params.setMargins(0, 0,0, Utils.convertDpToPixel(10, mContext));

        corporationView = new CorporationView(mContext);
        contentLayout.addView(corporationView, params);
        contentLayout.addView(createDivider());
    }

    private void setIndustryDistributionView() {

        // 產業分佈
        LayoutParams upDownParams = new LayoutParams(MATCH_PARENT, WRAP_CONTENT);
        industryDistributionView = new IndustryDistributionView(mContext);
        contentLayout.addView(industryDistributionView, upDownParams);
        contentLayout.addView(createDivider());
    }

    private void setUpDownView() {

        // 漲跌家數
        LayoutParams upDownParams = new LayoutParams(MATCH_PARENT, WRAP_CONTENT);
        upDownView = new UpDownView(mContext);
        contentLayout.addView(upDownView, upDownParams);
    }

    private void setOverViewLayout() {

        LinearLayout overViewLayout = new LinearLayout(mContext);
        overViewLayout.setBackgroundColor(mContext.getResources().getColor(R.color.favorite_overview_bg));
        AppBarLayout.LayoutParams overViewParams = new AppBarLayout.LayoutParams(AppBarLayout.LayoutParams.MATCH_PARENT, AppBarLayout.LayoutParams.WRAP_CONTENT);
        overViewParams.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL | AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED);
        baseView.addView(overViewLayout, overViewParams);

        // 加權
        twse = new OverviewItemView(mContext, getResources().getString(R.string.taiex_taiex_title));
        overViewLayout.addView(twse);
        View divider = new View(mContext);
        LayoutParams dividerParams = new LayoutParams(Utils.convertDpToPixel(1, mContext), ViewGroup.LayoutParams.MATCH_PARENT);
        divider.setBackgroundColor(getResources().getColor(R.color.favorite_overview_divider));
        dividerParams.setMargins(0, Utils.convertDpToPixel(2, mContext), 0, Utils.convertDpToPixel(2, mContext));
        overViewLayout.addView(divider, dividerParams);

        // 櫃買
        otc = new OverviewItemView(mContext, getResources().getString(R.string.taiex_otc_title));
        overViewLayout.addView(otc);
        View divider2 = new View(mContext);
        divider2.setBackgroundColor(getResources().getColor(R.color.favorite_overview_divider));
        overViewLayout.addView(divider2, dividerParams);

        // 台指近
        tx = new OverviewItemView(mContext, getResources().getString(R.string.taiex_txf_title));
        overViewLayout.addView(tx);
    }
    private View createDivider() {

        View divider = new View(mContext);
        LayoutParams dividerParams = new LayoutParams(MATCH_PARENT, Utils.convertDpToPixel(3, mContext));
        dividerParams.setMargins(0, Utils.convertDpToPixel(10, mContext), 0, Utils.convertDpToPixel(10, mContext));
        divider.setBackgroundColor(getResources().getColor(R.color.taiex_divider));
        divider.setLayoutParams(dividerParams);

        return divider;
    }
}