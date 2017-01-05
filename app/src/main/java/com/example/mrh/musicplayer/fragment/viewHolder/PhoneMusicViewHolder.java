package com.example.mrh.musicplayer.fragment.viewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mrh.musicplayer.R;

/**
* Created by MR.H on 2016/12/5 0005.
*/
public class PhoneMusicViewHolder {
    public View rootView;
    public ImageView mIvPhoneMusicSelect;
    public TextView mTvPhoneMusicTitle;
    public TextView mTvPhoneMusicSinger;

   private PhoneMusicViewHolder (View rootView) {
       this.rootView = rootView;
       this.mIvPhoneMusicSelect = (ImageView) rootView.findViewById(R.id.iv_phonemusic_select);
       this.mTvPhoneMusicTitle = (TextView) rootView.findViewById(R.id.tv_phonemusic_title);
       this.mTvPhoneMusicSinger = (TextView) rootView.findViewById(R.id.tv_phonemusic_singer);
       rootView.setTag(this);
   }
   public static PhoneMusicViewHolder getViewHolder(View rootView){
       return new PhoneMusicViewHolder(rootView);
   }
}
