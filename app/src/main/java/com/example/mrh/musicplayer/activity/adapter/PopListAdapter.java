package com.example.mrh.musicplayer.activity.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.mrh.musicplayer.R;
import com.example.mrh.musicplayer.activity.MainActivity;
import com.example.mrh.musicplayer.activity.viewHolder.PoplistViewHolder;
import com.example.mrh.musicplayer.domain.MusicInfo;

import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

/**
 * Created by MR.H on 2016/12/13 0013.
 */

public class PopListAdapter extends BaseAdapter {
    private String songsName;
    public TreeMap<Integer, Integer> conditionMap = new TreeMap<>();
    public HashMap<String, Object> shouldRevomeView = new HashMap<>();
    private List<MusicInfo> list;
    private Context context;
    public static final int CONDITION_POPLIST_0 = 0x00; //初始状态
    public static final int CONDITION_POPLIST_1 = 0x01; //播放
    public static final int CONDITION_POPLIST_2 = 0x02; //暂停

    public PopListAdapter (Context context, List<MusicInfo> list, String songsName) {
        super();
        this.list = list;
        this.context = context;
        this.songsName = songsName;
        for (int i = 0; i < list.size(); i++){
            if (list.get(i).getTITLE().equals(songsName)){
                //初始化
                if (((MainActivity) context).mPlayer.mMediaPlayer.isPlaying()){
                    conditionMap.put(i, CONDITION_POPLIST_1);
                }else {
                    conditionMap.put(i, CONDITION_POPLIST_2);
                }
                ((MainActivity)context).prePosition = i;
            }else {
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
        PoplistViewHolder poplistViewHolder;
        if (convertView == null){
            View rootView = View.inflate(context, R.layout.listview_mainpoplist, null);
            poplistViewHolder = PoplistViewHolder.getViewHolder(rootView);
        } else{
            poplistViewHolder = (PoplistViewHolder) convertView.getTag();
        }
        poplistViewHolder.mTvMainpopOrder.setText(String.valueOf(position + 1));
        poplistViewHolder.mTvMainpopListname.setText(list.get(position).getTITLE());
        poplistViewHolder.mTvMainlistArtist.setText(list.get(position).getARTIST());
        poplistViewHolder.mIvMainpopDelete.setBackgroundResource(R.drawable.delete_64px);
        poplistViewHolder.mIvMainpopDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {

            }
        });
        if (position == ((MainActivity)context).prePosition){
            ((MainActivity)context).mView = poplistViewHolder.rootView;
            shouldRevomeView.put("shouldRemoveView", poplistViewHolder.rootView);
            shouldRevomeView.put("position", position);
        }
        switch (conditionMap.get(position)){
        case CONDITION_POPLIST_0:
            poplistViewHolder.mVMainpopPlay.setVisibility(View.INVISIBLE);
            break;
        case CONDITION_POPLIST_1:
            poplistViewHolder.mVMainpopPlay.setVisibility(View.VISIBLE);
            shouldRevomeView.put("shouldRemoveView", poplistViewHolder.rootView);
            shouldRevomeView.put("position", position);
            break;
        case CONDITION_POPLIST_2:
            poplistViewHolder.mVMainpopPlay.setVisibility(View.VISIBLE);
            shouldRevomeView.put("shouldRemoveView", poplistViewHolder.rootView);
            shouldRevomeView.put("position", position);
            break;
        }

        return poplistViewHolder.rootView;
    }
}
