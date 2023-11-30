package com.example.ZhuJiaHong.Util;

import android.text.TextUtils;

import java.math.BigDecimal;

public class ValueParser {

    public static int parseInt(String text) {

        int value = 0;

        if (!isEmpty(text)) {

            try {

                value = Integer.parseInt(text);

            } catch (Exception e) {
//                e.printStackTrace();
            }
        }

        return value;
    }

    public static long parseLong(String text) {

        long value = 0;

        if (!isEmpty(text)) {

            try {

                value = Long.parseLong(text);

            } catch (Exception e) {
//                e.printStackTrace();
            }
        }

        return value;
    }

    public static double parseDouble(String text) {

        double value = 0;

        if (!isEmpty(text)) {

            try {

                value = Double.parseDouble(text);

            } catch (Exception e) {
//                e.printStackTrace();
            }
        }

        return value;
    }

    public static float parseFloat(String text) {

        float value = 0;

        if (!isEmpty(text)) {

            try {

                value = Float.parseFloat(text);

            } catch (Exception e) {
//                e.printStackTrace();
            }
        }

        return value;
    }

    public static BigDecimal parseBigDecimal(String text) {

        BigDecimal value = null;

        if (!isEmpty(text)) {

            try {

                value = new BigDecimal(text);

                if (value.scale() > 2) {

                    value = value.setScale(2, BigDecimal.ROUND_HALF_UP);
                }

            } catch (Exception e) {
//                e.printStackTrace();
            }
        }

        return value;
    }

    public static BigDecimal roundBigDecimal(BigDecimal value) {

        if (value.scale() > 2) {

            value = value.setScale(2, BigDecimal.ROUND_HALF_UP);
        }

        return value;
    }

    public static BigDecimal roundFloat(Float val) {

        BigDecimal value = BigDecimal.valueOf(val);

        if (value.scale() > 2) {

            value = value.setScale(2, BigDecimal.ROUND_HALF_UP);
        }

        return value;
    }

    public static BigDecimal roundFloat(Float val, int scale) {

        BigDecimal value = BigDecimal.valueOf(val);

        if (value.scale() > scale) {

            value = value.setScale(scale, BigDecimal.ROUND_HALF_UP);
        }

        return value;
    }

    public static BigDecimal roundDouble(Double val) {

        BigDecimal value = BigDecimal.valueOf(val);

        if (value.scale() > 2) {

            value = value.setScale(2, BigDecimal.ROUND_HALF_UP);
        }

        return value;
    }

    public static BigDecimal roundDouble(Double val, int scale) {

        BigDecimal value = BigDecimal.valueOf(val);

        if (value.scale() > scale) {

            value = value.setScale(scale, BigDecimal.ROUND_HALF_UP);
        }

        return value;
    }

    public static BigDecimal roundDouble(String val) {

        BigDecimal value = null;

        if (!isEmpty(val)) {

            value = BigDecimal.valueOf(parseDouble(val));

            if (value.scale() > 2) {

                value = value.setScale(2, BigDecimal.ROUND_HALF_UP);
            }
        }

        return value;
    }

    public static BigDecimal roundDownDouble(Double val) {

        BigDecimal value = BigDecimal.valueOf(val);

        if (value.scale() > 2) {

            value = value.setScale(2, BigDecimal.ROUND_DOWN);
        }

        return value;
    }

    public static boolean isEmpty(String text) {

        return TextUtils.isEmpty(text) || "null".equals(text);
    }
}