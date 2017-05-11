package com.xl.projectno.dao.table;

import android.net.Uri;

import com.xl.projectno.dao.DataProvider;

/**
 * Created by xingling on 2017/3/17.
 * 用户表
 */
public class UsersTable {
    public static final String TABLE_NAME = "users"; //基本数据


    public static final String ID = "id";//id
    public static final String NAME="username";//用户名
    public static final String PASSWORD="password";//密码
    public static final String NICKNAME = "nickname";//昵称
    public static final String PHONE = "phone";//电话
    public static final String IMAIL = "imail";//邮箱
    public static final String IMAGEURl = "imageurl";//用户头像地址
    public static final String CREATE_TIME="createTime";//创建时间
    public static final String RANK = "rank";//等级
    public static final String TOKEN = "token";//用户保持登录token


    public static int index = 0;
    public static final int ID_INDEX = index++;
    public static final int NAME_INDEX = index++;
    public static final int PASSWORD_INDEX = index++;
    public static final int NICKNAME_INDEX = index++;
    public static final int PHONE_INDEX = index++;
    public static final int IMAIL_INDEX = index++;
    public static final int IMAGEURl_INDEX = index++;
    public static final int CREATE_TIME_INDEX = index++;
    public static final int RANK_INDEX = index++;
    public static final int TOKEN_INDEX = index++;

    public static final Uri CONTENT_URI = Uri.parse("content://" + DataProvider.AUTHORITY
            + "/" + TABLE_NAME);

    public static final String CREATE_SQL = "create table "
            + TABLE_NAME + " ("
            + ID
            + " Integer PRIMARY KEY autoincrement,"
            + NAME
            + " string not null, "
            + PASSWORD
            + " string not null, "
            + NICKNAME
            + " string, "
            + PHONE
            + " varchar(30), "
            + IMAIL
            + " varchar(30), "
            + IMAGEURl
            + " varchar(30), "
            + CREATE_TIME
            + " timestamp, "
            + RANK
            + " bigint not null, "
            + TOKEN
            + " varchar(32))";
}
