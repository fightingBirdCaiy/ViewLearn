package com.caiy.view.learn.view.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;

/**
 * created by caiyong at 2020/11/10
 */

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.*;
import android.graphics.PorterDuff.Mode;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.animation.LinearInterpolator;
import android.widget.Scroller;

public class ScrollDrawable extends Drawable implements Drawable.Callback, Animatable {

    private final String TAG = "ScrollDrawable";

    private ScrollState mState;
    private boolean mMutated;
    private Scroller mScroller;
    private int DURATION_SCROLL = 3000;

    private Rect mDrawRect = new Rect();

    private boolean mAnimRunning;

    public ScrollDrawable(Drawable drawable, Context context) {
        this(drawable, context, Gravity.TOP);
    }

    public ScrollDrawable(Drawable drawable, Context context, int gravity) {
        this((ScrollState) null, (Resources) null);

        mState.mDrawable = drawable;
        mState.mGravity = gravity;
        mScroller = new Scroller(context, new LinearInterpolator());

        if (drawable != null) {
            drawable.setCallback(this);
        }
    }

    // overrides from Drawable.Callback
    @Override
    public void invalidateDrawable(Drawable who) {
        if (getCallback() != null) {
            getCallback().invalidateDrawable(this);
        }
    }

    @Override
    public void scheduleDrawable(Drawable who, Runnable what, long when) {
        if (getCallback() != null) {
            getCallback().scheduleDrawable(this, what, when);
        }
    }

    @Override
    public void unscheduleDrawable(Drawable who, Runnable what) {
        if (getCallback() != null) {
            getCallback().unscheduleDrawable(this, what);
        }
    }

    // overrides from Drawable
    @Override
    public void draw(Canvas canvas) {
        canvas.save();

        boolean shouldScroll = mScroller.computeScrollOffset();
        if (shouldScroll) {
            int scrollY = mScroller.getCurrY();
            canvas.translate(0, scrollY);
        } else if (mScroller.getFinalY() != 0) {
            mAnimRunning = false;
            canvas.translate(0, mScroller.getFinalY());
        }

        mState.mDrawable.draw(canvas);

        if (shouldScroll) {
            invalidateSelf();
        }

        canvas.restore();
    }

    @Override
    public int getChangingConfigurations() {
        return super.getChangingConfigurations()
                | mState.mChangingConfigurations
                | mState.mDrawable.getChangingConfigurations();
    }

    @Override
    public boolean getPadding(Rect padding) {
        return mState.mDrawable.getPadding(padding);
    }

    @Override
    public boolean setVisible(boolean visible, boolean restart) {
        mState.mDrawable.setVisible(visible, restart);
        return super.setVisible(visible, restart);
    }

    @Override
    public void setAlpha(int alpha) {
        mState.mDrawable.setAlpha(alpha);
    }


    @Override
    public int getAlpha() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//TODO---
            return mState.mDrawable.getAlpha();
        }
        return 0xFF;
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        mState.mDrawable.setColorFilter(cf);
    }

    @Override
    public void setTintList(ColorStateList tint) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mState.mDrawable.setTintList(tint);
        }
    }

    @Override
    public void setTintMode(Mode tintMode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mState.mDrawable.setTintMode(tintMode);
        }
    }

    @Override
    public int getOpacity() {
        return mState.mDrawable.getOpacity();
    }

    @Override
    public boolean isStateful() {
        return mState.mDrawable.isStateful();
    }

    @Override
    protected boolean onStateChange(int[] state) {
        return mState.mDrawable.setState(state);
    }

    @Override
    protected boolean onLevelChange(int level) {
        return mState.mDrawable.setLevel(level);
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        int w = bounds.width();
        int h = bounds.height();

        if (w > 0 && h > 0) {
            int targetW;
            int targetH;
            int bitmapW = mState.mDrawable.getIntrinsicWidth();
            int bitmapH = mState.mDrawable.getIntrinsicHeight();
            float ration = bitmapW * 1F / w;
            targetW = w;
            targetH = (int)(bitmapH / ration + 0.5F);
            mDrawRect.set(0, 0, targetW, targetH);
            Log.i(TAG, "onBoundsChange: bounds=" + bounds + " bitmapW=" + bitmapW + " bitmapH=" + bitmapH + " resultRect=" + mDrawRect);
            mState.mDrawable.setBounds(mDrawRect);
        } else {
            Log.w(TAG, "onBoundsChange warn: w=" + w + " h=" + h);
        }
    }

    @Override
    public int getIntrinsicWidth() {
        return mState.mDrawable.getIntrinsicWidth();
    }

    @Override
    public int getIntrinsicHeight() {
        return mState.mDrawable.getIntrinsicHeight();
    }

    @Override
    public ConstantState getConstantState() {
        if (mState.canConstantState()) {
            mState.mChangingConfigurations = getChangingConfigurations();
            return mState;
        }
        return null;
    }

    @Override
    public Drawable mutate() {
        if (!mMutated && super.mutate() == this) {
            mState.mDrawable.mutate();
            mMutated = true;
        }
        return this;
    }

    @Override
    public void start() {
        if (!isRunning()) {
            int yDistance = getBounds().height() - mDrawRect.height();
            Log.i(TAG, "start: yDistance=" + yDistance);
            if (yDistance != 0) {
                mAnimRunning = true;
                mScroller.startScroll(0, 0, 0, yDistance, DURATION_SCROLL);
                invalidateSelf();
            }
        }
    }

    @Override
    public void stop() {
        if (isRunning()) {
            mAnimRunning = false;
            mScroller.abortAnimation();
        }
    }

    @Override
    public boolean isRunning() {
        return mAnimRunning;
    }

    final static class ScrollState extends ConstantState {

        Drawable mDrawable;

        int mGravity = Gravity.TOP;

        int mChangingConfigurations;
        private boolean mCheckedConstantState;
        private boolean mCanConstantState;

        ScrollState(ScrollState orig, ScrollDrawable owner, Resources res) {
            if (orig != null) {
                mChangingConfigurations = orig.mChangingConfigurations;
                if (res != null) {
                    mDrawable = orig.mDrawable.getConstantState().newDrawable(res);
                } else {
                    mDrawable = orig.mDrawable.getConstantState().newDrawable();
                }
                mDrawable.setCallback(owner);
                mDrawable.setBounds(orig.mDrawable.getBounds());
                mGravity = orig.mGravity;
                mCheckedConstantState = mCanConstantState = true;
            }
        }

        @Override
        public boolean canApplyTheme() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                return (mDrawable != null && mDrawable.canApplyTheme())
                        || super.canApplyTheme();
            }
            return super.canApplyTheme();
        }

        @Override
        public Drawable newDrawable() {
            return new ScrollDrawable(this, null);
        }

        @Override
        public Drawable newDrawable(Resources res) {
            return new ScrollDrawable(this, res);
        }

        @Override
        public int getChangingConfigurations() {
            return mChangingConfigurations;
        }

        boolean canConstantState() {
            if (!mCheckedConstantState) {
                mCanConstantState = mDrawable.getConstantState() != null;
                mCheckedConstantState = true;
            }

            return mCanConstantState;
        }

    }

    private ScrollDrawable(ScrollState state, Resources res) {
        mState = new ScrollState(state, this, res);
    }
}

