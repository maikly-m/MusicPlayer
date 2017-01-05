package com.example.mrh.musicplayer.fragment;

import android.content.ContentValues;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mrh.musicplayer.R;
import com.example.mrh.musicplayer.activity.BaseActivity;
import com.example.mrh.musicplayer.domain.MusicInfo;
import com.example.mrh.musicplayer.fragment.adapter.PhoneMusicAdapter;
import com.example.mrh.musicplayer.fragment.viewHolder.PhoneMusicViewHolder;
import com.example.mrh.musicplayer.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import static com.example.mrh.musicplayer.fragment.adapter.PhoneMusicAdapter.CONDITION_PHONEMUSIC_0;
import static com.example.mrh.musicplayer.fragment.adapter.PhoneMusicAdapter.CONDITION_PHONEMUSIC_1;

/**
 * Created by MR.H on 2016/12/7 0007.
 */

public class PhoneMusicFragment extends BaseFragment implements View.OnClickListener {
    private static final String TAG = "PhoneMusicFragment";

    private View mRootView;
    private Button mBtnSubmitPhonemusic;
    private ListView mLvPhonemusic;
    private ArrayList<MusicInfo> list;
    private PhoneMusicAdapter mAdapter;
    private ImageView mIvPhonemusicBack;
    private ImageView mIvPhonemusicAll;
    private TreeMap<Integer, Integer> mConditionMap;
    private int count = 0; //用于判断是否点击了全选
    private TextView mTvPhonemusicSelect;
    private int num = 0;

    public PhoneMusicFragment () {
        super();
    }

    @Nullable
    @Override
    public View onCreateView (LayoutInflater inflater, @Nullable ViewGroup container,
                              @Nullable Bundle savedInstanceState) {
        if (mRootView == null){
            mRootView = inflater.inflate(R.layout.fragment_phonemusic, null);
            initView();
            initData();
        }
        return mRootView;

    }

