package com.example.mrh.musicplayer.constant;

/**
 * Created by MR.H on 2016/12/6 0006.
 */

public class Constant {
    public static final String SYSTEM_MUSIC_LIST_NAME = "musicPlayer";
    public static final String CUSTOM_LIST = "list_custom";
    public static final String CUSTOM_LIST_LOVE = "最爱";
    public static final String CUSTOM_LIST_LATELY = "最近";
    //theme
    public static final String CUSTOM_THEME = "custom_theme";
    public static final String CUSTOM_THEME_NAME = "custom_theme_name";
    public static final String CUSTOM_THEME_DAY = "白天";
    public static final String CUSTOM_THEME_NIGHT = "夜晚";
    //notification
    public static final int NOTIFICATION = 0x00;
    public static final String NOTIFICATION_PRE = "notification_pre";
    public static final String NOTIFICATION_NEXT = "notification_next";
    public static final String NOTIFICATION_PLAY = "notification_play";
    public static final String NOTIFICATION_EXIT = "notification_exit";
    public static final String NOTIFICATION_SHOW = "notification_show";
    //mainActivty
    public static final String OK_DATA = "ok_data"; //初始化加载数据完毕
    public static final String OK_INITSERVICE = "ok_initservice"; //初始化服务
    public static final String UPDATE = "update"; //更新数据变化
    public static final String UPDATE_FRAGMENT_MODEL = "update_fragment_model"; //更新播放模式变化
    public static final String UPDATE_FRAGMENT_LIST = "update_fragment_list"; //更list变化
    public static final String UPDATE_MUSIC_START = "update_music_start";//开始
    public static final String UPDATE_MUSIC_PAUSE = "update_music_pause";//暂停
    public static final String UPDATE_MUSIC_STOP = "update_music_stop";//停止
    public static final String UPDATE_MUSIC_RESET = "update_music_reset";//重置
    public static final String UPDATE_MUSIC_QUIT = "update_music_quit";//退出
    public static final String UPDATE_INIT = "update_init"; //初始化数据
    public static final String UPDATE_PREGRESS = "update_pregress"; //更新进度
    public static final String UPDATE_PLAY_RESET = "update_play_reset"; //清空播放,比如播放时删除了歌曲
    public static final String UPDATE_PLAY_LATELY = "update_play_lately"; //通知 最近 列表更新
    public static final String UPDATE_EXIT = "update_exit"; //通知 退出
    //playActivity
    public static final String PLAYACTIVITY_INITSERVICE =
            "playactivity_initservice";//playactivityl连接上服务
    public static final String PLAYMODEL_ORDER = "单曲播放";
    public static final String PLAYMODEL_RANDOM = "随机播放";
    public static final String PLAYMODEL_CYCLE = "循环播放";

    //sp保存数据
        //播放状态
    public static final String MUSIC_LIST = "music_list";
    public static final String MUSIC_LISTNAME = "music_listname";
    public static final String MUSIC_LISTFRAGMENT = "music_listfragment";
    public static final String MUSIC_SONGSNAME = "music_songsname";
    public static final String MUSIC_ARTISTNAME = "music_artistname";
    public static final String MUSIC_PLAYCONDITION = "music_playcondition";
    public static final String MUSIC_PLAYTIME = "music_playtime";
    public static final String MUSIC_PLAYMODEL = "music_playmodel";
        //播放效果
    public static final String MUSIC_EFFECT = "music_effect";
    public static final String MUSIC_EFFECT_EQULIZER = "music_effect_equlizer";
    public static final String MUSIC_EFFECT_EQULIZER_01 = "music_effect_equlizer_01";
    public static final String MUSIC_EFFECT_EQULIZER_02 = "music_effect_equlizer_02";
    public static final String MUSIC_EFFECT_EQULIZER_03 = "music_effect_equlizer_03";
    public static final String MUSIC_EFFECT_EQULIZER_04 = "music_effect_equlizer_04";
    public static final String MUSIC_EFFECT_EQULIZER_05 = "music_effect_equlizer_05";
    public static final String MUSIC_EFFECT_BASSBOOST = "music_effect_bassboost";
    public static final String MUSIC_EFFECT_PRESETREVERB = "music_effect_presetreverb";
        //最近播放
    public static final String MUSIC_PLAY_LATELY = "music_play_lately";
    public static final int MUSIC_PLAY_LATELY_SPACE = 100;
    public static final int MUSIC_PLAY_LATELY_COUNT = 5;

    //列表类型
    public static final String MUSIC_LIST_CUSTOM_ = "music_list_custom_";
    public static final String MUSIC_LIST_ALLSONGS_ = "music_list_allsongs_";
    public static final String MUSIC_LIST_ARTIST_ = "music_list_artist_";
    public static final String MUSIC_LIST_ALBUM_ = "music_list_album_";
    public static final String MUSIC_LIST_DATA_ = "music_list_data_";
}
