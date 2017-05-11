package com.xl.projectno.dao.table;

import android.net.Uri;

import com.xl.projectno.dao.DataProvider;

/**
 * Created by xingling on 2017/3/17.
 * 清单表
 */
public class InventorysTable {
    public static final String TABLE_NAME = "inventorys"; //基本数据


    public static final String ID = "id";//id
    public static final String NAME = "name";//清单名
    public static final String USER_ID = "userId";//用户id
    public static final String COLOR = "color";//颜色
    public static final String FOLDER_ID = "folderId";//文件夹
    public static final String IS_USING = "isUsing";//是否启用
    public static final String CREATE_TIME = "createTime";//创建时间
    public static final String IS_DROP = "isDrop";//是否进入回收站
    public static final String DEL_TIME = "delTime";//删除时间

    public static int index = 0;
    public static final int ID_INDEX = index++;
    public static final int NAME_INDEX = index++;
    public static final int USER_ID_INDEX = index++;
    public static final int COLOR_INDEX = index++;
    public static final int FOLDER_ID_INDEX = index++;
    public static final int IS_USING_INDEX = index++;
    public static final int CREATE_TIME_INDEX = index++;
    public static final int IS_DROP_INDEX = index++;
    public static final int DEL_TIME_INDEX = index++;

    public static final Uri CONTENT_URI = Uri.parse("content://" + DataProvider.AUTHORITY
            + "/" + TABLE_NAME);

    public static final String CREATE_SQL = "create table "
            + TABLE_NAME + " ("
            + ID
            + " Integer PRIMARY KEY autoincrement,"
            + NAME
            + " string not null, "
            + USER_ID
            + " bigint not null, "
            + COLOR
            + " smallint not null, "
            + FOLDER_ID
            + " bigint not null, "
            + IS_USING
            + " smallint not null, "
            + CREATE_TIME
            + " timestamp not null, "
            + IS_DROP
            + " smallint not null, "
            + DEL_TIME
            + " timestamp not null)";
}