    private void initData () {
        list = mActivity.mAllSongs;
        mAdapter = new PhoneMusicAdapter(context, list);
        mLvPhonemusic.setAdapter(mAdapter);
        mConditionMap = mAdapter.conditionMap;

        mLvPhonemusic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick (AdapterView<?> parent, View view, int position, long id) {
                //保存数据
                PhoneMusicViewHolder holder = (PhoneMusicViewHolder) view.getTag();
                //根据不同的状态设置
                switch (mConditionMap.get(position)){
                case CONDITION_PHONEMUSIC_0:
                    holder.mIvPhoneMusicSelect.setBackgroundResource(R.drawable.btn_check_on_holo_light);
                    mConditionMap.put(position, CONDITION_PHONEMUSIC_1);
                    break;
                case CONDITION_PHONEMUSIC_1:
                    holder.mIvPhoneMusicSelect.setBackgroundResource(R.drawable.btn_check_off_holo_light);
                    mConditionMap.put(position, CONDITION_PHONEMUSIC_0);
                    break;
                default:
                    break;
                }
                showSelectNum();
            }
        });

        //back键处理fragment返回
        mActivity.setOnFragmentBack(new BaseActivity.FragmentBack() {
            @Override
            public boolean execute (KeyEvent event) {
                showAndRemoveFragment("MusicListFragment", fragmentName);
                return true;
            }
        }, fragmentName);
    }

    private void initView () {
        mBtnSubmitPhonemusic = (Button) mRootView.findViewById(R.id.btn_submit_phonemusic);
        mLvPhonemusic = (ListView) mRootView.findViewById(R.id.lv_phonemusic);

        mBtnSubmitPhonemusic.setOnClickListener(this);
        mIvPhonemusicBack = (ImageView) mRootView.findViewById(R.id.iv_phonemusic_back);
        mIvPhonemusicBack.setOnClickListener(this);
        mIvPhonemusicAll = (ImageView) mRootView.findViewById(R.id.iv_phonemusic_all);
        mIvPhonemusicAll.setOnClickListener(this);
        mTvPhonemusicSelect = (TextView) mRootView.findViewById(R.id.tv_phonemusic_select);
    }

    @Override
    public void onClick (View v) {
        switch (v.getId()){
        case R.id.btn_submit_phonemusic:
            writeData();
            break;
        case R.id.iv_phonemusic_back:
            showAndRemoveFragment("MusicListFragment", fragmentName);
            break;
        case R.id.iv_phonemusic_all:
            if (count % 2 == 0){
                mIvPhonemusicAll.setBackgroundResource(R.drawable.btn_check_on_holo_light);
                for (int i = 0; i < list.size(); i++){
                    mConditionMap.put(i, CONDITION_PHONEMUSIC_1);
                }
            } else{
                mIvPhonemusicAll.setBackgroundResource(R.drawable.btn_check_off_holo_light);
                for (int i = 0; i < list.size(); i++){
                    mConditionMap.put(i, CONDITION_PHONEMUSIC_0);
                }
            }
            mAdapter.notifyDataSetChanged();
            count++;
            showSelectNum();
            break;
        }
    }

    private void showSelectNum () {
        num = 0;
        for (int i = 0; i < mConditionMap.size(); i++){
            if (mConditionMap.get(i).equals(CONDITION_PHONEMUSIC_1)){
                num++;
            }
        }
        mTvPhonemusicSelect.setText("已选择" + num + "首");
    }
    /**
     * 更新数据
     */
    private void writeData () {
        ArrayList<MusicInfo> l = new ArrayList<>();
        for (int i = 0; i < mConditionMap.size(); i++){
            if (mConditionMap.get(i) == CONDITION_PHONEMUSIC_1){
                l.add(list.get(i));
            }
        }
        if (l.size() == 0){
            Toast.makeText(context, "没有选择歌曲...", Toast.LENGTH_SHORT).show();
            return;
        }
        //判断原先是否有歌曲
        ArrayList<MusicInfo> musicInfos = mActivity.songs_custom.get(mActivity.customMusicListName);
        ArrayList<MusicInfo> mm = new ArrayList<>();
        for (int i = 0; i < l.size(); i++){
            mm.add(l.get(i));
        }
        if (musicInfos.size() != 0){
            for (MusicInfo m : musicInfos){
                for (MusicInfo m1 : l){
                    if (m1.getTITLE().equals(m.getTITLE())){
                        mm.remove(m1);
                    }
                }
            }
        }
        musicInfos.addAll(mm);
        mActivity.songs_custom.put(mActivity.customMusicListName, musicInfos);

        MusicListFragment fragment = (MusicListFragment) fm.findFragmentByTag("MusicListFragment");
        fragment.mAdapter.notifyDataSetChanged();

        List<ContentValues> lcv = new ArrayList<>();
        for (MusicInfo m : mm){
            ContentValues cv = new ContentValues();
            cv.put("_ID", m.get_ID());
            cv.put("DISPLAY_NAME", m.getDISPLAY_NAME());
            cv.put("TITLE", m.getTITLE());
            cv.put("DURATION", m.getDURATION());
            cv.put("ARTIST", m.getARTIST());
            cv.put("ALBUM", m.getALBUM());
            cv.put("YEAR", m.getYEAR());
            cv.put("MIME_TYPE", m.getMIME_TYPE());
            cv.put("SIZE", m.getSIZE());
            cv.put("DATA", m.getDATA());
            cv.put("IMAGE", m.getIMAGE());
            cv.put("LYRIC", m.getLYRIC());
            lcv.add(cv);
        }
        Utils.setMusicInfo(context, mActivity.customMusicListName, lcv);

        addAndRemoveFragment(R.id.fl_main, SongsListFragment.newInstance
                (mActivity.customMusicListName), mActivity.customMusicListName,
                "PhoneMusicFragment");
    }
}
