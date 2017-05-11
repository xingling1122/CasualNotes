package com.xl.projectno.model;

import android.content.ContentValues;
import android.database.Cursor;

import org.json.JSONObject;

/**
 * 
 * <br>类描述:数据库读写接口
 * <br>功能详细描述:
 * 
 * @author  liuheng
 * @date  [2012-9-4]
 */
public interface IDatabaseObject {

	/**
	 * 写入数据库
	 * table字段：有的对象可能写入不同的表，里面保存的数据不一样
	 * 
	 * @param values
	 */
	public void writeObject(ContentValues values, String table);

	/**
	 * 从Cursor中读取数据
	 * table字段：有的对象可能写入不同的表，里面保存的数据不一样
	 * 
	 * @param cursor
	 */
	public void readObject(Cursor cursor, String table);
}
