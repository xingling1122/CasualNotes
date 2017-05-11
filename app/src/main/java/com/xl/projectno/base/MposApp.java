package com.xl.projectno.base;

import android.app.Application;

/**
 * Created by huanglun on 16-8-18.
 */
public class MposApp extends Application {
    private static MposApp sApp;

    @Override
    public void onCreate() {
        super.onCreate();
        sApp = this;
    }

    public static MposApp getApp() {
        return sApp;
    }
}
