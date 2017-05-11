package com.xl.projectno.dao;

import android.content.ContentResolver;
import android.content.Context;

/**
 * Created by xingling on 2017/3/17.
 * 基本操作类
 */
public abstract class BaseOperator {

    protected Context mContext;
    protected ContentResolver mProvider;

    public BaseOperator(Context context) {
        mContext = context;
        mProvider = mContext.getContentResolver();
    }
}
