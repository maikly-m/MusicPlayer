package com.example.mrh.musicplayer.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by MR.H on 2016/12/25 0025.
 */

public class UpdateMediaStoreReceiver extends BroadcastReceiver{
    @Override
    public void onReceive (Context context, Intent intent) {
        if (Intent.ACTION_MEDIA_SCANNER_FINISHED.equals(intent.getAction())){

        }
    }
}
