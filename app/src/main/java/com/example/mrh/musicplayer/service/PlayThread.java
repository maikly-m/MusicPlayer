package com.example.mrh.musicplayer.service;

import android.os.HandlerThread;

import com.example.mrh.musicplayer.utils.DebugUtils;

/**
 * Created by MR.H on 2016/12/2 0002.
 */

public class PlayThread extends HandlerThread{
    private static final String TAG = "PlayThread";

    public PlayThread (String name) {
        super(name);
    }

    public PlayThread (String name, int priority) {
        super(name, priority);
    }

    @Override
    public void run () {
        super.run();
        DebugUtils.log_d(TAG, "PlayThread end");
    }
}
