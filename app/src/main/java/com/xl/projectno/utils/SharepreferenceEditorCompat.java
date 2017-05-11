package com.xl.projectno.utils;

import android.content.SharedPreferences;

/**
 * Created by huanglun on 16-10-14.
 */
public class SharepreferenceEditorCompat {
    /**
     * 不能够实例化
     */
    private SharepreferenceEditorCompat() {
    }

    /**
     * Interface for the full API.
     */
    interface EditorImpl {
        void save(SharedPreferences.Editor editor);
    }

    /**
     */
    static class BaseEditorImpl implements EditorImpl {
        @Override
        public void save(SharedPreferences.Editor editor) {
            editor.commit();
        }
    }

    /**
     * Interface implementation for devices with at least v9 APIs.
     */
    static class GingerbreadEditorImpl implements EditorImpl {
        @Override
        public void save(SharedPreferences.Editor editor) {
            editor.apply();
        }
    }

    /**
     * Select the correct implementation to use for the current platform.
     */
    static final EditorImpl IMPL;
    static {
        final int version = android.os.Build.VERSION.SDK_INT;
        if (version >= 9) {
            IMPL = new GingerbreadEditorImpl();
        } else {
            IMPL = new BaseEditorImpl();
        }
    }

    /**
     * 处理SharedPreferences.Editor保存的问题
     */
    public static void save(SharedPreferences.Editor editor) {
        IMPL.save(editor);
    }
}
