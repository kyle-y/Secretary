package com.example.yxb.secretary.common.db;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2016/6/8.
 */
public class DBHelper extends SQLiteOpenHelper{
    private static final String DATABASE_NAME = "weather_infos.db";
    private static final int DATABASE_VERSION = 1;

    public static final String WEATHER_PROVINCE_TABLE = "weather_province";
    public static final String WEATHER_CITY_TABLE = "weather_city";



    private static final String CREATE_WEAHTER_PROVINCE_SQL = "CREATE TABLE "
            + WEATHER_PROVINCE_TABLE
            + " (_id Integer primary key autoincrement,"
            + " name text,"
            + " province_id integer);";

    private static final String CREATE_WEAHTER_CITY_SQL = "CREATE TABLE "
            + WEATHER_CITY_TABLE
            + " (_id Integer primary key autoincrement,"
            + " province_id integer,"
            + " name text,"
            + " city_num integer);";


    public DBHelper(Context context){
        this(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_WEAHTER_PROVINCE_SQL);
        sqLiteDatabase.execSQL(CREATE_WEAHTER_CITY_SQL);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        /*
        如果需要保存原先数据库中的表数据，那么在执行升级表机构的之前，需要先将
        之前的表中的数据加载到内存中或者其他地方临时存放，然后删除原来的表，创建新表后，
        将临时存放的数据重新导入到新表中。如果不需要保存原先的数据，可以直接删除原来的表。
         */
        if(newVersion > oldVersion){
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS" + WEATHER_PROVINCE_TABLE);
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS" + WEATHER_CITY_TABLE);
            onCreate(sqLiteDatabase);
        }

    }
}
