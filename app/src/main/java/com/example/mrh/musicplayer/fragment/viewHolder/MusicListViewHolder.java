package com.example.mrh.musicplayer.fragment.viewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mrh.musicplayer.R;

/**
* Created by MR.H on 2016/12/5 0005.
*/
public class MusicListViewHolder {
    public View rootView;
    public TextView mTvMusiclist;
    public TextView mTvMusicCount;
    public ImageView mIvMusiclistDelete;

   private MusicListViewHolder (View rootView) {
       this.rootView = rootView;
       this.mTvMusiclist = (TextView) rootView.findViewById(R.id.tv_musiclist);
       this.mTvMusicCount = (TextView) rootView.findViewById(R.id.tv_music_count);
       this.mIvMusiclistDelete = (ImageView) rootView.findViewById(R.id.iv_musiclist_delete);
       rootView.setTag(this);
   }
   public static MusicListViewHolder getViewHolder(View rootView){
       return new MusicListViewHolder(rootView);
   }
}
