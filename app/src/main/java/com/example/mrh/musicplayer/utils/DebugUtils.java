package com.example.mrh.musicplayer.utils;

import android.util.Log;

/**
 * Created by MR.H on 2016/12/2 0002.
 */

public class DebugUtils {
    private static boolean debug = false;

    public static void setDebug(boolean isDebug){
        debug = isDebug;
    }
    public static void log_d(String TAG, String info){
        if (debug){
            Log.d(TAG, info);
        }
    }
    public static void log_e(String TAG, String info){
        if (debug){
            Log.e(TAG, info);
        }
    }
    public static void log_i(String TAG, String info){
        if (debug){
            Log.i(TAG, info);
        }
    }
}
