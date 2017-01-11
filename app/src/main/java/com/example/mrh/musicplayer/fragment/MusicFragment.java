package com.example.mrh.musicplayer.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.mrh.musicplayer.R;
import com.example.mrh.musicplayer.constant.Constant;

/**
 * Created by MR.H on 2016/12/3 0003.
 */

public class MusicFragment extends BaseFragment implements View.OnClickListener {
    private static final String TAG = "MusicFragment";
    private View mView;
    private LinearLayout mLlMusic;
    private LinearLayout mLlDownload;
    private LinearLayout mLlLately;
    private LinearLayout mLlLike;
    private LinearLayout mLlAll;

    public MusicFragment () {
        super();
    }

    @Nullable
    @Override
    public View onCreateView (LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        if (mView == null){
            mView = inflater.inflate(R.layout.fragment_music, null);
            initView();
        }
        return mView;
    }

    private void initView () {
        mLlMusic = (LinearLayout) mView.findViewById(R.id.ll_music);
        mLlMusic.setOnClickListener(this);
        mLlDownload = (LinearLayout) mView.findViewById(R.id.ll_download);
        mLlDownload.setOnClickListener(this);
        mLlLately = (LinearLayout) mView.findViewById(R.id.ll_lately);
        mLlLately.setOnClickListener(this);
        mLlLike = (LinearLayout) mView.findViewById(R.id.ll_like);
        mLlLike.setOnClickListener(this);
        mLlAll = (LinearLayout) mView.findViewById(R.id.ll_all);
        mLlAll.setOnClickListener(this);
    }

    @Override
    public void onClick (View v) {
        switch (v.getId()){
        case R.id.ll_like:
            addFragment(R.id.fl_main, SongsListFragment.newInstance(Constant.MUSIC_LIST_CUSTOM_ +
            Constant.CUSTOM_LIST_LOVE),
                    Constant.MUSIC_LIST_CUSTOM_ + Constant.CUSTOM_LIST_LOVE);
            break;
        case R.id.ll_music:
            addFragment(R.id.fl_main, new MusicListFragment(), "MusicListFragment");
            break;
        case R.id.ll_download:

            break;
        case R.id.ll_lately:

            break;
        case R.id.ll_all:
            addFragment(R.id.fl_main, new AllMusicFragment(), "AllMusicFragment");
            break;
        }
    }

}
