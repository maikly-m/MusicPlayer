package com.example.mrh.musicplayer.fragment;

import android.content.ContentValues;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mrh.musicplayer.R;
import com.example.mrh.musicplayer.activity.BaseActivity;
import com.example.mrh.musicplayer.activity.MainActivity;
import com.example.mrh.musicplayer.constant.Constant;
import com.example.mrh.musicplayer.domain.MusicInfo;
import com.example.mrh.musicplayer.domain.MusicList;
import com.example.mrh.musicplayer.fragment.adapter.MusicListAdapter;
import com.example.mrh.musicplayer.utils.SqlHelper;
import com.example.mrh.musicplayer.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.example.mrh.musicplayer.constant.Constant.CUSTOM_LIST;
import static com.example.mrh.musicplayer.constant.Constant.CUSTOM_LIST_LATELY;
import static com.example.mrh.musicplayer.constant.Constant.CUSTOM_LIST_LOVE;

/**
 * Created by MR.H on 2016/12/4 0004.
 */

public class MusicListFragment extends BaseFragment implements View.OnClickListener {
    private static final String TAG = "MusicListFragment";

    private View mRootView;
    private LinearLayout mLlListBack;
    private ImageView mIvAddMusic;
    private AlertDialog mDialog;
    private EditText mEtMusiclist;
    private ListView mLvMusiclist;
    private List<MusicList> list;
    public MusicListAdapter mAdapter;
    public TextView mTvListNum;
    public HashMap<String, ArrayList<MusicInfo>> songs;
    private MainActivity activity;

    public MusicListFragment () {
        super();
    }

    @Nullable
    @Override
    public View onCreateView (LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        if (mRootView == null){
            mRootView = inflater.inflate(R.layout.fragment_musiclist, null);
            initView();
            if (activity.list_custom != null){
                initData();
            }
        }
        return mRootView;
    }

    @Override
    public void onCreate (@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.activity = (MainActivity)mActivity;
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy () {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void initData () {
        list = activity.list_custom;
        songs = activity.songs_custom;
        mTvListNum.setText("歌单("+(list.size()-2)+")");
        mAdapter = new MusicListAdapter(context, this, list);
        mLvMusiclist.setAdapter(mAdapter);
        mLvMusiclist.setDividerHeight(0);
        mLvMusiclist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick (AdapterView<?> parent, View view, int position, long id) {
                selectFragment(list.get(position+1).getListName());
            }
        });
    }

    /**
     * 判断列表是否空，然后跳转
     */
    private void selectFragment (String name) {
        ArrayList<MusicInfo> list = activity.songs_custom.get(name);
        if (list == null || list.size() == 0){
            //转到添加歌曲界面
            activity.customMusicListName = name;
            addFragment(R.id.fl_main, new MusicNullFragment(), name);
        } else{
            //转到已有的歌曲界面
            addFragment(R.id.fl_main, SongsListFragment.newInstance(name), name);
        }
    }

    private void initView () {
        mLlListBack = (LinearLayout) mRootView.findViewById(R.id.ll_list_back);
        mIvAddMusic = (ImageView) mRootView.findViewById(R.id.iv_add_music);
        mLvMusiclist = (ListView) mRootView.findViewById(R.id.lv_musiclist);
        mTvListNum = (TextView) mRootView.findViewById(R.id.tv_list_num);

        mLlListBack.setOnClickListener(this);
        mIvAddMusic.setOnClickListener(this);
        mTvListNum.setOnClickListener(this);

        //back键处理fragment返回
        activity.setOnFragmentBack(new BaseActivity.FragmentBack() {
            @Override
            public boolean execute (KeyEvent event) {
                showAndRemoveFragment("ContentFragment", fragmentName);
                return true;
            }
        }, fragmentName);

    }

    @Override
    public void onClick (View v) {
        switch (v.getId()){
        case R.id.ll_list_back:
            showAndRemoveFragment("ContentFragment", fragmentName);
            break;
        case R.id.iv_add_music:
            openDialog();
            break;
        case R.id.btn_musiclist_cancle:
            mDialog.dismiss();
            break;
        case R.id.btn_musiclist_submit:
            String musicListName = mEtMusiclist.getText().toString();
            mDialog.dismiss();
            newMusicList(Constant.MUSIC_LIST_CUSTOM_+musicListName);
            break;
        }
    }

    private void newMusicList (String name) {
        //检查名字是否重复
        if (name.equals("")){
            return;
        }
        if (list != null){
            for (int i = 0; i < list.size(); i++){
                if (list.get(i).getListName().equals(name) || list.get(i).getListName().equals
                        (CUSTOM_LIST) || list.get(i).getListName().equals(CUSTOM_LIST_LOVE)  
                        || list.get(i).getListName().equals(CUSTOM_LIST_LATELY)){
                    Toast.makeText(context, "名字重复了，重新输入", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        }
        MusicList musicList = new MusicList();
        musicList.setListName(name);
        openDataBase(name);
        list.add(musicList);
        activity.songs_custom.put(name, new ArrayList<MusicInfo>());
        mAdapter.notifyDataSetChanged();
        mTvListNum.setText("歌单("+(list.size()-2)+")");
    }

    private void openDataBase (String name) {
        if (list == null){
            SqlHelper.CreatePlayTable(context, CUSTOM_LIST);
            ContentValues cv = new ContentValues();
            cv.put("listName", Constant.MUSIC_LIST_CUSTOM_ + Constant
                    .CUSTOM_LIST_LOVE);
            Utils.setList(context, CUSTOM_LIST, cv);
            SqlHelper.CreateMusicTable(context, Constant.MUSIC_LIST_CUSTOM_ + Constant
                    .CUSTOM_LIST_LOVE);
            MusicList m = new MusicList();
            m.setListName(Constant.MUSIC_LIST_CUSTOM_ + Constant
                    .CUSTOM_LIST_LOVE);
            list.add(m);
        }
        ContentValues cv = new ContentValues();
        cv.put("listName", name);
        Utils.setList(context, CUSTOM_LIST, cv);
        SqlHelper.CreateMusicTable(context, name);
    }

    private void openDialog () {
        View rootView = View.inflate(context, R.layout.dialog_newmusiclist, null);
        mEtMusiclist = (EditText) rootView.findViewById(R.id.et_musiclist);
        Button btnMusiclistCancle = (Button) rootView.findViewById(R.id.btn_musiclist_cancle);
        btnMusiclistCancle.setOnClickListener(this);
        Button btnMusiclistSubmit = (Button) rootView.findViewById(R.id.btn_musiclist_submit);
        btnMusiclistSubmit.setOnClickListener(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(rootView);
        mDialog = builder.show();
    }

    /**
     * eventBus 处理的方法
     *
     * @param flag
     */
    @Subscribe(threadMode = ThreadMode.MAIN)       //主线程标识
    public void onEventMainThread (String flag) {
        if (Constant.OK_DATA.equals(flag) && list == null){
            initData();
        }
    }
}
