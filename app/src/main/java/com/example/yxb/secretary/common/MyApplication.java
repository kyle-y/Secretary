package com.example.yxb.secretary.common;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

import com.baidu.apistore.sdk.ApiStoreSDK;

/**
 * PACKAGE_NAME:com.example.yxb.secretary.common
 * FUNCTIONAL_DESCRIPTION
 * CREATE_BY:xiaobo
 * CREATE_TIME:2016/8/9
 * MODIFY_BY:
 */
public class MyApplication extends Application{
    private static Context context;
    private static int mainThreadId;
    private static Handler handler;
    @Override
    public void onCreate() {
        super.onCreate();
        ApiStoreSDK.init(this, "647418005b9b5340304ce69b8a3fe5a8");
        context = getApplicationContext();
        mainThreadId = android.os.Process.myTid();// 获取当前主线程id
        handler = new Handler();
    }

    public static Context getContext() {
        return context;
    }


    public static int getMainThreadId() {
        return mainThreadId;
    }

    public static Handler getHandler() {
        return handler;
    }
}
