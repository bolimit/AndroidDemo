package com.marco.demo.ui;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.marco.demo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author boyang116245@sohu-inc.com
 * @date 2018/5/18
 */

public class RecyclerViewActivity extends Activity {

    private static final String TAG = "RecyclerViewDisplay";

    private RecyclerView mRecyclerView;

    private RecyclerView.Adapter mAdapter;

    private RecyclerView.LayoutManager mLayoutManager;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_recyclerview_example);

        mLayoutManager = new LinearLayoutManager(RecyclerViewActivity.this, LinearLayoutManager.VERTICAL, false);
//        mLayoutManager.setItemPrefetchEnabled(false);

        mAdapter = new RecyclerAdapter(getData());

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(RecyclerViewActivity.this, LinearLayoutManager.VERTICAL));

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                /**
                 * 	0（SCROLL_STATE_IDLE）表示recyclerview是不动的,
                 1（SCROLL_STATE_DRAGGING）表示recyclerview正在被拖拽
                 2（SCROLL_STATE_SETTLING）表示recyclerview正在惯性下滚动
                 曝光监测只需要统计手势拖动，惯性下滑只需要等滑动停止时一起计算
                 */
                super.onScrollStateChanged(recyclerView, newState);
                Log.i(TAG, "onScrollStateChanged : " + newState);

                if (newState == 0) {
                    Log.i(TAG, "onScrollStateStopped.");
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Log.i(TAG, "onScrolled : " + dx + "," + dy);

                Log.i(TAG, "onScrolled, ChildCount : " + mRecyclerView.getChildCount());
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG, "onScrollPosted.");
                mRecyclerView.scrollBy(0,1000);
                mRecyclerView.scrollBy(0,-500);
//                mRecyclerView.scrollBy(0,-200);
            }
        }, 100);


//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            mRecyclerView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
//                @Override
//                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//                    Log.i(TAG, "onScrollChange : " + scrollX + "," + scrollY + " ; " +oldScrollX + ","+ oldScrollY);
//                }
//            });
//        }
    }

    private List<String> getData() {
        List<String> data = new ArrayList<>();
        String temp = " item: ";
        for (int i = 0; i < 50; i++) {
            data.add(temp + i);
        }

        return data;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView mTextView;

        int id;

        String des;

        public ViewHolder(View itemView, int id) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.textView);
            this.id = id;
        }
    }


    public static class RecyclerAdapter extends RecyclerView.Adapter<ViewHolder> {

        private int mIncrement = 0;

        private List<String> mData;

        public RecyclerAdapter(List<String> data) {
            mData = data;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_recyclerview, parent, false);
            ViewHolder holder = new ViewHolder(view, mIncrement);
            Log.i(TAG, "onCreateViewHolder, index = " + mIncrement);
            mIncrement++;
            return holder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Log.i(TAG, "onBindViewHolder, index = " + holder.id + ", data = " + mData.get(position));
            holder.mTextView.setText(mData.get(position));
            holder.des = mData.get(position);
        }

        @Override
        public void onViewAttachedToWindow(ViewHolder holder) {
            Log.i(TAG, "onViewAttachedToWindow, data = " + holder.des);
            super.onViewAttachedToWindow(holder);
        }

        @Override
        public void onViewDetachedFromWindow(ViewHolder holder) {
            Log.i(TAG, "onViewDetachedFromWindow, data = " + holder.des);
            super.onViewDetachedFromWindow(holder);
        }

        @Override
        public int getItemCount() {
            return mData == null ? 0 : mData.size();
        }
    }

    public static class DividerItemDecoration extends RecyclerView.ItemDecoration {
        public static final int HORIZONTAL_LIST = LinearLayoutManager.HORIZONTAL;
        public static final int VERTICAL_LIST = LinearLayoutManager.VERTICAL;
        private static final int[] ATTRS = new int[]{
                android.R.attr.listDivider
        };
        /**
         * 用于绘制间隔样式
         */
        private Drawable mDivider;
        /**
         * 列表的方向，水平/竖直
         */
        private int mOrientation;


        public DividerItemDecoration(Context context, int orientation) {
            // 获取默认主题的属性
            final TypedArray a = context.obtainStyledAttributes(ATTRS);
            mDivider = a.getDrawable(0);
            a.recycle();
            setOrientation(orientation);
        }

        @Override
        public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
            // 绘制间隔
            if (mOrientation == VERTICAL_LIST) {
                drawVertical(c, parent);
            } else {
                drawHorizontal(c, parent);
            }
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            if (mOrientation == VERTICAL_LIST) {
                outRect.set(0, 0, 0, mDivider.getIntrinsicHeight());
            } else {
                outRect.set(0, 0, mDivider.getIntrinsicWidth(), 0);
            }
        }

        private void setOrientation(int orientation) {
            if (orientation != HORIZONTAL_LIST && orientation != VERTICAL_LIST) {
                throw new IllegalArgumentException("invalid orientation");
            }
            mOrientation = orientation;
        }

        /**
         * 绘制间隔
         */
        private void drawVertical(Canvas c, RecyclerView parent) {
            final int left = parent.getPaddingLeft();
            final int right = parent.getWidth() - parent.getPaddingRight();
            final int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                final View child = parent.getChildAt(i);
                final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                        .getLayoutParams();
                final int top = child.getBottom() + params.bottomMargin +
                        Math.round(ViewCompat.getTranslationY(child));
                final int bottom = top + mDivider.getIntrinsicHeight();
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }
        }

        /**
         * 绘制间隔
         */
        private void drawHorizontal(Canvas c, RecyclerView parent) {
            final int top = parent.getPaddingTop();
            final int bottom = parent.getHeight() - parent.getPaddingBottom();
            final int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                final View child = parent.getChildAt(i);
                final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                        .getLayoutParams();
                final int left = child.getRight() + params.rightMargin +
                        Math.round(ViewCompat.getTranslationX(child));
                final int right = left + mDivider.getIntrinsicHeight();
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }
        }
    }
}
