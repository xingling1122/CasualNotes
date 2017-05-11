package com.xl.projectno.utils;

import android.content.Context;
import android.content.res.Configuration;

import com.xl.projectno.R;
import com.xl.projectno.activity.LoginChildViewHolder;
import com.xl.projectno.activity.RegisterChildViewHolder;
import com.xl.projectno.activity.ViewHolder;
import com.xl.projectno.base.SettingManager;

import java.util.Locale;

/**
 * Created by Administrator on 2017/4/23.
 */
public class LanguageUtils {
    public static final int CHINESE = 0;
    public static final int ENGLISH = 1;

    public static int current_language = CHINESE;

    public static int getCurrent_language(){
        return SettingManager.getInstance().getCurrentLanguage();
    }

    public static void setCurrent_language(int language){
        current_language = language;
        SettingManager.getInstance().setCurrentLanguage(language);
    }

    public static void ChangeLanguage(Context context,int language){
        current_language = language;
        SettingManager.getInstance().setCurrentLanguage(language);
        Configuration config = context.getResources().getConfiguration();
        switch (language){
            case CHINESE:
                config.locale = new Locale("ch","CN");
                break;
            case ENGLISH:
                config.locale = new Locale("en","US");
                break;
        }
        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
    }
}
