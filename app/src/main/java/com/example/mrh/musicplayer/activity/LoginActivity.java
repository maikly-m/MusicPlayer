package com.example.mrh.musicplayer.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.mrh.musicplayer.R;
import com.example.mrh.musicplayer.constant.Constant;
import com.example.mrh.musicplayer.service.PlaySevice;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MR.H on 2016/12/2 0002.
 */

public class LoginActivity extends BaseActivity {
    private static final String TAG = "LoginActivity";

    private ImageView ivLogin;
    private static final int MY_PERMISSION = 0x00;

    @Override
    protected void onCreate (@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setContentView(R.layout.activity_login);
        initView();
        checkPermission();
    }

    private void checkPermission () {
        //开启权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            List<String> permissionsList = new ArrayList<>();
            final List<String> permissionsNeeded = new ArrayList<>();
            addPermission(permissionsList, permissionsNeeded, Manifest.permission
                    .WRITE_EXTERNAL_STORAGE);
            addPermission(permissionsList, permissionsNeeded, Manifest.permission
                    .RECORD_AUDIO);
            if (permissionsList.size() > 0) {
                String[] strings = permissionsList.toArray(new String[permissionsList
                        .size()]);
                requestPermissions(strings, MY_PERMISSION);
            } else if (permissionsNeeded.size() > 0){
                showMessageOKCancel("需要授权后才可以打开",
                        new DialogInterface.OnClickListener() {
                            @RequiresApi(api = Build.VERSION_CODES.M)
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String[] strings = permissionsNeeded.toArray(new String[permissionsNeeded
                                        .size()]);
                                requestPermissions(strings, MY_PERMISSION);
                            }
                        });
            } else {
                initService();
            }
        }else {
            initService();
        }
    }
    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(LoginActivity.this)
                .setMessage(message)
                .setPositiveButton("确定", okListener)
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick (DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .create()
                .show();
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void addPermission (List<String> permissionsList, List<String> permissionsNeeded,
                                String permission) {
        if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
            if (shouldShowRequestPermissionRationale(permission)){
                //拒绝授权
                permissionsNeeded.add(permission);
            }else {
                //第一次
                permissionsList.add(permission);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult (int requestCode, @NonNull String[] permissions,
                                            @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION:
                if (grantResults.length > 0) {
                    boolean grant = true;
                    for (int i = 0; i < grantResults.length; i++){
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED){
                            grant = false;
                            break;
                        }
                    }
                    if (grant){
                        //授权后开启服务
                        initService();
                    }else {
                        Toast.makeText(LoginActivity.this, "未授权，应用打开失败", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }else {
                    Toast.makeText(LoginActivity.this, "未授权，应用打开失败", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
                break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onDestroy () {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void init () {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run () {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 500);
    }
    //开启服务
    private void initService () {
        Intent intent = new Intent(LoginActivity.this, PlaySevice.class);
        intent.setAction("com.example.mrh.musicplayer.service.PlaySevice");
        startService(intent);

    }

    private void initView () {
        ivLogin = (ImageView) findViewById(R.id.iv_login);
    }
    /**
     * eventBus 处理的方法
     *
     * @param flag
     */
    @Subscribe(threadMode = ThreadMode.MAIN)       //主线程标识
    public void onEventMainThread (String flag) {
        if (flag.equals(Constant.OK_DATA)){
            init();
        }
    }
}
