package com.xl.projectno.dao.table;

import android.net.Uri;

import com.xl.projectno.dao.DataProvider;

/**
 * Created by xingling on 2017/3/17.
 * 基本数据表
 */
public class ItemsTable {
    public static final String TABLE_NAME = "items"; //基本数据


    public static final String ID = "id";//id
    public static final String USER_ID="userId";//用户id
    public static final String CREATE_TIME="createTime";//创建时间
    public static final String FINISH_TIME="finishTime";//完成时间
    public static final String TITLE = "title";//标题
    public static final String CONTENT="content";//内容
    public static final String INVENTORY="inventoryId";//清单id
    public static final String IS_FINISH="isFinish";//是否完成
    public static final String SORT_NO = "sortNo";//分类数字


    public static int index = 0;
    public static final int ID_INDEX = index++;
    public static final int USER_ID_INDEX = index++;
    public static final int CREATE_TIME_INDEX = index++;
    public static final int FINISH_TIME_INDEX = index++;
    public static final int TITLE_INDEX = index++;
    public static final int CONTENT_INDEX = index++;
    public static final int INVENTORY_INDEX = index++;
    public static final int IS_FINISH_INDEX = index++;
    public static final int SORT_NO_INDEX = index++;

    public static final Uri CONTENT_URI = Uri.parse("content://" + DataProvider.AUTHORITY
            + "/" + TABLE_NAME);

    public static final String CREATE_SQL = "create table "
            + TABLE_NAME + " ("
            + ID
            + " Integer PRIMARY KEY autoincrement,"
            + USER_ID
            + " bigint not null, "
            + CREATE_TIME
            + " timestamp not null, "
            + FINISH_TIME
            + " timestamp not null, "
            + TITLE
            + " string not null, "
            + CONTENT
            + " string not null, "
            + INVENTORY
            + " bigint not null, "
            + IS_FINISH
            + " smallint not null, "
            + SORT_NO
            + " bigint not null)";
}
