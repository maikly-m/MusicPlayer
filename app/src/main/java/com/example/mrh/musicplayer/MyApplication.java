package com.example.mrh.musicplayer;

import android.app.Application;
import android.content.Context;

import com.example.mrh.musicplayer.utils.DebugUtils;

/**
 * Created by MR.H on 2016/12/2 0002.
 */

public class MyApplication extends Application {

    public static Context mApplicationContext;

    @Override
    public void onCreate () {
        super.onCreate();
        mApplicationContext = getApplicationContext();
        //开启debug模式
        DebugUtils.setDebug(true);
    }


}
