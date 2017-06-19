package com.example.yzhou.customview;

import android.content.Context;

import java.math.BigDecimal;

/**
 * @author：yangZhou on 2017/6/19 21
 */
public class Utils {



    /**
     * 将double转换成string，保留index位小数
     */
    public static String doubleToString(int index, double num) {
        BigDecimal bigDecimal = new BigDecimal("" + num);
        bigDecimal = bigDecimal.setScale(index, BigDecimal.ROUND_HALF_UP);
        return bigDecimal.toString();
    }



    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }



}
