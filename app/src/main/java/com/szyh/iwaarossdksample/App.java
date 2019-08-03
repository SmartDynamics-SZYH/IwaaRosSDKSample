package com.szyh.iwaarossdksample;

import android.app.Application;
import android.util.Log;

import com.szyh.iwaarossdksample.util.UiUtil;
import com.szyh.iwaasdk.sdk.android.RobotAndroidApi;
import com.szyh.iwaasdk.sdk.mic.RobotMicApi;
import com.szyh.iwaasdk.sdk.ros.RobotRosApi;
import com.szyh.iwaasdk.sdk.ros.interfaces.InitListener;

/**
 * author  ruanhouli
 * email   ruanhouli@szyh-smart.com
 * created 2018/11/13 17:31
 * remark  TODO
 */

public class App extends Application {

    private static final String TAG = App.class.getSimpleName();

    public static boolean isInitSuccess = false;

    private static App instance;

    public static App getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        UiUtil.init(this);

        //初始化RobotRosApi
        RobotRosApi.get().init(getApplicationContext(), new InitListener() {
            @Override
            public void onInit() {
                isInitSuccess = true;
                Log.i(TAG, "onInit: ");
            }

            @Override
            public void onReConnect() {
                isInitSuccess = true;
                Log.i(TAG, "onReConnect: ");
            }
        });

        //初始化RobotMicApi
        RobotMicApi.get().init(getApplicationContext(), new InitListener() {
            @Override
            public void onInit() {
                Log.i(TAG, "RobotMicApi onInit: ");
            }

            @Override
            public void onReConnect() {
                Log.i(TAG, "RobotMicApi onReConnect: ");
            }
        });

        //RK3399必须要初始化，RK3288可以不需要。
        RobotAndroidApi.get().init(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        Log.i(TAG, "onTerminate: ");
        RobotRosApi.get().destroy();
        RobotMicApi.get().destroy();
        isInitSuccess = false;
    }
}
