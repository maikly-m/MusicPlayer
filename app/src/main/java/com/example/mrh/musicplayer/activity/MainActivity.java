package com.example.mrh.musicplayer.activity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.mrh.musicplayer.ActivityManager;
import com.example.mrh.musicplayer.R;
import com.example.mrh.musicplayer.activity.adapter.PopListAdapter;
import com.example.mrh.musicplayer.activity.adapter.PresetReverbAdapter;
import com.example.mrh.musicplayer.activity.viewHolder.PoplistViewHolder;
import com.example.mrh.musicplayer.constant.Constant;
import com.example.mrh.musicplayer.custom.SlidingMenu;
import com.example.mrh.musicplayer.domain.MusicInfo;
import com.example.mrh.musicplayer.domain.MusicList;
import com.example.mrh.musicplayer.fragment.ContentFragment;
import com.example.mrh.musicplayer.service.MusicBinder;
import com.example.mrh.musicplayer.service.PlaySevice;
import com.example.mrh.musicplayer.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

import static android.widget.RelativeLayout.ALIGN_PARENT_BOTTOM;
import static com.example.mrh.musicplayer.activity.adapter.PopListAdapter.CONDITION_POPLIST_0;
import static com.example.mrh.musicplayer.activity.adapter.PopListAdapter.CONDITION_POPLIST_1;
import static com.example.mrh.musicplayer.activity.adapter.PopListAdapter.CONDITION_POPLIST_2;
import static com.example.mrh.musicplayer.fragment.adapter.SongsListAdapter.CONDITION_SONGSLIST_1;
import static com.example.mrh.musicplayer.fragment.adapter.SongsListAdapter.CONDITION_SONGSLIST_2;

/**
 * Created by MR.H on 2016/12/3 0003.
 */

