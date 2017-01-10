package com.example.mrh.musicplayer.fragment.adapter;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mrh.musicplayer.R;
import com.example.mrh.musicplayer.activity.MainActivity;
import com.example.mrh.musicplayer.constant.Constant;
import com.example.mrh.musicplayer.custom.MyImageView;
import com.example.mrh.musicplayer.domain.MusicInfo;
import com.example.mrh.musicplayer.domain.MusicList;
import com.example.mrh.musicplayer.fragment.AllMusicFragment;
import com.example.mrh.musicplayer.fragment.AllMusicVPFragment;
import com.example.mrh.musicplayer.fragment.SongsListFragment;
import com.example.mrh.musicplayer.fragment.MusicListFragment;
import com.example.mrh.musicplayer.fragment.viewHolder.SongsListViewHolder;
import com.example.mrh.musicplayer.service.PlaySevice;
import com.example.mrh.musicplayer.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;


/**
 * Created by MR.H on 2016/12/6 0006.
 */

public class SongsListAdapter extends BaseAdapter {
    private static final String TAG = "SongsListAdapter";
    private Fragment f;
    private String songsName;
    private String listName;
    private Context context;
    public List<MusicInfo> list;
    public TreeMap<Integer, Integer> conditionMap = new TreeMap<>();
    public HashMap<String, Object> shouldRevomeView = new HashMap<>();
    public static final int CONDITION_SONGSLIST_0 = 0x00; //初始状态
    public static final int CONDITION_SONGSLIST_1 = 0x01; //播放
    public static final int CONDITION_SONGSLIST_2 = 0x02; //暂停
    public static final int CONDITION_SONGSLIST_3 = 0x03; //没选择
    public static final int CONDITION_SONGSLIST_4 = 0x04; //选择
    private int mHeight = -1;
    private TreeMap<Integer, Integer> cvs = new TreeMap<>();//记录是否打开动画
    private static final int IS_HIDE_ANIMATION = 0x00; //关闭动画
    private static final int IS_SHOW_ANIMATION = 0x01; //打开动画
    private List<SongsListViewHolder> cvList = new ArrayList<>();
    private static final int INITPOSTION = -1;
    private int prePosition = INITPOSTION; //上一次点击记录
    private int position = INITPOSTION; //本次点击位置
    private boolean mIsStopUpdateLayout = false;//动画刷新布局时getView只更新部分
    public boolean isShowAnimation; //是否开启动画
    private AlertDialog mDeleteDialog;
    private boolean isSureDelete = false;
    private AlertDialog mDetailDialog;

    public SongsListAdapter (Context context, Fragment f, List<MusicInfo> list, String listName,
                             String songsName) {
        super();
        this.list = list;
        this.context = context;
        this.f = f;
        this.listName = listName;
        this.songsName = songsName;
        setConditionMap();
    }

