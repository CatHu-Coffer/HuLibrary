package com.example.catl.hulibarry.sqlite.common;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by tanqimin on 2016/11/10.
 */
public class DatabaseOpenHelper extends SQLiteOpenHelper {

    //数据库名称
    private static final String name = "db_name.db";
    //数据库版本
    private static final int version = 1;

    private SQLiteDatabase readableDatabase = null;

    public DatabaseOpenHelper(Context context) {
        //第三个参数CursorFactory指定在执行查询时获得一个游标实例的工厂类,设置为null,代表使用系统默认的工厂类
        super(context, name, null, version);
    }

    public SQLiteDatabase getDatabase() {
        if (readableDatabase == null) {
            readableDatabase = this.getReadableDatabase();
        }
        return readableDatabase;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final int FIRST_DATABASE_VERSION = 1;
        onUpgrade(db, FIRST_DATABASE_VERSION, version);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 使用for实现跨版本升级数据库
        for (int i = oldVersion; i < newVersion; i++) {
            switch (i) {
                case 1:
                    break;
                default:
                    break;
            }
        }
    }
}
