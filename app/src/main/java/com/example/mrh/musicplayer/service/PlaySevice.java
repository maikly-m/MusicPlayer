package com.example.mrh.musicplayer.service;

import android.app.Activity;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.audiofx.BassBoost;
import android.media.audiofx.Equalizer;
import android.media.audiofx.Visualizer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;

import com.example.mrh.musicplayer.R;
import com.example.mrh.musicplayer.activity.MainActivity;
import com.example.mrh.musicplayer.broadcast.UpdateMediaStoreReceiver;
import com.example.mrh.musicplayer.constant.Constant;
import com.example.mrh.musicplayer.custom.MyMediaPlayer;
import com.example.mrh.musicplayer.domain.MusicInfo;
import com.example.mrh.musicplayer.domain.MusicList;
import com.example.mrh.musicplayer.domain.PlayList;
import com.example.mrh.musicplayer.utils.DebugUtils;
import com.example.mrh.musicplayer.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by MR.H on 2016/12/2 0002.
 */

public class PlaySevice extends Service{

    private static final String TAG = "PlaySevice";
    private MusicBinder mBinder;
    public MyMediaPlayer mMediaPlayer;//MediaPlayer开了子线程额
    private PlayThread mPlayThread;
    public Handler mPlayHandler;
    public int mPosition = -1; //播放list中歌曲位置
    private List<MusicInfo> mList;
    private SoftReference<Activity> mActivity;
    public ArrayList<MusicInfo> mAllSongs;
    public ArrayList<MusicList> list_allsongs = new ArrayList<>();
    public ArrayList<MusicList> list_custom;
    public ArrayList<MusicList> list_artist;
    public ArrayList<MusicList> list_album;
    public ArrayList<MusicList> list_data;
    public HashMap<String, ArrayList<MusicInfo>> songs_all = new HashMap<>();
    public HashMap<String, ArrayList<MusicInfo>> songs_custom = new HashMap<>();
    public HashMap<String, ArrayList<MusicInfo>> songs_artist;
    public HashMap<String, ArrayList<MusicInfo>> songs_album;
    public HashMap<String, ArrayList<MusicInfo>> songs_data;
    private boolean dataReady = false;
    public String mMusicListname;
    public boolean mMusicPlaycondition;
    public String mMusicPlaymodel = Constant.PLAYMODEL_ORDER;
    public int mMusicPlaytime;
    public String mMusicSongsname;
    public String mMusicArtistname;
    public boolean mIsExist = false;
    public int duration = 0;
    private ProgressThread mProgressThread;
    private Handler mProgressHandler;
    public boolean isRestartActivty = false; //重新进入MainActivity
    private BroadcastReceiver mReceiver;
    public Equalizer mEqualizer;
    public BassBoost mBassBoost;
    public Visualizer mVisualizer;
    /**
     * 手动改变了均衡器
     */
    public boolean isEditEqulizerEffect = false;
    public int mPresetReverbName = 0; //预设的音效
    /**
     * sp中取出的isEditEqulizerEffect
     */
    public boolean mMusicEffectEqulizer;
    public int mMusicEffectEqulizer01;
    public int mMusicEffectEqulizer02;
    public int mMusicEffectEqulizer03;
    public int mMusicEffectEqulizer04;
    public int mMusicEffectEqulizer05;
    public int mMusicEffectPresetreverb;
    public int mMusicEffectBassboost;

    @Nullable
    @Override
    public IBinder onBind (Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate () {
        super.onCreate();
        EventBus.getDefault().register(this);
        //注册广播接收器更新媒体库
        mReceiver = new UpdateMediaStoreReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_MEDIA_SCANNER_FINISHED);
        filter.addDataScheme("file");
        registerReceiver(mReceiver, filter);
        //网络下载后需要更新mediastore
//        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
//                Uri.parse("file://" + Environment.getExternalStorageDirectory())));
        initData();
        init();
        mBinder = new MusicBinder(this);
    }