    /**
     * 设置conditionMap
     */
    public void setConditionMap () {
        for (int i = 0; i < list.size(); i++){
            if (list.get(i).getTITLE().equals(songsName) &&
                    listName.equals(((MainActivity) context).mPlayer.mMusicListname)){
                //初始化
                if (((MainActivity) context).mPlayer.mMediaPlayer.isPlaying()){
                    conditionMap.put(i, CONDITION_SONGSLIST_1);
                } else{
                    conditionMap.put(i, CONDITION_SONGSLIST_2);
                }
                ((SongsListFragment) f).prePosition = i;
            } else{
                conditionMap.put(i, CONDITION_SONGSLIST_0);
            }
            cvs.put(i, IS_HIDE_ANIMATION);
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
    public View getView (final int position, View convertView, ViewGroup parent) {
        final SongsListViewHolder songsListViewHolder;
        if (convertView == null){
            View rootView = View.inflate(context, R.layout.listview_songslist, null);
            songsListViewHolder = SongsListViewHolder.getViewHolder(rootView);
            //初始化容器
            initView(songsListViewHolder);
            cvList.add(songsListViewHolder);
//            DebugUtils.log_d(TAG, position + "  convertView++songsListViewHolder++ " +
//                    ""+songsListViewHolder);
        } else{
            songsListViewHolder = (SongsListViewHolder) convertView.getTag();
        }
//        处理动画时getview问题,防止内存泄漏
        if (mIsStopUpdateLayout){
            return songsListViewHolder.rootView;
        }
        //加入position
        songsListViewHolder.mIvListsplayMore.setMyPosition(position);

        songsListViewHolder.mTvListsplayTitle.setText(list.get(position).getTITLE());
        songsListViewHolder.mTvListsplaySinger.setText(list.get(position).getARTIST());
        songsListViewHolder.mTvListsplayOrder.setText(String.valueOf(position + 1));

        //根据不同的状态恢复
        switch (conditionMap.get(position)){
        case CONDITION_SONGSLIST_0:
            songsListViewHolder.mVListsplayPlay.setVisibility(View.INVISIBLE);
            songsListViewHolder.mIvListsplaySelect.setVisibility(View.GONE);
            songsListViewHolder.mIvListsplayMore.setVisibility(View.VISIBLE);
            songsListViewHolder.rootView.setBackgroundColor(Color.argb(0, 0, 0, 0));
            break;
        case CONDITION_SONGSLIST_1:
            songsListViewHolder.mVListsplayPlay.setVisibility(View.VISIBLE);
            songsListViewHolder.mIvListsplaySelect.setVisibility(View.GONE);
            songsListViewHolder.mIvListsplayMore.setVisibility(View.VISIBLE);
            songsListViewHolder.rootView.setBackgroundColor(Color.argb(55, 99, 55, 55));
            shouldRevomeView.put("shouldRemoveView", songsListViewHolder.rootView);
            shouldRevomeView.put("position", position);

            break;
        case CONDITION_SONGSLIST_2:
            songsListViewHolder.mVListsplayPlay.setVisibility(View.VISIBLE);
            songsListViewHolder.mIvListsplaySelect.setVisibility(View.GONE);
            songsListViewHolder.mIvListsplayMore.setVisibility(View.VISIBLE);
            songsListViewHolder.rootView.setBackgroundColor(Color.argb(55, 55, 55, 55));
            shouldRevomeView.put("shouldRemoveView", songsListViewHolder.rootView);
            shouldRevomeView.put("position", position);

            break;
        case CONDITION_SONGSLIST_3:
            songsListViewHolder.mVListsplayPlay.setVisibility(View.INVISIBLE);
            songsListViewHolder.mIvListsplayMore.setVisibility(View.INVISIBLE);
            songsListViewHolder.mIvListsplaySelect.setVisibility(View.VISIBLE);
            songsListViewHolder.mIvListsplaySelect.setBackgroundResource(R.drawable
                    .btn_check_off_holo_light);
            songsListViewHolder.rootView.setBackgroundColor(Color.argb(33, 88, 88, 55));
            //多选时动画处理
            if (prePosition != INITPOSTION && prePosition == position){
                closeView();
            }
            break;
        case CONDITION_SONGSLIST_4:
            songsListViewHolder.mVListsplayPlay.setVisibility(View.INVISIBLE);
            songsListViewHolder.mIvListsplayMore.setVisibility(View.INVISIBLE);
            songsListViewHolder.mIvListsplaySelect.setVisibility(View.VISIBLE);
            songsListViewHolder.mIvListsplaySelect.setBackgroundResource(R.drawable
                    .btn_check_on_holo_light);
            songsListViewHolder.rootView.setBackgroundColor(Color.argb(33, 88, 88, 55));
            break;
        }

        RelativeLayout rootView = (RelativeLayout) songsListViewHolder.rootView;
        rootView.removeAllViews();
        RelativeLayout.LayoutParams lp_lllistsplayview =
                (RelativeLayout.LayoutParams) songsListViewHolder.mLlListsplayView
                        .getLayoutParams();
        lp_lllistsplayview.addRule(RelativeLayout.BELOW, R.id.ll_listsplay_);
        RelativeLayout.LayoutParams lp_lllistsplay = (RelativeLayout.LayoutParams)
                songsListViewHolder.mLlListsplay
                        .getLayoutParams();
        lp_lllistsplay.addRule(RelativeLayout.BELOW);

        switch (cvs.get(position)){
        case IS_HIDE_ANIMATION:
            lp_lllistsplayview.height = 0;
            songsListViewHolder.mLlListsplay.setBackgroundColor(Color.WHITE);
            break;
        case IS_SHOW_ANIMATION:
            lp_lllistsplayview.height = mHeight;
            songsListViewHolder.mLlListsplay.setBackgroundColor(Color.GRAY);
            break;
        }
        rootView.addView(songsListViewHolder.mLlListsplay, lp_lllistsplay);
        rootView.addView(songsListViewHolder.mLlListsplayView, lp_lllistsplayview);

        return songsListViewHolder.rootView;
    }

    /**
     * 初始化更多部分的隐藏控件
     *
     * @param cv
     */
    private void initView (final SongsListViewHolder cv) {
        cv.mLlListsplayView.measure(0, 0);
        if (mHeight == -1){
            mHeight = cv.mLlListsplayView.getMeasuredHeight();
        }

        cv.mIvListsplayMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                MyImageView myImageView = (MyImageView) v;
                position = myImageView.getMyPosition();
                //关闭上次动画
                if (prePosition == position){
                    closeView(cv);
                    return;
                }
                if (prePosition != INITPOSTION){
                    closeView();
                }
                showView(cv);
            }
        });
        cv.mLlListsplayDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                showDeleteView(cv);
            }
        });
        cv.mLlListsplayLove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                addOrRemoveMyLove(cv);
            }
        });
        cv.mLlListsplayDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                showDetailView(cv);
            }
        });
    }

    /**
     * 加入最爱列表
     */
    private void addOrRemoveMyLove (SongsListViewHolder cv) {
        ArrayList<MusicInfo> musicInfos = ((MainActivity) context).songs_love.get(Constant
                .MUSIC_LIST_CUSTOM_ + Constant.CUSTOM_LIST_LOVE);
        boolean isHave = false;
        if (musicInfos != null){
            for (int i = 0; i < musicInfos.size(); i++){
                if (this.list.get(position).getDATA().equals(musicInfos.get(i).getDATA())){
                    isHave = true;

                    Utils.deleteMusicInfo(context, Constant
                            .MUSIC_LIST_CUSTOM_ + Constant.CUSTOM_LIST_LOVE, "TITLE = " +
                            "?", new String[]{musicInfos.get(i).getTITLE()});
                    musicInfos.remove(i);
                    break;
                }
            }
        }
        if (isHave){
            cv.mIvListsplayLove.setBackgroundResource(R.drawable.love_48px_normal);
        } else{
            List<ContentValues> lcv = new ArrayList<>();
            ContentValues contentValues = new ContentValues();
            contentValues.put("_ID", list.get(position).get_ID());
            contentValues.put("DISPLAY_NAME", list.get(position).getDISPLAY_NAME());
            contentValues.put("TITLE", list.get(position).getTITLE());
            contentValues.put("DURATION", list.get(position).getDURATION());
            contentValues.put("ARTIST", list.get(position).getARTIST());
            contentValues.put("ALBUM", list.get(position).getALBUM());
            contentValues.put("YEAR", list.get(position).getYEAR());
            contentValues.put("MIME_TYPE", list.get(position).getMIME_TYPE());
            contentValues.put("SIZE", list.get(position).getSIZE());
            contentValues.put("DATA", list.get(position).getDATA());
            contentValues.put("IMAGE", list.get(position).getIMAGE());
            contentValues.put("LYRIC", list.get(position).getLYRIC());
            lcv.add(contentValues);
            Utils.setMusicInfo(context, Constant
                    .MUSIC_LIST_CUSTOM_ + Constant.CUSTOM_LIST_LOVE, lcv);
            if (musicInfos == null){
                ArrayList<MusicInfo> Infos = new ArrayList<>();
                Infos.add(list.get(position));
                ((MainActivity) context).songs_love.put(Constant
                        .MUSIC_LIST_CUSTOM_ + Constant.CUSTOM_LIST_LOVE, Infos);
                MusicList musicList = new MusicList();
                musicList.setListName(Constant
                        .MUSIC_LIST_CUSTOM_ + Constant.CUSTOM_LIST_LOVE);
                ((MainActivity) context).list_custom.add(musicList);
            }
            cv.mIvListsplayLove.setBackgroundResource(R.drawable.love_48px_pressed);
        }
    }

    private void showDetailView (SongsListViewHolder cv) {
        closeView(cv);
        View rootView = View.inflate(context, R.layout.dialog_songslist_songproperty, null);
        TextView mTvSongslisttSongproperty = (TextView) rootView.findViewById(R.id
                .tv_songslist_songproperty);
        mTvSongslisttSongproperty.setText("详情"+" - "+list.get(position).getTITLE());
        TextView mTvSongslisttSongpropertyTitle = (TextView) rootView.findViewById(R.id
                .tv_songslist_songproperty_title);
        mTvSongslisttSongpropertyTitle.setText("歌曲名"+" - "+list.get(position).getTITLE());
        TextView mTvSongslisttSongpropertyArtist = (TextView) rootView.findViewById(R.id
                .tv_songslist_songproperty_artist);
        mTvSongslisttSongpropertyArtist.setText("艺术家"+" - "+list.get(position).getARTIST());
        TextView mTvSongslisttSongpropertyAlbum = (TextView) rootView.findViewById(R.id
                .tv_songslist_songproperty_album);
        mTvSongslisttSongpropertyAlbum.setText("专辑"+" - "+list.get(position).getALBUM());
        TextView mTvSongslisttSongpropertyDuration = (TextView) rootView.findViewById(R.id
                .tv_songslist_songproperty_duration);
        mTvSongslisttSongpropertyDuration.setText("时长"+" - "+
                Utils.formatTime(Integer.valueOf(list.get(position).getDURATION())));
        TextView mTvSongslisttSongpropertySize = (TextView) rootView.findViewById(R.id
                .tv_songslist_songproperty_size);
        mTvSongslisttSongpropertySize.setText("文件大小"+" - "+list.get(position).getSIZE());
        Button mBtnSongslistSongproperty = (Button) rootView.findViewById(R.id
                .btn_songslist_songproperty);
        mBtnSongslistSongproperty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                mDetailDialog.dismiss();
            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(rootView);
        mDetailDialog = builder.show();
        WindowManager.LayoutParams lp = mDetailDialog.getWindow().getAttributes();
        lp.width = Utils.dip2px(context, 250);
        mDetailDialog.getWindow().setAttributes(lp);
    }

    /**
     * 打开删除dialog
     */
    private void showDeleteView (SongsListViewHolder cv) {
        closeView(cv);
        View rootView = View.inflate(context, R.layout.dialog_songslist_delete, null);

        TextView mTvSongslistDelete = (TextView) rootView.findViewById(R.id
                .tv_songslist_delete);
        mTvSongslistDelete.setText("删除歌曲 "+list.get(position).getTITLE());
        final ImageView mIvSongslistDelete = (ImageView) rootView.findViewById(R.id
                .iv_songslist_delete);
        Button mBtnSongslistDeleteSubmit = (Button) rootView.findViewById(R.id
                .btn_songslist_delete_submit);
        Button mBtnSongslisttDeleteCancle = (Button) rootView.findViewById(R.id
                .btn_songslist_delete_cancle);
        mIvSongslistDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                if (isSureDelete){
                    mIvSongslistDelete.setBackgroundResource(R.drawable.btn_check_off_holo_light);
                    isSureDelete = false;
                } else{
                    mIvSongslistDelete.setBackgroundResource(R.drawable.btn_check_on_holo_light);
                    isSureDelete = true;
                }
            }
        });
        mBtnSongslistDeleteSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                PlaySevice player = ((MainActivity) context).mPlayer;
                if (list.get(position).getTITLE().equals(player.mMusicSongsname)){
                    ((MainActivity) context).mPlayer.resetPlayMusic();
                    player.mMusicSongsname = null;
                }
                conditionMap.put(position, CONDITION_SONGSLIST_0);
                if (isSureDelete){
                    // TODO: 2016/12/21 0021
                    //删除本地文件
                    ((MainActivity) context).deleteSong(list.get(position));
                }else {
                    //移除
                    Utils.deleteMusicInfo(context, ((SongsListFragment) f).fragmentName, "TITLE = " +
                            "?", new String[]{list.get(position).getTITLE()});
                    list.remove(position);
                }

                if (Constant.MUSIC_LIST_CUSTOM_.equals(((SongsListFragment) f).fragmentPrefix)){
                    Fragment fragment = ((SongsListFragment) f).fm.findFragmentByTag
                            ("MusicListFragment");
                    if (fragment != null){
                        ((MusicListFragment) fragment).mAdapter.notifyDataSetChanged();
                    }
                } else if (((SongsListFragment) f).fragmentPrefix != null){
                    AllMusicFragment fragment = (AllMusicFragment) ((SongsListFragment) f).fm
                            .findFragmentByTag("AllMusicFragment");
                    List<Fragment> fragments = fragment.cfm.getFragments();
                    for (int i = 0; i < fragments.size(); i++){
                        if (fragments.get(i).getClass().getSimpleName().equals
                                ("AllMusicVPFragment")){
                            ((AllMusicVPFragment) fragments.get(i)).mAdapter.notifyDataSetChanged();
                        }
                    }
                }
                //更新conditionMap
                setConditionMap();
                SongsListAdapter.this.notifyDataSetChanged();
                mDeleteDialog.dismiss();
            }
        });
        mBtnSongslisttDeleteCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                mDeleteDialog.dismiss();
            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(rootView);
        mDeleteDialog = builder.show();
        WindowManager.LayoutParams lp = mDeleteDialog.getWindow().getAttributes();
        lp.width = Utils.dip2px(context, 200);
        mDeleteDialog.getWindow().setAttributes(lp);
    }

    /**
     * 显示隐藏控件动画
     *
     * @param cv
     */
    private void showView (final SongsListViewHolder cv) {
//        DebugUtils.log_d(TAG, "showview++ "+cv);
        if (mHeight != 0){
            Point point = new Point();
            ((Activity) context).getWindowManager().getDefaultDisplay().getSize(point);
            int y = point.y;
            int[] p = new int[2];
            cv.mLlListsplayView.getLocationOnScreen(p);
            int top = p[1];

            final RelativeLayout.LayoutParams layoutParams =
                    (RelativeLayout.LayoutParams) cv.mLlListsplayView.getLayoutParams();
            layoutParams.height = 0;
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) cv.mLlListsplay
                    .getLayoutParams();
            //重置显示动画的布局
            RelativeLayout rootView = (RelativeLayout) cv.rootView;
            rootView.removeAllViews();
            if (top > y * 0.6){
                lp.addRule(RelativeLayout.BELOW, R.id.ll_listsplay_view);
                layoutParams.addRule(RelativeLayout.BELOW);
                rootView.addView(cv.mLlListsplayView, layoutParams);
                rootView.addView(cv.mLlListsplay, lp);

            } else{
                lp.addRule(RelativeLayout.BELOW);
                layoutParams.addRule(RelativeLayout.BELOW, R.id.ll_listsplay_);
                rootView.addView(cv.mLlListsplayView, layoutParams);
                rootView.addView(cv.mLlListsplay, lp);
            }
            ArrayList<MusicInfo> musicInfos = ((MainActivity) context).songs_love.get(Constant
                    .MUSIC_LIST_CUSTOM_ + Constant.CUSTOM_LIST_LOVE);
            boolean isHave = false;
            if (musicInfos != null){
                for (int i = 0; i < musicInfos.size(); i++){
                    if (this.list.get(position).getDATA().equals(musicInfos.get(i).getDATA())){
                        isHave = true;
                        break;
                    }
                }
            }
            if (isHave){
                cv.mIvListsplayLove.setBackgroundResource(R.drawable.love_48px_pressed);
            } else{
                cv.mIvListsplayLove.setBackgroundResource(R.drawable.love_48px_normal);
            }
            ValueAnimator va = ValueAnimator.ofInt(0, mHeight);
            va.setDuration(50);
            //获取显示的view位置
            va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate (ValueAnimator animation) {
                    layoutParams.height = (int) animation.getAnimatedValue();
                    cv.mLlListsplayView.setLayoutParams(layoutParams);
                }
            });
            setListener(va);
            va.start();
        }
        cv.mLlListsplay.setBackgroundColor(Color.GRAY);
        isShowAnimation = true;
        cvs.put(position, IS_SHOW_ANIMATION);
        cvs.put(prePosition, IS_HIDE_ANIMATION);
        prePosition = position;
    }

    /**
     * 关闭隐藏控件动画，初始化时调用
     *
     * @param cv
     */
    private void closeView (final SongsListViewHolder cv) {
        if (mHeight != 0){
            ValueAnimator va = ValueAnimator.ofInt(mHeight, 0);
            va.setDuration(50);
            //获取显示的view位置
            final RelativeLayout.LayoutParams layoutParams =
                    (RelativeLayout.LayoutParams) cv.mLlListsplayView.getLayoutParams();
            va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate (ValueAnimator animation) {
                    layoutParams.height = (int) animation.getAnimatedValue();
                    cv.mLlListsplayView.setLayoutParams(layoutParams);
                }
            });
            setListener(va);
            va.start();
        }
        cv.mLlListsplay.setBackgroundColor(Color.WHITE);
        cvs.put(prePosition, IS_HIDE_ANIMATION);
        isShowAnimation = false;
        prePosition = INITPOSTION;
    }

    /**
     * 关闭隐藏控件动画，点击事件中调用
     */
    public void closeView () {
        if (mHeight != 0){
            //获取显示的view位置
            final SongsListViewHolder mCv; //上一次点击记录holder位置
            for (SongsListViewHolder cv : cvList){
                if (cv.mLlListsplayView.getHeight() == mHeight){
                    mCv = cv;
                    final RelativeLayout.LayoutParams layoutParams =
                            (RelativeLayout.LayoutParams) mCv.mLlListsplayView.getLayoutParams();
                    ValueAnimator va = ValueAnimator.ofInt(mHeight, 0);
                    va.setDuration(50);
//                    DebugUtils.log_d(TAG, "closeView+++"+mCv+" h "+mCv
//                            .mLlListsplayView.getHeight());
                    va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate (ValueAnimator animation) {
                            layoutParams.height = (int) animation.getAnimatedValue();
                            mCv.mLlListsplayView.setLayoutParams(layoutParams);
                        }
                    });
                    setListener(va);
                    va.start();

                    mCv.mLlListsplay.setBackgroundColor(Color.WHITE);
                    isShowAnimation = false;
                    cvs.put(prePosition, IS_HIDE_ANIMATION);
                    prePosition = INITPOSTION;
                    break;
                }
            }
//            DebugUtils.log_d(TAG, "closeView+++000000000000");
        }
    }

    private void setListener (ValueAnimator va) {
        va.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart (Animator animation) {
                mIsStopUpdateLayout = true;
            }

            @Override
            public void onAnimationEnd (Animator animation) {
                new Thread() {
                    @Override
                    public void run () {
                        try{
                            Thread.sleep(30);
                            mIsStopUpdateLayout = false;
                        } catch (InterruptedException e){
                            e.printStackTrace();
                        }

                    }
                }.start();
            }

            @Override
            public void onAnimationCancel (Animator animation) {

            }

            @Override
            public void onAnimationRepeat (Animator animation) {

            }
        });
    }
}
