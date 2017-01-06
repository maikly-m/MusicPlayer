package com.example.mrh.musicplayer.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.mrh.musicplayer.R;
import com.example.mrh.musicplayer.activity.MainActivity;
import com.example.mrh.musicplayer.fragment.adapter.MainAdapter;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MR.H on 2016/12/4 0004.
 */

public class ContentFragment extends BaseFragment implements View.OnClickListener {

    private View mRootView;
    private TabPageIndicator mIndicator;
    private ImageView mMainSearch;
    private ViewPager mVpMain;
    private List<Fragment> mList;

    public ContentFragment () {
        super();
    }

    @Nullable
    @Override
    public View onCreateView (LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        if (mRootView == null){
            mRootView = inflater.inflate(R.layout.activity_main_content_ll, null);
            initView();
            initFragment();
        }
        return mRootView;
    }

    private void initView () {
        mIndicator = (TabPageIndicator) mRootView.findViewById(R.id.indicator);
        mMainSearch = (ImageView) mRootView.findViewById(R.id.main_search);
        mVpMain = (ViewPager) mRootView.findViewById(R.id.vp_main);

        mMainSearch.setOnClickListener(this);
    }

    @Override
    public void onClick (View v) {
        switch (v.getId()){
        case R.id.main_search:

            break;
        }
    }

    private void initFragment () {
        this.mList = ((MainActivity)mActivity).mList;
        mList.add(new MusicFragment());
        mList.add(new CommunityFragment());
        mList.add(new MineFragment());
        List<String> title = new ArrayList<>();
        title.add("听歌");
        title.add("社区");
        title.add("我的");

        MainAdapter mainAdapter = new MainAdapter(cfm, mList, title);
        mVpMain.setAdapter(mainAdapter);
        mIndicator.setViewPager(mVpMain);
        mIndicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled (int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected (int position) {

            }

            @Override
            public void onPageScrollStateChanged (int state) {

            }
        });
    }
}
