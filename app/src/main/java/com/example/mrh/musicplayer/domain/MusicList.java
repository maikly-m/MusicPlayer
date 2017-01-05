package com.example.mrh.musicplayer.domain;

/**
 * Created by MR.H on 2016/12/6 0006.
 */

public class MusicList {
    private String listName;

    public MusicList () {
    }

    public MusicList (String listName, int songsCount) {
        this.listName = listName;
    }

    public String getListName () {
        return listName;
    }

    public void setListName (String listName) {
        this.listName = listName;
    }

}
