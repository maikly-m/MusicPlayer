package com.example.mrh.musicplayer.fragment.viewHolder;

import android.view.View;
import android.widget.TextView;

import com.example.mrh.musicplayer.R;

/**
* Created by MR.H on 2016/12/5 0005.
*/
public class AllMusicVPViewHolder {
    public View rootView;
    public TextView mTvAllmusicvp;
    public TextView mTvAllmusicvpCount;

   private AllMusicVPViewHolder (View rootView) {
       this.rootView = rootView;
       this.mTvAllmusicvp = (TextView) rootView.findViewById(R.id.tv_allmusicvp);
       this.mTvAllmusicvpCount = (TextView) rootView.findViewById(R.id.tv_allmusicvp_count);
       rootView.setTag(this);
   }
   public static AllMusicVPViewHolder getViewHolder(View rootView){
       return new AllMusicVPViewHolder(rootView);
   }
}
