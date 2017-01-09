package com.example.mrh.musicplayer.custom;

import android.animation.FloatEvaluator;
import android.animation.IntEvaluator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.ColorUtils;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.example.mrh.musicplayer.activity.MainActivity;
import com.nineoldandroids.view.ViewHelper;

import java.util.List;

/**
 *
 * Created by MR.H on 2016/6/24 0024.
 */
public class SlidingMenu extends FrameLayout{
    private static final String TAG = "SlidingMenu";

    private Context context;
    private ViewDragHelper helper;
    private View menu;
    private View main;
    private int width;
    private float dragRange;
    private State currentState = State.Close;
    private OnSlidingChangeListener listener;
    private FloatEvaluator floatEvaluator;
    private IntEvaluator intEvaluator;
    private float fraction;
    private MainActivity mActivity;
    private int mCount = 0; //记录非空fragment

    public SlidingMenu (Context context) {
        this(context, null);
    }

    public SlidingMenu (Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlidingMenu (Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initView();
    }

    public enum State{
        Open, Close;
    }
    public State getCurrentState(){
        return currentState;
    }
    private void initView () {
        mActivity = (MainActivity) this.context;
        helper = ViewDragHelper.create(SlidingMenu.this, new MyCallBack());
        floatEvaluator = new FloatEvaluator();
        intEvaluator = new IntEvaluator();
    }

    class MyCallBack extends ViewDragHelper.Callback{
        @Override
        public boolean tryCaptureView (View child, int pointerId) {
            return child == menu || child == main;
        }

        @Override
        public int getViewHorizontalDragRange (View child) {
            return (int) dragRange;
        }

        @Override
        public int clampViewPositionHorizontal (View child, int left, int dx) {
            if (child == main){
                if(left<0){
                    left=0;
                } else if(left>dragRange){
                    left=(int) dragRange;
                }
            }
            return left;
        }

        @Override
        public void onViewPositionChanged (View changedView, int left, int top, int dx, int dy) {
            super.onViewPositionChanged(changedView, left, top, dx, dy);
            if (changedView == menu){
                menu.layout(0, 0, menu.getMeasuredWidth(),menu.getMeasuredHeight());
                int mainLeft = main.getLeft() + dx;
                if (mainLeft <0 ){
                    mainLeft = 0;
                } else if (mainLeft > dragRange){
                    mainLeft = (int) dragRange;
                }
                main.layout(mainLeft, main.getTop()+ dy, mainLeft+main.getMeasuredWidth(), main
                        .getBottom() +dy);
            }
            fraction = main.getLeft() / dragRange;//比率
            executeAnim();
            if (currentState != State.Open && fraction >= 0.99f){
                if (listener != null){
                    listener.onOpen();
                }
                currentState = State.Open;
            } else if (currentState != State.Close && fraction == 0){
                if (listener != null){
                    listener.onClose();
                }
                currentState = State.Close;
            }else {
                if (listener != null){
                    listener.onDraging(fraction);
                }
            }
        }

        @Override
        public void onViewReleased (View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
            if(main.getLeft()<dragRange/2){
                //在左半边
                close();
            }else {
                //在右半边
                open();
            }

            //处理用户的稍微滑动
            if(xvel>200 && currentState!=State.Open){
                open();
            }else if (xvel<-200 && currentState!=State.Close) {
                close();
            }
        }
    }

    public void open () {
        helper.smoothSlideViewTo(main,(int) dragRange,main.getTop());
        //开始移动一下，调用方法computeScroll ()检测移动开始了，
        // 在computeScroll ()调用ViewCompat.postInvalidateOnAnimation(SlidingMenu.this);
        //不停地移动，一直移动 dragRange 距离；
        ViewCompat.postInvalidateOnAnimation(SlidingMenu.this);
    }

    public void close () {
        helper.smoothSlideViewTo(main,0,main.getTop());
        ViewCompat.postInvalidateOnAnimation(SlidingMenu.this);
    }

    private void executeAnim () {
        ViewHelper.setScaleX(main, floatEvaluator.evaluate(fraction,1f,0.8f));
        ViewHelper.setScaleY(main, floatEvaluator.evaluate(fraction,1f,0.8f));
        //移动menuView
        ViewHelper.setTranslationX(menu,intEvaluator.evaluate(fraction,-menu.getMeasuredWidth()/2,0));
        //放大menuView
        ViewHelper.setScaleX(menu,floatEvaluator.evaluate(fraction,0.5f,1f));
        ViewHelper.setScaleY(menu,floatEvaluator.evaluate(fraction,0.5f,1f));
        //改变menuView的透明度
        ViewHelper.setAlpha(menu,floatEvaluator.evaluate(fraction,0.3f,1f));

        //给SlideMenu的背景添加黑色的遮罩效果
        getBackground().setColorFilter(ColorUtils.setAlphaComponent(Color.BLACK, (int) (255-fraction
                * 255)), PorterDuff.Mode.SRC_OVER);
    }

    //接口
    public interface OnSlidingChangeListener{
        void onOpen ();
        void onClose ();
        void onDraging (float fration);
    }

    public void setOnSlidingChangeListener(OnSlidingChangeListener listener){
        this.listener = listener;
    }

    @Override
    protected void onSizeChanged (int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = getMeasuredWidth();
        dragRange = width*0.7f;
    }

    @Override
    public boolean onInterceptTouchEvent (MotionEvent ev) {
        if (fraction >= 0.99 && ev.getX() < width*(0.7+0.3*0.2)){
            return super.onInterceptTouchEvent(ev);
        }

        if (mCount <= 1 && !mActivity.mList.get(2).isVisible()){
            //只有ContentFragment的时候
            return helper.shouldInterceptTouchEvent(ev);
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean dispatchTouchEvent (MotionEvent ev) {
        //当viewpager为第一页时可以滑出侧面菜单
        mCount = 0;
        List<Fragment> mFragments = mActivity.getSupportFragmentManager().getFragments();
        for (int i = 0; i < mFragments.size(); i++){
            if (mFragments.get(i) != null){
                mCount++;
            }
        }
        if (mCount <= 1 && !mActivity.mList.get(2).isVisible()){
            //只有ContentFragment的时候
            //点击回到MainActivity界面
            if (fraction >= 0.99 && ev.getX() > width*(0.7+0.3*0.2)){
                close();
                return false;
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent (MotionEvent event) {
        if (fraction >= 0.99 && event.getX() < width*(0.7+0.3*0.2)){
            return super.onTouchEvent(event);
        }
        //当viewpager为第一页时可以滑出侧面菜单
        if (mCount <= 1 && !mActivity.mList.get(2).isVisible()){
            //只有ContentFragment的时候
            helper.processTouchEvent(event);
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onMeasure (int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //没有布局文件，不能走onFinishInflate()方法，所以走这里
        if (fraction < 0.99){
            getChildren();
        }
    }

    private void getChildren () {
        if (getChildCount() != 2){
            throw new IllegalArgumentException("只能有两个子控件");
        }
        menu = getChildAt(0);
        main = getChildAt(1);
    }

    @Override
    protected void onLayout (boolean changed, int left, int top, int right, int bottom) {
        if (fraction >= 0.99){
            menu.layout(0, 0, menu.getMeasuredWidth(),menu.getMeasuredHeight());
            main.layout((int) dragRange, main.getTop(), (int)dragRange+main.getMeasuredWidth(),
                    main.getBottom());
        }else {
            super.onLayout(changed, left, top, right, bottom);
        }
    }

    @Override
    public void computeScroll () {
        //持续滑动的时候调用，飞翔状态
        if(helper.continueSettling(true)){
            ViewCompat.postInvalidateOnAnimation(SlidingMenu.this);
        }
        super.computeScroll();
    }
}
