package com.xl.projectno.base;

import android.content.Context;
import android.content.SharedPreferences;

import com.xl.projectno.utils.SharepreferenceEditorCompat;

import java.util.Set;

/**
 * Created by huanglun on 16-10-14.
 */
public class PreferenceManager {

    public static final String PREFERENCE_NAME = "mpossp";

    private Context mContext;
    private SharedPreferences mSp;
    private SharedPreferences.Editor mEditor;
    public PreferenceManager(Context context) {
        mContext = context;
        mSp= context.getSharedPreferences(PREFERENCE_NAME,
                Context.MODE_PRIVATE);
        mEditor = mSp.edit();
    }

    public void putString(String key, String value) {
        mEditor.putString(key, value);
        SharepreferenceEditorCompat.save(mEditor);
    }

    public void putStringSet(String key, Set<String> values) {
        mEditor.putStringSet(key, values);
        SharepreferenceEditorCompat.save(mEditor);
    }

    public void putInt(String key, int value) {
        mEditor.putInt(key, value);
        SharepreferenceEditorCompat.save(mEditor);
    }

    public void putLong(String key, long value) {
        mEditor.putLong(key, value);
        SharepreferenceEditorCompat.save(mEditor);
    }

    public void putFloat(String key, float value) {
        mEditor.putFloat(key, value);
        SharepreferenceEditorCompat.save(mEditor);
    }

    public void putBoolean(String key, boolean value) {
        mEditor.putBoolean(key, value);
        SharepreferenceEditorCompat.save(mEditor);
    }

    public void remove(String key) {
        mEditor.remove(key);
        SharepreferenceEditorCompat.save(mEditor);
    }

    public String getString(String key, String value) {
        return mSp.getString(key, value);
    }

    public Set<String> getStringSet(String key, Set<String> values) {
        return mSp.getStringSet(key, values);
    }

    public int getInt(String key, int value) {
        return mSp.getInt(key, value);
    }

    public long getLong(String key, long value) {
        return mSp.getLong(key, value);
    }

    public float getFloat(String key, float value) {
        return mSp.getFloat(key, value);
    }

    public boolean getBoolean(String key, boolean value) {
        return mSp.getBoolean(key, value);
    }
}
