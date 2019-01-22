package com.szyh.iwaarossdksample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
}
