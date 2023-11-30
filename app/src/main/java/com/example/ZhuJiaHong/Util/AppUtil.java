package com.example.ZhuJiaHong.Util;

import com.mdbs.starwave_meta.params.RFOwlData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AppUtil {
    public static String getDiffString(float diffPrice, float diffRate) {

        if (diffPrice > 0) {
            return String.format("+%.2f (+%.2f%%)▲", diffPrice, diffRate);
        }
        else if (diffPrice == 0) {
            return "0.00 (0.00%) - ";
        }
        else if (diffPrice < 0) {
            return String.format(" %.2f ( %.2f%%)▼", diffPrice, diffRate);
        }

        return "--";
    }

    public static RFOwlData export(RFOwlData mReferenceData, String ... arrs) {

        /* Export Object */
        RFOwlData rfOwlData = new RFOwlData();
        rfOwlData.setTitle(new ArrayList<>(Arrays.asList(arrs)));
        rfOwlData.setData(new ArrayList<>());

        /* Insert Data Source */
        List<ArrayList<String>> source = mReferenceData.getData();
        if (source != null) {
            for (ArrayList<String> item : source) {
                ArrayList<String> $item = new ArrayList<>();
                rfOwlData.addItem($item);
                for (String title : rfOwlData.getTitle()) $item.add(mReferenceData.getValue(item, title));
            }
        }

        return rfOwlData;
    }
}
