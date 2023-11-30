package com.example.ZhuJiaHong.object.CustomPercentView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

import com.mdbs.basechart.base.Utils;
import com.mdbs.basechart.setting.ModelResourceSetting;
import com.mdbs.basechart.view.PercentView;
import com.mdbs.starwave_meta.network.rxhttp.RxOwlHttpClient;
import com.mdbs.starwave_meta.params.RFUpDownCount;

import java.util.Locale;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.internal.functions.Functions;
import io.reactivex.schedulers.Schedulers;

public class CustomPercentView extends PercentView {

    private String leftText = "";
    private String rightText = "";
    private int height = 0;
    private Context mContext;
    private CompositeDisposable disposes;
    private Handler handler = new Handler();
    private ModelResourceSetting resourceSetting;

    public CustomPercentView(Context context, int height) {
        super(context);
        this.mContext = context;
        this.height = height;
        init();
    }

    private void init() {
        resourceSetting = new ModelResourceSetting(true);
        paintText.setColor(this.mContext.getResources().getColor(android.R.color.white));
    }

    public CustomPercentView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void setCustomText(String leftText, String rightText) {
        this.leftText = leftText;
        this.rightText = rightText;
    }

    public void setCustomTextSize(int Size){
        paintText.setTextSize(Utils.convertDpToPixel(Size,mContext));
    }

    public void connect() {
        try {
            if (this.disposes == null) {
                this.disposes = new CompositeDisposable();
            }

            this.disposes.clear();
            this.disposes.add(RxOwlHttpClient.getInstance().getMetaApi()
                    .getUpDownCount()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .retry()
                    .subscribe(this::refreshUpDown, Functions.ERROR_CONSUMER));
        }
        catch (Exception var2) {var2.printStackTrace();}
    }

    public void disconnect() {
        this.handler.removeCallbacks(this.renewInfoRunnable);
        this.handler.removeCallbacksAndMessages((Object)null);
        if (this.disposes != null) {
            this.disposes.dispose();
            this.disposes = null;
        }
    }

    private final Runnable renewInfoRunnable = this::connect;

    private void refreshUpDown(RFUpDownCount rfUpDownCount) {
        if (rfUpDownCount != null) {
            float positive = 0;
            float negative = 0;
            float total = 0;
            float value = 0;

            for (int i = 0; i < rfUpDownCount.count.size(); i++){
                if (rfUpDownCount.group.get(i).startsWith("+")) positive += Float.parseFloat(rfUpDownCount.count.get(i));
                else if (rfUpDownCount.group.get(i).startsWith("-")) negative += Float.parseFloat(rfUpDownCount.count.get(i));
            }

            total = positive + negative;
            value = positive / total * 100;

            setBackgroundColor(resourceSetting.color漲(), resourceSetting.color跌());
            setCustomTextSize(14);
            setCustomText("漲家 "+String.format(Locale.US, "%.0f", positive),"跌家 "+String.format(Locale.US, "%.0f", negative));
            setValue(value);

            this.handler.removeCallbacks(this.renewInfoRunnable);
            this.handler.postDelayed(this.renewInfoRunnable, 60000L);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (!leftText.isEmpty() && !rightText.isEmpty()) {
            float offset = Utils.convertDpToPixel(5,mContext);
            float leftX = offset;
            float rightX = getWidth() - offset - paintText.measureText(rightText);

            Paint.FontMetrics fontMetrics = paintText.getFontMetrics();
            float textHeight = fontMetrics.bottom - fontMetrics.top;
            float y = height / 2 - textHeight / 2 + (textHeight - fontMetrics.bottom);

            canvas.drawText(leftText, leftX, y, paintText);
            canvas.drawText(rightText, rightX, y, paintText);
        }
    }
}
