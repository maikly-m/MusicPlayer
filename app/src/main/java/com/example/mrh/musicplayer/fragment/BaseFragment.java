package com.example.mrh.musicplayer.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.example.mrh.musicplayer.R;
import com.example.mrh.musicplayer.activity.BaseActivity;

import java.util.List;

/**
 * Created by MR.H on 2016/12/3 0003.
 */

public class BaseFragment extends Fragment {
    private static final String TAG = "BaseFragment";
    public Context context;
    public BaseActivity mActivity;
    public FragmentManager fm;
    public FragmentManager cfm;
    public String fragmentName;

    public BaseFragment () {
        super();
    }

    @Override
    public void onAttach (Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public void setFragmentName (String fragmentName) {
        this.fragmentName = fragmentName;
    }

    @Override
    public void onCreate (@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = (BaseActivity) context;
        fm = mActivity.getSupportFragmentManager();
        cfm = getChildFragmentManager();
    }

    @Override
    public void onDestroy () {
        super.onDestroy();
    }

    /**
     * hide others
     *
     * @param containerViewId
     * @param fragment
     * @param tag
     * @return
     */
    public boolean addFragment (@IdRes int containerViewId, Fragment fragment, String tag) {
        //getFragments()会取出所有add的fragment，不管是否调用remove的fragment
        List<Fragment> fragments = fm.getFragments();
        FragmentTransaction ft = fm.beginTransaction();
        ft.setCustomAnimations(R.anim.fragment_slide_left_enter, R.anim
                .fragment_slide_left_exit);
        if (fragments != null){
            for (int i = 0; i < fragments.size(); i++){
                if (fragments.get(i) != null){
                    //判断非空
                    ft.hide(fragments.get(i));
                }
            }
        }
        ft.add(containerViewId, fragment, tag)
                .commit();
        ((BaseFragment) fragment).setFragmentName(tag);
        return true;
    }

    /**
     * show fragment
     *
     * @param tag
     * @return
     */
    public boolean showFragment (String tag) {
        List<Fragment> fragments = fm.getFragments();
        if (fragments != null){
            FragmentTransaction ft = fm.beginTransaction();
            ft.setCustomAnimations(R.anim.fragment_slide_left_enter, R.anim
                    .fragment_slide_left_exit);
            for (int i = 0; i < fragments.size(); i++){
                if (fragments.get(i) != null){
                    //判断非空
                    ft.hide(fragments.get(i));
                }
            }
            ft.show(fm.findFragmentByTag(tag))
                    .commit();
            return true;
        }
        return false;
    }

    /**
     * remove Fragment
     *
     * @param tag
     * @return
     */
    public boolean removeFragment (String tag) {
        List<Fragment> fragments = fm.getFragments();
        if (fragments != null){
            fm.beginTransaction()
//                    .detach(fm.findFragmentByTag(tag))
                    .remove(fm.findFragmentByTag(tag))
                    .commit();
            return true;
        }
        return false;
    }

    /**
     * hide Fragment
     *
     * @param tag
     * @return
     */
    public boolean hideFragment (String tag) {
        List<Fragment> fragments = fm.getFragments();
        if (fragments != null){
            fm.beginTransaction()
                    .hide(fm.findFragmentByTag(tag))
                    .commit();
            return true;
        }
        return false;
    }

    /**
     * show And Remove Fragment
     *
     * @param showTag
     * @param removeTag
     * @return
     */
    public boolean showAndRemoveFragment (String showTag, String removeTag) {
        List<Fragment> fragments = fm.getFragments();
        if (fragments != null){
            fm.beginTransaction()
                    .setCustomAnimations(R.anim.fragment_slide_right_enter, R.anim
                            .fragment_slide_right_exit)
//                    .detach(fm.findFragmentByTag(removeTag))
                    .remove(fm.findFragmentByTag(removeTag))
                    .show(fm.findFragmentByTag(showTag))
                    .commit();
            return true;
        }
        return false;
    }

    /**
     *
     * @param containerViewId
     * @param fragment
     * @param addTag
     * @param removeTag
     * @return
     */
    public boolean addAndRemoveFragment (@IdRes int containerViewId, Fragment fragment, String
            addTag, String removeTag) {
        List<Fragment> fragments = fm.getFragments();
        if (fragments != null){
            fm.beginTransaction()
                    .setCustomAnimations(R.anim.fragment_slide_left_enter, R.anim
                            .fragment_slide_left_exit)
//                    .detach(fm.findFragmentByTag(removeTag))
                    .remove(fm.findFragmentByTag(removeTag))
                    .add(containerViewId, fragment, addTag)
                    .commit();
            ((BaseFragment) fragment).setFragmentName(addTag);
            return true;
        }
        return false;
    }

}