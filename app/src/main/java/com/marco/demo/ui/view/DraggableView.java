package com.marco.demo.ui.view;

import android.content.Context;
import android.graphics.Point;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

/**
 * 官方的SlidingPaneLayout和DrawerLayout都是利用ViewDragHelper实现的。
 *
 * @author yangbo
 */

public class DraggableView extends LinearLayout {

    private static final String TAG = "DraggableView";
    private ViewDragHelper mViewDragHelper;
    private View mAutoBackView;

    // 左右靠边悬停位置
    private Point mAutoBackLastPos = new Point();
    private Point mAutoBackOriginPos = new Point();

    private ViewDragHelper.Callback mCallback = new ViewDragHelper.Callback() {
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return child == mAutoBackView;
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            Log.i(TAG, "onViewReleased: xvel = " + xvel + " , yvel = " + yvel);
            if (releasedChild == mAutoBackView) {
                if (mAutoBackLastPos.x < (getMeasuredWidth() - mAutoBackView.getMeasuredWidth()) / 2) {
                    mAutoBackLastPos.x = mAutoBackOriginPos.x;
                } else {
                    mAutoBackLastPos.x = getMeasuredWidth() - mAutoBackView.getMeasuredWidth() - mAutoBackOriginPos.x;
                }

                // 注意，为了页面整体效果美观，可以使用mAutoBackOriginPos.x作为垂直方向的阈值
                if (mAutoBackLastPos.y < mAutoBackOriginPos.x) {
                    mAutoBackLastPos.y = mAutoBackOriginPos.x;
                } else if (mAutoBackLastPos.y > (getMeasuredHeight() - mAutoBackView.getMeasuredHeight() - mAutoBackOriginPos.x)) {
                    mAutoBackLastPos.y = getMeasuredHeight() - mAutoBackView.getMeasuredHeight() - mAutoBackOriginPos.x;
                }

                mViewDragHelper.settleCapturedViewAt(mAutoBackLastPos.x, mAutoBackLastPos.y);
                invalidate();
            }
        }

        @Override
        public int getViewHorizontalDragRange(View child) {
            Log.i(TAG, "getViewHorizontalDragRange: getMeasuredWidth() = " + getMeasuredWidth() + " , child.getMeasuredWidth() = " + child.getMeasuredWidth());
            return getMeasuredWidth() - child.getMeasuredWidth();
        }

        @Override
        public int getViewVerticalDragRange(View child) {
            Log.i(TAG, "getViewVerticalDragRange: getMeasuredHeight() = " + getMeasuredHeight() + " , child.getMeasuredHeight() = " + child.getMeasuredHeight());
            return getMeasuredHeight() - child.getMeasuredHeight();
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            Log.i(TAG, "clampViewPositionHorizontal: left = " + left + " , dx = " + dx);
            final int x1 = 0;
            final int x2 = Math.abs(getMeasuredWidth() - child.getMeasuredWidth());
            if (left <= x1) {
                left = x1;
            } else if (left > x2) {
                left = x2;
            }

            mAutoBackLastPos.x = left;
            return left;
//            return Math.max(left, 0);
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            Log.i(TAG, "clampViewPositionVertical: top = " + top + " , dy = " + dy);
            final int y1 = 0;
            final int y2 = Math.abs(getMeasuredHeight() - child.getMeasuredHeight());

            if (top <= y1) {
                top = y1;
            } else if (top > y2) {
                top = y2;
            }

            mAutoBackLastPos.y = top;
            return top;

            // 不能滑出顶部
//            return Math.max(top, 0);
        }
    };

    public DraggableView(Context context) {
        super(context);
        initView();
    }

    public DraggableView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public DraggableView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        mViewDragHelper = ViewDragHelper.create(DraggableView.this, 1.0f, mCallback);
        mViewDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_ALL);
    }

    @Override
    public void computeScroll() {
        // 重写了computeScroll 方法，以便在手指松开时，触发系统自动滑动

        // 不添加这个方法，自动复位到指定位置无法使用
        if (mViewDragHelper.continueSettling(true)) {
//            invalidate();
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mViewDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mViewDragHelper.processTouchEvent(event);
        return true;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        mAutoBackView = getChildAt(0);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        mAutoBackOriginPos.x = mAutoBackView.getLeft();
        mAutoBackOriginPos.y = mAutoBackView.getTop();
    }
}
