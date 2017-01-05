package com.example.mrh.musicplayer.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.mrh.musicplayer.R;
import com.example.mrh.musicplayer.constant.Constant;
import com.example.mrh.musicplayer.service.PlaySevice;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by MR.H on 2016/12/2 0002.
 */

public class LoginActivity extends BaseActivity {

    private ImageView ivLogin;
    private static final int MY_PERMISSION_WRITE_EXTERNAL_STORAGE = 0;

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
            if (ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission
                    .WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                //未授权
//                if (ActivityCompat.shouldShowRequestPermissionRationale(LoginActivity.this,
//                        Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
//
//                }else {
                    ActivityCompat.requestPermissions(LoginActivity.this, new String[]{Manifest.permission
                            .WRITE_EXTERNAL_STORAGE}, MY_PERMISSION_WRITE_EXTERNAL_STORAGE);
//                }

            }else {
                initService();
            }
        }else {
            initService();
        }
    }

    @Override
    public void onRequestPermissionsResult (int requestCode, @NonNull String[] permissions,
                                            @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_WRITE_EXTERNAL_STORAGE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //授权后开启服务
                    initService();
                } else {
                    Toast.makeText(LoginActivity.this, "未授权，应用打开失败", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            }
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
