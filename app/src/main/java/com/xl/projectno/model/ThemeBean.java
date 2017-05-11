package com.xl.projectno.model;

import android.content.ContentValues;
import android.database.Cursor;

import com.xl.projectno.dao.table.ThemesTable;

/**
 * Created by Administrator on 2017/5/1.
 */

public class ThemeBean extends BaseBean implements IDatabaseObject{
    public String name;
    public int drawable;
    public int color;
    public int style;
    public boolean choose;
    public ThemeBean(String name,int drawable,int color,int style,boolean choose){
        this.name = name;
        this.drawable = drawable;
        this.color = color;
        this.style=style;
        this.choose = choose;
    }
    public ThemeBean(){}

    @Override
    public void writeObject(ContentValues values, String table) {
        values.put(ThemesTable.NAME,name);
        values.put(ThemesTable.DRAWABLE,drawable);
        values.put(ThemesTable.COLOR,color);
        values.put(ThemesTable.STYLE,style);
        values.put(ThemesTable.CHOOSE,choose);
    }

    @Override
    public void readObject(Cursor cursor, String table) {
        id = cursor.getInt(ThemesTable.ID_INDEX);
        name = cursor.getString(ThemesTable.NAME_INDEX);
        drawable = cursor.getInt(ThemesTable.DRAWABLE_INDEX);
        color = cursor.getInt(ThemesTable.COLOR_INDEX);
        style = cursor.getInt(ThemesTable.STYLE_INDEX);
        choose = cursor.getInt(ThemesTable.CHOOSE_INDEX)==0?false:true;
    }
}
