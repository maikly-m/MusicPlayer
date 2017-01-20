package com.example.mrh.musicplayer.fragment;

import android.media.audiofx.Visualizer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mrh.musicplayer.R;
import com.example.mrh.musicplayer.activity.PlayActivity;
import com.example.mrh.musicplayer.constant.Constant;
import com.example.mrh.musicplayer.custom.MyLyrcisView;
import com.example.mrh.musicplayer.custom.MyVisualizerView;
import com.example.mrh.musicplayer.domain.LrcContent;
import com.example.mrh.musicplayer.domain.MusicInfo;
import com.example.mrh.musicplayer.service.PlaySevice;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by MR.H on 2017/1/6 0006.
 */

public class VisualizerFragment extends BaseFragment {
    private static final String TAG = "VisualizerFragment";

    private View mRootView;
    private PlayActivity mPlayActivity;
    private MyVisualizerView mVisualizerView;
    private TextView mTvPlayvisualizer;
    private boolean isDraw = true;
    private MyLyrcisView mLyrcisView;
    private List<LrcContent> mLrcList = new ArrayList<>();
    private String lrc_ = "有歌词";
    private boolean isReset = false;
    public static final String LRC_ = "有歌词";
    /**
     * 正常更新歌词，比如不拖动进度条时
     */
    private boolean isNormalUpdate = false;
    private int mPosition;
    /**
     * 上次更新歌词的时间
     */
    private long mPreUpdate;
    private String mLrc_name;
    /**
     * 刚进入activity时是否是暂停状态
     */
    private boolean mInitPause = true;
    private boolean isPrepare = false;

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
        initLrc();
        isPrepare = true;
    }

    private void initView () {
        mVisualizerView = (MyVisualizerView) mRootView.findViewById(R.id.visualizer_view);
        mLyrcisView = (MyLyrcisView) mRootView.findViewById(R.id.visualizer_lrc);
        mTvPlayvisualizer = (TextView) mRootView.findViewById(R.id.tv_playvisualizer);
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
        switch (flag){
        case Constant.UPDATE_MUSIC_START:
            isDraw = true;
            if (isReset){
                isReset = false;
                initLrc();
            } else if (!mInitPause && mPosition != mLyrcisView.position){
                mPosition = mLyrcisView.position;
                mPlayActivity.mPlayer.mMediaPlayer.seekTo(mLrcList.get(mPosition).getLrc_time());
            }
            break;
        case Constant.UPDATE_MUSIC_PAUSE:
            isDraw = false;
            break;
        case Constant.UPDATE_MUSIC_RESET:
            isReset = true;
            break;
        case Constant.UPDATE_PREGRESS:
            if (lrc_.equals(LRC_) && isPrepare){
                //更新歌词
                long mUpdate = mPlayActivity.mPlayer.mMediaPlayer.getCurrentPosition();
                isNormalUpdate = Math.abs(mUpdate - mPreUpdate) < PlaySevice.UPDATE_SPEED + 100;
                mPreUpdate = mUpdate;
                if (!mLyrcisView.isTouch){
                    if (mLyrcisView.isChange){
                        mLyrcisView.isChange = false;
                        mPosition = mLyrcisView.position;
                        mPlayActivity.mPlayer.mMediaPlayer.seekTo(mLrcList.get(mPosition).getLrc_time());
                        isNormalUpdate = true;
                    }
                    updateLrc();
                }
            }
            break;
        }
    }

    /**
     * 更新歌词
     */
    private void updateLrc () {
        mInitPause = false;
        int currentPosition = mPlayActivity.mPlayer.mMediaPlayer.getCurrentPosition();

        if (isNormalUpdate){
            if (mPosition < mLrcList.size() - 1){
                if (currentPosition < mLrcList.get(mPosition + 1).getLrc_time()){
                    mLyrcisView.updateLrc(mPosition);
                }else {
                    mLyrcisView.updateLrc(mPosition++);
                }
            } else if (mPosition == mLrcList.size() - 1){
                mLyrcisView.updateLrc(mPosition);
            }
        } else{
            if (currentPosition > mLrcList.get(mLrcList.size() - 1).getLrc_time()){
                mPosition = mLrcList.size() - 1;
                mLyrcisView.updateLrc(mLrcList.size() - 1);
                return;
            }
            for (int i = 1; i < mLrcList.size(); i++){
                if (currentPosition < mLrcList.get(i).getLrc_time()){
                    mPosition = i-1;
                    mLyrcisView.updateLrc(i-1);
                    break;
                }
            }
        }
    }

    /**
     * 初始化歌词
     */
    private void initLrc () {
        //清除上次的歌词
        mLrcList.clear();
        lrc_ = LRC_;
        String path = null;
        List<MusicInfo> list = mPlayActivity.mPlayer.mMediaPlayer.getPlayList().getList();
        for (int i = 0; i < list.size(); i++){
            if (list.get(i).getTITLE().equals(mPlayActivity.mPlayer.mMusicSongsname)){
                path = list.get(i).getDATA();
                String display_name = list.get(i).getDISPLAY_NAME();
                int lastIndexOf = display_name.lastIndexOf(".");
                mLrc_name = display_name.substring(0, lastIndexOf) + ".lrc";
                break;
            }
        }
        assert path != null;
        int lastIndexOf = path.lastIndexOf("/");
        String subPath = path.substring(0, lastIndexOf);
        File[] files = new File(subPath).listFiles(new FileFilter() {
            @Override
            public boolean accept (File pathname) {
                return pathname.getName().equals(mLrc_name);
            }
        });
        if (files.length <= 0){
            lrc_ = "歌词没有找到";
            mTvPlayvisualizer.setVisibility(View.VISIBLE);
            mLyrcisView.setVisibility(View.INVISIBLE);
            mTvPlayvisualizer.setText(lrc_);

        }else {
            //开始解析歌词
            FileInputStream fis = null;
            InputStreamReader isr = null;
            BufferedReader br = null;
            try{
                fis = new FileInputStream(files[0]);
                isr =  new InputStreamReader(fis, "GB2312");
                br = new BufferedReader(isr);
                String s ;
                boolean begin = false;
                while ((s = br.readLine()) != null) {
                    if (s.contains("[00:")){
                        begin = true;
                    }
                    if (begin){
                        s = s.replace("[", "");
                        //关键代码，歌词用的时候需要对时间进行排序
                        String[] splitLrc_data = s.split("]");
                        mLrcList.add(new LrcContent(splitLrc_data[1],
                                TimeStr(splitLrc_data[0])));
                    }
                }
                //设置到view中
                mLyrcisView.setList(mLrcList);
            } catch (IOException e){
                e.printStackTrace();
                lrc_ = "歌词错误";
                mTvPlayvisualizer.setVisibility(View.VISIBLE);
                mLyrcisView.setVisibility(View.INVISIBLE);
                mTvPlayvisualizer.setText(lrc_);
            } finally {
                try{
                    if (br != null){
                        br.close();
                    }
                    if (isr != null){
                        isr.close();
                    }
                    if (fis != null){
                        fis.close();
                    }
                } catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
        if (lrc_.equals(LRC_)){
            mTvPlayvisualizer.setVisibility(View.INVISIBLE);
            mLyrcisView.setVisibility(View.VISIBLE);
        }
    }
    private int TimeStr(String timeStr) {
        timeStr = timeStr.replace(":", ".");
        timeStr = timeStr.replace(".", "@");
        String timeData[] = timeStr.split("@");
        int currentTime = 0;
        // 分离出分、秒并转换为整型
        try {
            int minute = Integer.parseInt(timeData[0]);
            int second = Integer.parseInt(timeData[1]);
            int millisecond = Integer.parseInt(timeData[2]);
            currentTime = (minute * 60 + second) * 1000 + (millisecond * 10);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return currentTime;
    }

}
