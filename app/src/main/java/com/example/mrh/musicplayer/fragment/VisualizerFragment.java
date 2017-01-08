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
import com.example.mrh.musicplayer.custom.MyLyrcisView;
import com.example.mrh.musicplayer.custom.MyVisualizerView;
import com.example.mrh.musicplayer.domain.LrcContent;
import com.example.mrh.musicplayer.domain.MusicInfo;

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
import java.util.TreeMap;

/**
 * Created by MR.H on 2017/1/6 0006.
 */

public class VisualizerFragment extends BaseFragment {
    private static final String TAG = "VisualizerFragment";

    private View mRootView;
    private PlayActivity mPlayActivity;
    private MyVisualizerView mVisualizerView;
    private boolean isDraw = true;
    private MyLyrcisView mLyrcisView;
    private List<LrcContent> mLrcList = new ArrayList<>();
    private String lrc_null;
    private TreeMap<Integer, String> mTreeMap = new TreeMap<>();

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
        mLyrcisView = (MyLyrcisView) mRootView.findViewById(R.id.visualizer_lrc);
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
        if (flag.equals(Constant.UPDATE_MUSIC_START)){
            isDraw = true;
        }else if (flag.equals(Constant.UPDATE_MUSIC_PAUSE)){
            isDraw = false;

        }
        if (flag.equals(Constant.UPDATE_PREGRESS)){
            initLrc();
            //更新歌词
            updateLrc();
        }
    }

    /**
     * 更新歌词
     */
    private void updateLrc () {
        int currentPosition = mPlayActivity.mPlayer.mMediaPlayer.getCurrentPosition();
        for (int i = 1; i < mLrcList.size() + 1; i++){
            if (currentPosition < mLrcList.get(i).getLrc_time()){
                mTreeMap.put(0, mLrcList.get(i-1).getLrc());
                mLyrcisView.updateLrc(mTreeMap);
                break;
            }
        }

    }

    /**
     * 初始化歌词
     */
    private void initLrc () {
        //清除上次的歌词
        mLrcList.clear();
        String path = null;
        List<MusicInfo> list = mPlayActivity.mPlayer.mMediaPlayer.getPlayList().getList();
        for (int i = 0; i < list.size(); i++){
            if (list.get(i).getTITLE().equals(mPlayActivity.mPlayer.mMusicSongsname)){
                path = list.get(i).getDATA();
                break;
            }
        }
        assert path != null;
        int lastIndexOf = path.lastIndexOf("/");
        String subPath = path.substring(0, lastIndexOf);
        File[] files = new File(subPath).listFiles(new FileFilter() {
            @Override
            public boolean accept (File pathname) {
                return pathname.getName().equals(mPlayActivity.mPlayer.mMusicSongsname + ".lrc");
            }
        });
        if (files == null){
            lrc_null = "歌词不存在";
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

            } catch (IOException e){
                e.printStackTrace();
                lrc_null = "歌词错误";
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
