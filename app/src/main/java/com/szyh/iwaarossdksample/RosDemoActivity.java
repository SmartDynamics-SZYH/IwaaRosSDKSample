package com.szyh.iwaarossdksample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;

import com.leon.lfilepickerlibrary.LFilePicker;
import com.szyh.iwaarossdksample.util.ToastUtil;
import com.szyh.iwaarossdksample.util.UiUtil;
import com.szyh.iwaasdk.coreservice.smt.bean.ArmInfo;
import com.szyh.iwaasdk.coreservice.smt.bean.DeviceVersion;
import com.szyh.iwaasdk.sdk.ros.RobotRosApi;
import com.szyh.iwaasdk.sdk.ros.define.RosDefine;
import com.szyh.iwaasdk.sdk.ros.interfaces.McuUpdateListener;

import java.util.Arrays;
import java.util.List;

public class RosDemoActivity extends AppCompatActivity implements McuUpdateListener {

    private static final String TAG = "RosDemoActivity";

    private TextView infoText;

    static SparseArray<String> sparseArray = new SparseArray<>();

    static {
        sparseArray.append(DeviceVersion.TYPE_MAIN_BROAD, "主控板版本号");
        sparseArray.append(DeviceVersion.TYPE_UWB, "超宽带版本号");
        sparseArray.append(DeviceVersion.TYPE_POWER_BOARD, "电源管理板版本号");
        sparseArray.append(DeviceVersion.TYPE_CHASSIS_LEFT, "底盘左版本号");
        sparseArray.append(DeviceVersion.TYPE_CHASSIS_RIGHT, "底盘右版本号");
        sparseArray.append(DeviceVersion.TYPE_ULTRASONIC_FIRST, "超声波1版本号");
        sparseArray.append(DeviceVersion.TYPE_ULTRASONIC_SECOND, "超声波2版本号");
        sparseArray.append(DeviceVersion.TYPE_RUDDER_BROAD, "舵机板版本号");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ros_demo);
        infoText = findViewById(R.id.id_info_text);
    }

    private void setInfoText(final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                infoText.setText(text);
            }
        });
    }

    public void queryAllVersion(View view) {
        List<DeviceVersion> deviceVersions = RobotRosApi.get().queryMountDeviceVersions(true);
        DeviceVersion mainBroadVersion = RobotRosApi.get().queryMainBroadVersion(true);
        if (mainBroadVersion != null) {
            deviceVersions.add(mainBroadVersion);
        }
        DeviceVersion rudderBroadVersion = RobotRosApi.get().queryRudderBroadVersion(true);
        if (rudderBroadVersion != null) {
            deviceVersions.add(rudderBroadVersion);
        }

        StringBuffer sb = new StringBuffer();

        for (DeviceVersion deviceVersion : deviceVersions) {
            sb.append(sparseArray.get(deviceVersion.getType())).append("：")
                    .append(deviceVersion.getVersionString()).append("\r\t");
        }
        setInfoText(sb.toString());
    }

    public void queryMainBroadVersion(View view) {
        DeviceVersion deviceVersion = RobotRosApi.get().queryMainBroadVersion(true);
        if (deviceVersion != null) {
            StringBuffer sb = new StringBuffer();
            sb.append(sparseArray.get(deviceVersion.getType())).append("：")
                    .append(deviceVersion.getVersionString());
            setInfoText(sb.toString());
        }
    }

    public void queryBatteryInfo(View view) {
        setInfoText(RobotRosApi.get().queryBatteryInfo().toString());
    }

    public void queryMainBroadStatus(View view) {
        setInfoText(RobotRosApi.get().queryMainBroadStatus().toString());
    }

    public void queryUltrasoundValue(View view) {
        setInfoText(Arrays.toString(RobotRosApi.get().queryUltrasoundValue()));
    }

    public void queryMotionInfo(View view) {
        setInfoText(RobotRosApi.get().queryMotionInfo().toString());
    }

    public void queryInfraRedInfo(View view) {
        setInfoText(RobotRosApi.get().queryInfraRedInfo().toString());
    }

    public void queryChargeStatus(View view) {
        setInfoText(RobotRosApi.get().queryChargeStatus().toString());
    }

    public void queryPowerBroadInfo(View view) {
        setInfoText(RobotRosApi.get().queryPowerBroadInfo().toString());
    }

    public void queryWheelInfo(View view) {
        setInfoText(RobotRosApi.get().queryWheelInfo().toString());
    }

    public void queryMainBroadEnable(View view) {
        setInfoText(RobotRosApi.get().queryMainBroadEnable().toString());
    }

    public void queryUltrasoundScanIndexs(View view) {
        setInfoText(Arrays.toString(RobotRosApi.get().queryUltrasoundScanIndexs()));
    }

    public void queryUltrasoundBlindAreaDistances(View view) {
        setInfoText(Arrays.toString(RobotRosApi.get().queryUltrasoundBlindAreaDistances()));
    }

    public void queryUWBInfo(View view) {
        setInfoText(RobotRosApi.get().queryUWBInfo().toString());
    }

    public void queryMainBroadRTC(View view) {
        setInfoText(RobotRosApi.get().queryMainBroadRTC().toString());
    }

    public void queryInfraredThreshold(View view) {
        setInfoText(Arrays.toString(RobotRosApi.get().queryInfraredThreshold()));
    }

    //以下是舵机版////////////////////////////////////////////////////////////////////////////////////

    public void queryRudderBroadVersion(View view) {
        DeviceVersion deviceVersion = RobotRosApi.get().queryRudderBroadVersion(true);
        if (deviceVersion != null) {
            StringBuffer sb = new StringBuffer();
            sb.append(sparseArray.get(deviceVersion.getType())).append("：")
                    .append(deviceVersion.getVersionString());
            setInfoText(sb.toString());
        }
    }

    public void queryHeadInfo(View view) {
        setInfoText(RobotRosApi.get().queryHeadInfo().toString());
    }

    public void queryLeftArmInfo(View view) {
        setInfoText(RobotRosApi.get().queryArmInfo(ArmInfo.LEFT).toString());
    }

    public void queryRightArmInfo(View view) {
        setInfoText(RobotRosApi.get().queryArmInfo(ArmInfo.RIGHT).toString());
    }

    public void queryRudderStatus(View view) {
        setInfoText(RobotRosApi.get().queryRudderStatus().toString());
    }

    public void turnHead(View view) {
        ToastUtil.showMessage("头部开始转到170度");
        RobotRosApi.get().turnHead(10, 170);
        UiUtil.post(new Runnable() {
            @Override
            public void run() {
                ToastUtil.showMessage("头部开始转到10度");
                RobotRosApi.get().turnHead(10, 10);
                UiUtil.post(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.showMessage("头部开始转到90度");
                        RobotRosApi.get().turnHead(10, 90);
                    }
                }, 10 * 1000);
            }
        }, 5000);
    }


    public void turnLeftArm(View view) {
        RobotRosApi.get().turnLeftArm(10, 270);
        UiUtil.post(new Runnable() {
            @Override
            public void run() {
                RobotRosApi.get().turnLeftArm(10, 90);
                UiUtil.post(new Runnable() {
                    @Override
                    public void run() {
                        RobotRosApi.get().turnLeftArm(10, 180);
                    }
                }, 5000);
            }
        }, 3000);
    }

    public void turnRightArm(View view) {
        RobotRosApi.get().turnRightArm(10, 270);
        UiUtil.post(new Runnable() {
            @Override
            public void run() {
                RobotRosApi.get().turnRightArm(10, 90);
                UiUtil.post(new Runnable() {
                    @Override
                    public void run() {
                        RobotRosApi.get().turnRightArm(10, 180);
                    }
                }, 5000);
            }
        }, 3000);
    }

    public void setLedRed(View view) {
        RobotRosApi.get().ledSetColor(255, 0, 0);
    }

    public void setLedGreen(View view) {
        RobotRosApi.get().ledSetColor(0, 255, 0);
    }

    public void setLedBlue(View view) {
        RobotRosApi.get().ledSetColor(0, 0, 255);
    }

    public void ledSetOnOff(View view) {
        RobotRosApi.get().ledSetOnOff(true);
    }

    public void fanSetTempThreshold(View view) {
        RobotRosApi.get().fanSetTempThreshold(60);
    }

    public void fanGetTempThreshold(View view) {
        setInfoText("风扇启动温度阈值：" + RobotRosApi.get().fanGetTempThreshold());
    }

    public void getCurrentSensorTemp(View view) {
        setInfoText("当前温度传感器温度：" + RobotRosApi.get().getCurrentSensorTemp());
    }

    public void syncBothArms1(View view) {
        RobotRosApi.get().syncBothArms(RosDefine.SyncBothArm.SYNCTL_SAME_DIR);
        turnLeftArm(view);
    }

    public void syncBothArms2(View view) {
        RobotRosApi.get().syncBothArms(RosDefine.SyncBothArm.SYNCTL_DIFFERENT_DIR);
        turnLeftArm(view);
    }

    public void syncBothArms3(View view) {
        RobotRosApi.get().syncBothArms(RosDefine.SyncBothArm.SYNCTL_CANCLE);
    }

    public void updateMainBroadBin(View view) {
        fileChoose(RosDefine.McuBinType.MAIN_BROAD & 0xFF);
    }

    public void updateRudderBin(View view) {
        fileChoose(RosDefine.McuBinType.RUDDER_BROAD & 0xFF);
    }

    public void updatePWRBroadBin(View view) {
        fileChoose(RosDefine.McuBinType.PWR_BOARD & 0xFF);
    }

    public void updateUltrasonicBoard1Bin(View view) {
        fileChoose(RosDefine.McuBinType.ULTRASONIC_BOARD1 & 0xFF);
    }

    public void updateUltrasonicBoard2Bin(View view) {
        fileChoose(RosDefine.McuBinType.ULTRASONIC_BOARD2 & 0xFF);
    }

    private void fileChoose(int type) {
        new LFilePicker()
                .withActivity(this)
                .withRequestCode(type)
//                .withStartPath(Environment.getExternalStorageDirectory().getPath())
                .withStartPath("/mnt/usb_storage/USB_DISK7")
                .withIsGreater(false)
                .withMutilyMode(false)
                .withFileFilter(new String[]{".bin"})
                .withFileSize(500 * 1024)
                .start();
    }

    @Override
    public void onMcuUpdatePrepare() {
        Log.i(TAG, "onMcuUpdatePrepare: ");
        ToastUtil.showMessage("onMcuUpdatePrepare");
    }

    @Override
    public void onMcuUpdateProcess(int progress) {
        Log.i(TAG, "onMcuUpdateProcess: progress  = " + progress);
        ToastUtil.showMessage("onMcuUpdateProcess: progress  = " + progress);
    }

    @Override
    public void onMcuUpdateComplete() {
        Log.i(TAG, "onMcuUpdateComplete: ");
        ToastUtil.showMessage("onMcuUpdateComplete: ");
    }

    @Override
    public void onMcuUpdateError(int errorCode) {
        Log.i(TAG, "onMcuUpdateError: errorCode = " + errorCode);
        ToastUtil.showMessage("onMcuUpdateError: errorCode = " + errorCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(TAG, "onActivityResult: requestCode = " + requestCode);
        if (resultCode == RESULT_OK) {
            List<String> list = data.getStringArrayListExtra("paths");
            String path = null;
            if (list.size() > 0) {
                path = list.get(0);
            }
            if (TextUtils.isEmpty(path)) {
                ToastUtil.showMessage("升级文件不存在，请重新选择。");
                return;
            }
            if (!path.endsWith(".bin")) {
                ToastUtil.showMessage("升级文件后缀名不对，请重新选择。");
            } else {
                RobotRosApi.get().updateMcuByBinType(requestCode, path, this);
            }
        }
    }
}
