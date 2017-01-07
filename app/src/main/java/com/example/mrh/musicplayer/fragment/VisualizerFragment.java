package com.example.mrh.musicplayer.fragment;

import android.media.audiofx.Visualizer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mrh.musicplayer.R;
import com.example.mrh.musicplayer.activity.PlayActivity;
import com.example.mrh.musicplayer.constant.Constant;
import com.example.mrh.musicplayer.custom.MyVisualizerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by MR.H on 2017/1/6 0006.
 */

public class VisualizerFragment extends BaseFragment {
    private static final String TAG = "VisualizerFragment";

    private View mRootView;
    private PlayActivity mPlayActivity;
    private MyVisualizerView mVisualizerView;
    private boolean isDraw = true;

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
        EventBus.getDefault().register(this);
        setFragmentName(getArguments().getString("name"));
    }

    @Nullable
    @Override
    public View onCreateView (LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        if (mRootView == null){
            mRootView = inflater.inflate(R.layout.fragment_play_visualizer, null);
            initView();
            initData();
        }
        return mRootView;
    }

    @Override
    public void onViewCreated (View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (mPlayActivity.mPlayer.mVisualizer != null){
            initVisualizer();
        }
    }

    private void initVisualizer () {
        mPlayActivity.mPlayer.mVisualizer.setDataCaptureListener(new Visualizer.OnDataCaptureListener() {
            @Override
            public void onWaveFormDataCapture (Visualizer visualizer, byte[] waveform, int
                    samplingRate) {
                if (isDraw){
                        mVisualizerView.updateVisualizer(waveform);
                }
            }

            @Override
            public void onFftDataCapture (Visualizer visualizer, byte[] fft, int samplingRate) {

            }
        }, Visualizer.getMaxCaptureRate() / 2, true, false);
        mPlayActivity.mPlayer.mVisualizer.setEnabled(true);
    }

    private void initData () {
        mPlayActivity = (PlayActivity) this.context;

    }

    private void initView () {
        mVisualizerView = (MyVisualizerView) mRootView.findViewById(R.id.visualizer_view);
    }

    @Override
    public void onDestroy () {
        super.onDestroy();
        mPlayActivity.mPlayer.mVisualizer.setEnabled(false);
        EventBus.getDefault().unregister(this);
    }
    /**
     * eventBus 处理的方法
     *
     * @param flag
     */
    @Subscribe(threadMode = ThreadMode.MAIN)       //主线程标识
    public void onEventMainThread (String flag) {
        if (flag.equals(Constant.PLAYACTIVITY_PLAY)){
            isDraw = true;
        }else if (flag.equals(Constant.PLAYACTIVITY_PAUSE)){
            isDraw = false;
        }
    }
}
