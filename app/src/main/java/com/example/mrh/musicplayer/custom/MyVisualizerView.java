package com.example.mrh.musicplayer.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by MR.H on 2017/1/7 0007.
 */

public class MyVisualizerView extends View {
    private static final String TAG = "MyVisualizerView";

    private Paint mPaint;
    private byte[] waveform;
    private Rect mRect;
    private int mMeasuredWidth;
    private int mMeasuredHeight;
    private int mTop;
    private int speed = 0;
    private int mLeft;
    private int mRight;
    private int mBottom;

    public MyVisualizerView (Context context) {
        this(context, null);
    }

    public MyVisualizerView (Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyVisualizerView (Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.BLUE);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(1);
    }

    public void updateVisualizer(byte[] waveform){
        this.waveform = waveform;
        if (speed == 1){
            speed = 0;
            invalidate();
        }
        speed++;
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
        if (mMeasuredWidth <= 0){
            mMeasuredWidth = getMeasuredWidth();
            mMeasuredHeight = getMeasuredHeight();
            mRect = new Rect(0, 0, mMeasuredWidth, mMeasuredHeight);
            mBottom = mRect.height();
        }
    }

    @Override
    protected void onDraw (Canvas canvas) {
        super.onDraw(canvas);
        if (waveform == null){
            return;
        }
        for (int i = 0; i < 1000; i+=25){
            mLeft = mRect.width() * i / 1000;
            mTop = mBottom - (mBottom * (128 + waveform[i])) / 255;
            mRight = mLeft + 12;
            canvas.drawRect(mLeft, mTop, mRight, mBottom, mPaint);
        }
    }
}
