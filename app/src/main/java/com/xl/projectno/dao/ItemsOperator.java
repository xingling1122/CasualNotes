package com.xl.projectno.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.xl.projectno.base.MposApp;
import com.xl.projectno.dao.table.ItemsTable;
import com.xl.projectno.model.BaseBean;
import com.xl.projectno.model.ItemBean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/3/17.
 */
public class ItemsOperator extends BaseOperator{
    private static ItemsOperator sInstance;

    private ItemsOperator(Context context) {
        super(context);
    }

    public synchronized static ItemsOperator getInstance() {
        if (sInstance == null) {
            sInstance = new ItemsOperator(MposApp.getApp());
        }
        return sInstance;
    }

    public int addItem(ItemBean bean) {
        int ret = -1;
        try {
            ContentValues values = new ContentValues();
            bean.writeObject(values, null);
            Uri uri = mProvider.insert(ItemsTable.CONTENT_URI, values);
            if (uri != null) {
                ret = 1;
            }
        } catch (Exception ex) {

        }
        return ret;
    }

    public int delItem(ItemBean bean) {
        int ret = -1;
        try {
            ret = mProvider.delete(ItemsTable.CONTENT_URI, ItemsTable.TITLE+"=?",new String[]{bean.title});
        } catch (Exception ex) {

        }
        return ret;
    }

    public ItemBean getBeanByCreateTime(String createTime) {
        ItemBean bean=new ItemBean();
        Cursor cursor = null;
        try {
            Uri uri = Uri.parse(ItemsTable.CONTENT_URI.toString());
            cursor = mProvider.query(uri, null, ItemsTable.CREATE_TIME+"=?",new String[]{createTime}, null);
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

    public ItemBean getBeanByName(String name) {
        ItemBean bean=new ItemBean();
        Cursor cursor = null;
        try {
            Uri uri = Uri.parse(ItemsTable.CONTENT_URI.toString());
            cursor = mProvider.query(uri, null, ItemsTable.TITLE+"=?",new String[]{name}, null);
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
            Uri uri = Uri.parse(ItemsTable.CONTENT_URI.toString());
            cursor = mProvider.query(uri, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    ItemBean bean = new ItemBean();
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

    public ArrayList<BaseBean> getOrderItems() {
        ArrayList<BaseBean> list = new ArrayList<>();
        Cursor cursor = null;
        try {
            Uri uri = Uri.parse(ItemsTable.CONTENT_URI.toString());
            cursor = mProvider.query(uri, null, null, null, "sortNo asc");
            if (cursor != null && cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    ItemBean bean = new ItemBean();
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

    public int getMaxSortN(){
        ItemBean bean=new ItemBean();
        Cursor cursor = null;
        try {
            Uri uri = Uri.parse(ItemsTable.CONTENT_URI.toString());
            cursor = mProvider.query(uri, null,null,null,"sortNo desc limit 1");
            if (cursor!=null&&cursor.moveToNext()){
                bean.readObject(cursor,null);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if(cursor != null) {
                cursor.close();
            }
        }
        return bean.sortNo;
    }

    public ArrayList<ItemBean> getItemsByUserId(int userId) {
        ArrayList<ItemBean> list = new ArrayList<>();
        Cursor cursor = null;
        try {
            Uri uri = Uri.parse(ItemsTable.CONTENT_URI.toString());
            cursor = mProvider.query(uri, null, ItemsTable.USER_ID+"=?", new String[]{String.valueOf(userId)}, null);
            if (cursor != null && cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    ItemBean bean = new ItemBean();
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

    public boolean isExit(String name){
        Cursor cursor = null;
        try {
            Uri uri = Uri.parse(ItemsTable.CONTENT_URI.toString());
            cursor = mProvider.query(uri, null, ItemsTable.TITLE+"=?", new String[]{name}, null);
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

    public ArrayList<ItemBean> getItemsByInventoryId(int inventoryId) {
        ArrayList<ItemBean> list = new ArrayList<>();
        Cursor cursor = null;
        try {
            Uri uri = Uri.parse(ItemsTable.CONTENT_URI.toString());
            cursor = mProvider.query(uri, null, ItemsTable.INVENTORY+"=?", new String[]{String.valueOf(inventoryId)}, null);
            if (cursor != null && cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    ItemBean bean = new ItemBean();
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

    public ArrayList<ItemBean> getItemsByUserIdAInventoryId(int userId,int inventoryId) {
        ArrayList<ItemBean> list = new ArrayList<>();
        Cursor cursor = null;
        try {
            Uri uri = Uri.parse(ItemsTable.CONTENT_URI.toString());
            cursor = mProvider.query(uri, null, ItemsTable.INVENTORY+"=? AND "+ItemsTable.USER_ID+"=?", new String[]{String.valueOf(inventoryId),String.valueOf(userId)}, null);
            if (cursor != null && cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    ItemBean bean = new ItemBean();
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

    public int updateItem(String title, ItemBean newBean) {
        int ret = -1;
        try {
            ContentValues values = new ContentValues();
            newBean.writeObject(values,null);
            ret = mProvider.update(ItemsTable.CONTENT_URI, values, ItemsTable.TITLE+"=?", new String[]{title});
        } catch (Exception ex) {

        }
        return ret;
    }
}
