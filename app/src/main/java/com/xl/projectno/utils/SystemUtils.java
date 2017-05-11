package com.xl.projectno.utils;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;

/**
 * Created by Administrator on 2017/3/28.
 */
public class SystemUtils {

    public static final int getPhoneWidth(Context context){
        DisplayMetrics dm = getDisplayMetrics((Activity) context);
        int screenWidth = dm.widthPixels;
        return screenWidth;
    }

    @NonNull
    private static DisplayMetrics getDisplayMetrics(Activity context) {
        DisplayMetrics dm = new DisplayMetrics();
//获取屏幕信息
        context.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm;
    }

    public static final int getPhoneHeight(Context context){
        DisplayMetrics dm = getDisplayMetrics((Activity) context);
        int screenHeigh = dm.heightPixels;
        return screenHeigh;
    }

}
