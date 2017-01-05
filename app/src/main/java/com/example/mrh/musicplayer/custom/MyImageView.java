package com.example.mrh.musicplayer.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by MR.H on 2016/12/11 0011.
 */

public class MyImageView extends ImageView{
    //记录当前的position
    private int myPosition;

    public MyImageView (Context context) {
        super(context);
    }

    public MyImageView (Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyImageView (Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public int getMyPosition () {
        return myPosition;
    }

    public void setMyPosition (int myPosition) {
        this.myPosition = myPosition;
    }
}
