package com.szyh.iwaarossdksample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.szyh.iwaarossdksample.util.ToastUtil;
import com.szyh.iwaarossdksample.util.UiUtil;
import com.szyh.iwaasdk.sdk.navi.RobotNaviApi;
import com.szyh.iwaasdk.sdk.navi.define.NaviDefine;
import com.szyh.iwaasdk.sdk.navi.interfaces.AddMapPlanCallback;
import com.szyh.iwaasdk.sdk.navi.interfaces.AllMapPlanCallback;
import com.szyh.iwaasdk.sdk.navi.interfaces.ConnectNaviCallback;
import com.szyh.iwaasdk.sdk.navi.interfaces.CreateMapCallback;
import com.szyh.iwaasdk.sdk.navi.interfaces.DefaultCallback;
import com.szyh.iwaasdk.sdk.navi.interfaces.MapInfoCallback;
import com.szyh.iwaasdk.sdk.navi.interfaces.RobotStatusListener;
import com.szyh.iwaasdk.sdk.navi.interfaces.UploadMappingListener;
import com.szyh.iwaasdk.sdk.navi.websocket.bean.AllMapPlanResponse;
import com.szyh.iwaasdk.sdk.navi.websocket.bean.ElectricFenceLineBean;
import com.szyh.iwaasdk.sdk.navi.websocket.bean.ElectricFenceRectBean;
import com.szyh.iwaasdk.sdk.navi.websocket.bean.MapBean;
import com.szyh.iwaasdk.sdk.navi.websocket.bean.MapByNameResponse;
import com.szyh.iwaasdk.sdk.navi.websocket.bean.MapPoint;
import com.szyh.iwaasdk.sdk.navi.websocket.bean.MotionStatusResponse;
import com.szyh.iwaasdk.sdk.navi.websocket.bean.NavigationStatusResponse;
import com.szyh.iwaasdk.sdk.navi.websocket.bean.SensorStatusResponse;
import com.szyh.iwaasdk.sdk.navi.websocket.bean.UploadMappingResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Navi demo activity.
 */
public class NaviDemoActivity extends AppCompatActivity implements RobotStatusListener {

