package com.example.mrh.musicplayer.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.example.mrh.musicplayer.R;

import java.util.TreeMap;

/**
 * Created by MR.H on 2017/1/8 0008.
 */

public class MyLyrcisView extends View {

    private Paint mPaint;
    private Paint mPaint_;
    private int mMeasuredWidth;
    private int mMeasuredHeight;
    private TreeMap<Integer, String> treeMap;

    public MyLyrcisView (Context context) {
        this(context, null);
    }

    public MyLyrcisView (Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(context.getResources().getColor(R.color.white_));
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(1);
        mPaint.setTextSize(35);

        mPaint_ = new Paint();
        mPaint_.setAntiAlias(true);
        mPaint_.setColor(context.getResources().getColor(R.color.colorAccent));
        mPaint_.setStyle(Paint.Style.FILL);
        mPaint_.setStrokeWidth(1);
        mPaint_.setTextSize(35);
    }

    public void updateLrc(TreeMap<Integer, String> treeMap){
        this.treeMap = treeMap;
        if (treeMap != null){
            postInvalidate();
        }
    }

    @Override
    protected void onMeasure (int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int width;
        int height ;
        if (widthMode == MeasureSpec.EXACTLY && heightMode == MeasureSpec.EXACTLY){
            width = widthSize;
            height = heightSize;
            setMeasuredDimension(width, height);
        }else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onSizeChanged (int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mMeasuredWidth = getMeasuredWidth();
        mMeasuredHeight = getMeasuredHeight();
    }

    @Override
    protected void onDraw (Canvas canvas) {
        super.onDraw(canvas);
        if (treeMap == null){
            return;
        }
        for (int i = 0; i < treeMap.size(); i++){
            if (i == 3){
                canvas.drawText(treeMap.get(i), 0, mMeasuredHeight*i/treeMap.size(), mPaint_);
            }
            canvas.drawText(treeMap.get(i), 0, mMeasuredHeight*i/treeMap.size()+50, mPaint);
        }
    }
}
