package com.xl.projectno.dao.table;

import android.net.Uri;

import com.xl.projectno.dao.DataProvider;

/**
 * Created by Administrator on 2017/5/2.
 */

public class ThemesTable {
    public static final String TABLE_NAME = "themes"; //基本数据


    public static final String ID = "id";//id
    public static final String NAME="name";//主题名
    public static final String DRAWABLE = "drawable";
    public static final String COLOR="color";//颜色
    public static final String STYLE = "style";//样式
    public static final String CHOOSE = "choose";//是否选择


    public static int index = 0;
    public static final int ID_INDEX = index++;
    public static final int NAME_INDEX = index++;
    public static final int DRAWABLE_INDEX = index++;
    public static final int COLOR_INDEX = index++;
    public static final int STYLE_INDEX = index++;
    public static final int CHOOSE_INDEX = index++;

    public static final Uri CONTENT_URI = Uri.parse("content://" + DataProvider.AUTHORITY
            + "/" + TABLE_NAME);

    public static final String CREATE_SQL = "create table "
            + TABLE_NAME + " ("
            + ID
            + " Integer PRIMARY KEY autoincrement,"
            + NAME
            + " string not null, "
            + DRAWABLE
            + " bigint not null, "
            + COLOR
            + " bigint not null, "
            + STYLE
            + " bigint not null, "
            + CHOOSE
            + " smallint not null)";
}
