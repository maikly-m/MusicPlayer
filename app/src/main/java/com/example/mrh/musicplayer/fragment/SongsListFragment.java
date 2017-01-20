package com.example.mrh.musicplayer.fragment;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.ContentValues;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mrh.musicplayer.R;
import com.example.mrh.musicplayer.activity.BaseActivity;
import com.example.mrh.musicplayer.activity.MainActivity;
import com.example.mrh.musicplayer.constant.Constant;
import com.example.mrh.musicplayer.domain.MusicInfo;
import com.example.mrh.musicplayer.domain.MusicList;
import com.example.mrh.musicplayer.domain.PlayList;
import com.example.mrh.musicplayer.fragment.adapter.SendtoAdapter;
import com.example.mrh.musicplayer.fragment.adapter.SongsListAdapter;
import com.example.mrh.musicplayer.fragment.viewHolder.SongsListViewHolder;
import com.example.mrh.musicplayer.service.PlaySevice;
import com.example.mrh.musicplayer.utils.SqlHelper;
import com.example.mrh.musicplayer.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

import static com.example.mrh.musicplayer.fragment.adapter.SongsListAdapter.CONDITION_SONGSLIST_0;
import static com.example.mrh.musicplayer.fragment.adapter.SongsListAdapter.CONDITION_SONGSLIST_1;
import static com.example.mrh.musicplayer.fragment.adapter.SongsListAdapter.CONDITION_SONGSLIST_2;
import static com.example.mrh.musicplayer.fragment.adapter.SongsListAdapter.CONDITION_SONGSLIST_3;
import static com.example.mrh.musicplayer.fragment.adapter.SongsListAdapter.CONDITION_SONGSLIST_4;

/**
 * Created by MR.H on 2016/12/6 0006.
 */

public class SongsListFragment extends BaseFragment implements View.OnClickListener {
    private static final String TAG = "SongsListFragment";
    private View mRootView;
    public ListView mLvSongslistList;
    private TextView mTvSonglistNull;
    private List<MusicInfo> list;
    private SongsListAdapter mAdapter;
    public int prePosition = -1;
    private View mView;
    private TreeMap<Integer, Integer> mConditionMap;
    private HashMap<String, Object> mShouldRevomeView;
    private boolean isShouldRevomeView = false;
    private boolean isSelectting = false;
    private LinearLayout mLlSongslistBack;
    private LinearLayout mLlSongslist;
    private ImageView mIvSongslistAll;
    private TextView mTvSongslistSelect;
    private Button mBtnSongslistBack;
    private RelativeLayout mRlSongslistSelect;
    public ImageView mIvSongslistPlaymodel;
    public TextView mTvSongslistPlaymodel;
    private LinearLayout mLlSongslistPlaymodel;
    private Button mBtnSongslistMultiple;
    private RelativeLayout mRlSongslistNormal;
    private RelativeLayout mRlSongslist;
    private LinearLayout mLlSongslistDelete;
    private LinearLayout mLlSongslistBottom;
    private int mHeight_bottom;
    private ViewGroup.LayoutParams mLp_bottom;
    private int mHeight_select;
    private ViewGroup.LayoutParams mLp_select;
    private PopupWindow pw;
    private int mCount = 0;//全选计数器
    private int movePosition = 0; //滑动位置定位
    private PlaySevice player;
    public String mPlayModel = Constant.PLAYMODEL_ORDER;
    private boolean isPlay = false;
    private LinearLayout mLlSongslistSendto;
    private boolean isSureDelete = false;
    private AlertDialog mDeleteDialog;
    private int num;
    private AlertDialog mSendtoDialog;
    private TextView mTvSongslistFragmentname;
    private ImageView mIvSongslistAdd;
    public String fragmentPrefix;
    public RelativeLayout RlSongslist;
    private int mX; //动画初始位置x
    private int mY; //动画初始位置y
    private boolean hasStatusBarHeight = false;
    private int mStatusBarHeight; //状态栏高度
    private int mScreenX; //屏幕绝对宽度
    private int mScreenY; //屏幕绝对高度
    private boolean isStartAnimation = false;
    private int mContainer_height;
    private MainActivity activity;

    /**
     * 调用newInstance方法来创建fragment，不要调用此方法
     */
    public SongsListFragment () {
        super();
    }