public class MainActivity extends BaseActivity implements View.OnClickListener, SeekBar
        .OnSeekBarChangeListener {
    private static final String TAG = "MainActivity";
    private ImageView mIvMainMusic;
    public ArrayList<MusicInfo> mAllSongs;
    public ArrayList<MusicList> list_custom;
    public HashMap<String, ArrayList<MusicInfo>> songs_custom;
    public List<Fragment> mList = new ArrayList<>();
    private SlidingMenu mSlidingMenu;
    private View mMainView;
    private View mMenuView;
    private LinearLayout mMainLl;
    public String customMusicListName;
    private FrameLayout mFlMain;
    public TextView mTvMusicName;
    public TextView mTvMusicArtist;
    public SeekBar mSbMusicProcess;
    public TextView mTvMusicProcess;
    private ImageView mIvMusicGo;
    private ImageView mIvMusicNext;
    private ImageView mMainList;
    private ServiceConnection mServiceConnection;
    public PlaySevice mPlayer;
    private ImageView mIvMainactivityPlaymodel;
    private TextView mTvMainactivityPlaymodel;
    private TextView mTvMainactivityListname;
    private ListView mLvMainactivityList;
    private PopListAdapter mPopListAdapter;
    public View mView;
    private Button mBtnExit;
    public int prePosition = 0;
    private boolean isShouldRevomeView = false;
    private HashMap<String, Object> mShouldRevomeView;
    private TreeMap<Integer, Integer> mConditionMap;
    private RotateAnimation mRotateAnimation;
    private ObjectAnimator mOa;
    private long mCurrentPlayTime;
    public HashMap<String, ArrayList<MusicInfo>> songs_all;
    public HashMap<String, ArrayList<MusicInfo>> songs_love;
    public HashMap<String, ArrayList<MusicInfo>> songs_artist;
    public HashMap<String, ArrayList<MusicInfo>> songs_album;
    public HashMap<String, ArrayList<MusicInfo>> songs_data;
    public ArrayList<MusicList> list_allsongs;
    public ArrayList<MusicList> list_artist;
    public ArrayList<MusicList> list_album;
    public ArrayList<MusicList> list_data;
    private LinearLayout mLlMainmenuEqulizer;
    private ImageView mIvMainmenuEqulizer;
    private SeekBar mSbMainmenu01;
    private SeekBar mSbMainmenu02;
    private SeekBar mSbMainmenu03;
    private SeekBar mSbMainmenu04;
    private SeekBar mSbMainmenu05;
    private LinearLayout mLlMainmenuEqulizerShow;
    private int mMinRange;
    private int mMaxRange;
    private TextView mTvMainmenu01;
    private TextView mTvMainmenuMinrange01;
    private TextView mTvMainmenuMaxrange01;
    private TextView mTvMainmenu02;
    private TextView mTvMainmenuMinrange02;
    private TextView mTvMainmenuMaxrange02;
    private TextView mTvMainmenu03;
    private TextView mTvMainmenuMinrange03;
    private TextView mTvMainmenuMaxrange03;
    private TextView mTvMainmenu04;
    private TextView mTvMainmenuMinrange04;
    private TextView mTvMainmenuMaxrange04;
    private TextView mTvMainmenu05;
    private TextView mTvMainmenuMinrange05;
    private TextView mTvMainmenuMaxrange05;
    private int mLlMainmenuEqulizerShow_Height;
    private SeekBar mSbMainmenuBassboost;
    private LinearLayout mLlMainmenuPresetReverb;
    private ImageView mIvMainmenuPresetReverb;
    private ListView mLvMainmenuPresetReverb;
    private int mLvMainmenuPresetReverb_Height;
    private ArrayList<String> mList_presets;
    private TextView mTvMainmenuPresetReverb;
    private LinearLayout mLlMainmenuPresetReverb_;
    private RelativeLayout mRlMain;//根布局
    private View popuViewExist;
    private View popuViewNull;
    private View mPopuView;
    private int mPopuViewmeasuredHeight;
    /**
     * 打开列表窗口
     */
    private boolean isShowPopuWindow = false;
    /**
     * 渲染打开歌曲列表后的背景
     */
    private View mVMain;
    /**
     * 是否点击了条目
     */
    private boolean isUserClick = false;
    private RelativeLayout mRlMainactivity;

    @Override
    protected void onCreate (@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        mSlidingMenu = new SlidingMenu(this);
        setContentView(mSlidingMenu);
        initMainView();
        initMenuView();
        mSlidingMenu.addView(mMenuView);
        mSlidingMenu.addView(mMainView);
        mSlidingMenu.setBackground(Utils.optimizeDrawble(MainActivity.this,
                R.drawable.mainactivity_bg));
        initService();
    }

    private void initData () {
        mAllSongs = mPlayer.mAllSongs;
        list_custom = mPlayer.list_custom;
        list_allsongs = mPlayer.list_allsongs;
        list_artist = mPlayer.list_artist;
        list_album = mPlayer.list_album;
        list_data = mPlayer.list_data;
        songs_all = mPlayer.songs_all;
        songs_love = mPlayer.songs_love;
        songs_custom = mPlayer.songs_custom;
        songs_artist = mPlayer.songs_artist;
        songs_album = mPlayer.songs_album;
        songs_data = mPlayer.songs_data;
        initEqulizer();
        initBassBoost();
        EventBus.getDefault().post(Constant.UPDATE_INIT);
    }

    private void initBassBoost () {
        short roundedStrength = mPlayer.mBassBoost.getRoundedStrength();
        mSbMainmenuBassboost.setProgress(roundedStrength / 10);
    }

    private void initEqulizer () {
        short[] bandLevelRange = mPlayer.mEqualizer.getBandLevelRange();
        mMinRange = bandLevelRange[0] / 100;
        mMaxRange = bandLevelRange[1] / 100;
        short numberOfBands = mPlayer.mEqualizer.getNumberOfBands();

        mTvMainmenuMinrange01.setText(mMinRange + "dB");
        mTvMainmenuMinrange02.setText(mMinRange + "dB");
        mTvMainmenuMinrange03.setText(mMinRange + "dB");
        mTvMainmenuMinrange04.setText(mMinRange + "dB");
        mTvMainmenuMinrange05.setText(mMinRange + "dB");
        mTvMainmenuMaxrange01.setText(mMaxRange + "dB");
        mTvMainmenuMaxrange02.setText(mMaxRange + "dB");
        mTvMainmenuMaxrange03.setText(mMaxRange + "dB");
        mTvMainmenuMaxrange04.setText(mMaxRange + "dB");
        mTvMainmenuMaxrange05.setText(mMaxRange + "dB");

        setCenterFreq((short) 0, mTvMainmenu01);
        setCenterFreq((short) 1, mTvMainmenu02);
        setCenterFreq((short) 2, mTvMainmenu03);
        setCenterFreq((short) 3, mTvMainmenu04);
        setCenterFreq((short) 4, mTvMainmenu05);
        setSeekBarProgress((short) 0, mSbMainmenu01);
        setSeekBarProgress((short) 1, mSbMainmenu02);
        setSeekBarProgress((short) 2, mSbMainmenu03);
        setSeekBarProgress((short) 3, mSbMainmenu04);
        setSeekBarProgress((short) 4, mSbMainmenu05);

        short presets = mPlayer.mEqualizer.getNumberOfPresets();
        mList_presets = new ArrayList<>();
        for (short i = 0; i < presets; i++){
            String s = null;
            switch (i){
            case 0:
                s = "普通";
                break;
            case 1:
                s = "古典";
                break;
            case 2:
                s = "舞会";
                break;
            case 3:
                s = "平音";
                break;
            case 4:
                s = "民族";
                break;
            case 5:
                s = "重金属";
                break;
            case 6:
                s = "嘻哈";
                break;
            case 7:
                s = "爵士";
                break;
            case 8:
                s = "流行";
                break;
            case 9:
                s = "摇滚";
                break;
            }
            mList_presets.add(s);
        }
        PresetReverbAdapter adapter = new PresetReverbAdapter(MainActivity.this, mList_presets);
        mLvMainmenuPresetReverb.setAdapter(adapter);
        mLvMainmenuPresetReverb.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick (AdapterView<?> parent, View view, int position, long id) {
                mPlayer.mEqualizer.usePreset((short) position);
                mPlayer.mPresetReverbName = position;
                mTvMainmenuPresetReverb.setText(mList_presets.get(position));
                setSeekBarProgress((short) 0, mSbMainmenu01);
                setSeekBarProgress((short) 1, mSbMainmenu02);
                setSeekBarProgress((short) 2, mSbMainmenu03);
                setSeekBarProgress((short) 3, mSbMainmenu04);
                setSeekBarProgress((short) 4, mSbMainmenu05);
                mPlayer.isEditEqulizerEffect = false;
            }
        });
        if (mPlayer.mMusicEffectEqulizer){
            mTvMainmenuPresetReverb.setText("自定义");
        } else{
            mTvMainmenuPresetReverb.setText(mList_presets.get(mPlayer.mMusicEffectPresetreverb));
        }
    }

    //初始化服务
    private void initService () {
        Intent intent = new Intent(MainActivity.this, PlaySevice.class);
        intent.setAction("com.example.mrh.musicplayer.service.PlaySevice");
        mServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected (ComponentName name, IBinder service) {
                Binder mService = (Binder) service;
                mPlayer = ((MusicBinder) mService).mPlayer;
                mPlayer.setActivity(MainActivity.this);
                EventBus.getDefault().post(Constant.OK_INITSERVICE);
            }

            @Override
            public void onServiceDisconnected (ComponentName name) {

            }
        };
        bindService(intent, mServiceConnection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy () {
        super.onDestroy();
        unbindService(mServiceConnection);
        EventBus.getDefault().unregister(this);
    }

    private void initMainView () {
        mMainView = View.inflate(this, R.layout.activity_main_content, null);
        ContentFragment contentFragment = new ContentFragment();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.fl_main, contentFragment, "ContentFragment");
        ft.commit();
        mRlMain = (RelativeLayout) mMainView.findViewById(R.id.mrl_main);
        initListPopuWindow();
        mFlMain = (FrameLayout) mMainView.findViewById(R.id.fl_main);
        mVMain = mMainView.findViewById(R.id.v_main);
        mMainLl = (LinearLayout) mMainView.findViewById(R.id.main_ll);
        mIvMainMusic = (ImageView) mMainView.findViewById(R.id.iv_main_music);
        mSbMusicProcess = (SeekBar) mMainView.findViewById(R.id.sb_music_process);
        mTvMusicProcess = (TextView) mMainView.findViewById(R.id.tv_music_process);
        mTvMusicName = (TextView) mMainView.findViewById(R.id.tv_music_name);
        mTvMusicArtist = (TextView) mMainView.findViewById(R.id.tv_music_artist);
        mIvMusicGo = (ImageView) mMainView.findViewById(R.id.iv_music_go);
        mIvMusicNext = (ImageView) mMainView.findViewById(R.id.iv_music_next);
        mMainList = (ImageView) mMainView.findViewById(R.id.main_list);

        mVMain.setOnClickListener(this);
        mIvMusicGo.setOnClickListener(this);
        mIvMusicNext.setOnClickListener(this);
        mMainList.setOnClickListener(this);
        mIvMainMusic.setOnClickListener(this);

        mRotateAnimation = new RotateAnimation(0, 359, Animation
                .RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        mRotateAnimation.setDuration(4000);
        mRotateAnimation.setRepeatCount(Animation.INFINITE);
        mRotateAnimation.setInterpolator(new LinearInterpolator());
        mRotateAnimation.setFillAfter(true);

        mOa = ObjectAnimator.ofFloat(mIvMainMusic, "rotation", 0f, 359f);
        mOa.setInterpolator(new LinearInterpolator());
        mOa.setRepeatCount(ValueAnimator.INFINITE);
        mOa.setDuration(10000);

        //进度条监听
        mSbMusicProcess.setOnSeekBarChangeListener(this);
    }

    /**
     * 初始化弹出歌曲列表
     */
    private void initListPopuWindow () {
        //存在列表
        popuViewExist = View.inflate(MainActivity.this, R.layout.popupwindow_mainactivity_list,
                null);
        mRlMainactivity = (RelativeLayout) popuViewExist.findViewById(R.id
                .rl_mainactivity);
        mRlMainactivity.setOnClickListener(this);
        mIvMainactivityPlaymodel = (ImageView) popuViewExist.findViewById(R.id
                .iv_mainactivity_playmodel);
        mIvMainactivityPlaymodel.setOnClickListener(this);
        mTvMainactivityPlaymodel = (TextView) popuViewExist.findViewById(R.id
                .tv_mainactivity_playmodel);
        mTvMainactivityListname = (TextView) popuViewExist.findViewById(R.id
                .tv_mainactivity_listname);
        mLvMainactivityList = (ListView) popuViewExist.findViewById(R.id.lv_mainactivity_list);
        //不存在列表
        popuViewNull = View.inflate(MainActivity.this, R.layout.popupwindow_mainactivity_null,
                null);
    }

    /**
     * 弹出歌曲列表
     */
    private void showListPopuWindow () {
            //popupwindow加载
        if (mPlayer.mMediaPlayer.getPlayList() != null && mPlayer.mMediaPlayer.getPlayList()
                    .getList() != null){
            prePosition = 0;
            switch (mPlayer.mMediaPlayer.getPlayList().getPlayModel()){
                case Constant.PLAYMODEL_ORDER:
                    mIvMainactivityPlaymodel.setBackgroundResource(R.drawable.order_64px);
                    break;
                case Constant.PLAYMODEL_RANDOM:
                    mIvMainactivityPlaymodel.setBackgroundResource(R.drawable.random_64px);
                    break;
                case Constant.PLAYMODEL_CYCLE:
                    mIvMainactivityPlaymodel.setBackgroundResource(R.drawable.cycle_64px);
                    break;
                default:
                    break;
            }
            mTvMainactivityPlaymodel.setText(mPlayer.mMediaPlayer.getPlayList().getPlayModel());
            String listName = mPlayer.mMediaPlayer.getPlayList().getListName();
            String substring = null;
            if (listName.contains(Constant.MUSIC_LIST_ALLSONGS_)){
                substring = listName.substring(Constant.MUSIC_LIST_ALLSONGS_.length());
            } else if (listName.contains(Constant.MUSIC_LIST_CUSTOM_)){
                substring = listName.substring(Constant.MUSIC_LIST_CUSTOM_.length());
            } else if (listName.contains(Constant.MUSIC_LIST_ARTIST_)){
                substring = listName.substring(Constant.MUSIC_LIST_ARTIST_.length());
            } else if (listName.contains(Constant.MUSIC_LIST_ALBUM_)){
                substring = listName.substring(Constant.MUSIC_LIST_ALBUM_.length());
            } else if (listName.contains(Constant.MUSIC_LIST_DATA_)){
                substring = listName.substring(Constant.MUSIC_LIST_DATA_.length());
            }
            mTvMainactivityListname.setText(substring);
            mPopListAdapter = new PopListAdapter(MainActivity.this, mPlayer
                    .mMediaPlayer.getPlayList().getList(), mPlayer.mMusicSongsname);
            mShouldRevomeView = mPopListAdapter.shouldRevomeView;
            mConditionMap = mPopListAdapter.conditionMap;
            mLvMainactivityList.setAdapter(mPopListAdapter);
            //设置位置
            mLvMainactivityList.setSelectionFromTop(prePosition, Utils.dip2px
                    (MainActivity.this, 100));
            mLvMainactivityList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick (AdapterView<?> parent, View view, int position, long id) {
                    isUserClick = true;
                    PoplistViewHolder poplistViewHolder = (PoplistViewHolder) view.getTag();
                    if (prePosition != position){
                        if (isShouldRevomeView){
                            //滑动条目滚出屏幕后滚回时
                            isShouldRevomeView = false;
                            View v = (View) mShouldRevomeView.get("shouldRemoveView");
                            int p = (int) mShouldRevomeView.get("position");
                            PoplistViewHolder _holder = (PoplistViewHolder) v.getTag();
                            _holder.mVMainpopPlay.setVisibility(View.INVISIBLE);
                            mConditionMap.put(p, CONDITION_POPLIST_0);

                        } else{
                            PoplistViewHolder mholder = (PoplistViewHolder) mView.getTag();
                            mholder.mVMainpopPlay.setVisibility(View.INVISIBLE);
                            mConditionMap.put(prePosition, CONDITION_POPLIST_0);
                        }

                        poplistViewHolder.mVMainpopPlay.setVisibility(View.VISIBLE);
                        mConditionMap.put(position, CONDITION_POPLIST_1);
                        prePosition = position;
                        mView = view;
                    } else{
                        //根据不同的状态设置
                        switch (mConditionMap.get(position)){
                        case CONDITION_POPLIST_0:
                            poplistViewHolder.mVMainpopPlay.setVisibility(View.VISIBLE);
                            mConditionMap.put(position, CONDITION_POPLIST_1);
                            prePosition = position;
                            mView = view;
                            break;
                        case CONDITION_POPLIST_1:
                            mConditionMap.put(position, CONDITION_SONGSLIST_2);
                            break;
                        case CONDITION_POPLIST_2:
                            mConditionMap.put(position, CONDITION_SONGSLIST_1);
                            break;
                        }
                    }

                    mTvMusicName.setText(mPlayer.mMediaPlayer.getPlayList().getList().get(mPlayer
                            .mPosition).getTITLE());
                    mTvMusicArtist.setText(mPlayer.mMediaPlayer.getPlayList().getList().get(mPlayer
                            .mPosition).getARTIST());
                    if (mPlayer.mMediaPlayer.isPlaying()){
                        mIvMusicGo.setBackgroundResource(R.drawable.play_64px_normal);
                    } else{
                        mIvMusicGo.setBackgroundResource(R.drawable.pause_64px_normal);
                    }
                    mPlayer.startMusic(position, mPlayer.mMediaPlayer.getPlayList().getListName());
                    EventBus.getDefault().post(Constant.UPDATE_FRAGMENT_LIST);
                }
            });

            mLvMainactivityList.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged (AbsListView view, int scrollState) {

                }

                //滚出滚入记录
                @Override
                public void onScroll (AbsListView view, int firstVisibleItem,
                                      int visibleItemCount, int totalItemCount) {
                    if (prePosition != -1 && !isShouldRevomeView){
                        if (prePosition < firstVisibleItem || prePosition >
                                firstVisibleItem + visibleItemCount){
                            isShouldRevomeView = true;
                        }
                    }
                }
            });
            mPopuView = popuViewExist;
        } else{
            mPopuView = popuViewNull;
        }
        RelativeLayout.LayoutParams rl_lp = new RelativeLayout.LayoutParams(ViewGroup
                .LayoutParams.MATCH_PARENT,
                Utils.dip2px(MainActivity.this, 270));
        rl_lp.addRule(ALIGN_PARENT_BOTTOM);
        rl_lp.bottomMargin = mMainLl.getHeight();
        mRlMain.addView(mPopuView, rl_lp);
        mPopuViewmeasuredHeight = Utils.dip2px(MainActivity.this, 270);
        ViewGroup.LayoutParams lp = mPopuView.getLayoutParams();
        lp.height = 0;
        mPopuView.setLayoutParams(lp);

        ValueAnimator va = ValueAnimator.ofInt(0, mPopuViewmeasuredHeight);
        va.setDuration(150);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate (ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams)
                        mPopuView.getLayoutParams();
                lp.height = value;
                mPopuView.setLayoutParams(lp);
                mVMain.setAlpha(0.5f * (value / mPopuViewmeasuredHeight));
            }
        });
        va.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart (Animator animation) {
                mVMain.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd (Animator animation) {
                isShowPopuWindow = true;
            }

            @Override
            public void onAnimationCancel (Animator animation) {

            }

            @Override
            public void onAnimationRepeat (Animator animation) {

            }
        });
        va.start();
    }

    /**
     * 关闭列表窗口
     */
    private void dismissListPopuWindow () {
        ValueAnimator va = ValueAnimator.ofInt(mPopuViewmeasuredHeight, 0);
        va.setDuration(150);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate (ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams)
                        mPopuView.getLayoutParams();
                lp.height = value;
                mPopuView.setLayoutParams(lp);
                mVMain.setAlpha(0.5f * (value / mPopuViewmeasuredHeight));
            }
        });
        va.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart (Animator animation) {

            }

            @Override
            public void onAnimationEnd (Animator animation) {
                mRlMain.removeView(mPopuView);
                mVMain.setVisibility(View.GONE);
                isShowPopuWindow = false;
            }

            @Override
            public void onAnimationCancel (Animator animation) {

            }

            @Override
            public void onAnimationRepeat (Animator animation) {

            }
        });
        va.start();
    }

    private void initMenuView () {
        mMenuView = View.inflate(this, R.layout.activity_main_menu, null);
        mLlMainmenuEqulizer = (LinearLayout) mMenuView.findViewById(R.id.ll_mainmenu_equlizer);
        mLlMainmenuEqulizer.setOnClickListener(this);
        mLlMainmenuEqulizerShow = (LinearLayout) mMenuView.findViewById(R.id
                .ll_mainmenu_equlizer_show);
        mIvMainmenuEqulizer = (ImageView) mMenuView.findViewById(R.id.iv_mainmenu_equlizer);
        //先隐藏均衡器部分
        mLlMainmenuEqulizerShow.measure(0, 0);
        mLlMainmenuEqulizerShow_Height = mLlMainmenuEqulizerShow.getMeasuredHeight();
        ViewGroup.LayoutParams lp = mLlMainmenuEqulizerShow.getLayoutParams();
        lp.height = 0;
        mLlMainmenuEqulizerShow.setLayoutParams(lp);

        mTvMainmenu01 = (TextView) mMenuView.findViewById(R.id.tv_mainmenu_01);
        mTvMainmenuMinrange01 = (TextView) mMenuView.findViewById(R.id.tv_mainmenu_minrange_01);
        mTvMainmenuMaxrange01 = (TextView) mMenuView.findViewById(R.id.tv_mainmenu_maxrange_01);
        mTvMainmenu02 = (TextView) mMenuView.findViewById(R.id.tv_mainmenu_02);
        mTvMainmenuMinrange02 = (TextView) mMenuView.findViewById(R.id.tv_mainmenu_minrange_02);
        mTvMainmenuMaxrange02 = (TextView) mMenuView.findViewById(R.id.tv_mainmenu_maxrange_02);
        mTvMainmenu03 = (TextView) mMenuView.findViewById(R.id.tv_mainmenu_03);
        mTvMainmenuMinrange03 = (TextView) mMenuView.findViewById(R.id.tv_mainmenu_minrange_03);
        mTvMainmenuMaxrange03 = (TextView) mMenuView.findViewById(R.id.tv_mainmenu_maxrange_03);
        mTvMainmenu04 = (TextView) mMenuView.findViewById(R.id.tv_mainmenu_04);
        mTvMainmenuMinrange04 = (TextView) mMenuView.findViewById(R.id.tv_mainmenu_minrange_04);
        mTvMainmenuMaxrange04 = (TextView) mMenuView.findViewById(R.id.tv_mainmenu_maxrange_04);
        mTvMainmenu05 = (TextView) mMenuView.findViewById(R.id.tv_mainmenu_05);
        mTvMainmenuMinrange05 = (TextView) mMenuView.findViewById(R.id.tv_mainmenu_minrange_05);
        mTvMainmenuMaxrange05 = (TextView) mMenuView.findViewById(R.id.tv_mainmenu_maxrange_05);

        mSbMainmenu01 = (SeekBar) mMenuView.findViewById(R.id.sb_mainmenu_01);
        mSbMainmenu02 = (SeekBar) mMenuView.findViewById(R.id.sb_mainmenu_02);
        mSbMainmenu03 = (SeekBar) mMenuView.findViewById(R.id.sb_mainmenu_03);
        mSbMainmenu04 = (SeekBar) mMenuView.findViewById(R.id.sb_mainmenu_04);
        mSbMainmenu05 = (SeekBar) mMenuView.findViewById(R.id.sb_mainmenu_05);
        mSbMainmenu01.setOnSeekBarChangeListener(this);
        mSbMainmenu02.setOnSeekBarChangeListener(this);
        mSbMainmenu03.setOnSeekBarChangeListener(this);
        mSbMainmenu04.setOnSeekBarChangeListener(this);
        mSbMainmenu05.setOnSeekBarChangeListener(this);

        mSbMainmenuBassboost = (SeekBar) mMenuView.findViewById(R.id.sb_mainmenu_bassboost);
        mSbMainmenuBassboost.setOnSeekBarChangeListener(this);

        mLlMainmenuPresetReverb = (LinearLayout) mMenuView.findViewById(R.id
                .ll_mainmenu_presetreverb);
        mLlMainmenuPresetReverb.setOnClickListener(this);
        mTvMainmenuPresetReverb = (TextView) mMenuView.findViewById(R.id
                .tv_mainmenu_presetreverb);
        mIvMainmenuPresetReverb = (ImageView) mMenuView.findViewById(R.id
                .iv_mainmenu_presetreverb);
        mLlMainmenuPresetReverb_ = (LinearLayout) mMenuView.findViewById(R.id
                .ll_mainmenu_presetreverb_);
        mLvMainmenuPresetReverb = (ListView) mMenuView.findViewById(R.id
                .lv_mainmenu_presetreverb);
        //先隐藏音效部分
        mLlMainmenuPresetReverb_.measure(0, 0);
        mLvMainmenuPresetReverb_Height = Utils.dip2px(MainActivity.this, 150);
        ViewGroup.LayoutParams lp_ = mLlMainmenuPresetReverb_.getLayoutParams();
        lp_.height = 0;
        mLlMainmenuPresetReverb_.setLayoutParams(lp_);

        mBtnExit = (Button) mMenuView.findViewById(R.id.btn_exit);
        mBtnExit.setOnClickListener(this);
    }

    /**
     * @param i
     * @param seekBar
     */
    private void setSeekBarProgress (short i, SeekBar seekBar) {
        short bandLevel = mPlayer.mEqualizer.getBandLevel(i);
        bandLevel = (short) (bandLevel + Math.abs(mMinRange) * 100);
        seekBar.setProgress(bandLevel / (mMaxRange - mMinRange));
    }

    /**
     * @param i
     * @param tv
     */
    private void setCenterFreq (short i, TextView tv) {
        int centerFreq = mPlayer.mEqualizer.getCenterFreq(i);
        if (centerFreq > 1000 * 1000){
            double v = (double) centerFreq / (1000 * 1000);
            DecimalFormat decimalFormat = new DecimalFormat("0.0");
            String format = decimalFormat.format(v);
            tv.setText(format + "kHZ");
        } else{
            tv.setText((centerFreq / 1000) + "HZ");
        }
    }

    /**
     * @param list
     * @param musicInfo
     */
    private void remove (ArrayList<MusicInfo> list, MusicInfo musicInfo) {
        if (list != null){
            int size = list.size();
            for (int i = 0; i < size; i++){
                if (list.get(i).getDATA().equals(musicInfo.getDATA())){
                    list.remove(i);
                    return;
                }
            }
        }
    }

    /**
     * 删除歌曲
     *
     * @param musicInfo
     */
    public void deleteSong (MusicInfo musicInfo) {
        int size = mAllSongs.size();
        for (int i = 0; i < size; i++){
            if (mAllSongs.get(i).getDATA().equals(musicInfo.getDATA())){
                mAllSongs.remove(i);
                i--;
                size--;
            }
        }
        int artist_size = list_artist.size();
        for (int i = 0; i < artist_size; i++){
            if (list_artist.get(i).getListName().equals(Constant.MUSIC_LIST_ARTIST_
                    + musicInfo.getARTIST())){
                ArrayList<MusicInfo> list = songs_artist.get(list_artist.get(i).getListName());
                remove(list, musicInfo);
                if (list == null || list.size() == 0){
                    list_artist.remove(i);
                    artist_size--;
                    i--;
                }
            }
        }
        int album_size = list_album.size();
        for (int i = 0; i < album_size; i++){
            if (list_album.get(i).getListName().equals(Constant.MUSIC_LIST_ALBUM_
                    + musicInfo.getALBUM())){
                ArrayList<MusicInfo> list = songs_album.get(list_album.get(i).getListName());
                remove(list, musicInfo);
                if (list == null || list.size() == 0){
                    list_album.remove(i);
                    album_size--;
                    i--;
                }
            }
        }
        int data_size = list_data.size();
        for (int i = 0; i < data_size; i++){
            if ((Constant.MUSIC_LIST_DATA_ + musicInfo.getDATA())
                    .contains(list_data.get(i).getListName())){
                ArrayList<MusicInfo> list = songs_data.get(list_data.get(i).getListName());
                remove(list, musicInfo);
                if (list == null || list.size() == 0){
                    list_data.remove(i);
                    data_size--;
                    i--;
                }
            }
        }

        for (int i = 0; i < list_custom.size(); i++){
            String listName = list_custom.get(i).getListName();
            ArrayList<MusicInfo> list = songs_custom.get(listName);
            int s = list.size();
            for (int j = 0; j < s; j++){
                if (list.get(j).getTITLE().equals(musicInfo.getTITLE())){
                    Utils.deleteMusicInfo(MainActivity.this, listName, "TITLE = ?", new
                            String[]{list.get(j).getTITLE()});
                    list.remove(j);
                    j--;
                    s--;
                }
            }
        }

        Utils.deleteFile(musicInfo.getDATA());
        MediaScannerConnection.scanFile(MainActivity.this, new String[]{musicInfo.getDATA()},
                null, new MediaScannerConnection.OnScanCompletedListener() {
                    @Override
                    public void onScanCompleted (String path, Uri uri) {

                    }
                });
    }

    @Override
    public void onClick (View v) {
        switch (v.getId()){
        case R.id.v_main:
            if (isShowPopuWindow){
                dismissListPopuWindow();
            }
            break;
        case R.id.iv_main_music:
            if (mPlayer.mIsExist){
                startActivity(new Intent(MainActivity.this, PlayActivity.class));
                overridePendingTransition(R.anim.activity_slide_left_enter,
                        R.anim.activity_slide_left_exit);
            }
            break;
        case R.id.iv_music_go:
            if (mPlayer.mIsExist){
                mPlayer.playOrPause();
                EventBus.getDefault().post(Constant.UPDATE_FRAGMENT_LIST);
            }
            break;
        case R.id.iv_music_next:
            if (mPlayer.mIsExist){
                mPlayer.nextMusic();
                EventBus.getDefault().post(Constant.UPDATE_FRAGMENT_LIST);
            }
            break;
        case R.id.main_list:
            if (isShowPopuWindow){
                dismissListPopuWindow();
            }else {
                showListPopuWindow();
            }
            break;
        case R.id.iv_mainactivity_playmodel:
            switch (mPlayer.mMediaPlayer.getPlayList().getPlayModel()){
            case Constant.PLAYMODEL_ORDER:
                mIvMainactivityPlaymodel.setBackgroundResource(R.drawable.random_64px);
                mTvMainactivityPlaymodel.setText(Constant.PLAYMODEL_RANDOM);
                mPlayer.mMediaPlayer.getPlayList().setPlayModel(Constant.PLAYMODEL_RANDOM);
                break;
            case Constant.PLAYMODEL_RANDOM:
                mIvMainactivityPlaymodel.setBackgroundResource(R.drawable.cycle_64px);
                mTvMainactivityPlaymodel.setText(Constant.PLAYMODEL_CYCLE);
                mPlayer.mMediaPlayer.getPlayList().setPlayModel(Constant.PLAYMODEL_CYCLE);
                break;
            case Constant.PLAYMODEL_CYCLE:
                mIvMainactivityPlaymodel.setBackgroundResource(R.drawable.order_64px);
                mTvMainactivityPlaymodel.setText(Constant.PLAYMODEL_ORDER);
                mPlayer.mMediaPlayer.getPlayList().setPlayModel(Constant.PLAYMODEL_ORDER);
                break;
            }
            EventBus.getDefault().post(Constant.UPDATE_FRAGMENT_MODEL);
            break;
        case R.id.ll_mainmenu_equlizer:
            ValueAnimator va;
            if (mLlMainmenuEqulizerShow.getLayoutParams().height != mLlMainmenuEqulizerShow_Height){
                va = ValueAnimator.ofInt(0, mLlMainmenuEqulizerShow_Height);
                mIvMainmenuEqulizer.setImageResource(R.drawable.back_32px_down);
            } else{
                va = ValueAnimator.ofInt(mLlMainmenuEqulizerShow_Height, 0);
                mIvMainmenuEqulizer.setImageResource(R.drawable.back_32px_right);
            }
            va.setDuration(150);
            va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate (ValueAnimator animation) {
                    int value = (int) animation.getAnimatedValue();
                    ViewGroup.LayoutParams lp = mLlMainmenuEqulizerShow
                            .getLayoutParams();
                    lp.height = value;
                    mLlMainmenuEqulizerShow.setLayoutParams(lp);
                }
            });
            va.start();
            break;
        case R.id.ll_mainmenu_presetreverb:
            ValueAnimator va2;
            if (mLlMainmenuPresetReverb_.getLayoutParams().height != mLvMainmenuPresetReverb_Height){
                va2 = ValueAnimator.ofInt(0, mLvMainmenuPresetReverb_Height);
                mIvMainmenuPresetReverb.setImageResource(R.drawable.back_32px_down);
            } else{
                va2 = ValueAnimator.ofInt(mLvMainmenuPresetReverb_Height, 0);
                mIvMainmenuPresetReverb.setImageResource(R.drawable.back_32px_right);
            }
            va2.setDuration(150);
            va2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate (ValueAnimator animation) {
                    int value = (int) animation.getAnimatedValue();
                    ViewGroup.LayoutParams lp = mLlMainmenuPresetReverb_
                            .getLayoutParams();
                    lp.height = value;
                    mLlMainmenuPresetReverb_.setLayoutParams(lp);
                }
            });
            va2.start();
            break;
        case R.id.btn_exit:
            List<Activity> activityList = ActivityManager.getActivityManager().getActivityList();
            for (Activity activity : activityList){
                if (!activity.getComponentName().getPackageName().equals(this.getPackageName())){
                    activity.finish();
                }
            }
            Intent intent = new Intent(MainActivity.this, PlaySevice.class);
            intent.setAction("com.example.mrh.musicplayer.service.PlaySevice");
            stopService(intent);
            finish();
            break;
        }
    }

    private void changeBackground (float alpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = alpha;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setAttributes(lp);
    }

    /**
     * eventBus 处理的方法
     *
     * @param flag
     */
    @Subscribe(threadMode = ThreadMode.MAIN)       //主线程标识
    public void onEventMainThread (String flag) {
        switch (flag){
        case Constant.OK_INITSERVICE:
            initData();
            break;
        case Constant.UPDATE_MUSIC_START:
            mTvMusicName.setText(mPlayer.mMusicSongsname);
            mTvMusicArtist.setText(mPlayer.mMusicArtistname);
            mIvMusicGo.setBackgroundResource(R.drawable.pause_64px_normal);
            if (isShowPopuWindow){
                if (!isUserClick){
                    mConditionMap.put(prePosition, CONDITION_POPLIST_0);
                    mConditionMap.put(mPlayer.mPosition, CONDITION_POPLIST_1);
                    prePosition = mPlayer.mPosition;
                    mPopListAdapter.notifyDataSetChanged();
                    //设置位置
                    mLvMainactivityList.setSelectionFromTop(mPlayer.mPosition, Utils.dip2px
                            (MainActivity.this, 100));
                }
                isUserClick = false;
            }

            mOa.start();
            //开始后才可以调用
            mOa.setCurrentPlayTime(mCurrentPlayTime);
            break;
        case Constant.UPDATE_MUSIC_PAUSE:
            mIvMusicGo.setBackgroundResource(R.drawable.play_64px_normal);

            mCurrentPlayTime = mOa.getCurrentPlayTime();
            mOa.cancel();
            break;
        case Constant.UPDATE_MUSIC_RESET:

            break;
        case Constant.UPDATE_MUSIC_QUIT:

            break;
        case Constant.UPDATE_PLAY_RESET:
            mTvMusicName.setText("");
            mTvMusicArtist.setText("");
            mIvMusicGo.setBackgroundResource(R.drawable.pause_64px_normal);
            mSbMusicProcess.setProgress(0);
            mTvMusicProcess.setText("00:00");
            if (mOa.isRunning()){
                mCurrentPlayTime = mOa.getCurrentPlayTime();
                mOa.cancel();
            }
            break;
        case Constant.UPDATE_PREGRESS:
            mSbMusicProcess.setProgress(mPlayer.mMediaPlayer.getCurrentPosition() * 100 /
                    mPlayer.duration);
            mTvMusicProcess.setText(Utils.formatTime(mPlayer.mMediaPlayer.getCurrentPosition
                    ()));
            if (mPlayer.isRestartActivty){
                mPlayer.isRestartActivty = false;
                mOa.start();
                //开始后才可以调用
                mOa.setCurrentPlayTime(mCurrentPlayTime);
            }
            break;
        default:
            break;
        }
    }

    @Override
    public void onProgressChanged (SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()){
        case R.id.sb_mainmenu_01:
            mPlayer.mEqualizer.setBandLevel((short) 0, (short) (progress * (mMaxRange - mMinRange) +
                    mMinRange * 100));
            seekBarExtractSameCode(fromUser);
            break;
        case R.id.sb_mainmenu_02:
            mPlayer.mEqualizer.setBandLevel((short) 1, (short) (progress * (mMaxRange - mMinRange) +
                    mMinRange * 100));
            seekBarExtractSameCode(fromUser);
            break;
        case R.id.sb_mainmenu_03:
            mPlayer.mEqualizer.setBandLevel((short) 2, (short) (progress * (mMaxRange - mMinRange) +
                    mMinRange * 100));
            seekBarExtractSameCode(fromUser);
            break;
        case R.id.sb_mainmenu_04:
            mPlayer.mEqualizer.setBandLevel((short) 3, (short) (progress * (mMaxRange - mMinRange) +
                    mMinRange * 100));
            seekBarExtractSameCode(fromUser);
            break;
        case R.id.sb_mainmenu_05:
            mPlayer.mEqualizer.setBandLevel((short) 4, (short) (progress * (mMaxRange - mMinRange) +
                    mMinRange * 100));
            seekBarExtractSameCode(fromUser);
            break;
        case R.id.sb_mainmenu_bassboost:
            mPlayer.mBassBoost.setStrength((short) (progress * 10));
            break;
        }
    }

    /**
     * 抽取重复代码而已
     *
     * @param fromUser
     */
    private void seekBarExtractSameCode (boolean fromUser) {
        mPlayer.isEditEqulizerEffect = true;
        if (fromUser){
            mTvMainmenuPresetReverb.setText("自定义");
        }
    }

    @Override
    public void onStartTrackingTouch (SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch (SeekBar seekBar) {
        if (seekBar.getId() == R.id.sb_music_process && mPlayer.mIsExist){
            //拖动进度条
            int i = seekBar.getProgress() * mPlayer.duration / 100;
            mPlayer.mMediaPlayer.seekTo(i);
        }
    }

    @Override
    public boolean onKeyDown (int keyCode, KeyEvent event) {
        if (isShowPopuWindow){
            dismissListPopuWindow();
            return true;
        }else {
            return super.onKeyDown(keyCode, event);
        }
    }
}
