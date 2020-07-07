package com.bfcy.blogdemo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class BaseActivity extends AppCompatActivity {

    public String TAG = this.getClass().getSimpleName(); //用于打印日志的类名

    private static final int RUNTIME_PERMISSION_REQUEST_CODE = 1; //运行时权限请求码
    private RuntimePermissionListener mRuntimePermissionListener;   //运行时权限回调接口
    /**
     * 检查运行时权限，如果权限全部已经授权，执行监听器的全部授权方法，如果有未授权的权限，去申请未获得的权限。
     * @param permissions               要申请的权限数组
     * @param runtimePermissionListener 运行时权限监听器
     */
    public void checkRuntimePermission(String[] permissions, RuntimePermissionListener runtimePermissionListener) {

        //版本小于android 6.0 不需要运行时权限
        if (Build.VERSION.SDK_INT< Build.VERSION_CODES.M) {
            return;
        }
        mRuntimePermissionListener = runtimePermissionListener;
        List<String> deniedPermissionList = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {//判断权限是否允许
                deniedPermissionList.add(permission);
            }
        }
        if (deniedPermissionList.isEmpty()) {
            //权限全部允许
            mRuntimePermissionListener.onRuntimePermissionGranted();
        } else {
            String[] deniedPermissionArray = deniedPermissionList.toArray(new String[deniedPermissionList.size()]);
            //请求未允许的权限
            ActivityCompat.requestPermissions(this, deniedPermissionArray, RUNTIME_PERMISSION_REQUEST_CODE);
        }
    }
    //申请运行时权限结果回调。调用运行时权限监听的方法
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case RUNTIME_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {
                    List<String> deniedPermissionList = new ArrayList<>();
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            deniedPermissionList.add(permissions[i]);
                        }
                    }
                    if (deniedPermissionList.isEmpty()) { //权限全部允许
                        mRuntimePermissionListener.onRuntimePermissionGranted();
                    } else {//有拒绝的权限
                        mRuntimePermissionListener.onRuntimePermissionDenied();
                    }
                }
        }
    }
    /**
     * 运行时权限监听器，用来处理权限申请成功和失败的业务逻辑。
     */
    public interface RuntimePermissionListener {
        /**
         * 允许所请求的全部权限
         */
        void onRuntimePermissionGranted();

        /**
         * 拒绝所请求的部分或全部权限
         */
        void onRuntimePermissionDenied();
    }

    //检查是否勾选不再提醒,待研究
    //华为手机没有勾选也返回false，但是可以弹出请求界面，勾选后还是返回false，不能弹出请求界面
    //所以不用判断，直接去请求授权。
    private boolean show(String permission) {
        return ActivityCompat.shouldShowRequestPermissionRationale(this, permission);
    }

    //页面跳转方法
    public void jumpTo(Class<? extends Activity> clz) {
        Intent intent = new Intent(this,clz);
        startActivity(intent);
    }
    public void jumpTo(Class<? extends Activity> clz, Bundle bundle) {
        Intent intent = new Intent(this,clz);
        intent.putExtras(bundle);
        startActivity(intent);
    }
    public void jumpToForResult(Class<? extends Activity> clz, int requestCode) {
        Intent intent = new Intent(this,clz);
        startActivityForResult(intent,requestCode);
    }
    @SuppressLint("RestrictedApi")
    public void jumpToForResult(Class<? extends Activity> clz, int requestCode, Bundle bundle) {
        Intent intent = new Intent(this,clz);
        this.startActivityForResult(intent,requestCode,bundle);
    }

    /**
     * 检测网络是否连接
     */
    public boolean isNetConnected(){
//        return NetUtil.isNetConnect(this);
        return false;
    }

//**************************************************   非常用方法   **************************************************//

    /**
     * 设置屏幕只能竖屏
     */
    public void setActivityState(Activity activity) {
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

}