    public static SongsListFragment newInstance (String name) {
        SongsListFragment fragment = new SongsListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("name", name);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView (LayoutInflater inflater, @Nullable ViewGroup container,
                              @Nullable Bundle savedInstanceState) {
        if (mRootView == null){
            mRootView = inflater.inflate(R.layout.fragment_songslist, null);
            initView();
            initData();
        }
        return mRootView;
    }

    @Override
    public void onCreate (@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setFragmentName(getArguments().getString("name"));
        this.activity = (MainActivity) mActivity;
    }

    @Override
    public void onDestroy () {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void initView () {
        //back键处理fragment返回
        if (fm.findFragmentByTag("MusicListFragment") != null){
            activity.setOnFragmentBack(new BaseActivity.FragmentBack() {
                @Override
                public boolean execute (KeyEvent event) {
                    showAndRemoveFragment("MusicListFragment", fragmentName);
                    return true;
                }
            }, fragmentName);
        } else if (fm.findFragmentByTag("AllMusicFragment") != null){
            activity.setOnFragmentBack(new BaseActivity.FragmentBack() {
                @Override
                public boolean execute (KeyEvent event) {
                    showAndRemoveFragment("AllMusicFragment", fragmentName);
                    return true;
                }
            }, fragmentName);
        }else if (fm.findFragmentByTag("ContentFragment") != null){
            activity.setOnFragmentBack(new BaseActivity.FragmentBack() {
                @Override
                public boolean execute (KeyEvent event) {
                    showAndRemoveFragment("ContentFragment", fragmentName);
                    return true;
                }
            }, fragmentName);
        }

        this.RlSongslist = (RelativeLayout) mRootView.findViewById(R.id.rl_songslist); //最大的父容器
        this.mLlSongslistBack = (LinearLayout) mRootView.findViewById(R.id.ll_songslist_back);
        this.mTvSongslistFragmentname = (TextView) mRootView.findViewById(R.id
                .tv_songslist_fragmentname);
        this.mIvSongslistAdd = (ImageView) mRootView.findViewById(R.id.iv_songslist_add);
        this.mLlSongslist = (LinearLayout) mRootView.findViewById(R.id.ll_songslist_);
        this.mIvSongslistPlaymodel = (ImageView) mRootView.findViewById(R.id
                .iv_songslist_playmodel);
        this.mTvSongslistPlaymodel = (TextView) mRootView.findViewById(R.id
                .tv_songslist_playmodel);
        this.mLlSongslistPlaymodel = (LinearLayout) mRootView.findViewById(R.id
                .ll_songslist_playmodel);
        this.mBtnSongslistMultiple = (Button) mRootView.findViewById(R.id
                .btn_songslist_multiple);
        this.mRlSongslistNormal = (RelativeLayout) mRootView.findViewById(R.id
                .rl_songslist_normal);
        this.mIvSongslistAll = (ImageView) mRootView.findViewById(R.id.iv_songslist_all);
        this.mTvSongslistSelect = (TextView) mRootView.findViewById(R.id.tv_songslist_select);
        this.mBtnSongslistBack = (Button) mRootView.findViewById(R.id.btn_songslist_back);
        this.mRlSongslistSelect = (RelativeLayout) mRootView.findViewById(R.id
                .rl_songslist_select);
        this.mRlSongslist = (RelativeLayout) mRootView.findViewById(R.id.rl_songslist_);
        this.mLvSongslistList = (ListView) mRootView.findViewById(R.id.lv_songslist_list);
        this.mTvSonglistNull = (TextView) mRootView.findViewById(R.id.tv_songlist_null);
        this.mLlSongslistDelete = (LinearLayout) mRootView.findViewById(R.id
                .ll_songslist_delete);
        this.mLlSongslistSendto = (LinearLayout) mRootView.findViewById(R.id
                .ll_songslist_sendto);
        this.mLlSongslistBottom = (LinearLayout) mRootView.findViewById(R.id
                .ll_songslist_bottom);

        mLlSongslistBack.setOnClickListener(this);
        mIvSongslistAdd.setOnClickListener(this);
        mLlSongslist.setOnClickListener(this);
        mIvSongslistAll.setOnClickListener(this);
        mBtnSongslistBack.setOnClickListener(this);
        mRlSongslistSelect.setOnClickListener(this);
        mIvSongslistPlaymodel.setOnClickListener(this);
        mTvSongslistPlaymodel.setOnClickListener(this);
        mLlSongslistPlaymodel.setOnClickListener(this);
        mBtnSongslistMultiple.setOnClickListener(this);
        mRlSongslistNormal.setOnClickListener(this);
        mRlSongslist.setOnClickListener(this);
        mLlSongslistDelete.setOnClickListener(this);
        mLlSongslistSendto.setOnClickListener(this);
        mLlSongslistBottom.setOnClickListener(this);
        //隐藏布局高度
        mRlSongslistSelect.measure(0, 0);
        mHeight_select = mRlSongslistSelect.getMeasuredHeight();
        mLp_select = mRlSongslistSelect.getLayoutParams();
        mLp_select.height = 0;

        mLlSongslistBottom.measure(0, 0);
        mHeight_bottom = mLlSongslistBottom.getMeasuredHeight();
        mLp_bottom = mLlSongslistBottom.getLayoutParams();
        mLp_bottom.height = 0;
    }

    @Override
    public void onClick (View v) {
        switch (v.getId()){
        case R.id.ll_songslist_back:
            if (fm.findFragmentByTag("MusicListFragment") != null){
                showAndRemoveFragment("MusicListFragment", fragmentName);
            }else if (fm.findFragmentByTag("AllMusicFragment") != null){
                showAndRemoveFragment("AllMusicFragment", fragmentName);
            }else if (fm.findFragmentByTag("ContentFragment") != null){
                showAndRemoveFragment("ContentFragment", fragmentName);
            }
            break;
        case R.id.iv_songslist_add:
            addAndRemoveFragment(R.id.fl_main, new PhoneMusicFragment(), "PhoneMusicFragment",
                    fragmentName);
            break;
        case R.id.btn_songslist_back:
            //关闭多选窗口
            closeMultiple();
            hideItems();
            mCount = 0;
            mIvSongslistAll.setBackgroundResource(R.drawable.btn_check_off_holo_light);
            break;
        case R.id.btn_songslist_multiple:
            //打开多选窗口
            openMultiple();
            showItems();
            break;
        case R.id.iv_songslist_playmodel:
            //播放顺序
            showPopupWindow();
            break;
        case R.id.iv_songslist_all:
            //点击全选
            selectOrNot();
            break;
        case R.id.ll_songslist_delete:
            //删除
            showDeleteView();
            break;
        case R.id.ll_songslist_sendto:
            //发送到
            showSendtoView();
            break;
        case R.id.ll_songslist_pop_order:
            //顺序播放
            mIvSongslistPlaymodel.setBackgroundResource(R.drawable.order_64px);
            mTvSongslistPlaymodel.setText(Constant.PLAYMODEL_ORDER);
            mPlayModel = Constant.PLAYMODEL_ORDER;
            transmitPlayModel();
            pw.dismiss();
            break;
        case R.id.ll_songslist_pop_random:
            //随机播放
            mIvSongslistPlaymodel.setBackgroundResource(R.drawable.random_64px);
            mTvSongslistPlaymodel.setText(Constant.PLAYMODEL_RANDOM);
            mPlayModel = Constant.PLAYMODEL_RANDOM;
            transmitPlayModel();
            pw.dismiss();
            break;
        case R.id.ll_songslist_pop_cycle:
            //循环播放
            mIvSongslistPlaymodel.setBackgroundResource(R.drawable.cycle_64px);
            mTvSongslistPlaymodel.setText(Constant.PLAYMODEL_CYCLE);
            mPlayModel = Constant.PLAYMODEL_CYCLE;
            transmitPlayModel();
            pw.dismiss();
            break;
        }

    }

    private void showSendtoView () {
        View rootView = View.inflate(context, R.layout.dialog_songslist_sendto, null);
        TextView mTvCustomlistSendto = (TextView) rootView.findViewById(R.id.tv_songslist_sendto);
        TextView mLvSongslistNull = (TextView) rootView.findViewById(R.id.tv_songslist_null);
        ListView mLvSongslistSendto = (ListView) rootView.findViewById(R.id.lv_songslist_sendto);
        Button mBtnSongslistDeleteCancle = (Button) rootView.findViewById(R.id
                .btn_songslist_delete_cancle);
        mBtnSongslistDeleteCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                mSendtoDialog.dismiss();
                //关闭多选窗口
                closeMultiple();
                hideItems();
                mCount = 0;
            }
        });
        ArrayList<MusicList> list_custom = ((MainActivity) context).list_custom;
        if (list_custom == null || (list_custom.size() == 1 && list_custom.get(0).getListName()
                .equals(fragmentName))){
            mLvSongslistNull.setVisibility(View.VISIBLE);
            mLvSongslistSendto.setVisibility(View.GONE);
        }else {
            final SendtoAdapter sendtoAdapter = new SendtoAdapter(context, this, list_custom);
            mLvSongslistSendto.setAdapter(sendtoAdapter);
            mLvSongslistSendto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick (AdapterView<?> parent, View view, int position, long id) {
                    String listName = sendtoAdapter.mlist.get(position).getListName();
                    ArrayList<MusicInfo> musicInfos = ((MainActivity) context).songs_custom.get
                            (listName);
                    List<MusicInfo> m = new ArrayList<>();
                    int size = musicInfos.size();
                    for (int i = 0; i < mConditionMap.size(); i++){
                        if (mConditionMap.get(i).equals(CONDITION_SONGSLIST_4)){
                            MusicInfo musicInfo = list.get(i);
                            if (size == 0){
                                m.add(musicInfo);
                            } else{
                                boolean has = false;
                                for (int j = 0; j < size; j++){
                                    if (musicInfos.get(j).getTITLE().equals(musicInfo.getTITLE())){
                                        has = true;
                                        break;
                                    }
                                }
                                if (!has){
                                    m.add(musicInfo);
                                }
                            }
                        }
                    }

                    mSendtoDialog.dismiss();
                    //关闭多选窗口
                    closeMultiple();
                    hideItems();
                    mCount = 0;
                    if (m.size() != 0){
                        musicInfos.addAll(m);
                        List<ContentValues> lcv = new ArrayList<>();
                        for (MusicInfo musicInfo : m){
                            ContentValues cv = new ContentValues();
                            cv.put("_ID", musicInfo.get_ID());
                            cv.put("DISPLAY_NAME", musicInfo.getDISPLAY_NAME());
                            cv.put("TITLE", musicInfo.getTITLE());
                            cv.put("DURATION", musicInfo.getDURATION());
                            cv.put("ARTIST", musicInfo.getARTIST());
                            cv.put("ALBUM", musicInfo.getALBUM());
                            cv.put("YEAR", musicInfo.getYEAR());
                            cv.put("MIME_TYPE", musicInfo.getMIME_TYPE());
                            cv.put("SIZE", musicInfo.getSIZE());
                            cv.put("DATA", musicInfo.getDATA());
                            cv.put("IMAGE", musicInfo.getIMAGE());
                            cv.put("LYRIC", musicInfo.getLYRIC());
                            lcv.add(cv);
                        }
                        Utils.setMusicInfo(context, listName, lcv);
                        Fragment fragment = fm.findFragmentByTag("MusicListFragment");
                        if (fragment != null){
                            ((MusicListFragment) fragment).mAdapter.notifyDataSetChanged();
                            addAndRemoveFragment(R.id.fl_main, SongsListFragment.newInstance
                                            (listName),
                                    listName, fragmentName);
                        }else if (fragmentName.equals(Constant.MUSIC_LIST_ALLSONGS_+"所有音乐")){
                            addFragment(R.id.fl_main, SongsListFragment.newInstance
                                            (listName), listName);
                        }else {
                            addAndRemoveFragment(R.id.fl_main, SongsListFragment.newInstance
                                            (listName),
                                    listName, fragmentName);
                        }
                    }
                }
            });
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(rootView);
        mSendtoDialog = builder.show();
        WindowManager.LayoutParams lp = mSendtoDialog.getWindow().getAttributes();
        lp.width = Utils.dip2px(context, 160);
        mSendtoDialog.getWindow().setAttributes(lp);
    }

    /**
     * 打开删除dialog
     */
    private void showDeleteView () {
        View rootView = View.inflate(context, R.layout.dialog_songslist_delete, null);
        final ImageView mIvSongslistDelete = (ImageView) rootView.findViewById(R.id
                .iv_songslist_delete);
        TextView mTvSongslistDelete = (TextView) rootView.findViewById(R.id
                .tv_songslist_delete);
        Button mBtnSongslistDeleteSubmit = (Button) rootView.findViewById(R.id
                .btn_songslist_delete_submit);
        Button mBtnSongslistDeleteCancle = (Button) rootView.findViewById(R.id
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
                int size = mConditionMap.size();
                int k = -1;
                SqlHelper sqlHelper = new SqlHelper(context);
                for (int i = 0; i < size; i++){
                    k++;
                    if (mConditionMap.get(i).equals(CONDITION_SONGSLIST_4)){
                        if (list.get(k).getTITLE().equals(player.mMusicSongsname)){
                            mConditionMap.put(i, CONDITION_SONGSLIST_0);
                            player.mMusicSongsname = null;
                            ((MainActivity) context).mPlayer.resetPlayMusic();
                        }
                        if (isSureDelete){
                            // TODO: 2016/12/21 0021
                            //删除本地文件
                            ((MainActivity) context).deleteSong(list.get(k));
                        }else {
                            //移除
                            switch (fragmentPrefix){
                            case Constant.MUSIC_LIST_ALLSONGS_:
                                break;
                            case Constant.MUSIC_LIST_CUSTOM_:
                                sqlHelper.deleteMusicInfo(fragmentName, "TITLE = ?", new
                                        String[]{list.get(k).getTITLE()});
                                break;
                            case Constant.MUSIC_LIST_ARTIST_:
                                break;
                            case Constant.MUSIC_LIST_ALBUM_:
                                break;
                            case Constant.MUSIC_LIST_DATA_:
                                break;
                            }
                            list.remove(k);
                        }
                        k--;
                    }
                }
                sqlHelper.closeDb();

                //更新conditionMap
                mAdapter.setConditionMap();
                if (Constant.MUSIC_LIST_CUSTOM_.equals(fragmentPrefix)){
                    Fragment fragment = fm.findFragmentByTag("MusicListFragment");
                    if (fragment != null){
                        ((MusicListFragment) fragment).mAdapter.notifyDataSetChanged();
                    }
                } else if (fragmentPrefix != null){
                    AllMusicFragment fragment = (AllMusicFragment) fm.findFragmentByTag("AllMusicFragment");
                    List<Fragment> fragments = fragment.cfm.getFragments();
                    for (int i = 0; i < fragments.size(); i++){
                        if (fragments.get(i).getClass().getSimpleName().equals
                                ("AllMusicVPFragment")){
                            ((AllMusicVPFragment) fragments.get(i)).mAdapter.notifyDataSetChanged();
                        }
                    }
                }
                mAdapter.notifyDataSetChanged();
                mDeleteDialog.dismiss();
                //关闭多选窗口
                closeMultiple();
                mCount = 0;
                //重置listview
                initData2();
            }
        });
        mBtnSongslistDeleteCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                mDeleteDialog.dismiss();
                //关闭多选窗口
                closeMultiple();
                hideItems();
                mCount = 0;
            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(rootView);
        mDeleteDialog = builder.show();
        WindowManager.LayoutParams lp = mDeleteDialog.getWindow().getAttributes();
        lp.width = Utils.dip2px(context, 160);
        mDeleteDialog.getWindow().setAttributes(lp);
    }

    //更改播放模式
    private void transmitPlayModel () {
        if (player.mMediaPlayer.getPlayList() != null){
            player.mMediaPlayer.getPlayList().setPlayModel(mPlayModel);
        }
    }

    private void selectOrNot () {
        mCount++;
        int condition;
        if (mCount % 2 == 0){
            condition = CONDITION_SONGSLIST_3;
            mIvSongslistAll.setBackgroundResource(R.drawable.btn_check_off_holo_light);
        } else{
            condition = CONDITION_SONGSLIST_4;
            mIvSongslistAll.setBackgroundResource(R.drawable.btn_check_on_holo_light);
        }
        for (int i = 0; i < mConditionMap.size(); i++){
            mConditionMap.put(i, condition);
        }
        mAdapter.notifyDataSetChanged();
        showSelectNum();
    }

    private void showSelectNum () {
        num = 0;
        for (int i = 0; i < mConditionMap.size(); i++){
            if (mConditionMap.get(i).equals(CONDITION_SONGSLIST_4)){
                num++;
            }
        }
        mTvSongslistSelect.setText("已选择" + num + "首");
    }

    private void hideItems () {
        isSelectting = false;
        for (int i = 0; i < mConditionMap.size(); i++){
            if (list.get(i).getTITLE().equals(player.mMusicSongsname)){
                mConditionMap.put(i, CONDITION_SONGSLIST_1);
                continue;
            }
            mConditionMap.put(i, CONDITION_SONGSLIST_0);
        }
        mAdapter.notifyDataSetChanged();
    }

    private void showItems () {
        isSelectting = true;
        for (int i = 0; i < mConditionMap.size(); i++){
            mConditionMap.put(i, CONDITION_SONGSLIST_3);
        }
        //先处理动画，后更新数据
        if (mAdapter.isShowAnimation){
            mAdapter.closeView();
            new Thread() {
                @Override
                public void run () {
                    try{
                        Thread.sleep(100);
                    } catch (InterruptedException e){
                        e.printStackTrace();
                    }
                    ((Activity) context).runOnUiThread(new Runnable() {
                        @Override
                        public void run () {
                            mAdapter.notifyDataSetChanged();
                        }
                    });
                }
            }.start();
        } else{
            mAdapter.notifyDataSetChanged();
        }
    }

    private void showPopupWindow () {
        pw = new PopupWindow(Utils.dip2px(context, 100), Utils.dip2px(context, 100));
        View rootView = View.inflate(context, R.layout.popupwindow_songslist_playmodel, null);
        LinearLayout mLlSongslistPopOrder = (LinearLayout) rootView.findViewById(R.id
                .ll_songslist_pop_order);
        LinearLayout mLlSongslistPopRandom = (LinearLayout) rootView.findViewById(R.id
                .ll_songslist_pop_random);
        LinearLayout mLlSongslistPopCycle = (LinearLayout) rootView.findViewById(R.id
                .ll_songslist_pop_cycle);

        mLlSongslistPopOrder.setOnClickListener(this);
        mLlSongslistPopRandom.setOnClickListener(this);
        mLlSongslistPopCycle.setOnClickListener(this);
        pw.setContentView(rootView);
        pw.setBackgroundDrawable(new BitmapDrawable(BitmapFactory.decodeResource(getResources(),
                R.drawable.popupwindow_rectangle_bg)));
        pw.setFocusable(true);
        pw.showAsDropDown(mRlSongslistSelect, Utils.dip2px(context, 40), Utils.dip2px(context,
                30));
    }

    private void closeMultiple () {
        ValueAnimator va_normal = ValueAnimator.ofInt(mHeight_select, 0);
        va_normal.setDuration(100);
        va_normal.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate (ValueAnimator animation) {
                mLp_select.height = (int) animation.getAnimatedValue();
                mRlSongslistSelect.setLayoutParams(mLp_select);
            }
        });
        va_normal.start();

        ValueAnimator va_bottom = ValueAnimator.ofInt(mHeight_bottom, 0);
        va_bottom.setDuration(100);
        va_bottom.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate (ValueAnimator animation) {
                mLp_bottom.height = (int) animation.getAnimatedValue();
                mLlSongslistBottom.setLayoutParams(mLp_bottom);
            }
        });
        va_bottom.start();
    }

    private void openMultiple () {
        ValueAnimator va_normal = ValueAnimator.ofInt(0, mHeight_select);
        va_normal.setDuration(200);
        va_normal.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate (ValueAnimator animation) {
                mLp_select.height = (int) animation.getAnimatedValue();
                mRlSongslistSelect.setLayoutParams(mLp_select);
            }
        });
        va_normal.start();

        ValueAnimator va_bottom = ValueAnimator.ofInt(0, mHeight_bottom);
        va_bottom.setDuration(200);
        va_bottom.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate (ValueAnimator animation) {
                mLp_bottom.height = (int) animation.getAnimatedValue();
                mLlSongslistBottom.setLayoutParams(mLp_bottom);
            }
        });
        va_bottom.start();
    }

    private void initData () {
        player = activity.mPlayer;
        PlayList playList = player.mMediaPlayer.getPlayList();
        if (playList != null){
            switch (playList.getPlayModel()){
            case Constant.PLAYMODEL_ORDER:
                mIvSongslistPlaymodel.setBackgroundResource(R.drawable.order_64px);
                mTvSongslistPlaymodel.setText(Constant.PLAYMODEL_ORDER);
                break;
            case Constant.PLAYMODEL_RANDOM:
                mIvSongslistPlaymodel.setBackgroundResource(R.drawable.random_64px);
                mTvSongslistPlaymodel.setText(Constant.PLAYMODEL_RANDOM);
                break;
            case Constant.PLAYMODEL_CYCLE:
                mIvSongslistPlaymodel.setBackgroundResource(R.drawable.cycle_64px);
                mTvSongslistPlaymodel.setText(Constant.PLAYMODEL_CYCLE);
                break;
            }
        } else{
            mIvSongslistPlaymodel.setBackgroundResource(R.drawable.order_64px);
            mTvSongslistPlaymodel.setText(Constant.PLAYMODEL_ORDER);
        }

        activity.customMusicListName = fragmentName;
        if (fragmentName.contains(Constant.MUSIC_LIST_ALLSONGS_)){
            this.fragmentPrefix = Constant.MUSIC_LIST_ALLSONGS_;
            this.mLlSongslist.setVisibility(View.GONE);
            list = activity.songs_all.get(fragmentName);
        } else if (fragmentName.contains(Constant.MUSIC_LIST_CUSTOM_)){
            if (fragmentName.equals(Constant.MUSIC_LIST_CUSTOM_ + Constant.CUSTOM_LIST_LOVE)){
                list = activity.songs_love.get(fragmentName);
                this.mLlSongslistDelete.setVisibility(View.GONE);
                this.mIvSongslistAdd.setVisibility(View.INVISIBLE);
            } else if (fragmentName.equals(Constant.MUSIC_LIST_CUSTOM_ + Constant.CUSTOM_LIST_LATELY)){
                list = activity.songs_lately.get(fragmentName);
                this.mLlSongslistDelete.setVisibility(View.GONE);
                this.mIvSongslistAdd.setVisibility(View.INVISIBLE);
            }else{
                list = activity.songs_custom.get(fragmentName);
            }
            this.fragmentPrefix = Constant.MUSIC_LIST_CUSTOM_;
            String substring = fragmentName.substring(Constant.MUSIC_LIST_CUSTOM_.length());
            mTvSongslistFragmentname.setText(substring);
        } else if (fragmentName.contains(Constant.MUSIC_LIST_ARTIST_)){
            this.fragmentPrefix = Constant.MUSIC_LIST_ARTIST_;
            this.mIvSongslistAdd.setVisibility(View.INVISIBLE);
            list = activity.songs_artist.get(fragmentName);
            String substring = fragmentName.substring(Constant.MUSIC_LIST_ARTIST_.length());
            mTvSongslistFragmentname.setText(substring);
        } else if (fragmentName.contains(Constant.MUSIC_LIST_ALBUM_)){
            this.fragmentPrefix = Constant.MUSIC_LIST_ALBUM_;
            this.mIvSongslistAdd.setVisibility(View.INVISIBLE);
            list = activity.songs_album.get(fragmentName);
            String substring = fragmentName.substring(Constant.MUSIC_LIST_ALBUM_.length());
            mTvSongslistFragmentname.setText(substring);
        } else if (fragmentName.contains(Constant.MUSIC_LIST_DATA_)){
            this.fragmentPrefix = Constant.MUSIC_LIST_DATA_;
            this.mIvSongslistAdd.setVisibility(View.INVISIBLE);
            list = activity.songs_data.get(fragmentName);
            String substring = fragmentName.substring(Constant.MUSIC_LIST_DATA_.length());
            mTvSongslistFragmentname.setText(substring);
        }
        initData2();
    }

    public void initData2 () {
        if (list == null || list.size() == 0 ){
            mTvSonglistNull.setVisibility(View.VISIBLE);
            mLvSongslistList.setVisibility(View.GONE);
        } else{
            mTvSonglistNull.setVisibility(View.GONE);
            mLvSongslistList.setVisibility(View.VISIBLE);
            mAdapter = new SongsListAdapter(context, this, list, fragmentName, player.mMusicSongsname);
            mConditionMap = mAdapter.conditionMap;
            mShouldRevomeView = mAdapter.shouldRevomeView;
            mLvSongslistList.setAdapter(mAdapter);
            if (prePosition != -1){
                mLvSongslistList.setSelectionFromTop(prePosition, Utils.dip2px(context, 200));
            }
            mLvSongslistList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick (AdapterView<?> parent, View view, int position, long id) {
                    //保存数据
                    SongsListViewHolder holder = (SongsListViewHolder) view.getTag();
                    int[] location = new int[2];
                    holder.mTvListsplayIcon.getLocationOnScreen(location);
                    mX = location[0];
                    mY = location[1];

                    if (!isSelectting){
                        //位置不同时
                        if (prePosition == -1){
                            holder.mVListsplayPlay.setVisibility(View.VISIBLE);
                            mConditionMap.put(position, CONDITION_SONGSLIST_1);

                            //播放或暂停音乐
                            initPlayMusic(position);

                            prePosition = position;
                            mView = view;
                        } else if (prePosition != position){
                            if (isShouldRevomeView){
                                //滑动条目滚出屏幕后滚回时
                                isShouldRevomeView = false;
                                View v = (View) mShouldRevomeView.get("shouldRemoveView");
                                int p = (int) mShouldRevomeView.get("position");
                                SongsListViewHolder _holder = (SongsListViewHolder) v.getTag();
                                _holder.mVListsplayPlay.setVisibility(View.INVISIBLE);
                                mConditionMap.put(p, CONDITION_SONGSLIST_0);

                            } else{
                                SongsListViewHolder mholder = (SongsListViewHolder) mView.getTag();
                                mholder.mVListsplayPlay.setVisibility(View.INVISIBLE);
                                mConditionMap.put(prePosition, CONDITION_SONGSLIST_0);
                            }

                            holder.mVListsplayPlay.setVisibility(View.VISIBLE);
                            mConditionMap.put(position, CONDITION_SONGSLIST_1);

                            //播放或暂停音乐
                            initPlayMusic(position);

                            prePosition = position;
                            mView = view;
                        } else{
                            //根据不同的状态设置
                            switch (mConditionMap.get(position)){
                            case CONDITION_SONGSLIST_0:
                                break;
                            case CONDITION_SONGSLIST_1:
                                mConditionMap.put(position, CONDITION_SONGSLIST_2);
                                break;
                            case CONDITION_SONGSLIST_2:
                                mConditionMap.put(position, CONDITION_SONGSLIST_1);
                                break;
                            case CONDITION_SONGSLIST_3:
                                break;
                            case CONDITION_SONGSLIST_4:
                                break;
                            default:
                                break;
                            }
                            //播放或暂停音乐
                            initPlayMusic(position);
                        }
                    } else{
                        //选择条目
                        switch (mConditionMap.get(position)){
                        case CONDITION_SONGSLIST_0:
                            break;
                        case CONDITION_SONGSLIST_1:
                            break;
                        case CONDITION_SONGSLIST_2:
                            break;
                        case CONDITION_SONGSLIST_3:
                            holder.mIvListsplaySelect.setBackgroundResource(R.drawable
                                    .btn_check_on_holo_light);
                            mConditionMap.put(position, CONDITION_SONGSLIST_4);
                            break;
                        case CONDITION_SONGSLIST_4:
                            holder.mIvListsplaySelect.setBackgroundResource(R.drawable
                                    .btn_check_off_holo_light);
                            mConditionMap.put(position, CONDITION_SONGSLIST_3);
                            break;
                        default:
                            break;
                        }
                        showSelectNum();
                    }
                }
            });


            mLvSongslistList.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged (AbsListView view, int scrollState) {

                }

                //滚出滚入记录
                @Override
                public void onScroll (AbsListView view, int firstVisibleItem,
                                      int visibleItemCount, int totalItemCount) {
                    if (!isSelectting && prePosition != -1 && !isShouldRevomeView){
                        if (prePosition < firstVisibleItem || prePosition >
                                firstVisibleItem + visibleItemCount){
                            isShouldRevomeView = true;
                        }
                    }
                }
            });
        }
    }

    //开始播放音乐或者暂停
    private void initPlayMusic (int position) {
        if (!isPlay){
            isPlay = true;
            if (player.mMediaPlayer.getPlayList() == null ||
                    !player.mMediaPlayer.getPlayList().getListName().equals(fragmentName)){
                PlayList playList = new PlayList();
                playList.setListName(fragmentName);
                playList.setList(this.list);
                playList.setPlayModel(mPlayModel);
                player.mMediaPlayer.setPlayList(playList);
            }
        }
        //加入动画
        if (fragmentName.equals(player.mMusicListname) && player.mMediaPlayer.isStart
                && prePosition == position){
            player.startMusic(position, fragmentName);
        }else {
            addAnimation(position);
        }
    }

    /**
     *
     * @param position
     */
    private void addAnimation (final int position) {
        //获取状态栏高度,屏幕宽高
        if (!hasStatusBarHeight){
            hasStatusBarHeight = true;
            try {
                Class<?> clazz = Class.forName("com.android.internal.R$dimen");
                Object object = clazz.newInstance();
                int height = Integer.parseInt(clazz.getField("status_bar_height")
                        .get(object).toString());
                mStatusBarHeight = getResources().getDimensionPixelSize(height);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Activity activity = (Activity) this.context;
            Display defaultDisplay = activity.getWindowManager().getDefaultDisplay();
            Point point = new Point();
            defaultDisplay.getSize(point);
            mScreenX = point.x;
            mScreenY = point.y;

            int[] p = new int[2];
            RlSongslist.getLocationInWindow(p);
            mContainer_height = p[1];
        }

        if (!isStartAnimation){
            isStartAnimation = true;
            final ImageView iv = new ImageView(context);
            iv.setImageResource(R.drawable.music_96px);
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(Utils.dip2px(context, 25),
                    Utils.dip2px(context, 25));
            lp.leftMargin = mY - mContainer_height + Utils.dip2px(context, 8);
            lp.topMargin = mX + Utils.dip2px(context, 8);
            RlSongslist.addView(iv, lp);

            final int x = mX * 2;
            final ValueAnimator va = ValueAnimator.ofInt(-x, mScreenY-mY);
            double time = 0.80;
            va.setDuration((long) ((mScreenY-mY+x) / time));
            va.setInterpolator(new AccelerateInterpolator(2));
            va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate (ValueAnimator animation) {
                    int value = (int) animation.getAnimatedValue();
                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)
                            iv.getLayoutParams();
                    if (value <= 0){
                        layoutParams.topMargin = (int) (mY - mContainer_height + Utils.dip2px
                                (context, 8) + Math.sin(value * (Math.PI / x)) * mX * 0.4);
                        layoutParams.leftMargin = mX/2 - value/4;
                    } else{
                        layoutParams.topMargin = mY - mContainer_height + Utils.dip2px(context, 8)
                                + value;
                        layoutParams.leftMargin = mX / 2;
                        float v = (float) value / (mScreenY - mY);
                        iv.setAlpha(1 - v);
                    }
                    iv.setLayoutParams(layoutParams);
                }
            });
            va.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart (Animator animation) {

                }

                @Override
                public void onAnimationEnd (Animator animation) {
                    RlSongslist.removeView(iv);
                    player.startMusic(position, fragmentName);
                    isStartAnimation = false;
                }

                @Override
                public void onAnimationCancel (Animator animation) {

                }

                @Override
                public void onAnimationRepeat (Animator animation) {

                }
            });
            va.start();
        }
    }

    /**
     * eventBus 处理的方法
     *
     * @param flag
     */
    @Subscribe(threadMode = ThreadMode.MAIN)       //主线程标识
    public void onEventMainThread (String flag) {
        switch (flag){
        case Constant.UPDATE_FRAGMENT_MODEL:
            switch (activity.mPlayer.mMediaPlayer.getPlayList().getPlayModel()){
            case Constant.PLAYMODEL_ORDER:
                mIvSongslistPlaymodel.setBackgroundResource(R
                        .drawable.order_64px);
                mTvSongslistPlaymodel.setText(Constant.PLAYMODEL_ORDER);
                break;
            case Constant.PLAYMODEL_RANDOM:
                mIvSongslistPlaymodel.setBackgroundResource(R
                        .drawable.random_64px);
                mTvSongslistPlaymodel.setText(Constant.PLAYMODEL_RANDOM);
                break;
            case Constant.PLAYMODEL_CYCLE:
                mIvSongslistPlaymodel.setBackgroundResource(R
                        .drawable.cycle_64px);
                mTvSongslistPlaymodel.setText(Constant.PLAYMODEL_CYCLE);
                break;
            }
            break;
        case Constant.UPDATE_FRAGMENT_LIST:
            initData2();
            break;
        case Constant.UPDATE_PLAY_LATELY:

            break;
        }
    }
}
