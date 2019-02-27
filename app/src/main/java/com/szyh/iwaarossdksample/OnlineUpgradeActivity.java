package com.szyh.iwaarossdksample;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.szyh.iwaarossdksample.util.ToastUtil;
import com.szyh.iwaasdk.sdk.upgrade.RobotOnlineUpgradeApi;
import com.szyh.iwaasdk.sdk.upgrade.bean.OnlineDeviceVersion;
import com.szyh.iwaasdk.sdk.upgrade.bean.ReportRobotRes;
import com.szyh.iwaasdk.sdk.upgrade.bean.RobotUpdateInfo;
import com.szyh.iwaasdk.sdk.upgrade.bean.RobotUpdatePushInfo;
import com.szyh.iwaasdk.sdk.upgrade.bean.RobotUpgradeRes;
import com.szyh.iwaasdk.sdk.upgrade.interfaces.ConnectUpgradeCallback;
import com.szyh.iwaasdk.sdk.upgrade.interfaces.ReportRobotVersionsInfoListener;
import com.szyh.iwaasdk.sdk.upgrade.interfaces.RobotUpdateInfoListener;
import com.szyh.iwaasdk.sdk.upgrade.interfaces.RobotUpgradePushListener;
import com.szyh.iwaasdk.sdk.upgrade.interfaces.RobotUpgradeStatusListener;

import java.util.ArrayList;
import java.util.List;

public class OnlineUpgradeActivity extends AppCompatActivity implements RobotUpgradePushListener {

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

        RobotOnlineUpgradeApi.get().registerRobotUpgradePushListener(this);
    }

    public void queryAllUpgradeInfo(View view) {
        RobotOnlineUpgradeApi.get().queryUpdateDataInfo(robotCode, new RobotUpdateInfoListener() {
            @Override
            public void onRobotUpdateInfoResponse(RobotUpdateInfo robotUpdateInfo) {
                OnlineUpgradeActivity.this.robotUpdateInfo = robotUpdateInfo;
                ToastUtil.showMessage(JSON.toJSONString(robotUpdateInfo));
            }

            @Override
            public void onRobotUpdateInfoFail(Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void connectUpgradeService(View view) {
        RobotOnlineUpgradeApi.get().connectUpgradeService(new ConnectUpgradeCallback() {
            @Override
            public void onConnectUpgradeOpen() {
                Log.i(TAG, "onConnectUpgradeOpen: ");
                ToastUtil.showMessage("升级服务器连接成功！");
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

        if (!RobotOnlineUpgradeApi.get().isConnectUpgradeService()) {
            ToastUtil.showMessage("升级服务器未连接，请先连接上服务器");
            return;
        }

        RobotOnlineUpgradeApi.get().reportRobotVersionsInfo(robotCode, "", hardwareInfoList,
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
        RobotOnlineUpgradeApi.get().sendUpgradeCmd(Integer.parseInt(robotUpdateInfo.getRows().get(0).getId()),
                "1.0.1", new RobotUpgradeStatusListener() {
                    @Override
                    public void onRobotUpgrade(RobotUpgradeRes upgradeCmdRes) {
                        if (upgradeCmdRes.getResponseCode() == 1) {
                            //TODO 1、可以升级，接受升级数据
                        }
                    }
                });
    }

    @Override
    public void onRobotUpgradePush(RobotUpdatePushInfo robotUpdatePushInfo) {

        //TODO 2、发送客户端的升级响应
        RobotOnlineUpgradeApi.get().sendAnswerToUpgradePush(1, "成功", robotUpdatePushInfo.getId(),
                robotUpdatePushInfo.getSocketId(), robotUpdatePushInfo.getVersion());

        //TODO 3、处理推送过来的数据，RobotUpdatePushInfo fileStr(base64解析成bin文件/url就直接下载)，校验

        //TODO 4、调用MCU升级接口，安卓固件的话，调用固件差分升级API。

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isFinishing()) {
            RobotOnlineUpgradeApi.get().unregisterRobotUpgradePushListener(this);
            RobotOnlineUpgradeApi.get().disconnectUpgradeService();
        }
    }
}
