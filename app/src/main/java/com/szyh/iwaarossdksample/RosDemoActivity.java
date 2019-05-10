package com.szyh.iwaarossdksample;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;

import com.android.xhapimanager.XHApiManager;
import com.leon.lfilepickerlibrary.LFilePicker;
import com.szyh.iwaarossdksample.util.ToastUtil;
import com.szyh.iwaarossdksample.util.UiUtil;
import com.szyh.iwaasdk.coreservice.smt.bean.ArmInfo;
import com.szyh.iwaasdk.coreservice.smt.bean.DeviceVersion;
import com.szyh.iwaasdk.coreservice.smt.bean.MainBroadEnable;
import com.szyh.iwaasdk.coreservice.smt.bean.MainBroadStatus;
import com.szyh.iwaasdk.sdk.ros.RobotRosApi;
import com.szyh.iwaasdk.sdk.ros.define.RosDefine;
import com.szyh.iwaasdk.sdk.ros.interfaces.MainBroadStatusListener;
import com.szyh.iwaasdk.sdk.ros.interfaces.McuUpdateListener;

import java.util.Arrays;
import java.util.List;

public class RosDemoActivity extends AppCompatActivity implements McuUpdateListener, MainBroadStatusListener {

    private static final String TAG = "RosDemoActivity";

    private TextView infoText;

    static SparseArray<String> sparseArray = new SparseArray<>();

    static {
        sparseArray.append(DeviceVersion.TYPE_MAIN_BROAD, "主控板");
        sparseArray.append(DeviceVersion.TYPE_UWB, "超宽带");
        sparseArray.append(DeviceVersion.TYPE_POWER_BOARD, "电源管理板");
        sparseArray.append(DeviceVersion.TYPE_CHASSIS_LEFT, "底盘左");
        sparseArray.append(DeviceVersion.TYPE_CHASSIS_RIGHT, "底盘右");
        sparseArray.append(DeviceVersion.TYPE_ULTRASONIC_FIRST, "超声波1");
        sparseArray.append(DeviceVersion.TYPE_ULTRASONIC_SECOND, "超声波2");
        sparseArray.append(DeviceVersion.TYPE_RUDDER_BROAD, "舵机板");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ros_demo);
        infoText = findViewById(R.id.id_info_text);
        RobotRosApi.get().registerMainBroadStatusListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isFinishing()) {
            RobotRosApi.get().unregisterMainBroadStatusListener(this);
        }
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

    private int count = 0;

    public void ultrasoundEnables(View view) {
        MainBroadEnable mainBroadEnable = RobotRosApi.get().queryMainBroadEnable();
        Log.i(TAG, "ultrasoundEnables: " + mainBroadEnable.toString());
        boolean[] ultrasoundEnables = mainBroadEnable.getUltrasoundEnables();
        for (int i = 0; i < ultrasoundEnables.length; i++) {
            ultrasoundEnables[i] = count % 2 == 0;
        }
        count++;
        mainBroadEnable.setUltrasoundEnables(ultrasoundEnables);
        mainBroadEnable.setFdkDriverFlag(1);
        RobotRosApi.get().updateMainBroadEnable(mainBroadEnable);
    }


    public void queryExpressionScreenVersion(View view) {
        String version = RobotRosApi.get().queryExpressionScreenVersion();
        ToastUtil.showMessage("表情屏版本号:" + version);
        setInfoText("表情屏版本号:" + version);
    }

    public void queryExpressionIndex(View view) {
        int index = RobotRosApi.get().queryExpressionIndex();
        ToastUtil.showMessage("表情屏图片序号:" + index);
        setInfoText("表情屏图片序号:" + index);
    }

    int index = 1;

    public void changeExpression(View view) {
        if (index > 9) {
            index = 0;
        }
        ToastUtil.showMessage("开始播放第" + (index + 1) + "组动画~~");
        RobotRosApi.get().changeExpression(index);
        index++;
    }

    public void shutdownRobotSystem(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // 设置参数
        builder.setTitle("请做出选择").setIcon(R.mipmap.ic_launcher)
                .setMessage("请问是否需要关闭机器人系统？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        if (RobotRosApi.get().shutdownRobotSystem()) {
                            ToastUtil.showMessage("关机命令发送成功，30s后关机！");
                            new XHApiManager().XHShutDown();
                        } else {
                            ToastUtil.showMessage("关机命令发送失败！");
                        }
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog,
                                int which) {
            }
        });
        builder.create().show();
    }

    public void restartRobotSystem(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("请做出选择").setIcon(R.mipmap.ic_launcher)
                .setMessage("请问是否需要重启机器人系统？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        if (RobotRosApi.get().restartRobotSystem()) {
                            ToastUtil.showMessage("重启命令发送成功，30S后重启！");
                            new XHApiManager().XHShutDown();
                        } else {
                            ToastUtil.showMessage("重启命令发送失败！");
                        }
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog,
                                int which) {
            }
        });
        builder.create().show();
    }

    @Override
    public void onMainBroadStatusResult(MainBroadStatus mainBroadStatus) {

    }

    public void gotoUpdate(View view) {
        Intent intent = new Intent(this, MCUUpdateActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void getLowPercent(View view) {
        setInfoText("读取低电量设置值为：" + RobotRosApi.get().getLowBatteryAlarmPercent() + "%");
    }

    public void setLow20(View view) {
        setLowPercent(20);
    }

    public void setLow30(View view) {
        setLowPercent(30);
    }

    public void setLow40(View view) {
        setLowPercent(40);
    }

    public void setLow50(View view) {
        setLowPercent(50);
    }

    private void setLowPercent(int Percent) {
        RobotRosApi.get().setLowBatteryAlarmPercent(Percent);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // 设置参数
        builder.setTitle("请做出选择").setIcon(R.mipmap.ic_launcher)
                .setMessage("请问是否确认要设置低电量为" + Percent + "%?")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        RobotRosApi.get().writeConfigConfirm();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog,
                                int which) {
            }
        });
        builder.create().show();
    }
}
