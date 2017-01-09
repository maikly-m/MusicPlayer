package com.example.mrh.musicplayer.activity.viewHolder;

import android.view.View;
import android.widget.TextView;

import com.example.mrh.musicplayer.R;

/**
* Created by MR.H on 2016/12/5 0005.
*/
public class ShowlistViewHolder {
    public View rootView;
    public View mVPlaypopPlay;
    public TextView mTvPlaypopOrder;
    public TextView mTvPlaypopListname;
    public TextView mTvPlaylistArtist;

   private ShowlistViewHolder (View rootView) {
       this.rootView = rootView;
       this.mVPlaypopPlay = (View) rootView.findViewById(R.id.v_playpop_play);
       this.mTvPlaypopOrder = (TextView) rootView.findViewById(R.id.tv_playpop_order);
       this.mTvPlaypopListname = (TextView) rootView.findViewById(R.id.tv_playpop_listname);
       this.mTvPlaylistArtist = (TextView) rootView.findViewById(R.id.tv_playlist_artist);
       rootView.setTag(this);
   }
   public static ShowlistViewHolder getViewHolder(View rootView){
       return new ShowlistViewHolder(rootView);
   }
}
