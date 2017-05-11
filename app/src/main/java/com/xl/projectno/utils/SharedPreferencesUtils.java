package com.xl.projectno.utils;

import android.content.Context;
import android.content.SharedPreferences;


/**
 * 
 * <br>类描述: SharedPreferences的工具类
 * <br>功能详细描述:
 */
public class SharedPreferencesUtils {
	private SharedPreferences mPreference;
	private SharedPreferences.Editor mEditor;
	
	public SharedPreferencesUtils(Context context, String fileName) {
		mPreference = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
		mEditor = mPreference.edit();
	}
	
	public String getString(String key, String value) {
		return mPreference.getString(key, value);
	}

	public void putLong(String key, long value) {
		mEditor.putLong(key, value);
	}

	public long getLong(String key, long value) {
		return mPreference.getLong(key, value);
	}

	public void putString(String key, String value) {
		mEditor.putString(key, value);
	}

	public void putBoolean(String key, boolean value) {
		mEditor.putBoolean(key, value);
	}

	public boolean getBoolean(String key) {
		return mPreference.getBoolean(key, false);
	}

	public void putInt(String key, int value) {
		mEditor.putInt(key, value);
	}

	public int getInt(String key, int value) {
		return mPreference.getInt(key, value);
	}

	public void commit() {
		mEditor.commit();
	}
	
}
