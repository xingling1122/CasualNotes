/*
 * Copyright (C) 2008 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.xl.projectno.dao;

import android.appwidget.AppWidgetHost;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

/**
 * <br>类描述:数据层Provider
 * <br>功能详细描述:
 *
 * @author liuheng
 * @date [2012-9-4]
 */
public class DataProvider extends ContentProvider{
    public static final String AUTHORITY = "com.xl.projectno.data";

    public static final String PARAMETER_NOTIFY = "notify";

    /**
     * {@link Uri} triggered at any registered
     * {@link android.database.ContentObserver} when
     * {@link AppWidgetHost#deleteHost()} is called during database creation.
     * Use this to recall {@link AppWidgetHost#startListening()} if needed.
     */
    public static final Uri CONTENT_APPWIDGET_RESET_URI = Uri.parse("content://" + AUTHORITY
            + "/appWidgetReset");

    // 数据库重新创建的uri
    public static final Uri DATABASE_RESET_URI = Uri.parse("content://" + AUTHORITY
            + "/databaseReset");

    private static final String TAG = "DataProvider";

    private SQLiteOpenHelper mOpenHelper;

    @Override
    public boolean onCreate() {
        mOpenHelper = new Dbhelper(getContext());
        return true;
    }

    @Override
    public String getType(Uri uri) {
        SqlArguments args = new SqlArguments(uri, null, null);
        if (TextUtils.isEmpty(args.where)) {
            return "vnd.android.cursor.dir/" + args.table;
        } else {
            return "vnd.android.cursor.item/" + args.table;
        }
    }

    @Override
    public Cursor query(Uri uriArg, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {

		/*
         * limt
		 * groupby
		 * having
		 */
        String[] value = uriArg.toString().split("###");
        Uri uriValue = Uri.parse(value[0]);

        SqlArguments args = new SqlArguments(uriValue, selection, selectionArgs);
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(args.table);

        SQLiteDatabase db = mOpenHelper.getReadableDatabase();

        String limt = null;
        if (value.length > 1) {
            limt = value[1];
        }

        String groupby = null;
        if (value.length > 2) {
            groupby = value[2];
        }

        String having = null;
        // 判断数据库是否已经关闭
        Cursor result = null;
        if (db.isOpen()) {
            result = qb.query(db, projection, args.where, args.args, groupby, having, sortOrder,
                    limt);

            result.setNotificationUri(getContext().getContentResolver(), uriValue);
        }

        return result;
    }

    @Override
    public Uri insert(Uri uri, ContentValues initialValues) {
        SqlArguments args = new SqlArguments(uri);

        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        long rowId = -1;
        try {
            rowId = db.insert(args.table, null, initialValues);
        } catch (SQLException e) {
            Log.i(TAG, e.getMessage());
        }

        if (rowId <= 0) {
            return null;
        }

        uri = ContentUris.withAppendedId(uri, rowId);
        sendNotify(uri);

        return uri;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        SqlArguments args = new SqlArguments(uri);

        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            int numValues = values.length;
            for (int i = 0; i < numValues; i++) {
                if (db.insert(args.table, null, values[i]) < 0) {
                    return 0;
                }
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }

        sendNotify(uri);
        return values.length;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SqlArguments args = new SqlArguments(uri, selection, selectionArgs);

        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int count;
        try {
            count = db.delete(args.table, args.where, args.args);
            if (count > 0) {
                sendNotify(uri);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            count = 0;
        }

        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SqlArguments args = new SqlArguments(uri, selection, selectionArgs);

        // values为空，通过where传进来需要执行的sql语句
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        if (null == values) {
            int ret = 0;
            String[] arrayStrings = args.where.split("##");
            try {
                db.beginTransaction();
                int len = arrayStrings.length;
                for (int i = 0; i < len; ++i) {
                    db.execSQL(arrayStrings[i]);
                }

                db.setTransactionSuccessful();
                ret = 1; // 返回1表示通知成功
            } catch (SQLException e) {
                e.printStackTrace();
                ret = 0;
            } finally {
                db.endTransaction();
            }

            return ret;
        }

        int count = db.update(args.table, values, args.where, args.args);
        if (count > 0) {
            sendNotify(uri);
        }

        return count;
    }

    private void sendNotify(Uri uri) {
        String notify = uri.getQueryParameter(PARAMETER_NOTIFY);
        if (notify == null || "true".equals(notify)) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
    }

    /**
     * <br>类描述:SQL参数组装器
     * <br>功能详细描述:
     *
     * @author liuheng
     * @date [2012-9-4]
     */
    public static class SqlArguments {
        public final String table;
        public final String where;
        public final String[] args;

        public SqlArguments(Uri url, String where, String[] args) {
            if (url.getPathSegments().size() == 1) {
                this.table = url.getPathSegments().get(0);
                this.where = where;
                this.args = args;
            } else if (url.getPathSegments().size() != 2) {
                throw new IllegalArgumentException("Invalid URI: " + url);
            } else if (!TextUtils.isEmpty(where)) {
                throw new UnsupportedOperationException("WHERE clause not supported: " + url);
            } else {
                // 处理 Favorites.getContentUri() 返回的Uri这种情况
                this.table = url.getPathSegments().get(0);
                this.where = "_id=" + ContentUris.parseId(url);
                this.args = null;
            }
        }

        public SqlArguments(Uri url) {
            if (url.getPathSegments().size() == 1) {
                table = url.getPathSegments().get(0);
                where = null;
                args = null;
            } else {
                throw new IllegalArgumentException("Invalid URI: " + url);
            }
        }
    }
}
