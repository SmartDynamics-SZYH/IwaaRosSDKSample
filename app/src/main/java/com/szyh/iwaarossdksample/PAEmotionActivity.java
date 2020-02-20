package com.szyh.iwaarossdksample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.szyh.paemotion.ConnectEmotionCallback;
import com.szyh.paemotion.PAEmotionApi;

public class PAEmotionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paemotion);
        PAEmotionApi.getInstance().connectPAEmotionService(true, new ConnectEmotionCallback() {
            @Override
            public void onConnectEmotionOpen() {

            }

            @Override
            public void onConnectEmotionClose(int i, String s, boolean b) {

            }

            @Override
            public void onConnectEmotionError(Exception e) {

            }
        });
    }

    /**
     * 默认有 blink,sleep,smile,xixi
     */
    public void changeSleepEmtion() {
        PAEmotionApi.getInstance().sendMessage("sleep");
    }


    @Override
    protected void onStop() {
        super.onStop();
        if (isFinishing()) {
            PAEmotionApi.getInstance().disconnectPAEmotionService();
        }
    }
}
