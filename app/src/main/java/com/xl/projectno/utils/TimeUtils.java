package com.xl.projectno.utils;

import android.os.SystemClock;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2017/3/18.
 */
public class TimeUtils {
    private long s;


    public TimeUtils(long s){
        this.s=s;
    }

    public static String getNowTime(){
        long s = System.currentTimeMillis();
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(s);
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String now = sd.format(c.getTime());
        return now;
    }

    public static String getDefaultTimeTick(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(2*60*60*1000- TimeZone.getDefault().getRawOffset());
        SimpleDateFormat sd = new SimpleDateFormat("HH:mm:ss");
        String now = sd.format(calendar.getTime());
        return now;
    }

    public String getTimeTick(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(s*1000- TimeZone.getDefault().getRawOffset());
        SimpleDateFormat sd = new SimpleDateFormat("HH:mm:ss");
        String now = sd.format(calendar.getTime());
        s-=1;
        return now;
    }

    public static long StrToDate(String str) {

        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        Date date1 = null;
        Date date2 = null;
        try {
            date1 = format.parse(str);
            date2 = format.parse("00:00:00");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date1.getTime()-date2.getTime();
    }
}
