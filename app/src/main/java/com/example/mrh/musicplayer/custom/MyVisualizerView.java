package com.example.mrh.musicplayer.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.example.mrh.musicplayer.R;

/**
 * Created by MR.H on 2017/1/7 0007.
 */

public class MyVisualizerView extends View {
    private static final String TAG = "MyVisualizerView";
    private Paint mPaint;
    private Paint mPaint_mirror;
    private byte[] waveform;
    private Rect mRect;
    private int mMeasuredWidth;
    private int mMeasuredHeight;
    private int mTop;
    private int speed = 0;
    private int mLeft;
    private int mRight;
    private int mHeight;
    private Rect mRect_mirror;
    private int mBottom_mirror;
    private int mHeight_mirror;
    private double mRect_top;
    private double mRect_mirror_top;

    public MyVisualizerView (Context context) {
        this(context, null);
    }

    public MyVisualizerView (Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyVisualizerView (Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.visualizerColor, typedValue, true);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(context.getResources().getColor(typedValue.resourceId));
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(1);

        mPaint_mirror = new Paint();
        mPaint_mirror.setAntiAlias(true);
        mPaint_mirror.setColor(context.getResources().getColor(typedValue.resourceId));
        mPaint_mirror.setStyle(Paint.Style.FILL);
        mPaint_mirror.setStrokeWidth(1);
        mPaint_mirror.setAlpha(60);
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
            mRect = new Rect(0, (int) (0.3 * mMeasuredHeight), mMeasuredWidth,
                    (int) (0.8 * mMeasuredHeight));
            mHeight = mRect.height();
            mRect_top = 0.3 * mMeasuredHeight;

            mRect_mirror = new Rect(0, (int) (0.8 * mMeasuredHeight), mMeasuredWidth,
                    mMeasuredHeight);
            mHeight_mirror = mRect_mirror.height();
            mRect_mirror_top = 0.8 * mMeasuredHeight;
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
            mTop = mHeight - (mHeight * (128 + waveform[i])) / 255;
            mRight = mLeft + 12;

            mBottom_mirror = (mHeight_mirror * (128 + waveform[i])) / 255; //倒影的高度

            canvas.drawRect(mLeft, (float) (mRect_top + mTop),
                    mRight, (float) (mRect_top + mHeight), mPaint);
            canvas.drawRect(mLeft, (float) mRect_mirror_top, mRight,
                    (float) (mRect_mirror_top + mBottom_mirror), mPaint_mirror); //绘制倒影

        }
    }
}
