package com.example.mrh.musicplayer.fragment.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by MR.H on 2016/12/3 0003.
 */

public class MainAdapter extends FragmentPagerAdapter{
    private List<String> title;
    private List<Fragment> mFragments;

    public MainAdapter (FragmentManager fm, List<Fragment> list, List<String> title) {
        super(fm);
        mFragments = list;
        this.title = title;
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

    @Override
    public CharSequence getPageTitle (int position) {
        String s = title.get(position);
        if (s != null){
            return s;
        }
        return super.getPageTitle(position);
    }

}
