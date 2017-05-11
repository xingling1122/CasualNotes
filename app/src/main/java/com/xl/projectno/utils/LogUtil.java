package com.xl.projectno.utils;

import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.xl.projectno.base.MposApp;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;

/**
 * Created by huanglun on 16-10-15.
 */
public class LogUtil {
    public static boolean sEnable = true;

    public static String TAG = "mpos";

    public static void log(String msg) {
        if (sEnable) {
            Log.i(TAG, msg);
        }
    }

    public static boolean isRelease() {
        return !sEnable;
    }

    public static File logFile(String path, String fileName) {
        if (!Environment.getExternalStorageState().equalsIgnoreCase("mounted")) {
            return null;
        }

        File logFile;

        if (TextUtils.isEmpty(path)) {
            path = MposApp.getApp().getPackageName();
        }

        if (TextUtils.isEmpty(fileName)) {
            String time = CalendarUtil.getTime(new SimpleDateFormat("yyyyMMdd"));
            fileName = MposApp.getApp().getPackageName() + "_" + time + ".txt";
        }

        logFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + path, fileName);
        log(new StringBuilder("log file path:").append(logFile).toString());

        if(!logFile.exists()) {
            File parentFile = logFile.getParentFile();
            if(!parentFile.exists()) {
                parentFile.mkdirs();
            }
        }

        try {
            if (!logFile.exists()){
                logFile.createNewFile();
            }
        } catch (IOException ex) {
            log(new StringBuilder("create log file failed!ps:%s").append(ex.toString()).toString());
        }

        return logFile;
    }

    public static void d(String tag, String msg) {
        if (!sEnable)
            return;
        Log.d(tag, msg);
    }
    /**
     * Send an INFO log message.
     * @param tag Used to identify the source of a log message. It usually identifies the class or activity where the log call occurs.
     * @param msg The message you would like logged.  
     */
    public static void i(String tag, String msg) {
        if (!sEnable)
            return;
        Log.i(tag, msg);
    }
    /**
     * Send an ERROR log message.
     * @param tag Used to identify the source of a log message. It usually identifies the class or activity where the log call occurs.
     * @param msg The message you would like logged.  
     */
    public static void e(String tag, String msg) {
        if (!sEnable)
            return;
        Log.e(tag, msg);
    }
    /**
     * Send a WARN log message.
     * @param tag Used to identify the source of a log message. It usually identifies the class or activity where the log call occurs.
     * @param msg The message you would like logged.  
     */
    public static void w(String tag, String msg) {
        if (!sEnable)
            return;
        Log.w(tag, msg);
    }

    public static void e(String tag, String msg, Throwable tr) {

        if (!sEnable) {
            return;
        } else {
            Log.e(tag, msg, tr);
        }
    }

}
