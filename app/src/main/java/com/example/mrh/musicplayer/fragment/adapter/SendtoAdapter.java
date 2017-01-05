package com.example.mrh.musicplayer.fragment.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.mrh.musicplayer.R;
import com.example.mrh.musicplayer.constant.Constant;
import com.example.mrh.musicplayer.domain.MusicList;
import com.example.mrh.musicplayer.fragment.SongsListFragment;
import com.example.mrh.musicplayer.fragment.viewHolder.SendtoViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MR.H on 2016/12/22 0022.
 */

public class SendtoAdapter extends BaseAdapter {

    private Fragment f;
    private Context context;
    public List<MusicList> mlist = new ArrayList<>();

    public SendtoAdapter (Context context, Fragment f, List<MusicList> list) {
        super();
        this.context = context;
        this.f = f;
        for (int i = 0; i < list.size(); i++){
            if (((SongsListFragment) f).fragmentName.equals(list.get(i).getListName())){
                continue;
            }
            this.mlist.add(list.get(i));
        }
    }

    @Override
    public int getCount () {
        return mlist.size();
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
        final SendtoViewHolder sendtoViewHolder;
        if (convertView == null){
            View rootView = View.inflate(context, R.layout.listview_songslist_sendto, null);
            sendtoViewHolder = SendtoViewHolder.getViewHolder(rootView);
        }else {
            sendtoViewHolder = (SendtoViewHolder) convertView.getTag();
        }
        String listName = mlist.get(position).getListName();
        String substring = listName.substring(Constant.MUSIC_LIST_CUSTOM_.length());
        sendtoViewHolder.mTvSongslistSendtoListname.setText(substring);
        return sendtoViewHolder.rootView;
    }
}
