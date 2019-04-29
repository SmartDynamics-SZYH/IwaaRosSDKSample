package com.szyh.iwaarossdksample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;

import com.leon.lfilepickerlibrary.LFilePicker;
import com.szyh.iwaarossdksample.util.FileIOUtils;
import com.szyh.iwaarossdksample.util.ParseUtility2;
import com.szyh.iwaarossdksample.util.ToastUtil;
import com.szyh.iwaasdk.sdk.ros.RobotRosApi;
import com.szyh.iwaasdk.sdk.ros.define.RosDefine;
import com.szyh.iwaasdk.sdk.ros.interfaces.McuUpdateListener;

import java.util.List;

public class MCUUpdateActivity extends AppCompatActivity implements McuUpdateListener {

    private static final String TAG = "MCUUpdateActivity";

    TextView binPathText;
    TextView updateInfoText;
    Button selectBtn;
    Button updateBtn;
    ImageButton closeBtn;
    ScrollView svResult;

    private boolean isMcuBinVerify = false;

    static SparseArray<String> sparseArray = new SparseArray<>();

    static {
        sparseArray.append(RosDefine.McuBinType.MAIN_BROAD, "主控板");
        sparseArray.append(RosDefine.McuBinType.PWR_BOARD, "电源管理板");
        sparseArray.append(RosDefine.McuBinType.BATTERY_BOARD, "电源管理板");
        sparseArray.append(RosDefine.McuBinType.LEFT_WHEEL, "底盘左");
        sparseArray.append(RosDefine.McuBinType.RIGHT_WHEEL, "底盘右");
        sparseArray.append(RosDefine.McuBinType.ULTRASONIC_BOARD1, "超声波1");
        sparseArray.append(RosDefine.McuBinType.ULTRASONIC_BOARD2, "超声波2");
        sparseArray.append(RosDefine.McuBinType.RUDDER_BROAD, "舵机板");
    }

    private void fileChoose() {
        new LFilePicker()
                .withActivity(this)
//                .withStartPath("/mnt/usb_storage/")
                .withStartPath("/sdcard/FwBinFile/")
                .withIsGreater(false)
                .withMutilyMode(false)
                .withFileFilter(new String[]{".bin"})
                .withFileSize(500 * 1024)
                .start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mcuupdate);
        binPathText = findViewById(R.id.id_text_bin_path);
        updateInfoText = findViewById(R.id.id_text_update_info);
        selectBtn = findViewById(R.id.id_btn_select_bin);
        updateBtn = findViewById(R.id.id_btn_update_bin);
        closeBtn = findViewById(R.id.id_finish_btn);
        svResult = findViewById(R.id.id_update_scroll);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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
                loadMcuFile(path);
            }
        }
    }

    int moduleAddress;

    String path;

    public void loadMcuFile(String path) {
        this.path = path;
        binPathText.setText(path);
        byte[] binDatas = FileIOUtils.readFile2BytesByStream(path);
        if (binDatas != null) {
            moduleAddress = (binDatas[9] & 0xFF);
            isMcuBinVerify = ParseUtility2.get24Int(binDatas, 6) == 115200;
            refreshMsg("mcu bin文件验证通过，可以开始一键升级");
            refreshMsg("文件类型：0x" + ParseUtility2.byteToHexString(binDatas[9]) + ","
                    + sparseArray.get(moduleAddress));
        } else {
            refreshMsg("mcu 数据流为空，无法升级。");
        }
    }

    void refreshMsg(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (updateInfoText != null) {
                    updateInfoText.append(msg);
                    updateInfoText.append("\n");
                }
                if (svResult != null) {
                    svResult.fullScroll(ScrollView.FOCUS_DOWN);
                }
            }
        });
    }


    @Override
    public void onMcuUpdatePrepare() {
        refreshMsg(sparseArray.get(moduleAddress) + "准备开始升级：");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                selectBtn.setClickable(false);
                updateBtn.setClickable(false);
                closeBtn.setClickable(false);
            }
        });
    }

    @Override
    public void onMcuUpdateProcess(int progress) {
        refreshMsg(sparseArray.get(moduleAddress) + "升级进度：" + progress + "%");
    }

    @Override
    public void onMcuUpdateComplete() {
        refreshMsg("恭喜你，" + sparseArray.get(moduleAddress) + "升级成功！");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                selectBtn.setClickable(true);
                updateBtn.setClickable(true);
                closeBtn.setClickable(true);
            }
        });
    }

    @Override
    public void onMcuUpdateError(int errorCode) {
        refreshMsg(sparseArray.get(moduleAddress) + "升级出错，错误码：" + errorCode);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                selectBtn.setClickable(true);
                updateBtn.setClickable(true);
                closeBtn.setClickable(true);
            }
        });
    }

    public void selectBinFile(View view) {
        fileChoose();
    }

    public void updateBin(View view) {
        if (isMcuBinVerify) {
            RobotRosApi.get().updateMcuByBinType(moduleAddress, path, this);
        }
    }

    public void closeUpdate(View view) {
        finish();
    }
}
