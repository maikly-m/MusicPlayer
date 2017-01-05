package com.example.mrh.musicplayer.activity.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.mrh.musicplayer.R;
import com.example.mrh.musicplayer.activity.viewHolder.PresetReverblistViewHolder;

import java.util.ArrayList;

/**
 * Created by MR.H on 2017/1/2 0002.
 */

public class PresetReverbAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<String> list;

    public PresetReverbAdapter (Context context, ArrayList<String> list) {
        this.context = context;
        this.list = list;
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
        PresetReverblistViewHolder presetReverblistViewHolder;
        if (convertView == null){
            View rootView = View.inflate(context, R.layout.listview_mainmenu_presetreverb, null);
            presetReverblistViewHolder = PresetReverblistViewHolder.getViewHolder(rootView);
        } else{
            presetReverblistViewHolder = (PresetReverblistViewHolder) convertView.getTag();
        }
        presetReverblistViewHolder.mTvMainmenuPresetreverb_.setText(list.get(position));
        return presetReverblistViewHolder.rootView;
    }
}
