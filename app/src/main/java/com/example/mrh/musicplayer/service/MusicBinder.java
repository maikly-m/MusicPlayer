package com.example.mrh.musicplayer.service;

import android.os.Binder;

/**
 * Created by MR.H on 2016/12/2 0002.
 */

public class MusicBinder extends Binder{
    private static final String TAG = "MusicBinder";
    public PlaySevice mPlayer;
    MusicBinder(PlaySevice service ){
        mPlayer = service;
    }
}
