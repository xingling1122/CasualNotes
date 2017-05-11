package com.xl.projectno.dao.table;

import android.net.Uri;

import com.xl.projectno.dao.DataProvider;

/**
 * Created by xingling on 2017/3/17.
 * 文件夹表
 */
public class FoldersTable {
    public static final String TABLE_NAME = "folders";

    public static final String ID = "id";//id
    public static final String NAME = "name";//文件夹名
    public static final String CREATE_TIME = "createTime";//创建时间
    public static final String ISCHECK = "isCheck";//是否选择

    public static int index = 0;
    public static final int ID_INDEX = index++;
    public static final int NAME_INDEX=index++;
    public static final int CREATE_TIME_INDEX = index++;
    public static final int ISCHECK_INDEX=index++;

    public static final Uri CONTENT_URI = Uri.parse("content://" + DataProvider.AUTHORITY
            + "/" + TABLE_NAME);

    public static final String CREATE_SQL = "create table "
            + TABLE_NAME + " ("
            + ID
            + " Integer PRIMARY KEY autoincrement,"
            + NAME
            + " string not null, "
            + CREATE_TIME
            + " timestamp not null, "
            + ISCHECK
            + " smallint not null)";
}
