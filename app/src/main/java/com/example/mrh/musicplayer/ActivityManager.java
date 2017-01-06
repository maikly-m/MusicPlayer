package com.example.mrh.musicplayer;

import android.app.Activity;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by MR.H on 2016/12/3 0003.
 */

public class ActivityManager {
    private static ActivityManager activityManager;
    private List<Activity> mActivities = new LinkedList<>();

    private ActivityManager(){

    }
    //获得ActivityManager
    public static synchronized ActivityManager getActivityManager(){
        if (activityManager == null){
            activityManager = new ActivityManager();
        }
        return activityManager;
    }
    //添加activity
    public void addActivity(Activity activity){
        if (activity != null && !mActivities.contains(activity)){
            mActivities.add(activity);
        }
    }

    /**
     * 移除activity, activityd的ondestroy()调用
     * @param activity
     */
    public void deleteActivity(Activity activity){
        if (activity != null && mActivities.contains(activity)){
            mActivities.remove(activity);
        }
    }
    //移除并关闭activity
    public void removeActivity(Activity activity){
        if (activity != null && mActivities.contains(activity)){
            mActivities.remove(activity);
            activity.finish();
        }
    }
    //移除和关闭所有activity
    public void removeAllActivity(){
        for (Activity activity: mActivities){
            if (activity != null){
                activity.finish();
            }
        }
        mActivities.clear();
    }
    //移除和关闭指定cls的activity
    public void removeActivity(Class<?> cls){
        for (Activity activity : mActivities){
            if (activity.getClass().equals(cls)){
                mActivities.remove(activity);
                activity.finish();
                break;
            }
        }
    }
    //获得activity列表
    public List<Activity> getActivityList(){
        return mActivities;
    }
    //退出程序
    public void exitApp(){
        try{
            removeAllActivity();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

}
