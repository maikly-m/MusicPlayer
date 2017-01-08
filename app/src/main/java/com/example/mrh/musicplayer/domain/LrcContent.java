package com.example.mrh.musicplayer.domain;

/**
 * Created by MR.H on 2017/1/8 0008.
 */

public class LrcContent {
    private String Lrc;
    private Integer Lrc_time;

    public LrcContent () {
    }

    public LrcContent (String lrc, Integer lrc_time) {
        Lrc = lrc;
        Lrc_time = lrc_time;
    }

    public String getLrc () {
        return Lrc;
    }

    public void setLrc (String lrc) {
        Lrc = lrc;
    }

    public Integer getLrc_time () {
        return Lrc_time;
    }

    public void setLrc_time (Integer lrc_time) {
        Lrc_time = lrc_time;
    }
}
