package com.xl.projectno.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.xl.projectno.base.MposApp;
import com.xl.projectno.dao.table.InventorysTable;
import com.xl.projectno.dao.table.UsersTable;
import com.xl.projectno.model.BaseBean;
import com.xl.projectno.model.InventoryBean;
import com.xl.projectno.model.UserBean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/3/18.
 */
public class InventorysOperator extends BaseOperator {
    private static InventorysOperator sInstance;

    private InventorysOperator(Context context) {
        super(context);
    }

    public synchronized static InventorysOperator getInstance() {
        if (sInstance == null) {
            sInstance = new InventorysOperator(MposApp.getApp());
        }
        return sInstance;
    }

    public int addItem(InventoryBean bean) {
        int ret = -1;
        try {
            ContentValues values = new ContentValues();
            bean.writeObject(values, null);
            Uri uri = mProvider.insert(InventorysTable.CONTENT_URI, values);
            if (uri != null) {
                ret = 1;
            }
        } catch (Exception ex) {

        }
        return ret;
    }

    public int delItem(InventoryBean bean) {
        int ret = -1;
        try {
            ret = mProvider.delete(InventorysTable.CONTENT_URI, InventorysTable.CREATE_TIME+"=?",new String[]{bean.createTime});
        } catch (Exception ex) {

        }
        return ret;
    }

    public InventoryBean getBeanByCreateTime(String createTime) {
        InventoryBean bean=new InventoryBean();
        Cursor cursor = null;
        try {
            Uri uri = Uri.parse(InventorysTable.CONTENT_URI.toString());
            cursor = mProvider.query(uri, null, InventorysTable.CREATE_TIME+"=?",new String[]{createTime}, null);
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

    public InventoryBean getBeanByName(String name) {
        InventoryBean bean=new InventoryBean();
        Cursor cursor = null;
        try {
            Uri uri = Uri.parse(InventorysTable.CONTENT_URI.toString());
            cursor = mProvider.query(uri, null, InventorysTable.NAME+"=?",new String[]{name}, null);
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
            Uri uri = Uri.parse(InventorysTable.CONTENT_URI.toString());
            cursor = mProvider.query(uri, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    InventoryBean bean = new InventoryBean();
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

    public ArrayList<BaseBean> getItemsDrop() {
        ArrayList<BaseBean> list = new ArrayList<>();
        Cursor cursor = null;
        try {
            Uri uri = Uri.parse(InventorysTable.CONTENT_URI.toString());
            cursor = mProvider.query(uri, null, InventorysTable.IS_DROP+"=?", new String[]{String.valueOf(1)}, null);
            if (cursor != null && cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    InventoryBean bean = new InventoryBean();
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

    public ArrayList<BaseBean> getItemsByFolderId(int folderId) {
        ArrayList<BaseBean> list = new ArrayList<>();
        Cursor cursor = null;
        try {
            Uri uri = Uri.parse(InventorysTable.CONTENT_URI.toString());
            cursor = mProvider.query(uri, null, InventorysTable.FOLDER_ID+"=? AND "+InventorysTable.IS_DROP+"=?", new String[]{String.valueOf(folderId),String.valueOf(0)}, null);
            if (cursor != null && cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    InventoryBean bean = new InventoryBean();
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

    public int updateItem(int id, InventoryBean newBean) {
        int ret = -1;
        try {
            ContentValues values = new ContentValues();
            newBean.writeObject(values,null);
            ret = mProvider.update(InventorysTable.CONTENT_URI, values, InventorysTable.ID + "=" + id, null);
        } catch (Exception ex) {

        }
        return ret;
    }

    public boolean isExit(String name){
        Cursor cursor = null;
        try {
            Uri uri = Uri.parse(InventorysTable.CONTENT_URI.toString());
            cursor = mProvider.query(uri, null, InventorysTable.NAME+"=?", new String[]{name}, null);
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
