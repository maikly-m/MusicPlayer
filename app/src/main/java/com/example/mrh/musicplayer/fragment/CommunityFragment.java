package com.example.mrh.musicplayer.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mrh.musicplayer.R;

/**
 * Created by MR.H on 2016/12/3 0003.
 */

public class CommunityFragment extends BaseFragment{
    private static final String TAG = "CommunityFragment";
    public CommunityFragment () {
        super();

    }

    @Nullable
    @Override
    public View onCreateView (LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_community, null);
        return view;
    }
}
