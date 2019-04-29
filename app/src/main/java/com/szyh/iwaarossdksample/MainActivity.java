package com.szyh.iwaarossdksample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.szyh.iwaarossdksample.util.UiUtil;
import com.szyh.iwaasdk.sdk.mic.RobotMicApi;
import com.szyh.iwaasdk.sdk.ros.RobotRosApi;
import com.szyh.iwaasdk.sdk.ros.interfaces.InitListener;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //初始化RobotRosApi
        RobotRosApi.get().init(getApplicationContext(), new InitListener() {
            @Override
            public void onInit() {
                Log.i(TAG, "RobotRosApi onInit: ");
            }

            @Override
            public void onReConnect() {
                Log.i(TAG, "RobotRosApi onReConnect: ");
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
    }


    public void startRosActivity(View view) {
        gotoActivity(RosDemoActivity.class);
    }

    public void startFaceActivity(View view) {
        gotoActivity(FaceDemoActivity.class);
    }

    public void startMicActivity(View view) {
        gotoActivity(MicDemoActivity.class);
    }

    public void startNaviActivity(View view) {
        gotoActivity(NaviDemoActivity.class);
    }

    private void gotoActivity(Class clazz) {
        Intent intent = new Intent(this, clazz);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void onlineUpgradeActivity(View view) {
        gotoActivity(OnlineUpgradeActivity.class);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isFinishing()) {
            RobotRosApi.get().destroy();
            RobotMicApi.get().destroy();
            UiUtil.post(new Runnable() {
                @Override
                public void run() {
                    android.os.Process.killProcess(android.os.Process.myPid());
                }
            }, 1000);
        }
    }
}
