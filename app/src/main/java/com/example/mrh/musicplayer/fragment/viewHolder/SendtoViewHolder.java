package com.example.mrh.musicplayer.fragment.viewHolder;

import android.view.View;
import android.widget.TextView;

import com.example.mrh.musicplayer.R;

/**
 * Created by MR.H on 2016/12/22 0022.
 */

public class SendtoViewHolder {
    public View rootView;
    public TextView mTvSongslistSendtoListname;

    private SendtoViewHolder (View rootView) {
        this.rootView = rootView;
        this.mTvSongslistSendtoListname = (TextView) rootView.findViewById(R.id
                .tv_songslist_sendto_listname);
        rootView.setTag(this);
    }
    public static SendtoViewHolder getViewHolder(View rootView){
        return new SendtoViewHolder(rootView);
    }
}
