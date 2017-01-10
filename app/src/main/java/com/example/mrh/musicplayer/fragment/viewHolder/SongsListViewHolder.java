package com.example.mrh.musicplayer.fragment.viewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mrh.musicplayer.R;
import com.example.mrh.musicplayer.custom.MyImageView;

/**
* Created by MR.H on 2016/12/5 0005.
*/
public class SongsListViewHolder {
    public View rootView;
    public LinearLayout mLlListsplay;
    public View mVListsplayPlay;
    public ImageView mIvListsplaySelect;
    public TextView mTvListsplayOrder;
    public ImageView mTvListsplayIcon;
    public TextView mTvListsplayTitle;
    public TextView mTvListsplaySinger;
    public MyImageView mIvListsplayMore;
    public LinearLayout mLlListsplayDelete;
    public LinearLayout mLlListsplayLove;
    public ImageView mIvListsplayLove;
    public LinearLayout mLlListsplayDetail;
    public LinearLayout mLlListsplayView;

   private SongsListViewHolder (View rootView) {
       this.rootView = rootView;
       this.mLlListsplay = (LinearLayout) rootView.findViewById(R.id.ll_listsplay_);
       this.mVListsplayPlay = (View) rootView.findViewById(R.id.v_listsplay_play);
       this.mIvListsplaySelect = (ImageView) rootView.findViewById(R.id.iv_listsplay_select);
       this.mTvListsplayOrder = (TextView) rootView.findViewById(R.id.tv_listsplay_order);
       this.mTvListsplayIcon = (ImageView) rootView.findViewById(R.id.iv_listsplay_icon);
       this.mTvListsplayTitle = (TextView) rootView.findViewById(R.id.tv_listsplay_title);
       this.mTvListsplaySinger = (TextView) rootView.findViewById(R.id.tv_listsplay_singer);
       this.mIvListsplayMore = (MyImageView) rootView.findViewById(R.id.iv_listsplay_more);
       this.mLlListsplayDelete = (LinearLayout) rootView.findViewById(R.id
               .ll_listsplay_delete);
       this.mLlListsplayLove = (LinearLayout) rootView.findViewById(R.id
               .ll_listsplay_love);
       this.mIvListsplayLove = (ImageView) rootView.findViewById(R.id
               .iv_listplay_love);
       this.mLlListsplayDetail = (LinearLayout) rootView.findViewById(R.id
               .ll_listsplay_detail);
       this.mLlListsplayView = (LinearLayout) rootView.findViewById(R.id.ll_listsplay_view);
       rootView.setTag(this);
   }
   public static SongsListViewHolder getViewHolder(View rootView){
       return new SongsListViewHolder(rootView);
   }
}