    /**
     * 初始化data
     */
    private void initData () {
        new Thread() {
            @Override
            public void run () {
                mAllSongs = Utils.getAllSongs();
                list_allsongs.add(new MusicList(Constant.MUSIC_LIST_ALLSONGS_+"所有音乐", mAllSongs.size
                        ()));
                songs_all.put(Constant.MUSIC_LIST_ALLSONGS_+"所有音乐", mAllSongs);
                list_custom = Utils.getList(PlaySevice.this, Constant.CUSTOM_LIST, null, null);
                //自定义列表获取数据
                if (list_custom.size() != 0 && mAllSongs.size() != 0){
                    for (MusicList m : list_custom){
                        ArrayList<MusicInfo> transfer = new ArrayList<>();
                        ArrayList<MusicInfo> musicInfo = Utils.getMusicInfo(PlaySevice.this, m
                                .getListName(), null, null);
                        for (int i = 0; i < mAllSongs.size(); i++){
                            for (int j = 0; j < musicInfo.size(); j++){
                                //判断文件是否还存在
                                if (mAllSongs.get(i).getDATA().equals(musicInfo.get(j).getDATA())){
                                    transfer.add(musicInfo.get(j));
                                }
                            }
                        }
                        songs_custom.put(m.getListName(), transfer);
                    }
                }
                //系统列表获取数据,每次开启服务就重建一次
                if (mAllSongs.size() != 0){
                    HashMap<String, Object> hashMap1 = Utils.sortAndCreateList(mAllSongs, "artist");
                    list_artist= (ArrayList<MusicList>) hashMap1.get("listName");
                    songs_artist = (HashMap<String, ArrayList<MusicInfo>>) hashMap1.get("list");

                    HashMap<String, Object> hashMap2 = Utils.sortAndCreateList(mAllSongs, "album");
                    list_album= (ArrayList<MusicList>) hashMap2.get("listName");
                    songs_album = (HashMap<String, ArrayList<MusicInfo>>) hashMap2.get("list");

                    HashMap<String, Object> hashMap3 = Utils.sortAndCreateList(mAllSongs, "data");
                    list_data= (ArrayList<MusicList>) hashMap3.get("listName");
                    songs_data = (HashMap<String, ArrayList<MusicInfo>>) hashMap3.get("list");
                }
                getSpData();
                initMediaEffect();
                dataReady = true;
                EventBus.getDefault().post(Constant.OK_DATA);
            }
        }.start();

    }

    private void initMediaEffect () {
        if (mMusicEffectEqulizer){
            mEqualizer.setBandLevel((short) 0, (short) mMusicEffectEqulizer01);
            mEqualizer.setBandLevel((short) 1, (short) mMusicEffectEqulizer02);
            mEqualizer.setBandLevel((short) 2, (short) mMusicEffectEqulizer03);
            mEqualizer.setBandLevel((short) 3, (short) mMusicEffectEqulizer04);
            mEqualizer.setBandLevel((short) 4, (short) mMusicEffectEqulizer05);
        }else {
            mEqualizer.usePreset((short) mMusicEffectPresetreverb);
        }
        mBassBoost.setStrength((short) mMusicEffectBassboost);
    }

