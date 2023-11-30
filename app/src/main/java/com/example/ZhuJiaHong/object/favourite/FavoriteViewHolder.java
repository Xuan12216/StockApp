package com.example.ZhuJiaHong.object.favourite;

import static android.widget.TextView.AUTO_SIZE_TEXT_TYPE_UNIFORM;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.core.widget.TextViewCompat;
import com.example.ZhuJiaHong.AppApplication;
import com.example.ZhuJiaHong.Util.ValueParser;
import com.example.ZhuJiaHong.activity.ActivityStockIndustry;
import com.example.ZhuJiaHong.databinding.ItemStockFavorBinding;
import com.example.ZhuJiaHong.object.kf.PriceStyler;
import com.mdbs.base.view.object.favorite.BaseRecycleLayout;
import com.mdbs.base.view.utils.BaseUtil;
import com.mdbs.starwave_meta.common.stock.ProductSymbol;
import com.mdbs.starwave_meta.params.RFMatchField;
import com.mdbs.starwave_meta.params.RFStock0Data;
import io.reactivex.disposables.CompositeDisposable;
import static com.example.ZhuJiaHong.AppApplication.mStockLoader;

// TODO 自選列表樣式
public class FavoriteViewHolder extends BaseRecycleLayout {

    private static final int NON_VALID_VALUE = 999;
    public CompositeDisposable disposes = new CompositeDisposable();

    // Fields
    private final Context mContext;
    public ItemStockFavorBinding binding;
    public FavoriteViewHolder(Context context) {
        super(context);

        mContext = context;
        itemView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        binding = ItemStockFavorBinding.inflate(LayoutInflater.from(context));
        View view = binding.getRoot();
        itemView.addView(view);
        TextViewCompat.setAutoSizeTextTypeUniformWithConfiguration(binding.percentTv, 10, 14, AUTO_SIZE_TEXT_TYPE_UNIFORM, TypedValue.COMPLEX_UNIT_SP);
        TextViewCompat.setAutoSizeTextTypeUniformWithConfiguration(binding.amountTv, 8, 12, AUTO_SIZE_TEXT_TYPE_UNIFORM, TypedValue.COMPLEX_UNIT_SP);
    }

    public void initValue(ProductSymbol productSymbol) {

        binding.stockNoTv.setText(productSymbol.no);
        binding.stockNameTv.setText(productSymbol.name);

        String industryName = (!com.mdbs.base.view.utils.AppUtil.isEmpty(productSymbol.industryName))
                ? productSymbol.industryName
                : "--";

        if (industryName.length() > 4) {

            industryName = industryName.substring(0, 4) + "...";
        }

        //產業
        binding.industryTypeTv.setText(industryName);
        binding.stockTypeTv.setVisibility(productSymbol.futuresNo != null ? View.VISIBLE : View.GONE);
        binding.stockTypeTv2.setVisibility(AppApplication.isOTC(productSymbol) ? View.VISIBLE : View.GONE);

        binding.industryTypeTv.setOnClickListener(view -> {

            if (!TextUtils.isEmpty(productSymbol.industryName)) {

                if (BaseUtil.isSubscribe(mContext)) {

                    Intent intent = new Intent(mContext, ActivityStockIndustry.class);
                    intent.putExtra("isMain", false);
                    intent.putExtra("stockNo", productSymbol.no);
                    intent.putExtra("industryName", mStockLoader.safeGet(productSymbol.no).industryName);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    mContext.startActivity(intent);

                } else {

                    BaseUtil.popUpPurchasingDialog(mContext);
                }
            }
        });
    }

    public void setValue(RFStock0Data data) {

        PriceStyler priceStyler = new PriceStyler();
        priceStyler.invoke(binding.priceContainer, binding.priceTv, data, RFMatchField.MatchField.PRICE, true);
        binding.priceTv.getText().toString().replace("+0.00 (+0.00%)", "0.00 (0.00%)");
        binding.percentTv.setTextColor(binding.priceTv.getCurrentTextColor());
        binding.percentTv.setText(data.清盤 ? null : data.get(RFStock0Data.Stock0Field.DF_INFO).replace("+0.00 (+0.00%)", "0.00 (0.00%)"));

        //量
        binding.amountTv.setText(String.valueOf(data.total));

        long total = data.total;
        String amount = String.valueOf(total);

        if (total > 10000) {

            amount = ValueParser.roundDouble(total / 1000d, 1) + "K";
        }

        binding.amountTv.setText(amount);
    }

    public boolean isTrendMode() { return false; }
}
