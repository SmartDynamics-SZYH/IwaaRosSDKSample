package com.szyh.iwaarossdksample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.szyh.iwaarossdksample.util.ToastUtil;
import com.szyh.iwaasdk.sdk.mic.RobotMicApi;
import com.szyh.iwaasdk.sdk.mic.interfaces.MicAwakenListener;

public class MicDemoActivity extends AppCompatActivity implements MicAwakenListener {

    private static final String TAG = "RosDemoActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mic_demo);
        RobotMicApi.get().registerMicAwakenListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isFinishing()) {
            RobotMicApi.get().unregisterMicAwakenListener(this);
        }
    }

    public void queryMicVersion(View view) {
        String version = RobotMicApi.get().queryMicCoreBroadVersion(false);
        if (version != null) {
            ToastUtil.showMessage("MicVersion = " + version);
        } else {
            ToastUtil.showMessage("MicVersion = null");
        }
    }

    public void queryAwakenAngle(View view) {
        int angle = RobotMicApi.get().queryAwakenAngle();
        ToastUtil.showMessage("MicAngle = " + angle);
    }

    @Override
    public void onMicAwaken(int angle) {
        ToastUtil.showMessage("MicDemoActivity angle = " + angle);
    }
}
