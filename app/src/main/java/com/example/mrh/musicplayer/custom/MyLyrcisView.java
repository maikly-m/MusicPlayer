package com.example.mrh.musicplayer.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.example.mrh.musicplayer.R;
import com.example.mrh.musicplayer.domain.LrcContent;
import com.example.mrh.musicplayer.utils.Utils;

import java.util.List;

/**
 * Created by MR.H on 2017/1/8 0008.
 */

public class MyLyrcisView extends View {
    private static final String TAG = "MyLyrcisView";

    private Paint mPaint;
    private Paint mPaint_;
    private Paint mPaint_null;
    private int mMeasuredWidth;
    private int mMeasuredHeight;
    private List<LrcContent> mList;
    public int position = -1;
    private int mHeight;
    private int mDx;
    private float mDownY;
    private float mMoveY;
    /**
     * 是否触摸了
     */
    public boolean isTouch = false;
    /**
     * 是否改变了歌词位置
     */
    public boolean isChange = false;

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
        mPaint.setTextSize(Utils.dip2px(context, 12));
        mPaint.setTextAlign(Paint.Align.CENTER);

        mPaint_ = new Paint();
        mPaint_.setAntiAlias(true);
        mPaint_.setColor(Color.WHITE);
        mPaint_.setStyle(Paint.Style.FILL);
        mPaint_.setStrokeWidth(1);
        mPaint_.setTextSize(Utils.dip2px(context, 16));
        mPaint_.setTextAlign(Paint.Align.CENTER);

        mPaint_null = new Paint();
        mPaint_null.setColor(Color.TRANSPARENT);
        mPaint_null.setTextSize(Utils.dip2px(context, 12));

        mDx = Utils.dip2px(context, 15);
    }

    public void updateLrc(int position){
        this.position = position;
        if (position != -1){
            postInvalidate();
        }
    }

    public void setList (List<LrcContent> list) {
        mList = list;
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
        mHeight = mMeasuredHeight - Utils.dip2px(getContext(), 50);
    }

    @Override
    protected void onDraw (Canvas canvas) {
        super.onDraw(canvas);
        if (position == -1){
            return;
        }
        for (int i = position - 3; i < position + 4; i++){
            if (i < 0){
                canvas.drawText("没有", 0, mHeight * (i+4) / 7, mPaint_null);
            } else if (i >= mList.size()){
                canvas.drawText("没有", 0, mHeight * (i-mList.size()+5) / 7, mPaint_null);
            } else {
                if (i == position){
                    canvas.drawText(mList.get(i).getLrc(), mMeasuredWidth / 2, mHeight * 4 / 7,
                            mPaint_);
                }else {
                    canvas.drawText(mList.get(i).getLrc(), mMeasuredWidth / 2,
                            mHeight * (i-position+4) / 7, mPaint);
                }
            }
        }
    }

    @Override
    public boolean onTouchEvent (MotionEvent event) {
        switch (event.getAction()){
        case MotionEvent.ACTION_DOWN:
            mDownY = event.getY();
            isTouch = true;
            break;
        case MotionEvent.ACTION_MOVE:
            mMoveY = event.getY();
            if (mMoveY - mDownY < -mDx){
                mDownY = mMoveY;
                ++position;
                if (position > mList.size() - 1){
                    position = mList.size() - 1;
                }
                postInvalidate();
                isChange = true;
            }else if (mMoveY - mDownY > mDx){
                mDownY = mMoveY;
                --position;
                if (position < 0){
                    position = 0;
                }
                postInvalidate();
                isChange = true;
            }else {
                isTouch = false;//没有改变需要复原位置，使用了viewpager后up事件不会触发
            }
            break;
        case MotionEvent.ACTION_UP:
            isTouch = false;
            break;
        default:
            break;
        }
        return true;

    }
}
