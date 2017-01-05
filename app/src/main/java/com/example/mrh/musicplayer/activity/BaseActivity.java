package com.example.mrh.musicplayer.activity;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.widget.Toast;

import com.example.mrh.musicplayer.ActivityManager;

import java.util.HashMap;
import java.util.List;

/**
 * Created by MR.H on 2016/12/3 0003.
 */

public class BaseActivity extends AppCompatActivity {
    private static final String TAG = "BaseActivity";

    private HashMap<String, FragmentBack> mFragmentBacks = new HashMap<>();
    private FragmentManager fm;
    private long[] mHits = new long[2]; //返回键双击事件记录数组;

    @Override
    protected void onCreate (@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityManager.getActivityManager().addActivity(this);
        fm = getSupportFragmentManager();
    }

    @Override
    protected void onDestroy () {
        super.onDestroy();
        ActivityManager.getActivityManager().removeActivity(this);
    }

    /**
     * fragment返回键监听
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown (int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK){
            //不包括在childfragmentmanager中的，fragment要存到fragmentmanager才可以
            if (mFragmentBacks != null){
                List<Fragment> fragments = fm.getFragments();
                for (Fragment fragment : fragments){
                    if (fragment !=null && fragment.isVisible()){
                        String mTag = fragment.getTag();
                        FragmentBack fragmentBack = mFragmentBacks.get(mTag);
                        if (fragmentBack != null){
                            fragmentBack.execute(event);
                            return true;
                        }
                    }
                }
            }
            //转到后台

            //每点击一次 实现左移一格数据
            System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
            //给数组的最后赋当前时钟值
            mHits[mHits.length - 1] = SystemClock.uptimeMillis();
            //当0出的值大于当前时间-1500时  证明在1500秒内点击了2次
            if (mHits[0] > SystemClock.uptimeMillis() - 1200){
                moveTaskToBack(true);
            } else{
                Toast.makeText(this, "再点击一次返回桌面", Toast.LENGTH_SHORT).show();
            }
            return true;

        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     *
     * @param fragmentBack
     * @param tag
     */
    public void setOnFragmentBack (FragmentBack fragmentBack, String tag) {
        mFragmentBacks.put(tag, fragmentBack);
    }

    /**
     * interface
     */
    public interface FragmentBack {
        boolean execute (KeyEvent event);
    }
}
