package com.example.mrh.musicplayer.fragment.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.mrh.musicplayer.R;
import com.example.mrh.musicplayer.constant.Constant;
import com.example.mrh.musicplayer.domain.MusicList;
import com.example.mrh.musicplayer.fragment.AllMusicVPFragment;
import com.example.mrh.musicplayer.fragment.viewHolder.AllMusicVPViewHolder;

import java.util.List;

/**
 * Created by MR.H on 2016/12/6 0006.
 */

public class AllMusicVPAdapter extends BaseAdapter {
    private Fragment f;
    private Context context;
    private List<MusicList> list;
    private AlertDialog mDialog;

    public AllMusicVPAdapter (Context context, Fragment f, List<MusicList> list) {
        super();
        this.f = f;
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount () {
        return list.size();
    }

    @Override
    public Object getItem (int position) {
        return null;
    }

    @Override
    public long getItemId (int position) {
        return 0;
    }

    @Override
    public View getView (final int position, View convertView, ViewGroup parent) {
        AllMusicVPViewHolder allMusicVPViewHolder;
        if (convertView == null){
            View rootView = View.inflate(context, R.layout.listview_allmusicvp, null);
            allMusicVPViewHolder = AllMusicVPViewHolder.getViewHolder(rootView);
        } else{
            allMusicVPViewHolder = (AllMusicVPViewHolder) convertView.getTag();
        }
        String listName = list.get(position).getListName();
        String substring = null;
        if (listName.contains(Constant.MUSIC_LIST_ALLSONGS_)){
            substring = listName.substring(Constant.MUSIC_LIST_ALLSONGS_.length());
        } else if (listName.contains(Constant.MUSIC_LIST_CUSTOM_)){
            substring = listName.substring(Constant.MUSIC_LIST_CUSTOM_.length());
        } else if (listName.contains(Constant.MUSIC_LIST_ARTIST_)){
            substring = listName.substring(Constant.MUSIC_LIST_ARTIST_.length());
        } else if (listName.contains(Constant.MUSIC_LIST_ALBUM_)){
            substring = listName.substring(Constant.MUSIC_LIST_ALBUM_.length());
        } else if (listName.contains(Constant.MUSIC_LIST_DATA_)){
            substring = listName.substring(Constant.MUSIC_LIST_DATA_.length());
        }
        allMusicVPViewHolder.mTvAllmusicvp.setText(substring);
        allMusicVPViewHolder.mTvAllmusicvpCount.setText(((AllMusicVPFragment)f).songs
                .get(list.get(position).getListName()).size() + " 首");
        return allMusicVPViewHolder.rootView;
    }
}
