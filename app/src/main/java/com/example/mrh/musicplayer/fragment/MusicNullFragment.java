package com.example.mrh.musicplayer.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.mrh.musicplayer.R;
import com.example.mrh.musicplayer.activity.BaseActivity;

/**
 * Created by MR.H on 2016/12/7 0007.
 */

public class MusicNullFragment extends BaseFragment implements View.OnClickListener {

    private View mRootView;
    private Button mBtnMusicnullAdd;

    public MusicNullFragment () {
        super();
    }

    @Nullable
    @Override
    public View onCreateView (LayoutInflater inflater, @Nullable ViewGroup container,
                              @Nullable Bundle savedInstanceState) {
        if (mRootView == null){
            mRootView = inflater.inflate(R.layout.fragment_musicnull, null);
            initView();
        }
        return mRootView;
    }

    private void initView () {
        mBtnMusicnullAdd = (Button) mRootView.findViewById(R.id.btn_musicnull_add);

        mBtnMusicnullAdd.setOnClickListener(this);

        //back键处理fragment返回
        mActivity.setOnFragmentBack(new BaseActivity.FragmentBack() {
            @Override
            public boolean execute (KeyEvent event) {
                showAndRemoveFragment("MusicListFragment", fragmentName);
                return true;
            }
        }, fragmentName);
    }

    @Override
    public void onClick (View v) {
        switch (v.getId()){
        case R.id.btn_musicnull_add:
            addAndRemoveFragment(R.id.fl_main, new PhoneMusicFragment(), "PhoneMusicFragment",
                    fragmentName);
            break;
        }
    }
}
