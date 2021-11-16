package com.szyh.iwaarossdksample;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.szyh.iwaarossdksample.util.ToastUtil;
import com.szyh.iwaasdk.sdk.navi.define.MessageDefine;
import com.szyh.iwaasdk.sdk.upgrade.RobotOnlineUpgradeApi;
import com.szyh.iwaasdk.sdk.upgrade.bean.OnlineDeviceVersion;
import com.szyh.iwaasdk.sdk.upgrade.bean.ReportAndroidRes;
import com.szyh.iwaasdk.sdk.upgrade.bean.ReportRobotRes;
import com.szyh.iwaasdk.sdk.upgrade.bean.RobotUpdateInfo;
import com.szyh.iwaasdk.sdk.upgrade.bean.RobotUpdatePushInfo;
import com.szyh.iwaasdk.sdk.upgrade.bean.RobotUpgradeRes;
import com.szyh.iwaasdk.sdk.upgrade.interfaces.AndroidUpgradePushListener;
import com.szyh.iwaasdk.sdk.upgrade.interfaces.ConnectUpgradeCallback;
import com.szyh.iwaasdk.sdk.upgrade.interfaces.McuUpgradePushListener;
import com.szyh.iwaasdk.sdk.upgrade.interfaces.ReportRobotVersionsInfoListener;
import com.szyh.iwaasdk.sdk.upgrade.interfaces.RobotUpdateInfoListener;
import com.szyh.iwaasdk.sdk.upgrade.interfaces.RobotUpgradeStatusListener;

import java.util.ArrayList;
import java.util.List;

import static com.szyh.iwaasdk.sdk.navi.define.MessageDefine.RequestCmd.ROBOT_UPGRADE_MCU_CMD;

public class OnlineUpgradeActivity extends AppCompatActivity implements McuUpgradePushListener, AndroidUpgradePushListener {

    private static final String TAG = "OnlineUpgradeActivity";

    private String robotCode;

