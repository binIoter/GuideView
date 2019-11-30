package com.binioter.guideview;

import android.content.Context;

/**
 * Created by binIoter
 */

public class DimenUtil {
    
    /** sp转换成px */
    public static int sp2px(Context context, float spValue) {
        float fontScale = context.getApplicationContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /** px转换成sp */
    public static int px2sp(Context context, float pxValue) {
        float fontScale = context.getApplicationContext().getResources().getDisplayMetrics().density;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /** dip转换成px */
    public static int dp2px(Context context, float dipValue) {
        float scale = context.getApplicationContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (dipValue * scale + 0.5f);
    }

    /** px转换成dip */
    public static int px2dp(Context context, float pxValue) {
        float scale = context.getApplicationContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / scale + 0.5f);
    }

}