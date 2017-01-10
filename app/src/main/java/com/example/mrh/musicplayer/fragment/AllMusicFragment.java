package com.example.mrh.musicplayer.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.mrh.musicplayer.R;
import com.example.mrh.musicplayer.activity.BaseActivity;
import com.example.mrh.musicplayer.constant.Constant;
import com.example.mrh.musicplayer.fragment.adapter.AllMusicAdapter;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MR.H on 2016/12/4 0004.
 */

public class AllMusicFragment extends BaseFragment implements View.OnClickListener {
    private static final String TAG = "AllMusicFragment";
    private View mRootView;
    private List<Fragment> mList = new ArrayList<>();
    private LinearLayout mLlAllmusicBack;
    private TabPageIndicator mAllmusicIndicator;
    private ViewPager mVpAllmusic;

    public AllMusicFragment () {
        super();
    }

    @Nullable
    @Override
    public View onCreateView (LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        if (mRootView == null){
            mRootView = inflater.inflate(R.layout.fragment_allmusic, null);
            initView();
            initFragment();
        }
        return mRootView;
    }

    private void initView () {
        //back键处理fragment返回
        mActivity.setOnFragmentBack(new BaseActivity.FragmentBack() {
            @Override
            public boolean execute (KeyEvent event) {
                showAndRemoveFragment("ContentFragment", fragmentName);
                return true;
            }
        }, fragmentName);

        mLlAllmusicBack = (LinearLayout) mRootView.findViewById(R.id.ll_allmusic_back);
        mLlAllmusicBack.setOnClickListener(this);
        mAllmusicIndicator = (TabPageIndicator) mRootView.findViewById(R.id.allmusic_indicator);
        mVpAllmusic = (ViewPager) mRootView.findViewById(R.id.vp_allmusic);
    }

    @Override
    public void onClick (View v) {
        switch (v.getId()){
        case R.id.ll_allmusic_back:
            showAndRemoveFragment("ContentFragment", fragmentName);
            break;
        default:

            break;
        }
    }

    private void initFragment () {
        mList.add(SongsListFragment.newInstance(Constant.MUSIC_LIST_ALLSONGS_+"所有音乐"));
        mList.add(AllMusicVPFragment.newInstance(Constant.MUSIC_LIST_ARTIST_));
        mList.add(AllMusicVPFragment.newInstance(Constant.MUSIC_LIST_ALBUM_));
        mList.add(AllMusicVPFragment.newInstance(Constant.MUSIC_LIST_DATA_));
        List<String> title = new ArrayList<>();
        title.add("所有音乐");
        title.add("歌手");
        title.add("专辑");
        title.add("文件夹");

        AllMusicAdapter adapter = new AllMusicAdapter(cfm, mList, title);
        mVpAllmusic.setAdapter(adapter);
        mAllmusicIndicator.setViewPager(mVpAllmusic);
    }

}
