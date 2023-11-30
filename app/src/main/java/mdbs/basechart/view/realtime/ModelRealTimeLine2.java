package mdbs.basechart.view.realtime;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.Typeface;
import java.text.DecimalFormat;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.ZhuJiaHong.R;
import com.mdbs.basechart.base.Utils;
import com.mdbs.basechart.setting.ModelResourceSetting;
import com.mdbs.basechart.setting.ModelSetting;
import com.mdbs.basechart.utils.DrawUtil;
import com.mdbs.starwave_meta.params.RFOwlData;

import java.util.ArrayList;

public class ModelRealTimeLine2 extends View {
    private final Context context;
    private ModelResourceSetting resource;
    protected int viewX = 0;
    protected int viewY = 0;
    private Paint mPaintRed;
    private Paint mPaintWhite;
    private Paint mPaintGreen;
    private Paint mPaintLine;
    private Paint mPaintOpenPrice;
    private Path path1;
    private Path path2;
    private Path upPath;
    private Path downPath;
    private Path avgPath;
    private Paint mPaintRise;
    private Paint mPaintRiseLine;
    private Paint mPaintFallen;
    private Paint mPaintFallenLine;
    private Paint mPaintExpected;
    private float maxPrice = 200.0F;
    private float minPrice = 100.0F;
    private float preClosePrice = 150.0F;
    private float openPrice = 0.0F;
    private float startX;
    private float startY;
    private float centerX;
    private float centerY;
    private float endX;
    private float endY;
    private RFOwlData itemOwlData;
    private float oneTime;
    private float oneTick;
    String maxStr;
    String cenStr;
    String minStr;
    int maxH;

    public ModelRealTimeLine2(Context context) {
        super(context);
        this.context = context;
        this.resource = new ModelResourceSetting(ModelSetting.此App為淺色底);
        this.init();
    }

    public ModelRealTimeLine2(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        this.resource = new ModelResourceSetting(ModelSetting.此App為淺色底);
        this.init();
    }

    private void init() {
        this.initPaint();
        this.path1 = new Path();
        this.path2 = new Path();
        this.upPath = new Path();
        this.downPath = new Path();
        this.avgPath = new Path();
    }

    private void initPaint() {
        this.mPaintRed = new Paint();
        this.mPaintRed.setAntiAlias(true);
        this.mPaintRed.setDither(true);
        this.mPaintRed.setColor(getResources().getColor(R.color.market_red));
        this.mPaintRed.setTextSize(24.0F);
        this.mPaintRed.setTypeface(Typeface.DEFAULT_BOLD);
        this.mPaintWhite = new Paint();
        this.mPaintWhite.setAntiAlias(true);
        this.mPaintWhite.setDither(true);
        this.mPaintWhite.setColor(this.resource.color小報價參考價());
        this.mPaintWhite.setTextSize(24.0F);
        this.mPaintWhite.setTypeface(Typeface.DEFAULT_BOLD);
        this.mPaintGreen = new Paint();
        this.mPaintGreen.setAntiAlias(true);
        this.mPaintGreen.setDither(true);
        this.mPaintGreen.setColor(getResources().getColor(R.color.market_green));
        this.mPaintGreen.setTextSize(24.0F);
        this.mPaintGreen.setTypeface(Typeface.DEFAULT_BOLD);
        this.mPaintLine = new Paint();
        this.mPaintLine.setAntiAlias(true);
        this.mPaintLine.setDither(true);
        this.mPaintLine.setStrokeWidth(1.0F);
        this.mPaintLine.setStyle(Paint.Style.FILL);
        this.mPaintLine.setColor(this.resource.color即時圖線框());
        this.mPaintLine.setTextSize(24.0F);
        this.mPaintOpenPrice = new Paint();
        this.mPaintOpenPrice.setAntiAlias(true);
        this.mPaintOpenPrice.setDither(true);
        this.mPaintOpenPrice.setStrokeWidth(1.0F);
        this.mPaintOpenPrice.setStyle(Paint.Style.STROKE);
        this.mPaintOpenPrice.setPathEffect(new DashPathEffect(new float[]{8.0F, 4.0F}, 0.0F));
        this.mPaintOpenPrice.setColor(this.resource.color小報價開盤線());
        this.mPaintExpected = new Paint();
        this.mPaintExpected.setAntiAlias(true);
        this.mPaintExpected.setDither(true);
        this.mPaintExpected.setStrokeWidth((float) Utils.convertDpToPixel(1.1F, this.context));
        this.mPaintExpected.setStyle(Paint.Style.STROKE);
        this.mPaintExpected.setColor(getResources().getColor(R.color.black1));//均價
        this.mPaintRise = new Paint();
        this.mPaintRise.setAntiAlias(true);
        this.mPaintRise.setDither(true);
        this.mPaintRise.setStyle(Paint.Style.FILL);
        this.mPaintRise.setColor(this.resource.color漲());
        this.mPaintRiseLine = new Paint();
        this.mPaintRiseLine.setAntiAlias(true);
        this.mPaintRiseLine.setDither(true);
        this.mPaintRiseLine.setStyle(Paint.Style.STROKE);
        this.mPaintRiseLine.setStrokeWidth((float)Utils.convertDpToPixel(1.5F, this.context));
        this.mPaintRiseLine.setColor(this.resource.color漲());
        this.mPaintFallen = new Paint();
        this.mPaintFallen.setAntiAlias(true);
        this.mPaintFallen.setDither(true);
        this.mPaintFallen.setStyle(Paint.Style.FILL);
        this.mPaintFallen.setColor(this.resource.color跌());
        this.mPaintFallen.setShader(new LinearGradient(this.startX, this.startY, this.startX, this.endY, new int[]{0, this.resource.color跌()}, (float[])null, Shader.TileMode.CLAMP));
        this.mPaintFallen.setAlpha(204);
        this.mPaintFallenLine = new Paint();
        this.mPaintFallenLine.setAntiAlias(true);
        this.mPaintFallenLine.setDither(true);
        this.mPaintFallenLine.setStyle(Paint.Style.STROKE);
        this.mPaintFallenLine.setStrokeWidth((float)Utils.convertDpToPixel(1.5F, this.context));
        this.mPaintFallenLine.setColor(this.resource.color跌());
    }

