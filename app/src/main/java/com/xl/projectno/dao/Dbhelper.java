package com.xl.projectno.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.xl.projectno.dao.table.FoldersTable;
import com.xl.projectno.dao.table.InventorysTable;
import com.xl.projectno.dao.table.ItemsTable;
import com.xl.projectno.dao.table.ThemesTable;
import com.xl.projectno.dao.table.UsersTable;
import com.xl.projectno.model.BaseBean;
import com.xl.projectno.model.FolderBean;
import com.xl.projectno.model.InventoryBean;
import com.xl.projectno.model.ThemeBean;
import com.xl.projectno.utils.TimeUtils;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/3/17.
 */
public class Dbhelper extends SQLiteOpenHelper {
    private static final String dbName="main.db";
    private static final int dbVersion=2;

    public Dbhelper(Context context) {
        super(context, dbName, null, dbVersion);

        SQLiteDatabase db = null;
        try {
            db = getWritableDatabase();
        } catch (Exception e) {
            e.printStackTrace();
            context.deleteDatabase(dbName);
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.beginTransaction();
        try {
            // 创建表
            db.execSQL(ItemsTable.CREATE_SQL);
            db.execSQL(UsersTable.CREATE_SQL);
            db.execSQL(InventorysTable.CREATE_SQL);
            db.execSQL(FoldersTable.CREATE_SQL);
            db.execSQL(ThemesTable.CREATE_SQL);
            initFolderTable(db);
            initInventoryTable(db);
            initThemes(db);
            db.setTransactionSuccessful();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    public void initThemes(SQLiteDatabase db) {
        DataProvider.SqlArguments args = new DataProvider.SqlArguments(ThemesTable.CONTENT_URI);
        ArrayList<BaseBean> list = ThemeOperator.getInstance().getThemes();
        ContentValues values = new ContentValues();
        for(BaseBean bean : list) {
            ThemeBean theme = (ThemeBean) bean;
            theme.writeObject(values, null);
            db.insert(args.table, null,values);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

    }

    private void initFolderTable(SQLiteDatabase db){
        DataProvider.SqlArguments args = new DataProvider.SqlArguments(FoldersTable.CONTENT_URI);
        ContentValues values = new ContentValues();
        FolderBean bean = new FolderBean(TimeUtils.getNowTime(),"默认文件夹",true);
        bean.writeObject(values,null);
        db.insert(args.table, null,values);
    }

    private void initInventoryTable(SQLiteDatabase db) {
        DataProvider.SqlArguments args = new DataProvider.SqlArguments(InventorysTable.CONTENT_URI);
        ContentValues values = new ContentValues();
        InventoryBean bean = new InventoryBean(TimeUtils.getNowTime(),"收集箱",0,0,1,true,false,TimeUtils.getDefaultTimeTick());
        bean.writeObject(values,null);
        db.insert(args.table, null,values);
    }
}
