package com.example.mrh.musicplayer.utils;

import android.os.Environment;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by MR.H on 2016/12/3 0003.
 */

public class FileUtils {
    private static final String TAG = "FileUtils";
    private String MNT_SDCARD_ROOT;
    private List<String> mList;

    public FileUtils () {
        this.MNT_SDCARD_ROOT = Environment.getExternalStorageDirectory().getAbsolutePath();
        DebugUtils.log_d(TAG, MNT_SDCARD_ROOT);
        mList = new ArrayList<>();
    }

    /**
     * 获取音乐文件的路径
     * @return
     */
    public List<String> getFile(){
        scanFile(MNT_SDCARD_ROOT);
        for (int j = 0; j < mList.size(); j++){
            DebugUtils.log_d(TAG, mList.get(j));
        }
        return mList;
    }

    private void scanFile (String path) {
        File[] file = getfile(path);
        for (int i = 0; i < file.length; i++){
            String absolutePath = file[i].getAbsolutePath();
            scanFile(absolutePath);
        }
    }

    private File[] getfile (String filePath) {
        File file = new File(filePath);
        File[] files = file.listFiles(new FileFilter() {
            @Override
            public boolean accept (File pathname) {
                if (pathname.isDirectory() && pathname.getPath().contains
                        (MNT_SDCARD_ROOT)){
                    return true;
                } else{
                    String path = pathname.getPath();
                    int length = path.length();
                    if (path.lastIndexOf(".mp3") + 4 == length || path.lastIndexOf(".wav") +
                            4 == length || path.lastIndexOf(".wma") + 4 == length){
                        mList.add(pathname.getAbsolutePath());
                    }
                    return false;
                }
            }
        });
        return files;
    }
}
