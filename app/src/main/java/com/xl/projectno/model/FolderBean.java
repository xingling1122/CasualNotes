package com.xl.projectno.model;

import android.content.ContentValues;
import android.database.Cursor;

import com.xl.projectno.dao.table.FoldersTable;
import com.xl.projectno.dao.table.InventorysTable;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/3/17.
 */
public class FolderBean extends BaseBean implements IDatabaseObject{
    public String name;
    public boolean isCheck;

    public FolderBean(){}

    public FolderBean(String createTime, String name,boolean isCheck) {
        super(createTime);
        this.name = name;
        this.isCheck = isCheck;
    }

    @Override
    public void writeObject(ContentValues values, String table) {
//        values.put(ItemsTable.ID,id);
        values.put(FoldersTable.NAME,name);
        values.put(FoldersTable.CREATE_TIME,createTime);
        values.put(FoldersTable.ISCHECK,isCheck?1:0);
    }

    @Override
    public void readObject(Cursor cursor, String table) {
        id = cursor.getInt(FoldersTable.ID_INDEX);
        name = cursor.getString(FoldersTable.NAME_INDEX);
        createTime = cursor.getString(FoldersTable.CREATE_TIME_INDEX);
        isCheck = (cursor.getInt(FoldersTable.ISCHECK_INDEX)==0)?false:true;
    }

    @Override
    public void parseJson(JSONObject jsonObject) {

    }
}
