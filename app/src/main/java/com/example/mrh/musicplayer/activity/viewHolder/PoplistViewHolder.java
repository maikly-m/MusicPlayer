package com.example.mrh.musicplayer.activity.viewHolder;

import android.view.View;
import android.widget.TextView;

import com.example.mrh.musicplayer.R;

/**
* Created by MR.H on 2016/12/5 0005.
*/
public class PoplistViewHolder {
    public View rootView;
    public View mVMainpopPlay;
    public TextView mTvMainpopOrder;
    public TextView mTvMainpopListname;
    public TextView mTvMainlistArtist;

   private PoplistViewHolder (View rootView) {
       this.rootView = rootView;
       this.mVMainpopPlay = (View) rootView.findViewById(R.id.v_mainpop_play);
       this.mTvMainpopOrder = (TextView) rootView.findViewById(R.id.tv_mainpop_order);
       this.mTvMainpopListname = (TextView) rootView.findViewById(R.id.tv_mainpop_listname);
       this.mTvMainlistArtist = (TextView) rootView.findViewById(R.id.tv_mainlist_artist);
       rootView.setTag(this);
   }
   public static PoplistViewHolder getViewHolder(View rootView){
       return new PoplistViewHolder(rootView);
   }
}
