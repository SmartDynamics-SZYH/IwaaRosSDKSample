package com.szyh.iwaarossdksample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.szyh.iwaasdk.sdk.navi.interfaces.DefaultCallback;
import com.szyh.iwaasdk.sdk.peripheral.RobotPeripheralApi;
import com.szyh.iwaasdk.sdk.peripheral.bean.ReadSSCCardInfoResponse;
import com.szyh.iwaasdk.sdk.peripheral.bean.ReadSSCUserInfoResponse;
import com.szyh.iwaasdk.sdk.peripheral.interfaces.ConnectPeripheralCallback;
import com.szyh.iwaasdk.sdk.peripheral.interfaces.MetalKeyboardInputCallback;
import com.szyh.iwaasdk.sdk.peripheral.interfaces.ReadSSCCardInfoCallback;
import com.szyh.iwaasdk.sdk.peripheral.interfaces.ReadSSCUserInfoCallback;

public class PeripheralActivity extends AppCompatActivity {

    private static final String TAG = "PeripheralActivity";

    private TextView infoText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peripheral);

        infoText = findViewById(R.id.id_info_text2);

        RobotPeripheralApi.get().registerMetalKeyboardCallback(new MetalKeyboardInputCallback() {
            @Override
            public void onMetalKeyboardInputNumber(String number) {
                setInfoText("Input:" + number);
            }

            @Override
            public void onMetalKeyboardInputDot() {
                setInfoText("Input:.");
            }

            @Override
            public void onMetalKeyboardInputBlank() {
                setInfoText("Input:Blank");
            }

            @Override
            public void onMetalKeyboardInputCancel() {
                setInfoText("Input:Cancel");
            }

            @Override
            public void onMetalKeyboardInputConfirm() {
                setInfoText("Input:confirm");
            }

            @Override
            public void onMetalKeyboardInputModify() {
                setInfoText("Input:modify");
            }
        });
    }

    public void connectPeripheralService(View view) {
        RobotPeripheralApi.get().connectPeripheralService(true, new ConnectPeripheralCallback() {
            @Override
            public void onConnectPeripheralOpen() {
                setInfoText("已与windows外设工控机连接成功~");
            }

            @Override
            public void onConnectPeripheralClose(int code, String reason, boolean remote) {

            }

            @Override
            public void onConnectPeripheralError(Exception e) {

            }
        });
    }

    private void setInfoText(final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                infoText.setText(text);
            }
        });
    }

    public void disconnectPeripheralService(View view) {
        RobotPeripheralApi.get().destroy();
    }

    public void readUserInfo(View view) {
        RobotPeripheralApi.get().readSocialSecurityUserInfo(new ReadSSCUserInfoCallback() {
            @Override
            public void onReadSSCUserInfoSuccess(ReadSSCUserInfoResponse readSSCUserInfoResponse) {
                setInfoText(JSON.toJSONString(readSSCUserInfoResponse));
            }

            @Override
            public void onReadSSCUserFail(int errorCode) {
                setInfoText("读取社保卡用户信息错误,错误码：" + errorCode);
            }
        });
    }

    public void readCardInfo(View view) {
        RobotPeripheralApi.get().readSocialSecurityCardInfo(new ReadSSCCardInfoCallback() {
            @Override
            public void onReadSSCCardInfoSuccess(ReadSSCCardInfoResponse readSSCCardInfoResponse) {
                setInfoText(JSON.toJSONString(readSSCCardInfoResponse));
            }

            @Override
            public void onReadSSCCardInfoFail(int errorCode) {
                setInfoText("读取社保卡卡片信息错误,错误码：" + errorCode);
            }
        });
    }

    public void openMetalKeyboard(View view) {
        RobotPeripheralApi.get().openMetalKeyboard(new DefaultCallback() {
            @Override
            public void onSuccess() {
                setInfoText("打开金属键盘成功~");
            }

            @Override
            public void onFail(int errorCode) {
                setInfoText("打开金属键盘失败，错误码：" + errorCode);
            }
        });
    }

    public void closeMetalKeyboard(View view) {
        RobotPeripheralApi.get().closeMetalKeyboard(new DefaultCallback() {
            @Override
            public void onSuccess() {
                setInfoText("关闭金属键盘成功~");
            }

            @Override
            public void onFail(int errorCode) {
                setInfoText("关闭金属键盘失败，错误码：" + errorCode);
            }
        });
    }
}
