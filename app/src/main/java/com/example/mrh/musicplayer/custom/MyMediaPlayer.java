package com.example.mrh.musicplayer.custom;

import android.media.MediaPlayer;

import com.example.mrh.musicplayer.domain.PlayList;

/**
 * Created by MR.H on 2016/12/13 0013.
 */

public class MyMediaPlayer extends MediaPlayer{
    private static final String TAG = "MyMediaPlayer";
    public static final int START = 0X01;
    public static final int STOP = 0X02;
    public static final int PAUSE = 0X03;
    public static final int NEXT = 0X04;
    public static final int PREVIOUS = 0X05;
    public static final int QUIT = 0X06;
    public static final int RESET = 0X07;
    public static final int PLAY_RESET = 0X08;

    public boolean isStart = false; //是否播放
    private PlayList playList; //播放信息

    public MyMediaPlayer () {
        super();
    }

    //设置播放信息
    public PlayList getPlayList () {
        return playList;
    }

    public void setPlayList (PlayList playList) {
        this.playList = playList;
    }
}
