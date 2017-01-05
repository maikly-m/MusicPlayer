package com.example.mrh.musicplayer.domain;

import java.util.List;

/**
 * Created by MR.H on 2016/12/13 0013.
 */

public class PlayList {
    private String listName;
    private List<MusicInfo> list;
    private String playModel;

    public PlayList () {
    }

    public PlayList (String listName, List<MusicInfo> list, String playModel) {
        this.listName = listName;
        this.list = list;
        this.playModel = playModel;
    }

    public void setListName (String listName) {
        this.listName = listName;
    }

    public String getListName () {
        return listName;
    }

    public void setList (List<MusicInfo> list) {
        this.list = list;
    }

    public List<MusicInfo> getList () {
        return list;
    }

    public void setPlayModel (String playModel) {
        this.playModel = playModel;
    }

    public String getPlayModel () {
        return playModel;
    }

}
