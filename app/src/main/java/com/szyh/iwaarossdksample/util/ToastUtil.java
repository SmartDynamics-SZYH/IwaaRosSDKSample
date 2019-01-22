package com.szyh.iwaarossdksample.util;

import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.szyh.iwaarossdksample.App;

/**
 * Toast工具类
 */
public class ToastUtil {

    private static Handler handler = new Handler(Looper.getMainLooper());

    private static Toast toast = null;

    private static Object synObj = new Object();

    public static void showMessage(final String msg) {
        showMessage(msg, Toast.LENGTH_SHORT);
    }

    /**
     * 根据设置的文本显示
     *
     * @param msg
     */
    public static void showMessage(final int msg) {  //建议去掉这个方法,有可能传过来的不是资源ID,而是将Int等整型变量作为了参数传过来了,就会抛出Resources NotFoundException
        showMessage(msg, Toast.LENGTH_SHORT);
    }

    /**
     * 显示一个文本并且设置时长
     *
     * @param msg
     * @param len
     */
    public static void showMessage(final CharSequence msg, final int len) {
        if (msg == null || msg.equals("")) {
            return;
        }
        handler.post(new Runnable() {
            @Override
            public void run() {
                synchronized (synObj) { //加上同步是为了每个toast只要有机会显示出来
                    if (toast != null) {
                        //toast.cancel();
                        toast.setText(msg);
                        toast.setDuration(len);
                    } else {
                        toast = Toast.makeText(App.getInstance(), msg, len);
                    }
                    toast.show();
                }
            }
        });
    }

    /**
     * 资源文件方式显示文本
     *
     * @param msgId
     * @param len
     */
    public static void showMessage(final int msgId, final int len) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                synchronized (synObj) {
                    try {
                        if (toast != null) {
                            //toast.cancel();
                            toast.setText(msgId);//有可能传过来的不是资源ID,而是将Int等整型变量作为了参数传过来了
                            toast.setDuration(len);
                        } else {
                            toast = Toast.makeText(App.getInstance()
                                    .getApplicationContext(), msgId, len);
                        }
                        toast.show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
