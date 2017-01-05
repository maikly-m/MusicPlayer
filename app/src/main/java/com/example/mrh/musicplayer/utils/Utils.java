package com.example.mrh.musicplayer.utils;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.MediaStore;

import com.example.mrh.musicplayer.MyApplication;
import com.example.mrh.musicplayer.constant.Constant;
import com.example.mrh.musicplayer.domain.MusicInfo;
import com.example.mrh.musicplayer.domain.MusicList;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.example.mrh.musicplayer.utils.SqlHelper.getSQLiteDatabase;

/**
 * Created by MR.H on 2016/12/2 0002.
 */

public class Utils {
    private static final String TAG = "Utils";
    private static Context applicationContext;

    public static Context getApplicationContext () {
        if (applicationContext == null){
            applicationContext = MyApplication.mApplicationContext;
        }
        return applicationContext;
    }

    //像素转换
    public static int px2dip(Context context, float px){
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (px / density + 0.5f);
    }
    public static int dip2px(Context context, float dip){
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (dip * density + 0.5f);
    }

    //从contentResolver中获取歌曲信息
    public static ArrayList<MusicInfo> getAllSongs () {
        ArrayList<MusicInfo> list = new ArrayList<>();
        ContentResolver cr = getApplicationContext().getContentResolver();
        //查询文件名， 歌曲名， 时长， 歌手， 专辑， 年代， 类型， 大小， 路径
        //截取大于0.5M的音乐文件
        final Cursor cursor = cr.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Audio.Media._ID, MediaStore.Audio.Media.DISPLAY_NAME,
                        MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.DURATION,
                        MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media.ALBUM,
                        MediaStore.Audio.Media.YEAR, MediaStore.Audio.Media.MIME_TYPE,
                        MediaStore.Audio.Media.SIZE, MediaStore.Audio.Media.DATA},
                MediaStore.Audio.Media.SIZE + ">=?", new String[]{String.valueOf(1024*1024 / 2)}
                , null);
        MusicInfo musicInfo;
        if (cursor != null){
            if (cursor.moveToFirst()){
                do{
                    musicInfo = new MusicInfo();
                    musicInfo.set_ID(cursor.getString(0));
                    musicInfo.setDISPLAY_NAME(cursor.getString(1));
                    musicInfo.setTITLE(cursor.getString(2));
                    musicInfo.setDURATION(cursor.getString(3));
                    musicInfo.setARTIST(cursor.getString(4));
                    musicInfo.setALBUM(cursor.getString(5));
                    if (cursor.getString(6) != null){
                        musicInfo.setYEAR(cursor.getString(6));
                    }else {
                        musicInfo.setYEAR("未知");
                    }
                    if ("audio/x-ms-wma".equals(cursor.getString(7).trim())){
                        musicInfo.setMIME_TYPE("wma");
                    }else {
                        musicInfo.setMIME_TYPE("mp3");
                    }
                    if (cursor.getString(8) != null){

                        float size = cursor.getInt(8) / 1024f / 1024f;
                        musicInfo.setSIZE((size+" ").substring(0, 3) + "M");
                    }else {
                        musicInfo.setSIZE("未知");
                    }
                    if (cursor.getString(9) != null){
                        musicInfo.setDATA(cursor.getString(9));
                    }
                    list.add(musicInfo);
                }while (cursor.moveToNext());
            }
            cursor.close();
        }
        return list;
    }

    /**
     * 获取列表
     * @param context
     * @param selection
     * @param selectionArgs
     * @return
     */
    public static ArrayList<MusicList> getList (Context context, String table, String selection,
                                                String[]selectionArgs){
        ArrayList<MusicList> list = new ArrayList<>();
        HashMap<String, Object> query = SqlHelper.Query(context, table,
                null, selection, selectionArgs, null, null, null);
        Cursor cursor = (Cursor) query.get("cursor");
        SQLiteDatabase db = (SQLiteDatabase) query.get("db");
        if (cursor != null){
            if (cursor.moveToFirst()){
                do{
                    MusicList musicList = new MusicList();
                    musicList.setListName(cursor.getString(1));
                    list.add(musicList);
                }
                while (cursor.moveToNext());
            }
            cursor.close();
        }
        db.close();
        return list;
    }


    /**
     * 添加列表
     * @param context
     * @param cv
     */
    public static void setList (Context context, String table, ContentValues cv){
        SqlHelper.Insert(context, table, cv);
    }

    /**
     * 更新列表
     * @param context
     * @param values
     * @param whereClause
     * @param whereArgs
     */
    public static void updateCustomList (Context context, String table, ContentValues values, String
            whereClause, String[] whereArgs){
        SqlHelper.Update(context, table, values, whereClause, whereArgs);
    }
    /**
     * 删除列表
     * @param context
     * @param whereClause
     * @param whereArgs
     */
    public static void deleteList (Context context, String table, String whereClause,
                                   String[] whereArgs){
        SqlHelper.Delete(context, table, whereClause, whereArgs);
    }
    /**
     * 获取列表歌曲
     * @param context
     * @param selection
     * @param selectionArgs
     * @return
     */
    public static ArrayList<MusicInfo> getMusicInfo (Context context, String table, String
            selection, String[]selectionArgs){
        ArrayList<MusicInfo> list = new ArrayList<>();
        HashMap<String, Object> query = SqlHelper.Query(context, table,
                null, selection, selectionArgs, null, null, null);
        Cursor cursor = (Cursor) query.get("cursor");
        SQLiteDatabase db = (SQLiteDatabase) query.get("db");
        if (cursor != null){
            if (cursor.moveToFirst()){
                do{
                    MusicInfo musicInfo = new MusicInfo();
                    musicInfo.set_ID(cursor.getString(1));
                    musicInfo.setDISPLAY_NAME(cursor.getString(2));
                    musicInfo.setTITLE(cursor.getString(3));
                    musicInfo.setDURATION(cursor.getString(4));
                    musicInfo.setARTIST(cursor.getString(5));
                    musicInfo.setALBUM(cursor.getString(6));
                    musicInfo.setYEAR(cursor.getString(7));
                    musicInfo.setMIME_TYPE(cursor.getString(8));
                    musicInfo.setSIZE(cursor.getString(9));
                    musicInfo.setDATA(cursor.getString(10));
                    musicInfo.setIMAGE(cursor.getString(11));
                    musicInfo.setLYRIC(cursor.getString(12));
                    list.add(musicInfo);
                }
                while (cursor.moveToNext());
            }
            cursor.close();
        }
        db.close();
        return list;
    }

    /**
     * 向列表添加歌曲
     * @param context
     * @param table
     * @param lcv
     */
    public static void setMusicInfo(Context context, String table, List<ContentValues> lcv){
        SQLiteDatabase db= getSQLiteDatabase(context);
        for (ContentValues values : lcv){
            try{
                db.insert(table, null, values);
            }
            catch(Exception e){
                e.getStackTrace();
            }
        }
        db.close();
    }

    /**
     * 更新music信息
     * @param context
     * @param table
     * @param values
     * @param whereClause
     * @param whereArgs
     */
    public static void updateMusicInfo(Context context, String table, ContentValues values, String
            whereClause, String[] whereArgs){
        SqlHelper.Update(context, table, values, whereClause, whereArgs);
    }

    /**
     * 删除歌曲
     * @param context
     * @param whereClause
     * @param whereArgs
     */
    public static void deleteMusicInfo(Context context, String table, String whereClause, String[]
            whereArgs){
        SqlHelper.Delete(context, table, whereClause, whereArgs);
    }

    /**
     * 格式化时间
     * @param duration
     * @return
     */
    public static String formatTime(int duration){
        StringBuilder sb = new StringBuilder();
        int hour = duration / (60*60*1000);
        int minu = (duration - hour*60*60*1000) / (60*1000);
        int sec = (duration - hour*60*60*1000 - minu*60*1000) / 1000;
        if (hour < 10){
            sb.append(0);
        }
        sb.append(hour);
        sb.append(":");
        if (minu < 10){
            sb.append(0);
        }
        sb.append(minu);
        sb.append(":");
        if (sec < 10){
            sb.append(0);
        }
        sb.append(sec);
        if (hour == 0){
            return sb.toString().substring(3);
        }
        return sb.toString();
    }

    public static void deleteTable(Context context,String table){
        SqlHelper.DeleteTable(context, table);
    }

    /**
     * sort 的类型为artist, album, data
     * @param musicInfos
     * @param sort
     * @return
     */
    public static HashMap<String, Object> sortAndCreateList (
            List<MusicInfo> musicInfos, String sort){

        HashMap<String, Object> hashMap = new HashMap<>();
        ArrayList<MusicList> listName = new ArrayList<>();
        HashMap<String, ArrayList<MusicInfo>> map = new HashMap<>();
        String[] sort_init = new String[musicInfos.size()];
        String[] sort_handle = new String[musicInfos.size()];
        switch (sort){
        case "artist":
            for (int i = 0; i < musicInfos.size(); i++){
                sort_handle[i] = musicInfos.get(i).getARTIST();
                sort_init[i] = musicInfos.get(i).getARTIST();
            }
            //去除重复的sort 名
            for (int i = 0; i < sort_handle.length; i++){
                if (!sort_handle[i].equals("no_artist")){
                    for (int j = i+1; j < sort_handle.length; j++){
                        if (sort_handle[i].equals(sort_handle[j])){
                            sort_handle[j] = "no_artist";
                        }
                    }
                }
            }
            //匹配加入map
            for (int i = 0; i < sort_handle.length; i++){
                if (!sort_handle[i].equals("no_artist")){
                    ArrayList<MusicInfo> m = new ArrayList<>();
                    m.add(musicInfos.get(i));
                    for (int j = i+1; j < sort_init.length; j++){
                        if (sort_handle[i].equals(sort_init[j])){
                            m.add(musicInfos.get(j));
                        }
                    }
                    MusicList musicList = new MusicList();
                    musicList.setListName(Constant.MUSIC_LIST_ARTIST_+sort_handle[i]);
                    listName.add(musicList);
                    map.put(Constant.MUSIC_LIST_ARTIST_+sort_handle[i], m);
                }
            }
            break;
        case "album":
            for (int i = 0; i < musicInfos.size(); i++){
                sort_handle[i] = musicInfos.get(i).getALBUM();
                sort_init[i] = musicInfos.get(i).getALBUM();
            }
            //去除重复的sort 名
            for (int i = 0; i < sort_handle.length; i++){
                if (!sort_handle[i].equals("no_album")){
                    for (int j = i+1; j < sort_handle.length; j++){
                        if (sort_handle[i].equals(sort_handle[j])){
                            sort_handle[j] = "no_album";
                        }
                    }
                }
            }
            //匹配加入map
            for (int i = 0; i < sort_handle.length; i++){
                if (!sort_handle[i].equals("no_album")){
                    ArrayList<MusicInfo> m = new ArrayList<>();
                    m.add(musicInfos.get(i));
                    for (int j = i+1; j < sort_init.length; j++){
                        if (sort_handle[i].equals(sort_init[j])){
                            m.add(musicInfos.get(j));
                        }
                    }
                    MusicList musicList = new MusicList();
                    musicList.setListName(Constant.MUSIC_LIST_ALBUM_+sort_handle[i]);
                    listName.add(musicList);
                    map.put(Constant.MUSIC_LIST_ALBUM_+sort_handle[i], m);
                }
            }
            break;
        case "data":
            for (int i = 0; i < musicInfos.size(); i++){
                String s1= musicInfos.get(i).getDATA();
                int lastIndexOf1 = s1.lastIndexOf("/");
                sort_handle[i] = s1.substring(0, lastIndexOf1);

                String s2= musicInfos.get(i).getDATA();
                int lastIndexOf2 = s2.lastIndexOf("/");
                sort_init[i] = s1.substring(0, lastIndexOf2);
            }
            //去除重复的sort 名
            for (int i = 0; i < sort_handle.length; i++){
                if (!sort_handle[i].equals("no_data")){
                    for (int j = i+1; j < sort_handle.length; j++){
                        if (sort_handle[i].equals(sort_handle[j])){
                            sort_handle[j] = "no_data";
                        }
                    }
                }
            }
            //匹配加入map
            for (int i = 0; i < sort_handle.length; i++){
                if (!sort_handle[i].equals("no_data")){
                    ArrayList<MusicInfo> m = new ArrayList<>();
                    m.add(musicInfos.get(i));
                    for (int j = i+1; j < sort_init.length; j++){
                        if (sort_handle[i].equals(sort_init[j])){
                            m.add(musicInfos.get(j));
                        }
                    }
                    MusicList musicList = new MusicList();
                    musicList.setListName(Constant.MUSIC_LIST_DATA_+sort_handle[i]);
                    listName.add(musicList);
                    map.put(Constant.MUSIC_LIST_DATA_+sort_handle[i], m);
                }
            }
            break;
        }
        hashMap.put("listName", listName);
        hashMap.put("list", map);
        return hashMap;
    }

    /**
     * 删除文件
     * @param path
     * @return
     */
    public static boolean deleteFile(String path){
        File file = new File(path);
        if (file.isFile()){
            return file.delete();
        }
        return false;
    }
}
