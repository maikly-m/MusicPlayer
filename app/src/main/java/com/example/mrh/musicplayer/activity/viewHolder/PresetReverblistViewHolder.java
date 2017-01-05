package com.example.mrh.musicplayer.activity.viewHolder;

import android.view.View;
import android.widget.TextView;

import com.example.mrh.musicplayer.R;

/**
* Created by MR.H on 2016/12/5 0005.
*/
public class PresetReverblistViewHolder {
    public View rootView;
    public TextView mTvMainmenuPresetreverb_;

   private PresetReverblistViewHolder (View rootView) {
       this.rootView = rootView;
       this.mTvMainmenuPresetreverb_ = (TextView) rootView.findViewById(R.id
               .tv_mainmenu_presetreverb_);
       rootView.setTag(this);
   }
   public static PresetReverblistViewHolder getViewHolder(View rootView){
       return new PresetReverblistViewHolder(rootView);
   }
}
