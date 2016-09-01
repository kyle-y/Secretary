package com.example.yxb.secretary.common;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Handler;

import com.baidu.apistore.sdk.ApiStoreSDK;
import com.baidu.mapapi.SDKInitializer;
import com.example.yxb.secretary.utils.PreferencesUtils;

import cn.bmob.v3.Bmob;

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
    private  static boolean isLoaded = false;
    private int goldBeanNum;
    @Override
    public void onCreate() {
        super.onCreate();
        ApiStoreSDK.init(this, "647418005b9b5340304ce69b8a3fe5a8");
        Bmob.initialize(this, "30f8c2e5ccf61481c2d0f152b4db5bce");
        SDKInitializer.initialize(this);
        context = getApplicationContext();
        mainThreadId = android.os.Process.myTid();// 获取当前主线程id
        handler = new Handler();

        if (PreferencesUtils.getBoolean(this,"isLogin") && isOnNetWork()){
            setIsLoaded(true);
        }
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

    public static boolean isLoaded() {
        return isLoaded;
    }

    public static void setIsLoaded(boolean isLoaded) {
        MyApplication.isLoaded = isLoaded;
    }

    public static boolean isOnNetWork(){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo().isAvailable();
    }
}