    private void init () {
        mPlayThread = new PlayThread(getString(R.string.PlayThread));
        mPlayThread.start();
        //初始化播放器
        mMediaPlayer = new MyMediaPlayer();
        //设置均衡器
        setEqualizer();
        setBassBoost();
        setVisualizer();
        mPlayHandler = new Handler(mPlayThread.getLooper(), new Handler.Callback() {
            @Override
            public boolean handleMessage (Message msg) {
                Message message = Message.obtain();
                switch (msg.what){
                case MyMediaPlayer.START:
                    //发出更新进度消息
                    message.what = ProgressThread.START;
                    mMediaPlayer.isStart = true;
                    mMediaPlayer.start();
                    ProgressThread.flag = true;
                    mProgressHandler.sendMessage(message);
                    EventBus.getDefault().post(Constant.UPDATE_PREGRESS_ROTATE);
                    EventBus.getDefault().post(Constant.UPDATE);
                    EventBus.getDefault().post(Constant.UPDATE_MUSIC_START);
                    break;
                case MyMediaPlayer.STOP:
                    //发出暂停更新进度消息
                    mMediaPlayer.stop();
                    ProgressThread.flag = false;
                    EventBus.getDefault().post(Constant.UPDATE_PREGRESS_ROTATE);
                    EventBus.getDefault().post(Constant.UPDATE);
                    break;
                case MyMediaPlayer.PAUSE:
                    mMediaPlayer.pause();
                    ProgressThread.flag = false;
                    EventBus.getDefault().post(Constant.UPDATE_PREGRESS_ROTATE);
                    EventBus.getDefault().post(Constant.UPDATE);
                    EventBus.getDefault().post(Constant.UPDATE_MUSIC_PAUSE);
                    break;
                case MyMediaPlayer.QUIT:
                    //结束进程
                    ProgressThread.flag = false;
                    mEqualizer.release();
                    mBassBoost.release();
                    mVisualizer.release();
                    mMediaPlayer.release();
                    mPlayThread.quit();
                    mProgressThread.quit();
                    EventBus.getDefault().post(Constant.UPDATE_PREGRESS_ROTATE);
                    EventBus.getDefault().post(Constant.UPDATE);
                    break;
                case MyMediaPlayer.RESET:
                    ProgressThread.flag = false;
                    mMediaPlayer.reset();
                    mMediaPlayer.isStart = false;
                    EventBus.getDefault().post(Constant.UPDATE_PREGRESS_ROTATE);
                    EventBus.getDefault().post(Constant.UPDATE);
                    EventBus.getDefault().post(Constant.UPDATE_MUSIC_PAUSE);
                    break;
                case MyMediaPlayer.PLAY_RESET:
                    ProgressThread.flag = false;
                    mMediaPlayer.reset();
                    mMediaPlayer.isStart = false;
                    mIsExist = false;
                    EventBus.getDefault().post(Constant.UPDATE_PLAY_RESET);
                    break;
                }

                return false;
            }
        });
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion (MediaPlayer mp) {
                nextMusic();
                EventBus.getDefault().post(Constant.UPDATE_FRAGMENT_LIST);
            }
        });
        mProgressThread = new ProgressThread(getString(R.string.ProgressThread));
        mProgressThread.start();
        mProgressHandler = new Handler(mProgressThread.getLooper(), new Handler.Callback() {
            @Override
            public boolean handleMessage (Message msg) {
                switch (msg.what){
                case ProgressThread.START:
                    while (ProgressThread.flag){
                        EventBus.getDefault().post(Constant.UPDATE_PREGRESS);
                        try{
                            Thread.sleep(500);
                        } catch (InterruptedException e){
                            e.printStackTrace();
                        }
                    }
                    break;
                }
                return false;
            }
        });
    }

    /**
     * 播放下一首
     */
    public void nextMusic () {
        Message message = Message.obtain();
        message.what = MyMediaPlayer.RESET;
        mPlayHandler.sendMessage(message);
        switch (mMediaPlayer.getPlayList().getPlayModel()){
        case Constant.PLAYMODEL_ORDER:
            if (mPosition >= 0 && mPosition < mList.size()-1){
                setSongPath(++mPosition);
                MusicInfo musicInfo = mList.get(mPosition);
                mMusicSongsname = musicInfo.getTITLE();
                mMusicArtistname = musicInfo.getARTIST();
                duration = Integer.valueOf(musicInfo.getDURATION());
            }
            break;
        case Constant.PLAYMODEL_RANDOM:
            int p = (int) (Math.random() * (mList.size()-1) + 0.5f);
            setSongPath(p);
            MusicInfo musicInfo = mList.get(p);
            mMusicSongsname = musicInfo.getTITLE();
            mMusicArtistname = musicInfo.getARTIST();
            duration = Integer.valueOf(musicInfo.getDURATION());
            mPosition = p;
            break;
        case Constant.PLAYMODEL_CYCLE:
            if (mPosition >= 0 && mPosition < mList.size()-1){
                setSongPath(++mPosition);
            }else {
                mPosition = 0;
                setSongPath(mPosition);
            }
            MusicInfo m = mList.get(mPosition);
            mMusicSongsname = m.getTITLE();
            mMusicArtistname = m.getARTIST();
            duration = Integer.valueOf(m.getDURATION());
            break;
        }
    }

    /**
     * 播放上一首
     */
    public void preMusic () {
        Message message = Message.obtain();
        message.what = MyMediaPlayer.RESET;
        mPlayHandler.sendMessage(message);
        switch (mMediaPlayer.getPlayList().getPlayModel()){
        case Constant.PLAYMODEL_ORDER:
            if (mPosition >= 0 && mPosition < mList.size()-1){
                setSongPath(--mPosition);
                MusicInfo musicInfo = mList.get(mPosition);
                mMusicSongsname = musicInfo.getTITLE();
                mMusicArtistname = musicInfo.getARTIST();
                duration = Integer.valueOf(musicInfo.getDURATION());
            }
            break;
        case Constant.PLAYMODEL_RANDOM:
            int p = (int) (Math.random() * (mList.size()-1) + 0.5f);
            setSongPath(p);
            MusicInfo musicInfo = mList.get(p);
            mMusicSongsname = musicInfo.getTITLE();
            mMusicArtistname = musicInfo.getARTIST();
            duration = Integer.valueOf(musicInfo.getDURATION());
            mPosition = p;
            break;
        case Constant.PLAYMODEL_CYCLE:
            if (mPosition >= 0 && mPosition < mList.size()-1){
                setSongPath(--mPosition);
            }else {
                mPosition = 0;
                setSongPath(mPosition);
            }
            MusicInfo m = mList.get(mPosition);
            mMusicSongsname = m.getTITLE();
            mMusicArtistname = m.getARTIST();
            duration = Integer.valueOf(m.getDURATION());
            break;
        }
    }
    //从sp中获取数据
    public void getSpData () {
        //取出播放状态
        SharedPreferences sp = getSharedPreferences(Constant.MUSIC_LIST, Context.MODE_PRIVATE);
        mMusicListname = sp.getString(Constant.MUSIC_LISTNAME, "");
        if (!mMusicListname.equals("")){
            mMusicPlaycondition = sp.getBoolean(Constant.MUSIC_PLAYCONDITION, false);
            mMusicPlaymodel = sp.getString(Constant.MUSIC_PLAYMODEL, Constant.PLAYMODEL_ORDER);
            mMusicPlaytime = sp.getInt(Constant.MUSIC_PLAYTIME, 0);
            mMusicSongsname = sp.getString(Constant.MUSIC_SONGSNAME, "");
            mMusicArtistname = sp.getString(Constant.MUSIC_ARTISTNAME, "");
        }
        //取出播放效果
        SharedPreferences sp1 = getSharedPreferences(Constant.MUSIC_EFFECT, Context
                .MODE_PRIVATE);
        mMusicEffectEqulizer = sp1.getBoolean(Constant.MUSIC_EFFECT_EQULIZER, false);
        if (mMusicEffectEqulizer){
            mMusicEffectEqulizer01 = sp1.getInt(Constant.MUSIC_EFFECT_EQULIZER_01, 0);
            mMusicEffectEqulizer02 = sp1.getInt(Constant.MUSIC_EFFECT_EQULIZER_02, 0);
            mMusicEffectEqulizer03 = sp1.getInt(Constant.MUSIC_EFFECT_EQULIZER_03, 0);
            mMusicEffectEqulizer04 = sp1.getInt(Constant.MUSIC_EFFECT_EQULIZER_04, 0);
            mMusicEffectEqulizer05 = sp1.getInt(Constant.MUSIC_EFFECT_EQULIZER_05, 0);
        }else {
            mMusicEffectPresetreverb = sp1.getInt(Constant.MUSIC_EFFECT_PRESETREVERB, 0);
        }
        mMusicEffectBassboost = sp1.getInt(Constant.MUSIC_EFFECT_BASSBOOST, 0);
    }

    private void setSongPath (int position) {
        Message message = Message.obtain();
        message.what = MyMediaPlayer.START;
        final String path = mList.get(position).getDATA();
        mPlayHandler.post(new Runnable() {
            @Override
            public void run () {
                try{
                    mMediaPlayer.setDataSource(path);
                    mMediaPlayer.prepare();
                } catch (IOException e){
                    e.printStackTrace();
                }
            }
        });
        mPlayHandler.sendMessage(message);
    }

    /**
     * playOrPause
     */
    public void playOrPause(){
        if (ProgressThread.flag){
            pauseMusic();
        }else {
            playMusic();
        }
    }
    /**
     * pause
     */
    public void pauseMusic(){
        Message message = Message.obtain();
        message.what = MyMediaPlayer.PAUSE;
        mPlayHandler.sendMessage(message);
    }

    /**
     * play
     */
    public void playMusic(){
        if (mMediaPlayer.isStart){
            Message message = Message.obtain();
            message.what = MyMediaPlayer.START;
            mPlayHandler.sendMessage(message);
        } else{
            nextMusic();
        }
    }

    /**
     * stop
     */
    public void stopMusic(){
        Message message = Message.obtain();
        message.what = MyMediaPlayer.STOP;
        mPlayHandler.sendMessage(message);
    }

    /**
     * reset
     */
    public void resetPlayMusic (){
        Message message = Message.obtain();
        message.what = MyMediaPlayer.PLAY_RESET;
        mPlayHandler.sendMessage(message);
    }
    /**
     * 定位歌曲播放位置,初始化调用
     * @param position
     */
    public void seekMusic(int position, final int msec){
        MusicInfo musicInfo = mList.get(position);
        final String path = musicInfo.getDATA();
        mPlayHandler.post(new Runnable() {
            @Override
            public void run () {
                try{
                    mMediaPlayer.setDataSource(path);
                    mMediaPlayer.prepare();
                    mMediaPlayer.seekTo(msec);
                } catch (IOException e){
                    e.printStackTrace();
                }
            }
        });
        mMediaPlayer.isStart = true;
        this.mPosition = position;
    }

    /**
     * 播放音乐
     * @param position
     * @param listName
     */
    public void startMusic (int position, String listName){
        mIsExist = true;
        //判断是否为同一张列表
        Message message = Message.obtain();
        if (listName.equals(this.mMusicListname)){
            //判断是否为同一位置
            if (this.mPosition == -1 || position == this.mPosition){
                if (!mMediaPlayer.isStart){
                    //开始播放
                    mList = mMediaPlayer.getPlayList().getList();//播放列表
                    MusicInfo musicInfo = mList.get(position);
                    this.mMusicSongsname = musicInfo.getTITLE();
                    this.mMusicArtistname = musicInfo.getARTIST();
                    duration = Integer.valueOf(musicInfo.getDURATION());
                    setSongPath(position);
                    this.mPosition = position;
                }else if (!mMediaPlayer.isPlaying()){
                    //暂停状态，开始播放
                    message.what = MyMediaPlayer.START;
                    mPlayHandler.sendMessage(message);
                }else if(mMediaPlayer.isPlaying()) {
                    //播放状态，点击暂停
                    message.what = MyMediaPlayer.PAUSE;
                    mPlayHandler.sendMessage(message);
                }
            }else {
                //不同的歌曲
                MusicInfo musicInfo = mList.get(position);
                this.mMusicSongsname = musicInfo.getTITLE();
                this.mMusicArtistname = musicInfo.getARTIST();
                duration = Integer.valueOf(musicInfo.getDURATION());
                message.what = MyMediaPlayer.RESET;
                mPlayHandler.sendMessage(message);
                setSongPath(position);
                this.mPosition = position;
            }
        }else {
            //另一列表的歌曲
            mList = mMediaPlayer.getPlayList().getList();//播放列表
            MusicInfo musicInfo = mList.get(position);
            this.mMusicListname = listName;
            this.mMusicSongsname = musicInfo.getTITLE();
            this.mMusicArtistname = musicInfo.getARTIST();
            duration = Integer.valueOf(musicInfo.getDURATION());
            message.what = MyMediaPlayer.RESET;
            mPlayHandler.sendMessage(message);
            setSongPath(position);
            this.mPosition = position;
        }
    }

    @Override
    public int onStartCommand (Intent intent, int flags, int startId) {
        DebugUtils.log_d(TAG, "onStartCommand");
        //加载已有的缓存,即重新进入MainActivity
        if (dataReady){
            isRestartActivty = true;
            EventBus.getDefault().post(Constant.OK_DATA);
        }
        //保证kill掉后不重启
        onStart(intent, startId);
        return Service.START_NOT_STICKY;
    }

    @Override
    public void onDestroy () {
        super.onDestroy();
        //正常退出保存，直接kill掉虚拟机不保存
        saveData();
        EventBus.getDefault().unregister(this);
        //反注册receiver
        unregisterReceiver(mReceiver);
        Message message = Message.obtain();
        message.what = MyMediaPlayer.QUIT;
        mPlayHandler.sendMessage(message);
        DebugUtils.log_d(TAG, "destroy");
    }

    private void saveData () {
        //存播放状态
        if (mMediaPlayer.getPlayList() != null){
            SharedPreferences sp = getSharedPreferences(Constant.MUSIC_LIST, Context
                    .MODE_PRIVATE);
            SharedPreferences.Editor edit = sp.edit();
            edit.putString(Constant.MUSIC_LISTNAME, mMediaPlayer.getPlayList().getListName());
            edit.putBoolean(Constant.MUSIC_PLAYCONDITION, mMediaPlayer.isPlaying());
            edit.putString(Constant.MUSIC_PLAYMODEL, mMediaPlayer.getPlayList().getPlayModel());
            edit.putInt(Constant.MUSIC_PLAYTIME, mMediaPlayer.getCurrentPosition());
            edit.putString(Constant.MUSIC_SONGSNAME, mMediaPlayer.getPlayList().getList().get
                    (mPosition).getTITLE());
            edit.putString(Constant.MUSIC_ARTISTNAME, mMediaPlayer.getPlayList().getList().get
                    (mPosition).getARTIST());
            edit.apply();
        }
        //存播放效果
        SharedPreferences sp = getSharedPreferences(Constant.MUSIC_EFFECT, Context
                .MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        if (isEditEqulizerEffect){
            edit.putBoolean(Constant.MUSIC_EFFECT_EQULIZER, true);
            edit.putInt(Constant.MUSIC_EFFECT_EQULIZER_01, mEqualizer.getBandLevel((short) 0));
            edit.putInt(Constant.MUSIC_EFFECT_EQULIZER_02, mEqualizer.getBandLevel((short) 1));
            edit.putInt(Constant.MUSIC_EFFECT_EQULIZER_03, mEqualizer.getBandLevel((short) 2));
            edit.putInt(Constant.MUSIC_EFFECT_EQULIZER_04, mEqualizer.getBandLevel((short) 3));
            edit.putInt(Constant.MUSIC_EFFECT_EQULIZER_05, mEqualizer.getBandLevel((short) 4));
        }else {
            edit.putBoolean(Constant.MUSIC_EFFECT_EQULIZER, false);
            edit.putInt(Constant.MUSIC_EFFECT_PRESETREVERB, mPresetReverbName);
        }
        edit.putInt(Constant.MUSIC_EFFECT_BASSBOOST, mBassBoost.getRoundedStrength());
        edit.apply();
    }

    @Override
    public boolean onUnbind (Intent intent) {
        DebugUtils.log_d(TAG, "onunbind");
        return super.onUnbind(intent);

    }
    public Activity getActivity () {
        return mActivity.get();
    }

    public void setActivity (Activity activity) {
        mActivity = new SoftReference<>(activity);
    }

    /**
     * 设置示波器
     */
    private void setVisualizer () {
        mVisualizer = new Visualizer(mMediaPlayer.getAudioSessionId());
        mVisualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[1]);
    }

    /**
     * 设置均衡器
     */
    private void setEqualizer(){
        mEqualizer = new Equalizer(0, mMediaPlayer.getAudioSessionId());
        mEqualizer.setEnabled(true); //开始起作用
    }

    /**
     * 设置重低音
     */
    private void setBassBoost () {
        mBassBoost = new BassBoost(0, mMediaPlayer.getAudioSessionId());
        mBassBoost.setEnabled(true);
    }

    /**
     * eventBus 处理的方法
     *
     * @param flag
     */
    @Subscribe(threadMode = ThreadMode.MAIN)       //主线程标识
    public void onEventMainThread (String flag) {
        if (flag.equals(Constant.UPDATE_INIT)){
            if (!mIsExist){
                if (!mMusicListname.equals("")){
                    //存在列表
                    ArrayList<MusicInfo> list = null;
                    if (mMusicListname.contains(Constant.MUSIC_LIST_ALLSONGS_)){
                        list = songs_all.get(mMusicListname);
                    } else if (mMusicListname.contains(Constant.MUSIC_LIST_CUSTOM_)){
                        list = songs_custom.get(mMusicListname);
                    } else if (mMusicListname.contains(Constant.MUSIC_LIST_ARTIST_)){
                        list = songs_artist.get(mMusicListname);
                    } else if (mMusicListname.contains(Constant.MUSIC_LIST_ALBUM_)){
                        list = songs_album.get(mMusicListname);
                    } else if (mMusicListname.contains(Constant.MUSIC_LIST_DATA_)){
                        list = songs_data.get(mMusicListname);
                    }
                    if (list != null){
                        for (int i=0; i < list.size(); i++){
                            if (list.get(i).getTITLE().equals(mMusicSongsname)){
                                //存在上次播放的歌曲
                                mIsExist = true;
                                PlayList playList = new PlayList();
                                playList.setListName(mMusicListname);
                                playList.setList(list);
                                playList.setPlayModel(mMusicPlaymodel);
                                mMediaPlayer.setPlayList(playList);
                                mList = list;
                                mPosition = i;
                                duration = Integer.valueOf(list.get(i).getDURATION());
                                seekMusic(i, mMusicPlaytime);
                                break;
                            }
                        }
                    }
                }
            }
            MainActivity mainActivity = (MainActivity) getActivity();
            if (mIsExist){
                mainActivity.mTvMusicName.setText(mMusicSongsname);
                mainActivity.mTvMusicArtist.setText(mMusicArtistname);
                int process = mMusicPlaytime * 100 / duration;
                mainActivity.mSbMusicProcess.setProgress(process);
                mainActivity.mTvMusicProcess.setText(Utils.formatTime(mMusicPlaytime));
            }else {
                mainActivity.mTvMusicName.setText("");
                mainActivity.mTvMusicArtist.setText("");
            }
            mainActivity.setVolumeControlStream(AudioManager.STREAM_MUSIC);//可以控制音量
        }
    }
}