    private RobotUpdateInfo robotUpdateInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_upgrade);
        robotCode = Build.SERIAL + Build.SERIAL;
        //华为平板
        Log.i(TAG, "queryAllUpgradeInfo: robotCode = " + robotCode);

        RobotOnlineUpgradeApi.get().registerMcuUpgradePushListener(this);
        RobotOnlineUpgradeApi.get().registerAndroidUpgradePushListener(this);
    }

    public void queryAllUpgradeInfo(View view) {
        RobotOnlineUpgradeApi.get().queryUpdateDataInfo(robotCode, 0, new RobotUpdateInfoListener() {
            @Override
            public void onRobotUpdateInfoResponse(RobotUpdateInfo robotUpdateInfo) {
                OnlineUpgradeActivity.this.robotUpdateInfo = robotUpdateInfo;
                Log.i(TAG, "onRobotUpdateInfoResponse: " + robotUpdateInfo.toString());
                ToastUtil.showMessage(JSON.toJSONString(robotUpdateInfo));
            }

            @Override
            public void onRobotUpdateInfoFail(Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void connectUpgradeService(View view) {
        //ws:// update.szyh-smart.com:10086/WebSocketMessager
        RobotOnlineUpgradeApi.get().connectUpgradeService("update.szyh-smart.com", "10086", new ConnectUpgradeCallback() {
            @Override
            public void onConnectUpgradeOpen() {
                Log.i(TAG, "onConnectUpgradeOpen: ");
                ToastUtil.showMessage("升级服务器连接成功！");
                // TODO: 2019/3/7 连接服务器成功之后，必须邀上报本地版本号状态，否则无法接受到推送的数据，MCU固件版本号和安卓系统版本号。
                reportRobotVersionsInfo();
            }

            @Override
            public void onConnectUpgradeClose(int code, String reason, boolean remote) {
                Log.i(TAG, "onConnectUpgradeClose: code = " + code + ",reason = " + reason);
                ToastUtil.showMessage("升级服务器连接失败！");
            }

            @Override
            public void onConnectUpgradeError(Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void disconnectUpgradeService(View view) {
        RobotOnlineUpgradeApi.get().disconnectUpgradeService();
        if (!RobotOnlineUpgradeApi.get().isConnectUpgradeService()) {
            ToastUtil.showMessage("断开升级服务器成功！");
        }
    }

    static List<OnlineDeviceVersion> hardwareInfoList = new ArrayList<>();

    static {

        OnlineDeviceVersion onlineDeviceVersion2 = new OnlineDeviceVersion("主控板",
                "1.0.0", "2019-02-01", 0x81, "");

        OnlineDeviceVersion onlineDeviceVersion3 = new OnlineDeviceVersion("电源管理板",
                "1.0.0", "2019-02-01", 0x04, "");

        OnlineDeviceVersion onlineDeviceVersion4 = new OnlineDeviceVersion("底盘左",
                "1.0.0", "2019-02-01", 0x1, "");

        OnlineDeviceVersion onlineDeviceVersion5 = new OnlineDeviceVersion("底盘右",
                "1.0.0", "2019-02-01", 0x2, "");

        OnlineDeviceVersion onlineDeviceVersion6 = new OnlineDeviceVersion("超声波1",
                "1.0.0", "2019-02-01", 0x6, "");

        OnlineDeviceVersion onlineDeviceVersion7 = new OnlineDeviceVersion("超声波2",
                "1.0.0", "2019-02-01", 0x7, "");

        OnlineDeviceVersion onlineDeviceVersion8 = new OnlineDeviceVersion("舵机板",
                "1.0.0", "2019-02-01", 0x82, "");

        hardwareInfoList.add(onlineDeviceVersion2);
        hardwareInfoList.add(onlineDeviceVersion3);
        hardwareInfoList.add(onlineDeviceVersion4);
        hardwareInfoList.add(onlineDeviceVersion5);
        hardwareInfoList.add(onlineDeviceVersion6);
        hardwareInfoList.add(onlineDeviceVersion7);
        hardwareInfoList.add(onlineDeviceVersion8);
    }

    //上报版本号信息
    public void reportRobotVersionsInfo(View view) {
        reportRobotVersionsInfo();
    }

    private void reportRobotVersionsInfo() {
        if (!RobotOnlineUpgradeApi.get().isConnectUpgradeService()) {
            ToastUtil.showMessage("升级服务器未连接，请先连接上服务器");
            return;
        }

        RobotOnlineUpgradeApi.get().reportMcuVersionsInfo(robotCode, "", hardwareInfoList,
                new ReportRobotVersionsInfoListener() {
                    @Override
                    public void onReportRobotVersionsInfo(ReportRobotRes reportRobotRes) {
                        Log.i(TAG, "onReportRobotVersionsInfo: " + JSON.toJSONString(reportRobotRes));
                        ToastUtil.showMessage(JSON.toJSONString(reportRobotRes));
                    }
                });
    }

    //TODO 0、发送升级指令。
    public void sendUpgradeCmd(View view) {
        if (!RobotOnlineUpgradeApi.get().isConnectUpgradeService()) {
            ToastUtil.showMessage("升级服务器未连接，请先连接上服务器");
            return;
        }
        if (robotUpdateInfo == null) {
            ToastUtil.showMessage("请先获取升级版本信息");
            return;
        }
        RobotUpdateInfo.RowsBean rowsBean = robotUpdateInfo.getRows().get(0);
        Log.i(TAG, "sendUpgradeCmd: id = " + rowsBean.getId());
        RobotOnlineUpgradeApi.get().sendUpgradeCmd(MessageDefine.RequestCmd.ROBOT_UPGRADE_MCU_CMD, rowsBean.getId(),
                rowsBean.getNewversion(), new RobotUpgradeStatusListener() {
                    @Override
                    public void onRobotUpgrade(RobotUpgradeRes upgradeCmdRes) {
                        Log.i(TAG, "onRobotUpgrade: " + JSON.toJSONString(upgradeCmdRes));
                        if (upgradeCmdRes.getResponseCode() == 1) {
                            //TODO 1、可以升级，接受升级数据
                            ToastUtil.showMessage("可以升级，接受升级数据");
                        } else {
                            Log.e(TAG, "onRobotUpgrade: " + upgradeCmdRes.getMsg());
                            ToastUtil.showMessage(upgradeCmdRes.getMsg());
                        }
                    }
                });
    }

    @Override
    public void onMcuUpgradePush(RobotUpdatePushInfo robotUpdatePushInfo) {

        Log.i(TAG, "onMcuUpgradePush: " + JSON.toJSONString(robotUpdatePushInfo));
        //TODO 2、发送客户端的升级响应
        RobotOnlineUpgradeApi.get().sendAnswerToUpgradePush(ROBOT_UPGRADE_MCU_CMD, 1, "成功", robotUpdatePushInfo.getId(),
                robotUpdatePushInfo.getSocketId(), robotUpdatePushInfo.getVersion());
        //TODO 3、处理推送过来的数据，RobotUpdatePushInfo fileStr(base64解析成bin文件)，校验

        //TODO 4、调用MCU升级接口，安卓固件的话，调用固件差分升级API。

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isFinishing()) {
            try {
                RobotOnlineUpgradeApi.get().unregisterMcuUpgradePushListener(this);
                RobotOnlineUpgradeApi.get().unregisterAndroidUpgradePushListener(this);
                RobotOnlineUpgradeApi.get().disconnectUpgradeService();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onAndroidUpgradePush(ReportAndroidRes reportAndroidRes) {
        RobotOnlineUpgradeApi.get().sendAnswerToUpgradePush(ROBOT_UPGRADE_MCU_CMD, 1, "成功", reportAndroidRes.getId(),
                reportAndroidRes.getSocketId(), reportAndroidRes.getSoftNewVersion());
        // TODO: 2019/3/7 下载固件；
        // TODO: 2019/3/7 调用安卓系统升级API升级固件
    }
}
