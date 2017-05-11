package com.xl.projectno.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.Drawable;

import com.xl.projectno.R;
import com.xl.projectno.dao.table.InventorysTable;
import com.xl.projectno.dao.table.UsersTable;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/3/17.
 */
public class InventoryBean extends BaseBean implements IDatabaseObject {
    public String name;
    public int userId;
    public int color;
    public int folderId;
    public boolean isUsing;
    public boolean isDrop;
    public String delTime;

    public int position;

    public int colorDrawableId;

    public InventoryBean(){super();}

    public InventoryBean(String createTime, String name, int userId, int color, int folderId, boolean isUsing,boolean isDrop,String delTime) {
        super(createTime);
        this.name = name;
        this.userId = userId;
        this.color = color;
        this.folderId = folderId;
        this.isUsing = isUsing;
        this.isDrop=isDrop;
        this.delTime = delTime;
    }

    public static final int[] colorArray = new int[]{R.drawable.color_no,R.drawable.bg_color_highblue,R.drawable.bg_color_lightblue,
            R.drawable.bg_color_highgreen,R.drawable.bg_color_lightgreen,R.drawable.bg_color_yellow,R.drawable.bg_color_lightyellow,
            R.drawable.bg_color_minyellow,R.drawable.bg_color_mediumvioletred,R.drawable.bg_color_purple,R.drawable.bg_color_coral,
            R.drawable.bg_color_lightgrey,R.drawable.bg_color_grey,R.drawable.bg_color_seagreen,R.drawable.bg_color_khaki};

    @Override
    public void writeObject(ContentValues values, String table) {
//        values.put(ItemsTable.ID,id);
//        color = getcolor(colorString);
        values.put(InventorysTable.NAME,name);
        values.put(InventorysTable.USER_ID,userId);
        values.put(InventorysTable.COLOR,color);
        values.put(InventorysTable.FOLDER_ID,folderId);
        values.put(InventorysTable.IS_USING,isUsing);
        values.put(InventorysTable.CREATE_TIME,createTime);
        values.put(InventorysTable.IS_DROP,isDrop);
        values.put(InventorysTable.DEL_TIME,delTime);
    }

    @Override
    public void readObject(Cursor cursor, String table) {
        id = cursor.getInt(InventorysTable.ID_INDEX);
        name = cursor.getString(InventorysTable.NAME_INDEX);
        userId = cursor.getInt(InventorysTable.USER_ID_INDEX);
        color = cursor.getInt(InventorysTable.COLOR_INDEX);
        folderId = cursor.getInt(InventorysTable.FOLDER_ID_INDEX);
        isUsing = cursor.getInt(InventorysTable.IS_USING_INDEX)==0?false:true;
        createTime = cursor.getString(InventorysTable.CREATE_TIME_INDEX);
        colorDrawableId = getColor(color);
        isDrop = cursor.getInt(InventorysTable.IS_DROP_INDEX)==0?false:true;
        delTime = cursor.getString(InventorysTable.DEL_TIME_INDEX);
    }

    @Override
    public void parseJson(JSONObject jsonObject) {

    }

    private int getColor(int color) {
        return colorArray[color];
    }
}
