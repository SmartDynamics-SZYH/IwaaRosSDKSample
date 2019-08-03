package com.szyh.iwaarossdksample;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.szyh.iwaasdk.sdk.android.RobotAndroidApi;

import java.io.File;

public class AndroidDemoActivity extends AppCompatActivity {

    private static final String TAG = "RosDemoActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_android_demo);
    }

    public void androidShutdown(View view) {
        RobotAndroidApi.get().androidShutdown();
    }

    public void androidReboot(View view) {
        RobotAndroidApi.get().androidReboot();
    }

    public void installOnBackground(View view) {

        String apkPath =
                Environment.getExternalStorageDirectory()
                        + File.separator + "Iwaa4TestApp-release-1.0.7.apk";
        RobotAndroidApi.get().installOnBackground(apkPath,
                "com.iwaatest.szyh");
    }

    public void uninstallOnBackground(View view) {
        RobotAndroidApi.get().uninstallOnBackground("com.iwaatest.szyh");
    }

    public void showStatusBar(View view) {
        RobotAndroidApi.get().showStatusBar();
    }

    public void hideStatusBar(View view) {
        RobotAndroidApi.get().hideStatusBar();
    }
}
