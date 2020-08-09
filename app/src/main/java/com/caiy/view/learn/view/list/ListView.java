package com.caiy.view.learn.view.list;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * created by caiyong at 2020/8/8
 */
public class ListView extends ViewGroup {

    private String TAG = "ListView@" + hashCode();

    private int mWidthMeasureSpec;
    private Adapter mAdapter;
    private DataSetObserver mDataSetObserver;
    private boolean mDataChanged;
    private RecycleBin mRecycleBin = new RecycleBin();
    private boolean mIsInLayout = false;
    private int mFirstAttachPosition = 0;
    private final boolean[] mIsScrap = new boolean[1];

    public ListView(Context context) {
        this(context, null);
    }

    public ListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setAdaper(ListAdapter adapter) {
        if (mAdapter != null && mDataSetObserver != null) {
            mAdapter.unregisterObserver(mDataSetObserver);
        }
        mAdapter = adapter;
        if (mAdapter != null) {
            mDataSetObserver = new AdapterDataSetObserver();
            mAdapter.registerObserver(mDataSetObserver);
        }
        requestLayout();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidthMeasureSpec = widthMeasureSpec;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        Log.d(TAG, "onLayout: changed=" + changed + " l=" + l + " t=" + t + " r=" + r + " b=" + b);
        if (changed) {
            int count = getChildCount();
            for (int i = 0; i < count; i++) {
                View view = getChildAt(i);
                view.forceLayout();
            }
        }

        layoutChildren();
    }

    private void layoutChildren() {
        mIsInLayout = true;
        try {
            if (mAdapter == null || mAdapter.getCount() == 0) {
                resetList();
                return;
            }
            final RecycleBin recycleBin = mRecycleBin;
            if (mDataChanged) {
                recycleBin.addToScrapViews(this, mFirstAttachPosition);
            } else {
                recycleBin.addToActiveViews(this, mFirstAttachPosition);
            }
            detachAllViewsFromParent();
            fill();
            recycleBin.clearActiveViews();
            mDataChanged = false;
        } finally {
            mIsInLayout = false;
        }
    }

    private void fill() {
        fillFromTop();
    }

    private void fillFromTop() {
        mFirstAttachPosition = Math.min(mFirstAttachPosition, mAdapter.getCount() -1);
        mFirstAttachPosition = Math.max(mFirstAttachPosition, 0);
        fillDown(mFirstAttachPosition);
    }

