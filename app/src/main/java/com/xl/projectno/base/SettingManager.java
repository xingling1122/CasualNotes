package com.xl.projectno.base;

import android.content.Context;

import com.xl.projectno.utils.SharedPreferencesUtils;

/**
 * Created by Administrator on 2017/5/2.
 */

public class SettingManager {
    private Context mContext;
    private static SettingManager mInstance;
    private PreferenceManager mSpManager;
    private SharedPreferencesUtils mSpUtils;

    public static final String THEME = "theme";

    public static final String LANGUAGE = "language";

    private SettingManager() {
        mContext = MposApp.getApp().getApplicationContext();
        mSpManager = new PreferenceManager(mContext);
        mSpUtils = new SharedPreferencesUtils(MposApp.getApp().getApplicationContext(), "settings");
    }

    public synchronized static SettingManager getInstance() {
        if(mInstance == null) {
            mInstance = new SettingManager();
        }
        return mInstance;
    }

    public void setCurrentLanguage(int language){
        mSpManager.putInt(LANGUAGE,language);
    }

    public int getCurrentLanguage(){
        return mSpManager.getInt(LANGUAGE,-1);
    }

}
