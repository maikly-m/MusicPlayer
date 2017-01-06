package com.example.mrh.musicplayer.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mrh.musicplayer.R;

/**
 * Created by MR.H on 2017/1/6 0006.
 */

public class VisualizerFragment extends BaseFragment {

    private View mRootView;

    public VisualizerFragment () {
        super();
    }
    public static VisualizerFragment newInstance (String name) {
        VisualizerFragment fragment = new VisualizerFragment();
        Bundle bundle = new Bundle();
        bundle.putString("name", name);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate (@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFragmentName(getArguments().getString("name"));
    }

    @Nullable
    @Override
    public View onCreateView (LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        if (mRootView == null){
            mRootView = inflater.inflate(R.layout.fragment_play_visualizer, null);
        }
        return mRootView;
    }
}
