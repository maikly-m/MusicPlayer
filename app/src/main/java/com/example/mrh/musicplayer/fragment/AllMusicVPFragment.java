package com.example.mrh.musicplayer.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.mrh.musicplayer.R;
import com.example.mrh.musicplayer.activity.MainActivity;
import com.example.mrh.musicplayer.constant.Constant;
import com.example.mrh.musicplayer.domain.MusicInfo;
import com.example.mrh.musicplayer.domain.MusicList;
import com.example.mrh.musicplayer.fragment.adapter.AllMusicVPAdapter;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by MR.H on 2016/12/3 0003.
 */

public class AllMusicVPFragment extends BaseFragment {
    private static final String TAG = "AllMusicVPFragment";
    private View mRootView;
    private ListView mLvAllmusicvp;
    private ArrayList<MusicList> mList;
    public AllMusicVPAdapter mAdapter;
    public HashMap<String, ArrayList<MusicInfo>> songs;

    public AllMusicVPFragment () {
        super();

    }

    public static AllMusicVPFragment newInstance(String name){
        AllMusicVPFragment fragment = new AllMusicVPFragment();
        Bundle bundle = new Bundle();
        bundle.putString("name", name);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView (LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        if (mRootView == null){
            mRootView = inflater.inflate(R.layout.fragment_allmusicvp, null);
            initView();
        }
        return mRootView;
    }

    private void initView () {
        mLvAllmusicvp = (ListView) mRootView.findViewById(R.id.lv_allmusicvp);
        String name = getArguments().getString("name");
        if (name != null){
            switch (name){
            case Constant.MUSIC_LIST_ARTIST_:
                mList = ((MainActivity) context).list_artist;
                songs = ((MainActivity) context).songs_artist;
                break;
            case Constant.MUSIC_LIST_ALBUM_:
                mList = ((MainActivity) context).list_album;
                songs = ((MainActivity) context).songs_album;
                break;
            case Constant.MUSIC_LIST_DATA_:
                mList = ((MainActivity) context).list_data;
                songs = ((MainActivity) context).songs_data;
                break;
            }
        }

        if (mList != null){
            mAdapter = new AllMusicVPAdapter(context, this, mList);
            mLvAllmusicvp.setAdapter(mAdapter);
            mLvAllmusicvp.setDividerHeight(0);
            mLvAllmusicvp.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick (AdapterView<?> parent, View view, int position, long id) {
                    addFragment(R.id.fl_main, SongsListFragment.newInstance(mList.get(position)
                            .getListName()), mList.get(position)
                            .getListName());
                }
            });
        }
    }
}
