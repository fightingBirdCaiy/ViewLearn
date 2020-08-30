package com.caiy.view.learn.view.drawable;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;

public class ProgressDrawable extends Drawable {
    private static final int COLOR_NORMAL = 0;
    private static final int COLOR_GRADIENT = 1;

    private final Paint mProgressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint mBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private RectF mBackgroundRoundRectF = new RectF();
    private RectF mProgressRoundRectF = new RectF();

    private int mMax = 100;
    private int mProgress;
    private LinearGradient mLinearGradient;
    private int[] mProgressColors;
    private int mColorType;
    private boolean mIsRoundCorner = false;

    public void setMax(int max) {
        this.mMax = max;
    }

    public void setProgressMax() {
        setProgress(mMax);
    }

    public void setProgress(int progress) {
        if (mProgress != progress) {
            this.mProgress = progress;
            this.mLinearGradient = null;
            invalidateSelf();
        }
    }

    /**
     * 设置一个色值则为单色；多个色值为渐变
     */
    public void setProgressColor(int... colors) {
        if (colors == null || colors.length == 0) {
            return;
        }
        if (colors.length == 1) {
            mProgressPaint.setColor(colors[0]);
            mProgressColors = null;
            mColorType = COLOR_NORMAL;
        } else {
            mProgressColors = colors;
            mColorType = COLOR_GRADIENT;
        }
        mLinearGradient = null;
    }

    public void setBackgroundColor(int color) {
        mBackgroundPaint.setColor(color);
    }

    public void setRoundCorner(boolean isRoundCorner) {
        mIsRoundCorner = isRoundCorner;
    }

    @Override
    public void draw( Canvas canvas) {
        if (mIsRoundCorner) {
            drawRound(canvas);
        } else {
            drawNormal(canvas);
        }
    }

    private void drawRound(Canvas canvas) {
        Rect bound = getBounds();
        int corner = (bound.bottom - bound.top)/2;
        if (mProgress <= 0) {
            mBackgroundRoundRectF.set(bound);
            canvas.drawRoundRect(mBackgroundRoundRectF, corner, corner, mBackgroundPaint);
        } else if (mProgress >= mMax) {
            mProgressRoundRectF.set(bound);
            ensureProgressPaint(bound.left, bound.top, bound.right, bound.bottom);
            canvas.drawRoundRect(mProgressRoundRectF, corner, corner, mProgressPaint);
        } else {
            int left = bound.left;
            int right = bound.right;
            int progress = left + (right - left) * mProgress / mMax;
            mProgressRoundRectF.set(left, bound.top, progress, bound.bottom);
            ensureProgressPaint(left, bound.top, progress, bound.bottom);
            canvas.drawRoundRect(mProgressRoundRectF, corner, corner, mProgressPaint);

            mBackgroundRoundRectF.set(bound);
            canvas.drawRoundRect(mBackgroundRoundRectF, corner, corner, mBackgroundPaint);
        }
    }

    private void drawNormal(Canvas canvas) {
        Rect bound = getBounds();
        if (mProgress <= 0) {
            canvas.drawRect(bound, mBackgroundPaint);
        } else if (mProgress >= mMax) {
            ensureProgressPaint(bound.left, bound.top, bound.right, bound.bottom);
            canvas.drawRect(bound, mProgressPaint);
        } else {
            int left = bound.left;
            int right = bound.right;
            int progress = left + (right - left) * mProgress / mMax;

            ensureProgressPaint(left, bound.top, progress, bound.bottom);
            canvas.drawRect(left, bound.top, progress, bound.bottom, mProgressPaint);

            canvas.drawRect(progress, bound.top, right, bound.bottom, mBackgroundPaint);
        }
    }

    private void ensureProgressPaint(int left, int top, int right, int bottom) {
        if (mColorType == COLOR_GRADIENT) {
            // 如果进度条是渐变色，则需要进行初始化
            if (mLinearGradient == null && mProgressColors != null) {
                mLinearGradient = new LinearGradient(left, top, right, bottom,
                        mProgressColors, null, Shader.TileMode.CLAMP);
                mProgressPaint.setShader(mLinearGradient);
            }
        }
    }

    @Override
    public void setAlpha(int alpha) {}

    @Override
    public void setColorFilter( ColorFilter colorFilter) {}

    @Override
    public int getOpacity() {
        return PixelFormat.OPAQUE;
    }
}
