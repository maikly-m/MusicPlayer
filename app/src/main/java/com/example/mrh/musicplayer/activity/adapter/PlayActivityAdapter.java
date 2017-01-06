package com.example.mrh.musicplayer.activity.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by MR.H on 2016/12/3 0003.
 */

public class PlayActivityAdapter extends FragmentPagerAdapter{
    private List<Fragment> mFragments;

    public PlayActivityAdapter (FragmentManager fm, List<Fragment> list) {
        super(fm);
        mFragments = list;
        if (mFragments == null){
            throw new NullPointerException("Fragments 为空");
        }
    }

    @Override
    public Fragment getItem (int position) {
        return mFragments.get(position);

    }

    @Override
    public int getCount () {
        return mFragments.size();
    }

}
