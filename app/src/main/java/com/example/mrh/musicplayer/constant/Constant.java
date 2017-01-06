package com.example.mrh.musicplayer.constant;

/**
 * Created by MR.H on 2016/12/6 0006.
 */

public class Constant {
    public static final String SYSTEM_MUSIC_LIST_NAME = "musicPlayer";
    public static final String CUSTOM_LIST = "list_custom";
    //mainActivty
    public static final String OK_DATA = "ok_data"; //初始化加载数据完毕
    public static final String OK_INITSERVICE = "ok_initservice"; //初始化服务
    public static final String UPDATE = "update"; //更新数据变化
    public static final String UPDATE_FRAGMENT_MODEL = "update_fragment_model"; //更新播放模式变化
    public static final String UPDATE_FRAGMENT_LIST = "update_fragment_list"; //更list变化
    public static final String UPDATE_POPUPWINDOW_LOCATION =
            "update_popupwindow_location";//更新数据变化位置
    public static final String UPDATE_INIT = "update_init"; //初始化数据
    public static final String UPDATE_PREGRESS = "update_pregress"; //更新进度
    public static final String UPDATE_PREGRESS_ROTATE = "update_pregress_rotate"; //转动
    public static final String UPDATE_PLAY_RESET = "update_play_reset"; //清空播放
    //playActivity
    public static final String PLAYACTIVITY_INITSERVICE =
            "playactivity_initservice";//playactivityl连接上服务

    public static final String PLAYMODEL_ORDER = "顺序播放";
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

    //列表类型
    public static final String MUSIC_LIST_CUSTOM_ = "music_list_custom_";
    public static final String MUSIC_LIST_ALLSONGS_ = "music_list_allsongs_";
    public static final String MUSIC_LIST_ARTIST_ = "music_list_artist_";
    public static final String MUSIC_LIST_ALBUM_ = "music_list_album_";
    public static final String MUSIC_LIST_DATA_ = "music_list_data_";
}
