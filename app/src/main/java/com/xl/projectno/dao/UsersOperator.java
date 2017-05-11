package com.xl.projectno.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.xl.projectno.base.MposApp;
import com.xl.projectno.dao.table.ItemsTable;
import com.xl.projectno.dao.table.UsersTable;
import com.xl.projectno.model.BaseBean;
import com.xl.projectno.model.ItemBean;
import com.xl.projectno.model.UserBean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/3/17.
 */
public class UsersOperator extends BaseOperator {
    private static UsersOperator sInstance;

    private UsersOperator(Context context) {
        super(context);
    }

    public synchronized static UsersOperator getInstance() {
        if (sInstance == null) {
            sInstance = new UsersOperator(MposApp.getApp());
        }
        return sInstance;
    }

    public int addItem(UserBean bean) {
        int ret = -1;
        try {
            ContentValues values = new ContentValues();
            bean.writeObject(values, null);
            Uri uri = mProvider.insert(UsersTable.CONTENT_URI, values);
            if (uri != null) {
                ret = 1;
            }
        } catch (Exception ex) {

        }
        return ret;
    }

    public int delItem(UserBean bean) {
        int ret = -1;
        try {
            ret = mProvider.delete(UsersTable.CONTENT_URI, UsersTable.NAME+"=?",new String[]{bean.name});
        } catch (Exception ex) {

        }
        return ret;
    }

    public int delAllUsers() {
        int ret = -1;
        try {
            ret = mProvider.delete(UsersTable.CONTENT_URI, null,null);
        } catch (Exception ex) {

        }
        return ret;
    }

    public UserBean getBeanByName(String name) {
        UserBean bean=new UserBean();
        Cursor cursor = null;
        try {
            Uri uri = Uri.parse(UsersTable.CONTENT_URI.toString());
            cursor = mProvider.query(uri, null, UsersTable.NAME+"=?",new String[]{name}, null);
            if (cursor != null && cursor.moveToFirst()) {
                bean.readObject(cursor,null);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if(cursor != null) {
                cursor.close();
            }
        }
        return bean;
    }

    public UserBean getUser() {
        UserBean bean = null;
        Cursor cursor = null;
        try {
            Uri uri = Uri.parse(UsersTable.CONTENT_URI.toString());
            cursor = mProvider.query(uri, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                    bean = new UserBean();
                    bean.readObject(cursor, null);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if(cursor != null) {
                cursor.close();
            }
        }
        return bean;
    }

    public int updateItem(int id, UserBean newBean) {
        int ret = -1;
        try {
            ContentValues values = new ContentValues();
            newBean.writeObject(values,null);
            ret = mProvider.update(UsersTable.CONTENT_URI, values, UsersTable.ID + "=" + id, null);
        } catch (Exception ex) {

        }
        return ret;
    }
}
