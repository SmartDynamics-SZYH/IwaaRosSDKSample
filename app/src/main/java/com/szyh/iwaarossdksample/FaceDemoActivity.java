package com.szyh.iwaarossdksample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.szyh.iwaarossdksample.util.ToastUtil;
import com.szyh.iwaasdk.sdk.face.RobotFaceApi;
import com.szyh.iwaasdk.sdk.face.bean.FaceBean;
import com.szyh.iwaasdk.sdk.face.bean.LoginBean;
import com.szyh.iwaasdk.sdk.face.bean.PadLoginBean;
import com.szyh.iwaasdk.sdk.face.bean.ScreenBean;
import com.szyh.iwaasdk.sdk.face.bean.SubjectBean;
import com.szyh.iwaasdk.sdk.face.exception.KoalaCamException;
import com.szyh.iwaasdk.sdk.face.interfaces.AllScreensCallback;
import com.szyh.iwaasdk.sdk.face.interfaces.AuthLoginCallback;
import com.szyh.iwaasdk.sdk.face.interfaces.FaceRecognizeCallback;
import com.szyh.iwaasdk.sdk.face.interfaces.PadLoginCallback;
import com.szyh.iwaasdk.sdk.face.interfaces.SubjectCallback;

import java.util.List;

public class FaceDemoActivity extends AppCompatActivity {
    private static final String TAG = "FaceDemoActivity";
    private String userName = "776878676@163.com";//17Y007-14
    private String pwd = "123456";

    private int userId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_demo);
        RobotFaceApi.get().init("192.168.5.50");
    }

    public void authUserLogin(View view) {
        RobotFaceApi.get().authLogin(userName, pwd, new AuthLoginCallback() {
            @Override
            public void onAuthSuccess(LoginBean loginBean) {
                Log.i(TAG, "onAuthSuccess: ");
                ToastUtil.showMessage("onAuthSuccess");
            }

            @Override
            public void onAuthFail(KoalaCamException exception) {
                ToastUtil.showMessage("onAuthFail: 错误码：" + exception.getCode() + ",描述：" + exception.getDescription());
                Log.i(TAG, "onAuthFail: 错误码：" + exception.getCode() + ",描述：" + exception.getDescription());
            }
        });
    }

    public void createUser(View view) {
        SubjectBean subjectBean = new SubjectBean();
        subjectBean.setSubject_type(0);
        subjectBean.setName("安卓Test");
        RobotFaceApi.get().createSubject(subjectBean, new SubjectCallback() {
            @Override
            public void onSubjectSuccess(SubjectBean subjectBean) {
                userId = subjectBean.getId();
                Log.i(TAG, "onSubjectSuccess: subjectBean.getId() = " + userId);//349
                ToastUtil.showMessage("createUser onSubjectSuccess subjectBean.getId() = " + userId);
            }

            @Override
            public void onSubjectFail(KoalaCamException exception) {
                ToastUtil.showMessage("createUser onSubjectFail: 错误码：" + exception.getCode() + ",描述：" + exception.getDescription());
                Log.i(TAG, "createUser: 错误码：" + exception.getCode() + ",描述：" + exception.getDescription());
            }
        });
    }

    public void deleteUser(View view) {
        RobotFaceApi.get().deleteSubject(userId, new SubjectCallback() {
            @Override
            public void onSubjectSuccess(SubjectBean subjectBean) {
                Log.i(TAG, "onSubjectSuccess: subjectBean.getId() = " + subjectBean.getId());//349
                ToastUtil.showMessage("deleteUser onSubjectSuccess subjectBean.getId() = " + subjectBean.getId());
            }

            @Override
            public void onSubjectFail(KoalaCamException exception) {
                ToastUtil.showMessage("deleteUser onSubjectFail: 错误码：" + exception.getCode() + ",描述：" + exception.getDescription());
                Log.i(TAG, "deleteUser: 错误码：" + exception.getCode() + ",描述：" + exception.getDescription());
            }
        });
    }

    public void findAllScreens(View view) {
        RobotFaceApi.get().findAllScreens(new AllScreensCallback() {
            @Override
            public void onAllScreensSuccess(List<ScreenBean> screenBeans) {
                ToastUtil.showMessage("onAllScreensSuccess size = " + screenBeans.size());
            }

            @Override
            public void onAllScreenFail(KoalaCamException exception) {
                Log.i(TAG, "onAllScreenFail: 错误码：" + exception.getCode() + ",描述：" + exception.getDescription());
            }
        });
    }

    public void padLogin(View view) {
        RobotFaceApi.get().padLogin(userName, pwd, "12232143423123", new PadLoginCallback() {
            @Override
            public void onPadLoginSuccess(PadLoginBean padLoginBean) {
                ToastUtil.showMessage("onPadLoginSuccess usrName  = " + padLoginBean.getUsername());
            }

            @Override
            public void onPadLoginFail(KoalaCamException exception) {
                Log.i(TAG, "onPadLoginFail: 错误码：" + exception.getCode() + ",描述：" + exception.getDescription());
            }
        });
    }

    private boolean isFaceRecognizing = false;

    public void startFaceService(View view) {
        RobotFaceApi.get().startRecognizeService(new FaceRecognizeCallback() {
            @Override
            public void onFaceRecognizing(FaceBean faceBean) {
                //  正在识别中
                if (!isFaceRecognizing) {
                    isFaceRecognizing = true;
                    Log.i(TAG, "onFaceRecognizing: 正在识别中 ");
                    ToastUtil.showMessage("onFaceRecognizing: 正在识别中");
                }
            }

            @Override
            public void onFaceRecognized(FaceBean faceBean) {
                // 识别到员工
                if (isFaceRecognizing) {
                    isFaceRecognizing = false;
                    Log.i(TAG, "onFaceRecognized: 识别员工，name =  " + faceBean.getPerson().getName());
                    ToastUtil.showMessage("onFaceRecognized: 识别员工，name =  " + faceBean.getPerson().getName());
                }
            }

            @Override
            public void onFaceUnrecognized(FaceBean faceBean) {
                // 识别到陌生人
                if (isFaceRecognizing) {
                    isFaceRecognizing = false;
                    Log.i(TAG, "onFaceUnrecognized: 陌生人");
                    ToastUtil.showMessage("onFaceUnrecognized: 陌生人");
                }
            }

            @Override
            public void onFaceGone(FaceBean faceBean) {
                // 人脸框消失
                Log.i(TAG, "onFaceGone: 人脸框消失");
                ToastUtil.showMessage("onFaceGone: 人脸框消失");
            }
        });
    }

    public void stopFaceService(View view) {
        RobotFaceApi.get().stopRecognizeService();
    }
}
