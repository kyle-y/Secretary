package com.example.yxb.secretary.common.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * PACKAGE_NAME:com.example.yxb.secretary.common.db
 * FUNCTIONAL_DESCRIPTION
 * CREATE_BY:xiaobo
 * CREATE_TIME:2016/8/31
 * MODIFY_BY:
 */
public class DBHelper2 extends SQLiteOpenHelper{

    private static final String DATABASE_NAME2 = "user_info.db";
    private static final int DATABASE_VERSION2= 1;

    public static final String GOLD_BEAN_TABLE = "gold_bean";
    private static final String CREATE_GOLD_BEAN_SQL = "CREATE TABLE "
            + GOLD_BEAN_TABLE
            + " (_id Integer primary key autoincrement,"
            + " name text,"
            + " type text,"
            + " num integer,"
            + " time text);";

    public DBHelper2(Context context){
        this(context, DATABASE_NAME2, null, DATABASE_VERSION2);
    }

    public DBHelper2(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_GOLD_BEAN_SQL);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(newVersion > oldVersion){
            db.execSQL("DROP TABLE IF EXISTS" + GOLD_BEAN_TABLE);
            onCreate(db);
        }
    }
}
