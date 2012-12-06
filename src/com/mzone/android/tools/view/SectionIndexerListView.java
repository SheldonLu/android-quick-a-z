
package com.mzone.android.tools.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.mzone.android.tools.adapter.SectionIndexerListAdapter;

public class SectionIndexerListView extends ListView {

    private View mPinnedHeaderView;

    private boolean mIsFastScrollEnabled = false;

    private boolean mPinnedHeaderViewVisible;

    private int mPinnedHeaderViewHeight;

    private int mPinnedHeaderViewWidth;

    private SectionIndexerListAdapter mSectionIndexerListAdapter;

    public SectionIndexerListView(Context context) {
        super(context);
    }

    public SectionIndexerListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SectionIndexerListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mPinnedHeaderView != null) {
            measureChild(mPinnedHeaderView, widthMeasureSpec, heightMeasureSpec);
            mPinnedHeaderViewHeight = mPinnedHeaderView.getMeasuredHeight();
            mPinnedHeaderViewWidth = mPinnedHeaderView.getMeasuredWidth();
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (mPinnedHeaderView != null) {
            mPinnedHeaderView.layout(0, 0, mPinnedHeaderViewWidth, mPinnedHeaderViewHeight);
            configurePinnedHeaderView(getFirstVisiblePosition());
        }

    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (mPinnedHeaderViewVisible) {
            drawChild(canvas, mPinnedHeaderView, getDrawingTime());
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
    }

    @Override
    public void setAdapter(ListAdapter adapter) {
        if (!(adapter instanceof SectionIndexerListAdapter)) {
            throw new IllegalArgumentException(SectionIndexerListView.class.getSimpleName()
                    + " must use adapter of type "
                    + SectionIndexerListAdapter.class.getSimpleName());
        }
        if (mSectionIndexerListAdapter != null) {
            this.setOnScrollListener(null);
        }
        mSectionIndexerListAdapter = (SectionIndexerListAdapter) adapter;
        this.setOnScrollListener(mSectionIndexerListAdapter);
        super.setAdapter(adapter);
    }

    @Override
    public SectionIndexerListAdapter getAdapter() {
        return mSectionIndexerListAdapter;
    }

    public void setPinnedHeaderView(View view) {
        mPinnedHeaderView = view;
        if (mPinnedHeaderView != null) {
            setFadingEdgeLength(0);
        }
        requestLayout();
    }


    /**
     * 
     * @param position
     */
    public void configurePinnedHeaderView(final int position) {
        if (mPinnedHeaderView == null) {
            return;
        }

        int state = mSectionIndexerListAdapter.getPinnedHeaderState(position);
        switch (state) {
            case SectionIndexerListAdapter.PINNED_HEADER_GONE: {
                mPinnedHeaderViewVisible = false;
                break;
            }

            case SectionIndexerListAdapter.PINNED_HEADER_VISIBLE: {
                mSectionIndexerListAdapter.configurePinnedHeader(mPinnedHeaderView, position, 255);
                if (mPinnedHeaderView.getTop() != 0) {
                    mPinnedHeaderView.layout(0, 0, mPinnedHeaderViewWidth, mPinnedHeaderViewHeight);
                }
                mPinnedHeaderViewVisible = true;
                break;
            }

            case SectionIndexerListAdapter.PINNED_HEADER_PUSHED_UP: {
                View firstView = getChildAt(0);
                if (firstView != null) {
                    int bottom = firstView.getBottom();
                    int headerHeight = mPinnedHeaderView.getHeight();
                    int y;
                    int alpha;
                    if (bottom < headerHeight) {
                        y = (bottom - headerHeight);
                        alpha = 255 * (headerHeight + y) / headerHeight;
                    } else {
                        y = 0;
                        alpha = 255;
                    }
                    mSectionIndexerListAdapter.configurePinnedHeader(mPinnedHeaderView, position,
                            alpha);
                    if (mPinnedHeaderView.getTop() != y) {
                        mPinnedHeaderView.layout(0, y, mPinnedHeaderViewWidth,
                                mPinnedHeaderViewHeight + y);
                    }
                    mPinnedHeaderViewVisible = true;
                }
                break;
            }
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }
    @Override
    public boolean isFastScrollEnabled() {
        return mIsFastScrollEnabled;
    }

    @Override
    public void setFastScrollEnabled(boolean enabled) {
        super.setFastScrollEnabled(false);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return super.onTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }

}
