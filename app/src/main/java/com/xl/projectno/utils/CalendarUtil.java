package com.xl.projectno.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by huanglun on 16-10-15.
 */
public class CalendarUtil {
    public static String sLocalTZ = "GMT+08:00";

    public static String getTime(SimpleDateFormat format) {
        return format.format(Calendar.getInstance().getTime());
    }

    public static String getTime(Calendar calendar) {
        int date = calendar.get(Calendar.DATE);
        int month = calendar.get(Calendar.MONTH);
        return calendar.get(Calendar.YEAR) + "-" + month + "-" + date;
    }

    public static Calendar getTime() {
        return Calendar.getInstance();
    }

    public static String getDataTime() {
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = sDateFormat.format(new java.util.Date());
        return date;
    }

    public static String getNoSplitDataTime() {
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String date = sDateFormat.format(new java.util.Date());
        return date;
    }
}
