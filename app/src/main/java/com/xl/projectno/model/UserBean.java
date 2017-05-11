package com.xl.projectno.model;

import android.content.ContentValues;
import android.database.Cursor;

import com.xl.projectno.dao.table.ItemsTable;
import com.xl.projectno.dao.table.UsersTable;

import org.json.JSONObject;

/**
 * Created by Administrator on 2017/3/17.
 */
public class UserBean extends BaseBean implements IDatabaseObject{
    public String name;
    public String password;
    public String nickname;
    public String phone;
    public String token;
    public String imail;
    public String imageUrl;
    public int rank;
    public String loginTime;

    public String message;

    @Override
    public void writeObject(ContentValues values, String table) {
//        values.put(ItemsTable.ID,id);
        values.put(UsersTable.NAME,name);
        values.put(UsersTable.PASSWORD,password);
        values.put(UsersTable.NICKNAME,nickname);
        values.put(UsersTable.PHONE,phone);
        values.put(UsersTable.IMAIL,imail);
        values.put(UsersTable.IMAGEURl,imageUrl);
        values.put(UsersTable.CREATE_TIME,createTime);
        values.put(UsersTable.RANK,rank);
        values.put(UsersTable.TOKEN,token);
    }

    @Override
    public void readObject(Cursor cursor, String table) {
        id = cursor.getInt(UsersTable.ID_INDEX);
        name = cursor.getString(UsersTable.NAME_INDEX);
        password = cursor.getString(UsersTable.PASSWORD_INDEX);
        nickname = cursor.getString(UsersTable.NICKNAME_INDEX);
        phone = cursor.getString(UsersTable.PHONE_INDEX);
        imail = cursor.getString(UsersTable.IMAIL_INDEX);
        imageUrl = cursor.getString(UsersTable.IMAGEURl_INDEX);
        createTime = cursor.getString(UsersTable.CREATE_TIME_INDEX);
        rank = cursor.getInt(UsersTable.RANK_INDEX);
        token = cursor.getString(UsersTable.TOKEN_INDEX);
    }

    @Override
    public void parseJson(JSONObject jsonObject) {

    }
}
