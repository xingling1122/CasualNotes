package com.xl.projectno.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.SystemClock;

import com.xl.projectno.dao.table.ItemsTable;
import com.xl.projectno.utils.TimeUtils;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Administrator on 2017/3/17.
 */
public class ItemBean extends BaseBean implements IDatabaseObject{
    public int userId;
    public String finishTime;
    public String title;
    public String content;
    public int inventoryId;
    public boolean isFinish;
    public int sortNo;

    public String time;

    public ItemBean(){}

    public ItemBean(int userId,String createTime, String finishTime, String title, String content, int inventoryId, boolean isFinish, int sortNo, String time) {
        super(createTime);
        this.userId = userId;
        this.finishTime = finishTime;
        this.title = title;
        this.content = content;
        this.inventoryId = inventoryId;
        this.isFinish = isFinish;
        this.sortNo = sortNo;
        this.time = getTime(time);
    }

    @Override
    public void writeObject(ContentValues values, String table) {
//        values.put(ItemsTable.ID,id);
        values.put(ItemsTable.USER_ID,userId);
        values.put(ItemsTable.CREATE_TIME,createTime);
        values.put(ItemsTable.FINISH_TIME,finishTime);
        values.put(ItemsTable.TITLE,title);
        values.put(ItemsTable.CONTENT,content);
        values.put(ItemsTable.INVENTORY,inventoryId);
        values.put(ItemsTable.IS_FINISH,isFinish==false?0:1);
        values.put(ItemsTable.SORT_NO,sortNo);
    }

    @Override
    public void readObject(Cursor cursor, String table) {
        id = cursor.getInt(ItemsTable.ID_INDEX);
        userId = cursor.getInt(ItemsTable.USER_ID_INDEX);
        createTime = cursor.getString(ItemsTable.CREATE_TIME_INDEX);
        finishTime = cursor.getString(ItemsTable.FINISH_TIME_INDEX);
        title = cursor.getString(ItemsTable.TITLE_INDEX);
        content = cursor.getString(ItemsTable.CONTENT_INDEX);
        inventoryId = cursor.getInt(ItemsTable.INVENTORY_INDEX);
        isFinish = cursor.getInt(ItemsTable.IS_FINISH_INDEX)==0?false:true;
        sortNo = cursor.getInt(ItemsTable.SORT_NO_INDEX);

        time = getTime(finishTime);
    }

    @Override
    public void parseJson(JSONObject jsonObject) {

    }

    public String getTime(String finishTime) {
        String date=null;
        String now = TimeUtils.getNowTime();
        String nowdate = splitTime(now);
        String finishdate = splitTime(finishTime);

        int mndate = change2Int(nowdate);
        int mfdate = change2Int(finishdate);

        if(isFinish){
            date="已完成";
        }else{
            if ((mfdate-mndate)==0){
                date="今天";
            }else if ((mfdate-mndate)==1){
                date="昨天";
                if (!isFinish){
                    date="已过期";
                }
            }else if ((mfdate-mndate)==2){
                date="前天";
                if (!isFinish){
                    date="已过期";
                }
            }else if ((mndate-mfdate)==1){
                date="明天";
            }else if ((mndate-mfdate)==2){
                date="后天";
            }else{
                date=finishdate;
            }
        }
        return date;
    }

    private String splitTime(String time) {
        String[] array=time.split("-");
        String[] array1 = array[2].split(" ");
        String date = array1[0];
        return date;
    }

    private int change2Int(String date){
        int number=0;
        try{
            number = Integer.valueOf(date);
        }catch (Exception e){

        }
        return number;
    }
}