    @RequiresApi(
            api = 24
    )
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        try {
            this.calcRange();
            this.calcPath();
            canvas.drawText(this.maxStr, 0.0F, this.startY + (float)this.maxH / 2.0F, this.mPaintRed);
            canvas.drawText(this.cenStr, 0.0F, this.centerY + (float)this.maxH / 2.0F, this.mPaintWhite);
            canvas.drawText(this.minStr, 0.0F, this.endY + (float)this.maxH / 2.0F, this.mPaintGreen);
            canvas.drawLine(this.startX, this.startY, (float)this.viewX, this.startY, this.mPaintLine);
            canvas.drawLine(this.centerX, this.centerY, (float)this.viewX, this.centerY, this.mPaintLine);
            canvas.drawLine(this.endX, this.endY, (float)this.viewX, this.endY, this.mPaintLine);
            if (this.openPrice != 0.0F) {
                float openPoint = this.openPrice - this.preClosePrice;
                float openY = this.centerY - openPoint * this.oneTick;
                canvas.drawLine(this.startX, openY, (float)this.viewX, openY, this.mPaintOpenPrice);
            }

            LinearGradient linearGradient2;
            if (!this.upPath.isEmpty()) {
                linearGradient2 = new LinearGradient(this.startX, this.startY, this.startX, this.endY, this.resource.color漲(), 0, Shader.TileMode.CLAMP);
                this.mPaintRise.setShader(linearGradient2);
                canvas.drawPath(this.upPath, this.mPaintRise);
            }

            if (!this.downPath.isEmpty()) {
                linearGradient2 = new LinearGradient(this.startX, this.startY, this.startX, this.endY, 0, this.resource.color跌(), Shader.TileMode.CLAMP);
                this.mPaintFallen.setShader(linearGradient2);
                canvas.drawPath(this.downPath, this.mPaintFallen);
            }

            if (!this.path1.isEmpty()) {
                canvas.drawPath(this.path1, this.mPaintRiseLine);
            }

            if (!this.path2.isEmpty()) {
                canvas.drawPath(this.path2, this.mPaintFallenLine);
            }

            if (!this.avgPath.isEmpty()) {
                canvas.drawPath(this.avgPath, this.mPaintExpected);
            }
        } catch (Exception var4) {
            var4.printStackTrace();
        }

    }

    private float calcTick(float startY, float endY) {
        float price = this.maxPrice - this.minPrice == 0.0F ? 1.0F : this.maxPrice - this.minPrice;
        return this.itemOwlData != null && this.itemOwlData.getData() != null && this.itemOwlData.getData().size() > 0 ? (endY - startY) / price : 0.0F;
    }

    private float calcTimeTick(float startX, float endX) {
        return (endX - startX) / 271.0F;
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        this.viewX = DrawUtil.measureWidth(widthMeasureSpec);
        this.viewY = DrawUtil.measureHeight(heightMeasureSpec);
    }

    public void setPrices(float openPrice, float preClose, float max, float min) {
        this.openPrice = openPrice;
        this.preClosePrice = preClose;
        if (max != 0.0F && min != 0.0F) {
            if (Math.abs(max - this.preClosePrice) > Math.abs(min - this.preClosePrice)) {
                this.maxPrice = max * 1.001F;
                this.minPrice = (this.preClosePrice - Math.abs(max - this.preClosePrice)) * 0.999F;
            } else if (Math.abs(max - this.preClosePrice) < Math.abs(min - this.preClosePrice)) {
                this.maxPrice = (this.preClosePrice + Math.abs(min - this.preClosePrice)) * 1.001F;
                this.minPrice = min * 0.999F;
            } else {
                this.maxPrice = max;
                this.minPrice = min;
            }
        } else {
            this.maxPrice = this.preClosePrice;
            this.minPrice = this.preClosePrice;
        }

    }

    @RequiresApi(
            api = 24
    )
    private void calcRange() {
        DecimalFormat df = new DecimalFormat("##0.00");
        this.maxStr = df.format((double)this.maxPrice);
        this.minStr = df.format((double)this.minPrice);
        this.cenStr = df.format((double)this.preClosePrice);
        Rect bounds = new Rect();
        this.mPaintRed.getTextBounds(this.maxStr, 0, this.maxStr.length(), bounds);
        this.maxH = bounds.height();
        this.startX = this.mPaintRed.measureText(this.maxStr) + 20.0F;
        this.startY = (float)this.maxH / 2.0F;
        Rect minBounds = new Rect();
        this.mPaintGreen.getTextBounds(this.minStr, 0, this.minStr.length(), minBounds);
        int minH = minBounds.height();
        this.endX = this.mPaintGreen.measureText(this.minStr) + 20.0F;
        this.endY = (float)this.viewY - (float)minH / 2.0F;
        Rect centerBounds = new Rect();
        this.mPaintWhite.getTextBounds(this.cenStr, 0, this.cenStr.length(), centerBounds);
        this.centerX = this.mPaintWhite.measureText(this.cenStr) + 20.0F;
        this.centerY = (this.endY + this.startY) / 2.0F;
        this.oneTime = this.calcTimeTick(this.startX, (float)this.viewX);
        this.oneTick = this.calcTick(this.startY, this.endY);
    }

    public void setItemOwlData(RFOwlData data) {
        this.itemOwlData = data;
        this.postInvalidate();
    }

    private void calcPath() {
        this.upPath.reset();
        this.downPath.reset();
        this.path1.reset();
        this.path2.reset();
        this.avgPath.reset();
        if (this.itemOwlData != null && this.itemOwlData.getData() != null && this.viewX != 0) {
            boolean isMoveTo = true;
            int times = 0;
            float preClose = 0.0F;
            float prePoint = 0.0F;
            float preTimes = 0.0F;
            float preUpY = 0.0F;
            float preDownY = 0.0F;
            float preAvgPoint = 0.0F;

            for(int i = 0; i < this.itemOwlData.getData().size(); ++i) {
                float avg = Float.parseFloat((String)((ArrayList)this.itemOwlData.getData().get(i)).get(7));
                float close = Float.parseFloat((String)((ArrayList)this.itemOwlData.getData().get(i)).get(5));
                float point = Math.abs(close - this.preClosePrice);
                float avgPoint = avg - this.preClosePrice;
                String time = (String)((ArrayList)this.itemOwlData.getData().get(i)).get(0);
                int h = Integer.parseInt(time.substring(0, 2));
                int m = Integer.parseInt(time.substring(2, 4));
                times = (h - 9) * 60 + m + 1;
                if (isMoveTo) {
                    this.upPath.moveTo(this.startX + this.oneTime * (float)times, preUpY = this.centerY);
                    this.downPath.moveTo(this.startX + this.oneTime * (float)times, preDownY = this.centerY);
                    isMoveTo = false;
                }

                if (preTimes != 0.0F && (float)times - preTimes > 1.0F) {
                    this.upPath.lineTo(this.startX + this.oneTime * (float)(times - 1), preUpY);
                    this.downPath.lineTo(this.startX + this.oneTime * (float)(times - 1), preDownY);
                }

                if (close > this.preClosePrice) {
                    this.upPath.lineTo(this.startX + this.oneTime * (float)times, preUpY = this.centerY - point * this.oneTick);
                    this.downPath.lineTo(this.startX + this.oneTime * (float)times, preDownY = this.centerY);
                } else if (close < this.preClosePrice) {
                    this.downPath.lineTo(this.startX + this.oneTime * (float)times, preDownY = this.centerY + point * this.oneTick);
                    this.upPath.lineTo(this.startX + this.oneTime * (float)times, preUpY = this.centerY);
                } else {
                    this.upPath.lineTo(this.startX + this.oneTime * (float)times, preUpY = this.centerY);
                    this.downPath.lineTo(this.startX + this.oneTime * (float)times, preDownY = this.centerY);
                }

                float x1 = this.startX + this.oneTime * preTimes;
                float x2 = this.startX + this.oneTime * ((preTimes + (float)times) / 2.0F);
                float x3 = this.startX + this.oneTime * (float)times;
                float y1 = this.centerY - prePoint * this.oneTick;
                float y3 = this.centerY - point * this.oneTick;
                float y2 = this.centerY + prePoint * this.oneTick;
                float y4 = this.centerY + point * this.oneTick;
                float avgPreY = this.centerY - preAvgPoint * this.oneTick;
                float avgY = this.centerY - avgPoint * this.oneTick;
                this.avgPath.moveTo(x1, avgPreY);
                if (x1 != this.startX) {
                    this.avgPath.lineTo(x3, avgY);
                }

                if (preTimes != 0.0F && (float)times - preTimes > 1.0F) {
                    if (preClose > this.preClosePrice) {
                        this.path1.moveTo(x1, y1);
                        this.path1.lineTo(x3, y1);
                    } else if (preClose < this.preClosePrice) {
                        this.path2.moveTo(x1, y2);
                        this.path2.lineTo(x3, y2);
                    } else {
                        this.path1.moveTo(x1, y1);
                        this.path2.moveTo(x3, y2);
                    }

                    x1 = this.startX + this.oneTime * (float)(times - 1);
                }

                if (i > 0) {
                    if (close > this.preClosePrice) {
                        if (preClose > this.preClosePrice) {
                            this.path1.moveTo(x1, y1);
                            this.path1.lineTo(x3, y3);
                        } else if (preClose < this.preClosePrice) {
                            this.path1.moveTo(x3, this.centerY);
                            this.path1.lineTo(x3, y3);
                            this.path2.moveTo(x1, y2);
                            this.path2.lineTo(x3, this.centerY);
                        } else {
                            this.path1.moveTo(x1, this.centerY);
                            this.path1.lineTo(x3, y3);
                            this.path2.moveTo(x2, this.centerY);
                        }
                    } else if (close < this.preClosePrice) {
                        if (preClose > this.preClosePrice) {
                            this.path2.moveTo(x3, this.centerY);
                            this.path2.lineTo(x3, y4);
                            this.path1.moveTo(x1, y1);
                            this.path1.lineTo(x3, this.centerY);
                        } else if (preClose < this.preClosePrice) {
                            this.path2.moveTo(x1, y2);
                            this.path2.lineTo(x3, y4);
                        } else {
                            this.path2.moveTo(x1, this.centerY);
                            this.path2.lineTo(x3, y4);
                        }
                    } else if (preClose > preClose) {
                        this.path1.moveTo(x1, y1);
                        this.path1.lineTo(x3, this.centerY);
                    } else if (preClose < this.preClosePrice) {
                        this.path2.moveTo(x1, y2);
                        this.path2.lineTo(x3, this.centerY);
                    } else {
                        this.path1.moveTo(x3, this.centerY);
                        this.path2.moveTo(x3, this.centerY);
                    }
                }

                preClose = close;
                prePoint = point;
                preTimes = (float)times;
                preAvgPoint = avgPoint;
            }

            this.upPath.lineTo(this.startX + this.oneTime * (float)times, this.centerY);
            this.downPath.lineTo(this.startX + this.oneTime * (float)times, this.centerY);
            this.upPath.close();
            this.downPath.close();
        } else {
            this.upPath.reset();
            this.downPath.reset();
            this.path1.reset();
            this.path2.reset();
            this.avgPath.reset();
        }
    }
}