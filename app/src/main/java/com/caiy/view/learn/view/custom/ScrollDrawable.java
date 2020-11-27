package com.caiy.view.learn.view.custom;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Path;
import android.graphics.PorterDuff.Mode;
import android.graphics.Rect;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.view.animation.LinearInterpolator;
import android.widget.Scroller;


/**
 * created by caiyong at 2020/11/10
 */

public class ScrollDrawable extends Drawable implements Drawable.Callback, Animatable {

    private final String TAG = "ScrollDrawable";
    private static final int DEFAULT_SPEED = 40;//40px/秒

    private ScrollState mState;
    private boolean mMutated;
    private Scroller mScroller;

    private Rect mDrawRect = new Rect();

    private boolean mAnimRunning;

    /**
     * 滚动速度： 像素/秒
     */
    private int mSpeed = DEFAULT_SPEED;

    /**
     * 是否保持以前setBounds的高度不变
     */
    private boolean mRetainBoundLastHeight;

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            invalidateSelf();
        }
    };

    public ScrollDrawable(Drawable drawable, Context context) {
        this((ScrollState) null, (Resources) null);

        mState.mDrawable = drawable;
        if (drawable != null) {
            drawable.setCallback(this);
        }

        mScroller = new Scroller(context, new LinearInterpolator());
    }

    public void setSpeed(int speed) {
        this.mSpeed = speed;
    }

    public void setRetainBoundLastHeight(boolean retain) {
        mRetainBoundLastHeight = retain;
    }

    //--- overrides from Drawable.Callback start ---
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
    //--- overrides from Drawable.Callback end ---

    //--- overrides from Drawable start ---
    @Override
    public void draw(Canvas canvas) {
        canvas.save();

        boolean shouldScroll = false;
        if (mAnimRunning) {
            if (shouldScroll = mScroller.computeScrollOffset()) {
                canvas.translate(0, mScroller.getCurrY());
            } else if (mScroller.isFinished()) {
                canvas.translate(0, mScroller.getFinalY());
            }
        }

        mState.mDrawable.draw(canvas);

        if (shouldScroll) {
            scheduleSelf(runnable, 0L);
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return mState.mDrawable.getAlpha();
        }
        return 255;
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

        if (mRetainBoundLastHeight) {
            mState.mDrawable.setBounds(0, 0, w, mDrawRect.height());//宽度逐渐变窄动画中: 宽度w不断减少，高度值保持以前的值不变
            return;
        }

        if (w > 0 && h > 0) {
            int bitmapW = mState.mDrawable.getIntrinsicWidth();
            int bitmapH = mState.mDrawable.getIntrinsicHeight();
            float ration = bitmapW * 1F / w;
            int targetW = w;
            int targetH = Math.max(h, (int) (bitmapH / ration + 0.5F));
            mDrawRect.set(0, 0, targetW, targetH);
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
    //--- overrides from Drawable end ---

    @Override
    public void start() {
        if (!isRunning()) {
            int yDistance = getBounds().height() - mDrawRect.height();
            Log.d(TAG, "start: scroll yDistance=" + yDistance);
            if (yDistance < 0) {
                mAnimRunning = true;
                mScroller.startScroll(0, 0, 0, yDistance, calculateDuration(yDistance));
                scheduleSelf(runnable, 0L);
            }
        }
    }

    @Override
    public void stop() {
        if (isRunning()) {
            Log.d(TAG, "stop");
            mAnimRunning = false;
            mScroller.abortAnimation();
        }
    }

    @Override
    public boolean isRunning() {
        return mAnimRunning;
    }

    private int calculateDuration(int distance) {
        int speed = mSpeed > 0 ? mSpeed : DEFAULT_SPEED;
        distance = Math.abs(distance);
        return (int)(distance * 1000L / speed);
    }

    final static class ScrollState extends ConstantState {

        Drawable mDrawable;

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
                mCheckedConstantState = mCanConstantState = true;
            }
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

