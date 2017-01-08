package com.example.mrh.musicplayer.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.mrh.musicplayer.R;
import com.example.mrh.musicplayer.activity.adapter.PlayActivityAdapter;
import com.example.mrh.musicplayer.constant.Constant;
import com.example.mrh.musicplayer.domain.MusicInfo;
import com.example.mrh.musicplayer.domain.MusicList;
import com.example.mrh.musicplayer.fragment.VisualizerFragment;
import com.example.mrh.musicplayer.fragment.playFragment;
import com.example.mrh.musicplayer.service.MusicBinder;
import com.example.mrh.musicplayer.service.PlaySevice;
import com.example.mrh.musicplayer.utils.Utils;
import com.viewpagerindicator.CirclePageIndicator;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PlayActivity extends BaseActivity implements View.OnClickListener, SeekBar
        .OnSeekBarChangeListener {

    private static final String TAG = "PlayActivity";
    private ServiceConnection mServiceConnection;
    private LinearLayout mLlPlaycontent;
    private LinearLayout mLlPlaycontentBack;
    private TextView mTvPlaycontentTitle;
    private TextView mTvPlaycontentArtist;
    private ViewPager mVpPlaycontent;
    private CirclePageIndicator mIndicatorPalycontent;
    private TextView mTvPlaycontentProcessDuration;
    private SeekBar mSbPlaycontentProcess;
    private TextView mTvPlaycontentProcessDuring;
    private ImageView mIvPlaycontentPlay;
    private ImageView mIvPlaycontentNext;
    private ImageView mIvPlaycontentPre;
    private ImageView mIvPlaycontentPlaymodel;
    private ImageView mIvPlaycontentList;
    public PlaySevice mPlayer;
    private ArrayList<MusicInfo> mAllSongs;
    private ArrayList<MusicList> list_custom;
    private ArrayList<MusicList> list_allsongs;
    private ArrayList<MusicList> list_artist;
    private ArrayList<MusicList> list_album;
    private ArrayList<MusicList> list_data;
    private HashMap<String, ArrayList<MusicInfo>> songs_all;
    private HashMap<String, ArrayList<MusicInfo>> songs_custom;
    private HashMap<String, ArrayList<MusicInfo>> songs_artist;
    private HashMap<String, ArrayList<MusicInfo>> songs_album;
    private HashMap<String, ArrayList<MusicInfo>> songs_data;
    private PlayActivityAdapter mAdapter;
    private List<Fragment> mList = new ArrayList<>();

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setContentView(R.layout.activity_play_content);
        initView();
        initService();
    }

    private void initService () {
        Intent intent = new Intent(PlayActivity.this, PlaySevice.class);
        intent.setAction("com.example.mrh.musicplayer.service.PlaySevice");
        mServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected (ComponentName name, IBinder service) {
                Binder mService = (Binder) service;
                mPlayer = ((MusicBinder) mService).mPlayer;
                EventBus.getDefault().post(Constant.PLAYACTIVITY_INITSERVICE);
            }

            @Override
            public void onServiceDisconnected (ComponentName name) {

            }
        };
        bindService(intent, mServiceConnection, BIND_AUTO_CREATE);
    }

    private void initView () {
        mLlPlaycontent = (LinearLayout) findViewById(R.id.ll_playcontent);
        mLlPlaycontentBack = (LinearLayout) findViewById(R.id.ll_playcontent_back);
        mTvPlaycontentTitle = (TextView) findViewById(R.id.tv_playcontent_title);
        mTvPlaycontentArtist = (TextView) findViewById(R.id.tv_playcontent_artist);
        mVpPlaycontent = (ViewPager) findViewById(R.id.vp_playcontent);
        mIndicatorPalycontent = (CirclePageIndicator) findViewById(R.id.indicator_palycontent);
        mTvPlaycontentProcessDuration = (TextView) findViewById(R.id
                .tv_playcontent_process_duration);
        mSbPlaycontentProcess = (SeekBar) findViewById(R.id.sb_playcontent_process);
        mTvPlaycontentProcessDuring = (TextView) findViewById(R.id.tv_playcontent_process_during);
        mIvPlaycontentPlay = (ImageView) findViewById(R.id.iv_playcontent_play);
        mIvPlaycontentNext = (ImageView) findViewById(R.id.iv_playcontent_next);
        mIvPlaycontentPre = (ImageView) findViewById(R.id.iv_playcontent_pre);
        mIvPlaycontentPlaymodel = (ImageView) findViewById(R.id.iv_playcontent_playmodel);
        mIvPlaycontentList = (ImageView) findViewById(R.id.iv_playcontent_list);

        mLlPlaycontent.setBackground(Utils.optimizeDrawble(PlayActivity.this,
                R.drawable.skin_player_bg));
        mLlPlaycontentBack.setOnClickListener(this);
        mIvPlaycontentPlay.setOnClickListener(this);
        mIvPlaycontentNext.setOnClickListener(this);
        mIvPlaycontentPre.setOnClickListener(this);
        mIvPlaycontentPlaymodel.setOnClickListener(this);
        mIvPlaycontentList.setOnClickListener(this);
        mSbPlaycontentProcess.setOnSeekBarChangeListener(this);

    }

    @Override
    public void onClick (View v) {
        switch (v.getId()){
        case R.id.ll_playcontent_back:
            startActivity(new Intent(PlayActivity.this, MainActivity.class));
            overridePendingTransition(R.anim.activity_slide_right_enter,
                    R.anim.activity_slide_right_exit);
            finish();
            break;
        case R.id.iv_playcontent_play:
            if (mPlayer.mMediaPlayer.isPlaying()){
                mPlayer.pauseMusic();
                mIvPlaycontentPlay.setBackgroundResource(R.drawable.btn_play_176px);

            }else {
                mPlayer.playMusic();
                mIvPlaycontentPlay.setBackgroundResource(R.drawable.btn_pause_176px);

            }
            break;
        case R.id.iv_playcontent_next:
            mPlayer.nextMusic();
            break;
        case R.id.iv_playcontent_pre:
            mPlayer.preMusic();
            break;
        case R.id.iv_playcontent_playmodel:
            switch (mPlayer.mMediaPlayer.getPlayList().getPlayModel()){
            case Constant.PLAYMODEL_ORDER:
                mIvPlaycontentPlaymodel.setBackgroundResource(R.drawable.random_64px);
                mPlayer.mMediaPlayer.getPlayList().setPlayModel(Constant.PLAYMODEL_RANDOM);
                break;
            case Constant.PLAYMODEL_RANDOM:
                mIvPlaycontentPlaymodel.setBackgroundResource(R.drawable.cycle_64px);
                mPlayer.mMediaPlayer.getPlayList().setPlayModel(Constant.PLAYMODEL_CYCLE);
                break;
            case Constant.PLAYMODEL_CYCLE:
                mIvPlaycontentPlaymodel.setBackgroundResource(R.drawable.order_64px);
                mPlayer.mMediaPlayer.getPlayList().setPlayModel(Constant.PLAYMODEL_ORDER);
                break;
            }
            break;
        case R.id.iv_playcontent_list:

            break;
        default:
            break;
        }
    }

    private void initData () {
        mAllSongs = mPlayer.mAllSongs;
        list_custom = mPlayer.list_custom;
        list_allsongs = mPlayer.list_allsongs;
        list_artist = mPlayer.list_artist;
        list_album = mPlayer.list_album;
        list_data = mPlayer.list_data;
        songs_all = mPlayer.songs_all;
        songs_custom = mPlayer.songs_custom;
        songs_artist = mPlayer.songs_artist;
        songs_album = mPlayer.songs_album;
        songs_data = mPlayer.songs_data;

        setView();
    }


    private void setView () {
        mList.add(VisualizerFragment.newInstance("visualizer"));
        mList.add(playFragment.newInstance("play"));
        mAdapter = new PlayActivityAdapter(getSupportFragmentManager(), mList);
        mVpPlaycontent.setAdapter(mAdapter);
        mIndicatorPalycontent.setViewPager(mVpPlaycontent);

        mTvPlaycontentTitle.setText(mPlayer.mMusicSongsname);
        mTvPlaycontentArtist.setText(mPlayer.mMusicArtistname);
        mTvPlaycontentProcessDuration.setText(Utils.formatTime(mPlayer.duration));
        mTvPlaycontentProcessDuring.setText(Utils.formatTime(mPlayer.mMusicPlaytime));
        mSbPlaycontentProcess.setProgress(mPlayer.mMusicPlaytime * 100 / mPlayer.duration);
        if (mPlayer.mMediaPlayer.isPlaying()){
            mIvPlaycontentPlay.setBackgroundResource(R.drawable.btn_pause_176px);
        } else{
            mIvPlaycontentPlay.setBackgroundResource(R.drawable.btn_play_176px);
        }
        switch (mPlayer.mMusicPlaymodel){
        case Constant.PLAYMODEL_ORDER:
            mIvPlaycontentPlaymodel.setBackgroundResource(R.drawable.order_64px);
            break;
        case Constant.PLAYMODEL_RANDOM:
            mIvPlaycontentPlaymodel.setBackgroundResource(R.drawable.random_64px);
            break;
        case Constant.PLAYMODEL_CYCLE:
            mIvPlaycontentPlaymodel.setBackgroundResource(R.drawable.cycle_64px);
            break;
        default:
            break;
        }

    }

    /**
     * eventBus 处理的方法
     *
     * @param flag
     */
    @Subscribe(threadMode = ThreadMode.MAIN)       //主线程标识
    public void onEventMainThread (String flag) {
        if (flag.equals(Constant.PLAYACTIVITY_INITSERVICE)){
            initData();
        }
        if (flag.equals(Constant.UPDATE_MUSIC_START)){
            mTvPlaycontentTitle.setText(mPlayer.mMediaPlayer.getPlayList().getList().get(mPlayer
                    .mPosition).getTITLE());
            mTvPlaycontentArtist.setText(mPlayer.mMediaPlayer.getPlayList().getList().get(mPlayer
                    .mPosition).getARTIST());
        }
        if (flag.equals(Constant.UPDATE_PREGRESS)){
            mSbPlaycontentProcess.setProgress(mPlayer.mMediaPlayer.getCurrentPosition() * 100 /
                    mPlayer.duration);
            mTvPlaycontentProcessDuring.setText(Utils.formatTime(mPlayer.mMediaPlayer
                    .getCurrentPosition()));
        }
    }

    @Override
    public void onProgressChanged (SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch (SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch (SeekBar seekBar) {
        if (seekBar.getId() == R.id.sb_playcontent_process && mPlayer.mIsExist){
            //拖动进度条
            int i = seekBar.getProgress() * mPlayer.duration / 100;
            mPlayer.mMediaPlayer.seekTo(i);
        }
    }

    @Override
    protected void onDestroy () {
        super.onDestroy();
        unbindService(mServiceConnection);
        EventBus.getDefault().unregister(this);
    }
}