    private void fillDown(int position) {
        int itemCount = mAdapter.getCount();
        int top = 0;
        int bottom = getBottom() - getTop();
        int left = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (getClipToPadding()) {//TODO---
                top = getPaddingTop();
                bottom = bottom - getPaddingBottom();
                left = getPaddingLeft();
            }
        }
        while (position < itemCount && top < bottom) {
            View child = makeAndAddView(position, left, top);
            position ++;
            top = child.getBottom();
        }
    }

    private View makeAndAddView(int position, int left, int top) {
        View child = obtainView(position, mIsScrap);
        setupChild(child, position, left, top, mIsScrap[0]);
        return child;
    }

    private void setupChild(View child, int position, int childLeft, int top, boolean isAttachedToWindow) {
        final boolean needToMeasure = !isAttachedToWindow || child.isLayoutRequested();

        if (child.getLayoutParams() == null || !(child.getLayoutParams() instanceof ListView.LayoutParams)) {
            throw new RuntimeException("invalid layoutParams");
        }
        LayoutParams layoutParams = (ListView.LayoutParams)child.getLayoutParams();
        if (isAttachedToWindow) {
            attachViewToParent(child, -1, layoutParams);
        } else {
            addViewInLayout(child, -1, layoutParams, true);
        }

        if (needToMeasure) {
            final int childWidthSpec = ViewGroup.getChildMeasureSpec(mWidthMeasureSpec,
                    getPaddingLeft() + getPaddingRight(), layoutParams.width);
            final int lpHeight = layoutParams.height;
            final int childHeightSpec;
            if (lpHeight > 0) {
                childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight, MeasureSpec.EXACTLY);
            } else {
                childHeightSpec = MeasureSpec.makeMeasureSpec(getMeasuredHeight(),
                        MeasureSpec.UNSPECIFIED);
            }
            child.measure(childWidthSpec, childHeightSpec);
        } else {
            cleanupLayoutState(child);
        }

        final int w = child.getMeasuredWidth();
        final int h = child.getMeasuredHeight();
        final int childTop = top;

        if (needToMeasure) {
            final int childRight = childLeft + w;
            final int childBottom = childTop + h;
            child.layout(childLeft, childTop, childRight, childBottom);
        } else {
            child.offsetLeftAndRight(childLeft - child.getLeft());
            child.offsetTopAndBottom(childTop - child.getTop());
        }
    }

    private View obtainView(int position, boolean[] mIsScrap) {
        RecycleBin recycleBin = mRecycleBin;
        View activeView = recycleBin.getActiveView(position);
        if (activeView != null) {
            mIsScrap[0] = true;
            return activeView;
        }

        mIsScrap[0] = false;
        View scrapView = recycleBin.getScrapView(position);
        View child = mAdapter.getView(position, scrapView, this);
        if (scrapView != null) {
            if (scrapView != child) {
                recycleBin.addToScrapView(scrapView, position);
            } else {
                ViewGroup.LayoutParams layoutParams = child.getLayoutParams();
                if (layoutParams != null && layoutParams instanceof LayoutParams) {
                    if (((LayoutParams) layoutParams).temporaryDetached) {
                        mIsScrap[0] = true;
                        ((LayoutParams) layoutParams).temporaryDetached = false;
                    }
                }
            }
        }
        setItemViewLayoutParams(child, position);
        return child;
    }

    private void setItemViewLayoutParams(View child, int position) {
        final ViewGroup.LayoutParams vlp = child.getLayoutParams();
        LayoutParams lp;
        if (vlp == null) {
            lp = (LayoutParams) generateDefaultLayoutParams();
        } else if (!checkLayoutParams(vlp)) {
            lp = (LayoutParams) generateLayoutParams(vlp);
        } else {
            lp = (LayoutParams) vlp;
        }

        if (lp != vlp) {
            child.setLayoutParams(lp);
        }
    }

    @Override
    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new ListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new ListView.LayoutParams(p);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new ListView.LayoutParams(getContext(), attrs);
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof ListView.LayoutParams;
    }

    private void resetList() {
        removeAllViewsInLayout();
        mDataChanged = false;
    }


    private class AdapterDataSetObserver implements DataSetObserver {

        @Override
        public void onChanged() {
            mDataChanged = true;
            requestLayout();
        }
    }

    private class RecycleBin {

        private List<View> mCurrentScrap = new ArrayList<>();
        private List<View> mActiveViews = new ArrayList<>();
        private int mFirstActivePosition;

        public void addToScrapViews(ViewGroup parent, int firstAttachPosition) {
            int count = parent.getChildCount();
            for (int i = 0; i < count; i++) {
                View view = parent.getChildAt(i);
                addToScrapView(view, i + firstAttachPosition);
            }
        }

        public void addToActiveViews(ViewGroup parent, int firstAttachPosition) {
            mFirstActivePosition = firstAttachPosition;
            int count = parent.getChildCount();
            for (int i = 0; i < count; i++) {
                View view = parent.getChildAt(i);
                addToActiveView(view, i + firstAttachPosition);
            }
        }

        View getActiveView(int position) {
            int index = position - mFirstActivePosition;
            if (index >= 0 && index < mActiveViews.size()) {
                final View match = mActiveViews.get(index);
                mActiveViews.set(index, null);
                return match;
            }
            return null;
        }

        View getScrapView(int position) {
            int size = mCurrentScrap.size();
            if (size > 0) {
                for (int i = size - 1; i >= 0; i--) {
                    View view = mCurrentScrap.get(i);
                    if (view != null && view.getLayoutParams() instanceof LayoutParams) {
                        if (((LayoutParams) view.getLayoutParams()).position == position) {
                            mCurrentScrap.remove(i);
                            return view;
                        }
                    }
                }
                return mCurrentScrap.remove(size -1);
            } else {
                return null;
            }
        }

        public void clearActiveViews() {
            int count = mActiveViews.size();
            for (int i = count -1; i >= 0; i--) {
                View activeView = mActiveViews.get(i);
                if (activeView != null) {
                    mActiveViews.set(i, null);
                    mCurrentScrap.add(activeView);
                    removeDetachedView(activeView, false);
                }
            }
        }

        public void addToScrapView(View view, int position) {
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            if (layoutParams != null && layoutParams instanceof LayoutParams) {
                ((LayoutParams) layoutParams).position = position;
                ((LayoutParams) layoutParams).temporaryDetached = true;
                mCurrentScrap.add(view);
            }
        }

        private void addToActiveView(View view, int position) {
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            if (layoutParams != null && layoutParams instanceof LayoutParams) {
                ((LayoutParams) layoutParams).position = position;
                mActiveViews.add(view);
            }
        }
    }

    private class LayoutParams extends ViewGroup.LayoutParams {

        int position = -1;
        boolean temporaryDetached = false;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }
    }
}
