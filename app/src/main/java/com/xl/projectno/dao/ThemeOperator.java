package com.xl.projectno.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.xl.projectno.R;
import com.xl.projectno.base.MposApp;
import com.xl.projectno.dao.table.ThemesTable;
import com.xl.projectno.dao.table.UsersTable;
import com.xl.projectno.model.BaseBean;
import com.xl.projectno.model.ThemeBean;
import com.xl.projectno.model.UserBean;
import com.xl.projectno.utils.LanguageUtils;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/5/1.
 */

public class ThemeOperator extends BaseOperator {
    private static ThemeOperator sInstance;
    Context context;

    private ThemeOperator(Context context) {
        super(context);
        this.context=context;
    }

    public synchronized static ThemeOperator getInstance() {
        if (sInstance == null) {
            sInstance = new ThemeOperator(MposApp.getApp());
        }
        return sInstance;
    }

    public int addItem(ThemeBean bean) {
        int ret = -1;
        try {
            ContentValues values = new ContentValues();
            bean.writeObject(values, null);
            Uri uri = mProvider.insert(ThemesTable.CONTENT_URI, values);
            if (uri != null) {
                ret = 1;
            }
        } catch (Exception ex) {

        }
        return ret;
    }

    public int delItem(ThemeBean bean) {
        int ret = -1;
        try {
            ret = mProvider.delete(ThemesTable.CONTENT_URI, ThemesTable.NAME+"=?",new String[]{bean.name});
        } catch (Exception ex) {

        }
        return ret;
    }

    public ArrayList<BaseBean> getThemes(){
        LanguageUtils.ChangeLanguage(context,LanguageUtils.current_language);
        ArrayList<BaseBean> themes = new ArrayList<>();
        themes.add(new ThemeBean(context.getString(R.string.blue),R.drawable.bg_item_theme_blue,R.color.blue,R.style.ThemeBlue,true));
        themes.add(new ThemeBean(context.getString(R.string.night),R.drawable.bg_item_theme_night,R.color.black,R.style.ThemeBlack,false));
        themes.add(new ThemeBean(context.getString(R.string.pink),R.drawable.bg_item_theme_pink,R.color.pink,R.style.ThemePink,false));
        themes.add(new ThemeBean(context.getString(R.string.black),R.drawable.bg_item_theme_night,R.color.black,R.style.ThemeBlack,false));
        themes.add(new ThemeBean(context.getString(R.string.green),R.drawable.bg_item_theme_green,R.color.green,R.style.ThemeGreen,false));
        themes.add(new ThemeBean(context.getString(R.string.grey),R.drawable.bg_item_theme_grey,R.color.grey,R.style.ThemeGrey,false));
        themes.add(new ThemeBean(context.getString(R.string.yellow),R.drawable.bg_item_theme_yellow,R.color.yellow,R.style.ThemeYellow,false));
        return themes;
    }

    public ThemeBean getTheme(String name){
        ThemeBean bean=new ThemeBean();
        Cursor cursor = null;
        try {
            Uri uri = Uri.parse(ThemesTable.CONTENT_URI.toString());
            cursor = mProvider.query(uri, null, ThemesTable.NAME+"=?",new String[]{name}, null);
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

    public ThemeBean getCurrentTheme(){
        ThemeBean bean=new ThemeBean();
        Cursor cursor = null;
        try {
            Uri uri = Uri.parse(ThemesTable.CONTENT_URI.toString());
            cursor = mProvider.query(uri, null, ThemesTable.CHOOSE+"=?",new String[]{String.valueOf(1)}, null);
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

    public ArrayList<BaseBean> getItems(){
        ArrayList<String> colorString = new ArrayList<>();
        LanguageUtils.ChangeLanguage(context,LanguageUtils.current_language);
        colorString.add(context.getString(R.string.blue));
        colorString.add(context.getString(R.string.night));
        colorString.add(context.getString(R.string.pink));
        colorString.add(context.getString(R.string.black));
        colorString.add(context.getString(R.string.green));
        colorString.add(context.getString(R.string.grey));
        colorString.add(context.getString(R.string.yellow));
        ArrayList<BaseBean> list = new ArrayList<>();
        Cursor cursor = null;
        try {
            Uri uri = Uri.parse(ThemesTable.CONTENT_URI.toString());
            cursor = mProvider.query(uri, null, null,null, null);
            if (cursor != null && cursor.moveToFirst()) {
                while(!cursor.isAfterLast()){
                    ThemeBean bean=new ThemeBean();
                    bean.readObject(cursor,null);
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
        for (int i=0;i<list.size();i++){
            ThemeBean theme = (ThemeBean)list.get(i);
            theme.name = colorString.get(i);
        }
        return list;
    }

    public int updateItem(int id, ThemeBean newBean) {
        int ret = -1;
        try {
            ContentValues values = new ContentValues();
            newBean.writeObject(values,null);
            ret = mProvider.update(ThemesTable.CONTENT_URI, values, ThemesTable.ID + "=" + id, null);
        } catch (Exception ex) {

        }
        return ret;
    }
}
