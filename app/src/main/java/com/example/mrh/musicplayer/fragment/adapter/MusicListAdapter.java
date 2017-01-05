package com.example.mrh.musicplayer.fragment.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.mrh.musicplayer.R;
import com.example.mrh.musicplayer.activity.MainActivity;
import com.example.mrh.musicplayer.constant.Constant;
import com.example.mrh.musicplayer.domain.MusicList;
import com.example.mrh.musicplayer.fragment.MusicListFragment;
import com.example.mrh.musicplayer.fragment.viewHolder.MusicListViewHolder;
import com.example.mrh.musicplayer.utils.Utils;

import java.util.List;

import static com.example.mrh.musicplayer.constant.Constant.CUSTOM_LIST;

/**
 * Created by MR.H on 2016/12/6 0006.
 */

public class MusicListAdapter extends BaseAdapter {
    private Fragment f;
    private Context context;
    private List<MusicList> list;
    private AlertDialog mDialog;

    public MusicListAdapter (Context context, Fragment f, List<MusicList> list) {
        super();
        this.f = f;
        this.list = list;
        this.context = context;
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
    public View getView (final int position, View convertView, ViewGroup parent) {
        MusicListViewHolder musicListViewHolder;
        if (convertView == null){
            View rootView = View.inflate(context, R.layout.listview_musiclist, null);
            musicListViewHolder = MusicListViewHolder.getViewHolder(rootView);
        } else{
            musicListViewHolder = (MusicListViewHolder) convertView.getTag();
        }
        String listName = list.get(position).getListName();
        musicListViewHolder.mTvMusiclist.setText(listName.substring(Constant.MUSIC_LIST_CUSTOM_
                .length()));
        if (((MusicListFragment)f).songs.size() != 0){
            musicListViewHolder.mTvMusicCount.setText(((MusicListFragment)f).songs
                    .get(list.get(position).getListName()).size() + " 首");
        }else {
            musicListViewHolder.mTvMusicCount.setText("0 首");
        }
        musicListViewHolder.mIvMusiclistDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                showDialog(position);

            }
        });
        return musicListViewHolder.rootView;
    }

    private void showDialog (final int position) {
        View rootView = View.inflate(context, R.layout.dialog_musiclist_delete, null);
        TextView tvMusiclistDelete = (TextView) rootView.findViewById(R.id.tv_musiclist_delete);
        String listName = list.get(position).getListName();
        tvMusiclistDelete.setText("确定删除列表  "+listName.substring(Constant.MUSIC_LIST_CUSTOM_
                .length()));
        Button btnMusiclistCancle = (Button) rootView.findViewById(R.id.btn_musiclist_delete_cancle);
        btnMusiclistCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                mDialog.dismiss();
            }
        });
        Button btnMusiclistSubmit = (Button) rootView.findViewById(R.id.btn_musiclist_delete_submit);
        btnMusiclistSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                //删除列表，数据库和内存中的list信息，重置播放界面
                Utils.deleteList(context, CUSTOM_LIST, "listName = ?", new String[]{list.get
                        (position)
                        .getListName()});
                Utils.deleteTable(context, list.get(position).getListName());
                if (list.get(position).getListName().equals(((MainActivity)context)
                        .mPlayer.mMusicListname)){
                    ((MainActivity)context).mPlayer.mMediaPlayer.setPlayList(null);
                    ((MainActivity)context).mPlayer.mMusicListname = null;
                    ((MainActivity)context).mPlayer.resetPlayMusic();
                }
                list.remove(position);
                notifyDataSetChanged();
                ((MusicListFragment)f).mTvListNum.setText("歌单("+list.size()+")");
                mDialog.dismiss();
            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(rootView);
        mDialog = builder.show();
    }
}
