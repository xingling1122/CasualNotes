package com.xl.projectno.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.xl.projectno.base.MposApp;
import com.xl.projectno.dao.table.FoldersTable;
import com.xl.projectno.dao.table.InventorysTable;
import com.xl.projectno.model.BaseBean;
import com.xl.projectno.model.FolderBean;
import com.xl.projectno.model.InventoryBean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/3/18.
 */
public class FoldersOperator extends BaseOperator {
    private static FoldersOperator sInstance;

    private FoldersOperator(Context context) {
        super(context);
    }

    public synchronized static FoldersOperator getInstance() {
        if (sInstance == null) {
            sInstance = new FoldersOperator(MposApp.getApp());
        }
        return sInstance;
    }

    public int addItem(FolderBean bean) {
        int ret = -1;
        try {
            ContentValues values = new ContentValues();
            bean.writeObject(values, null);
            Uri uri = mProvider.insert(FoldersTable.CONTENT_URI, values);
            if (uri != null) {
                ret = 1;
            }
        } catch (Exception ex) {

        }
        return ret;
    }

    public int delItem(FolderBean bean) {
        int ret = -1;
        try {
            ret = mProvider.delete(FoldersTable.CONTENT_URI, FoldersTable.CREATE_TIME+"=?",new String[]{bean.createTime});
        } catch (Exception ex) {

        }
        return ret;
    }

    public FolderBean getBeanByCreateTime(String createTime) {
        FolderBean bean=new FolderBean();
        Cursor cursor = null;
        try {
            Uri uri = Uri.parse(FoldersTable.CONTENT_URI.toString());
            cursor = mProvider.query(uri, null, FoldersTable.CREATE_TIME+"=?",new String[]{createTime}, null);
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

    public FolderBean getBeanByName(String name) {
        FolderBean bean=new FolderBean();
        Cursor cursor = null;
        try {
            Uri uri = Uri.parse(FoldersTable.CONTENT_URI.toString());
            cursor = mProvider.query(uri, null, FoldersTable.NAME+"=?",new String[]{name}, null);
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

    public FolderBean getBeanById(int id) {
        FolderBean bean=new FolderBean();
        Cursor cursor = null;
        try {
            Uri uri = Uri.parse(FoldersTable.CONTENT_URI.toString());
            cursor = mProvider.query(uri, null, FoldersTable.ID+"=?",new String[]{String.valueOf(id)}, null);
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

    public ArrayList<BaseBean> getItems() {
        ArrayList<BaseBean> list = new ArrayList<>();
        Cursor cursor = null;
        try {
            Uri uri = Uri.parse(FoldersTable.CONTENT_URI.toString());
            cursor = mProvider.query(uri, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    FolderBean bean = new FolderBean();
                    bean.readObject(cursor, null);
                    list.add(bean);
                    cursor.moveToNext();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if(cursor != null) {
                cursor.close();
            }
        }
        return list;
    }

    public int updateItem(int id, FolderBean newBean) {
        int ret = -1;
        try {
            ContentValues values = new ContentValues();
            newBean.writeObject(values,null);
            ret = mProvider.update(FoldersTable.CONTENT_URI, values, FoldersTable.ID + "=" + id, null);
        } catch (Exception ex) {

        }
        return ret;
    }

    public boolean isExit(String name){
        Cursor cursor = null;
        try {
            Uri uri = Uri.parse(FoldersTable.CONTENT_URI.toString());
            cursor = mProvider.query(uri, null, FoldersTable.NAME+"=?", new String[]{name}, null);
            if (cursor != null && cursor.moveToFirst()) {
                return true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if(cursor != null) {
                cursor.close();
            }
        }
        return false;
    }
}
