package com.example.mrh.musicplayer.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.view.View;
import android.widget.Button;

import com.example.mrh.musicplayer.R;
import com.example.mrh.musicplayer.custom.MyMediaPlayer;
import com.example.mrh.musicplayer.service.MusicBinder;
import com.example.mrh.musicplayer.utils.DebugUtils;

public class PlayActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "PlayActivity";
    private MusicBinder musicBinder;
    private Button mBtnPlay;
    private Button mBtnStop;
    private Button mBtnPause;
    private Button mBtnGoto;
    private ServiceConnection mServiceConnection;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        initView();
        initService();
    }

    private void initView () {
        mBtnPlay = (Button) findViewById(R.id.btn_play);
        mBtnPlay.setOnClickListener(this);
        mBtnStop = (Button) findViewById(R.id.btn_stop);
        mBtnStop.setOnClickListener(this);
        mBtnPause = (Button) findViewById(R.id.btn_pause);
        mBtnPause.setOnClickListener(this);
        mBtnGoto = (Button) findViewById(R.id.btn_goto);
        mBtnGoto.setOnClickListener(this);
    }

    private void initService () {
        Intent intent = new Intent("com.example.mrh.musicplayer.service.PlaySevice");
        intent.setPackage("com.example.mrh.musicplayer.service.PlaySevice");
        mServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected (ComponentName name, IBinder service) {
                Binder mService = (Binder) service;
                musicBinder = (MusicBinder) mService;
            }

            @Override
            public void onServiceDisconnected (ComponentName name) {

            }
        };
        bindService(intent, mServiceConnection, BIND_AUTO_CREATE);
    }

    public void onClick (View view) {
        Message message = Message.obtain();
        switch (view.getId()){
        case R.id.btn_play:
            message.what = MyMediaPlayer.START;
            musicBinder.mPlayer.mPlayHandler.sendMessage(message);
            break;
        case R.id.btn_stop:
            message.what = MyMediaPlayer.STOP;
            musicBinder.mPlayer.mPlayHandler.sendMessage(message);
            break;
        case R.id.btn_pause:
            message.what = MyMediaPlayer.PAUSE;
            musicBinder.mPlayer.mPlayHandler.sendMessage(message);
            break;
        case R.id.btn_goto:
            Intent intent = new Intent(PlayActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
            break;
        }
    }

    @Override
    protected void onDestroy () {
        super.onDestroy();
        unbindService(mServiceConnection);
        DebugUtils.log_d(TAG, "dddddddddd");
    }
}
