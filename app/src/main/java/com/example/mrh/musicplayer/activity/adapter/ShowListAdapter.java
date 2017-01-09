package com.example.mrh.musicplayer.activity.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.mrh.musicplayer.R;
import com.example.mrh.musicplayer.activity.PlayActivity;
import com.example.mrh.musicplayer.activity.viewHolder.ShowlistViewHolder;
import com.example.mrh.musicplayer.domain.MusicInfo;

import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

/**
 * Created by MR.H on 2016/12/13 0013.
 */

public class ShowListAdapter extends BaseAdapter {
    private String songsName;
    public TreeMap<Integer, Integer> conditionMap = new TreeMap<>();
    public HashMap<String, Object> shouldRevomeView = new HashMap<>();
    private List<MusicInfo> list;
    private Context context;
    public static final int CONDITION_POPLIST_0 = 0x00; //初始状态
    public static final int CONDITION_POPLIST_1 = 0x01; //播放
    public static final int CONDITION_POPLIST_2 = 0x02; //暂停

    public ShowListAdapter (Context context, List<MusicInfo> list, String songsName) {
        super();
        this.list = list;
        this.context = context;
        this.songsName = songsName;
        for (int i = 0; i < list.size(); i++){
            if (list.get(i).getTITLE().equals(songsName)){
                //初始化
                if (((PlayActivity) context).mPlayer.mMediaPlayer.isPlaying()){
                    conditionMap.put(i, CONDITION_POPLIST_1);
                } else{
                    conditionMap.put(i, CONDITION_POPLIST_2);
                }
                ((PlayActivity) context).prePosition = i;
            } else{
                conditionMap.put(i, CONDITION_POPLIST_0);
            }
        }
    }

    @Override
    public int getCount () {
        return list.size();
    }

    @Override
    public Object getItem (int position) {
        return null;
    }

    @Override
    public long getItemId (int position) {
        return 0;
    }

    @Override
    public View getView (int position, View convertView, ViewGroup parent) {
        ShowlistViewHolder showlistViewHolder;
        if (convertView == null){
            View rootView = View.inflate(context, R.layout.listview_playpoplist, null);
            showlistViewHolder = ShowlistViewHolder.getViewHolder(rootView);
        } else{
            showlistViewHolder = (ShowlistViewHolder) convertView.getTag();
        }
        showlistViewHolder.mTvPlaypopOrder.setText(String.valueOf(position + 1));
        showlistViewHolder.mTvPlaypopListname.setText(list.get(position).getTITLE());
        showlistViewHolder.mTvPlaylistArtist.setText(list.get(position).getARTIST());
        if (position == ((PlayActivity) context).prePosition){
            ((PlayActivity) context).mView = showlistViewHolder.rootView;
            shouldRevomeView.put("shouldRemoveView", showlistViewHolder.rootView);
            shouldRevomeView.put("position", position);
        }
        switch (conditionMap.get(position)){
        case CONDITION_POPLIST_0:
            showlistViewHolder.mVPlaypopPlay.setVisibility(View.INVISIBLE);
            break;
        case CONDITION_POPLIST_1:
            showlistViewHolder.mVPlaypopPlay.setVisibility(View.VISIBLE);
            shouldRevomeView.put("shouldRemoveView", showlistViewHolder.rootView);
            shouldRevomeView.put("position", position);
            break;
        case CONDITION_POPLIST_2:
            showlistViewHolder.mVPlaypopPlay.setVisibility(View.VISIBLE);
            shouldRevomeView.put("shouldRemoveView", showlistViewHolder.rootView);
            shouldRevomeView.put("position", position);
            break;
        }

        return showlistViewHolder.rootView;
    }

}