    private static final String TAG = "RosDemoActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navi_demo);
        RobotNaviApi.get().addRobotStatusListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isFinishing()) {
            RobotNaviApi.get().removeRobotStatusListener(this);
        }
    }

    public void connectNaviService(View view) {

        RobotNaviApi.get().connectNaviService(true, new ConnectNaviCallback() {
            @Override
            public void onConnectNaviOpen() {
                ToastUtil.showMessage("与工控机连接成功");
            }

            @Override
            public void onConnectNaviClose(int code, String reason, boolean remote) {
                Log.e(TAG, "onConnectNaviClose: code =" + code + ",reason = " + reason + ",remote = " + remote);
                ToastUtil.showMessage("与工控机断开连接");
            }

            @Override
            public void onConnectNaviError(Exception e) {
                Log.e(TAG, "onConnectNaviError: e = " + e.toString());
                ToastUtil.showMessage("与工控机连接出错");
            }
        });
    }

    public void disconnectNaviService(View view) {
        RobotNaviApi.get().destroy();
    }

    boolean isFreeMode = false;

    public void gotoFreeMode(View view) {
        //1 手动 2 自由 3导航  4 充电 5 电机释放
        isFreeMode = !isFreeMode;
        RobotNaviApi.get().changeMode(isFreeMode ? NaviDefine.RobotMode.FREE_MODE :
                NaviDefine.RobotMode.MANUAL_MODE);
        ToastUtil.showMessage(isFreeMode ? "进入自由模式" : "进入手动模式");
    }

    public void controlTo(View view) {
        RobotNaviApi.get().controlTo(30, 30);
        UiUtil.post(new Runnable() {
            @Override
            public void run() {
                RobotNaviApi.get().controlTo(0, 0);
            }
        }, 3000);
    }

    public void rotateTo(View view) {
        RobotNaviApi.get().rotateTo(180, 50, NaviDefine.RotateDirection.LEFT);
    }

    String base64MapName = "";
    String base64MapData = "";

    public void syncAllMapPlanData(View view) {
        RobotNaviApi.get().syncAllMapPlanData(new AllMapPlanCallback() {
            @Override
            public void onSyncAllMapPlanSuccess(AllMapPlanResponse allMapPlanResponse) {
                MapBean mapBean = allMapPlanResponse.getMapPlanList().get(0).getMapList().get(0);
                base64MapName = mapBean.getMapName();
                Log.i(TAG, "onSyncAllMapPlanSuccess: base64MapName = " + base64MapName);
                ToastUtil.showMessage("获取图片成功：" + base64MapName);
            }

            @Override
            public void onSyncAllMapPlanFail(int errorCode) {
                ToastUtil.showMessage("获取图片失败：" + errorCode);
            }
        });
    }

    public void getMapInfoByName(View view) {
        RobotNaviApi.get().getMapInfoByName(base64MapName, new MapInfoCallback() {
            @Override
            public void onMapInfoSuccess(MapByNameResponse mapByNameResponse) {
                base64MapData = mapByNameResponse.getMapData();
            }

            @Override
            public void onMapInfoFail(int errorCode) {

            }
        });
    }


    private int newMapPlanId = -1;

    public void addMapPlan(View view) {
        RobotNaviApi.get().addMapPlan("androidTest", "安卓测试新增", new AddMapPlanCallback() {
            @Override
            public void onAddMapPlanSuccess(int mapPlanId) {
                newMapPlanId = mapPlanId;
                ToastUtil.showMessage("新增地图方案成功：mapPlanId = " + mapPlanId);
            }

            @Override
            public void onAddMapPlanFail(int errorCode) {
                ToastUtil.showMessage("新增地图方案失败：errorCode = " + errorCode);
            }
        });
    }

    public void deleteMapPlan(View view) {
        if (newMapPlanId <= 0) {
            ToastUtil.showMessage("请先新增地图方案成功之后，再删除该地图方案");
            return;
        }
        RobotNaviApi.get().deleteMapPlan(newMapPlanId, new DefaultCallback() {
            @Override
            public void onSuccess() {
                newMapPlanId = -1;
                ToastUtil.showMessage("删除地图方案成功！");
            }

            @Override
            public void onFail(int errorCode) {
                ToastUtil.showMessage("删除地图方案失败：errorCode = " + errorCode);
            }
        });
    }

    private String mapName = "androidTestMap.png";

    private UploadMappingListener uploadMappingListener = new UploadMappingListener() {
        @Override
        public void onMapping(UploadMappingResponse uploadMappingResponse) {
            Log.i(TAG, "onMapping: map = " + uploadMappingResponse.getMapData());
        }
    };

    public void startCreateMap(View view) {
        if (newMapPlanId <= 0) {
            ToastUtil.showMessage("请先新增地图方案成功之后，再新建地图");
            return;
        }
        MapPoint startPoint = new MapPoint(1000, 1000, 0);
        MapPoint offsetPoint = new MapPoint(100, 100, 0);
        RobotNaviApi.get().startCreateMap(newMapPlanId, "androidTestMap", startPoint, offsetPoint, new CreateMapCallback() {

            @Override
            public void onCreateMapSuccess(int mapId) {
                ToastUtil.showMessage("开始建图成功，mapId = " + mapId);
                RobotNaviApi.get().addUploadMappingListener(uploadMappingListener);

            }

            @Override
            public void onCreateMapFail(int errorCode) {
                ToastUtil.showMessage("开始建图失败，errorCode = " + errorCode);
            }
        });
    }

    public void cancelCreateMap(View view) {
        if (newMapPlanId <= 0) {
            ToastUtil.showMessage("请先新增地图方案成功之后，再取消建图");
            return;
        }
        RobotNaviApi.get().cancelCreateMap(newMapPlanId, mapName, new CreateMapCallback() {
            @Override
            public void onCreateMapSuccess(int mapId) {
                ToastUtil.showMessage("取消建图成功，mapId = " + mapId);
                RobotNaviApi.get().removeUploadMappingListener(uploadMappingListener);
            }

            @Override
            public void onCreateMapFail(int errorCode) {
                ToastUtil.showMessage("取消建图失败，errorCode = " + errorCode);
            }
        });
    }

    private int newMapId = -1;

    public void finishCreateMap(View view) {
        if (newMapPlanId <= 0) {
            ToastUtil.showMessage("请先新增地图方案成功之后，再结束建图");
            return;
        }
        RobotNaviApi.get().finishCreateMap(newMapPlanId, mapName, new CreateMapCallback() {
            @Override
            public void onCreateMapSuccess(int mapId) {
                newMapId = mapId;
                ToastUtil.showMessage("完成建图成功，mapId = " + mapId);
                RobotNaviApi.get().removeUploadMappingListener(uploadMappingListener);
            }

            @Override
            public void onCreateMapFail(int errorCode) {
                ToastUtil.showMessage("完成建图失败，errorCode = " + errorCode);
            }
        });
    }


    public void replaceMap(View view) {
        if (TextUtils.isEmpty(base64MapData)) {
            ToastUtil.showMessage("请先同步数据，然后根据地图名字获取。！");
            return;
        }
        RobotNaviApi.get().replaceMap(mapName, base64MapData, new DefaultCallback() {
            @Override
            public void onSuccess() {
                ToastUtil.showMessage("替换地图成功");
            }

            @Override
            public void onFail(int errorCode) {
                ToastUtil.showMessage("替换地图失败，errorCode = " + errorCode);
            }
        });
    }

    @Override
    public void onSensorStatusResponse(SensorStatusResponse sensorStatusResponse) {
        //Logger.json(JSON.toJSONString(sensorStatusResponse));
    }

    @Override
    public void onRobotMotionStatusResponse(MotionStatusResponse motionStatusResponse) {
        //Logger.json(JSON.toJSONString(motionStatusResponse));
    }

    @Override
    public void onRobotNavigationStatusResponse(NavigationStatusResponse navigationStatusResponse) {
        //Logger.json(JSON.toJSONString(navigationStatusResponse));
    }

    public void setDefaultMap(View view) {
        if (newMapId < 0) {
            ToastUtil.showMessage("请先完成建图");
            return;
        }
        RobotNaviApi.get().setDefaultMap(newMapId, new DefaultCallback() {
            @Override
            public void onSuccess() {
                ToastUtil.showMessage("设置默认地图成功！");
            }

            @Override
            public void onFail(int errorCode) {
                ToastUtil.showMessage("设置默认地图失败！，errorCode = " + errorCode);
            }
        });
    }

    public void changeNaviMode(View view) {
        RobotNaviApi.get().setNaviMode(NaviDefine.NaviMode.MODE_UWB, new DefaultCallback() {
            @Override
            public void onSuccess() {
                ToastUtil.showMessage("修改导航模式成功");
            }

            @Override
            public void onFail(int errorCode) {
                ToastUtil.showMessage("修改导航模式失败");
            }
        });
    }

    public void setElectronicFence(View view) {
        List<ElectricFenceLineBean> electricFenceLines = new ArrayList<>();
        ElectricFenceLineBean electricFenceLineBean = new ElectricFenceLineBean();
        electricFenceLineBean.setX1(100f);
        electricFenceLineBean.setY1(100f);
        electricFenceLineBean.setX2(500f);
        electricFenceLineBean.setY2(500f);
        electricFenceLines.add(electricFenceLineBean);

        List<ElectricFenceRectBean> electricFenceRects = new ArrayList<>();
        ElectricFenceRectBean electricFenceRectBean = new ElectricFenceRectBean();
        electricFenceRectBean.setLeft(300f);
        electricFenceRectBean.setTop(345f);
        electricFenceRectBean.setRight(600f);
        electricFenceRectBean.setBottom(600f);
        electricFenceRects.add(electricFenceRectBean);
        RobotNaviApi.get().setElectronicFence(newMapId, electricFenceLines, electricFenceRects, new DefaultCallback() {
            @Override
            public void onSuccess() {
                ToastUtil.showMessage("设置电子围栏成功！");
            }

            @Override
            public void onFail(int errorCode) {
                ToastUtil.showMessage("设置电子围栏失败！");
            }
        });
    }
}
