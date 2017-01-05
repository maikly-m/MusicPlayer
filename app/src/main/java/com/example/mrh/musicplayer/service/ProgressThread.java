package com.example.mrh.musicplayer.service;

import android.os.HandlerThread;

import com.example.mrh.musicplayer.utils.DebugUtils;

/**
 * Created by MR.H on 2016/12/2 0002.
 */

public class ProgressThread extends HandlerThread{
    private static final String TAG = "ProgressThread";
    public static final int START = 0X00; //进度开始
    public static final int PAUSE = 0X01; //进度停止
    public static final int QUIT = 0X02; //进度停止
    public static boolean flag = false; //进度更新flag

    public ProgressThread (String name) {
        super(name);
    }

    public ProgressThread (String name, int priority) {
        super(name, priority);
    }

    @Override
    public void run () {
        super.run();
        DebugUtils.log_d(TAG, "ProgressThread end");
    }
}
