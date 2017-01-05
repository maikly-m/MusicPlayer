package com.example.mrh.musicplayer.fragment.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.mrh.musicplayer.R;
import com.example.mrh.musicplayer.domain.MusicInfo;
import com.example.mrh.musicplayer.fragment.viewHolder.PhoneMusicViewHolder;

import java.util.List;
import java.util.TreeMap;

/**
 * Created by MR.H on 2016/12/6 0006.
 */

public class PhoneMusicAdapter extends BaseAdapter {
    private Context context;
    public List<MusicInfo> list;
    public TreeMap<Integer, Integer> conditionMap = new TreeMap<>();
    public static final int CONDITION_PHONEMUSIC_0 = 0x00; //没选择
    public static final int CONDITION_PHONEMUSIC_1 = 0x01; //选择

    public PhoneMusicAdapter (Context context, List<MusicInfo> list) {
        super();
        this.list = list;
        this.context = context;
        for (int i = 0; i < list.size(); i++){
            conditionMap.put(i, CONDITION_PHONEMUSIC_0);
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
        PhoneMusicViewHolder phoneMusicViewHolder;
        if (convertView == null){
            View rootView = View.inflate(context, R.layout.listview_phonemusic, null);
            phoneMusicViewHolder = PhoneMusicViewHolder.getViewHolder(rootView);
        } else{
            phoneMusicViewHolder = (PhoneMusicViewHolder) convertView.getTag();
        }
        phoneMusicViewHolder.mTvPhoneMusicTitle.setText(list.get(position).getTITLE());
        phoneMusicViewHolder.mTvPhoneMusicSinger.setText(list.get(position).getARTIST());

        //根据不同的状态恢复
        switch (conditionMap.get(position)){
        case CONDITION_PHONEMUSIC_0:
            phoneMusicViewHolder.mIvPhoneMusicSelect.setBackgroundResource(R.drawable.btn_check_off_holo_light);
            break;
        case CONDITION_PHONEMUSIC_1:
            phoneMusicViewHolder.mIvPhoneMusicSelect.setBackgroundResource(R.drawable.btn_check_on_holo_light);
            break;
        }
        return phoneMusicViewHolder.rootView;
    }
}
