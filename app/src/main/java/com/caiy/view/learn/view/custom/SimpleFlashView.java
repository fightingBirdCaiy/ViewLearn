package com.caiy.view.learn.view.custom;

import android.animation.Keyframe;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.animation.LinearInterpolator;

import com.caiy.view.learn.R;

/**
 * created by caiyong at 2020/12/23
 * 扫光
 */
public class SimpleFlashView extends android.view.View {
    private static final long ANIMATOR_DURATION = 3000L;
    private static final int DEFAULT_RADIUS_DP = 9;
    private static final int DEFAULT_LIGHT_IMAGE_WIDTH = 0;
    private static final int DEFAULT_LIGHT_IMAGE_HEIGHT = 0;

    private String TAG = "SimpleFlashView@" + Integer.toHexString(hashCode());

    private Bitmap mLightBitmap;
    private Bitmap mRoundBitmap;

    private Paint mPaint;
    private Paint mBgPaint;

    private int mLeft;
    private int mTop;
    private int mStart;
    private int mEnd;

    private ValueAnimator mAnimator;

    private PorterDuffXfermode mPorterDuffXfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_IN);

    private int mLightImageWidth;
    private int mLightImageHeight;

    private int mCornerRadius;

    private Rect mLightImageRect = new Rect();

    public SimpleFlashView(Context context) {
        this(context,null);
    }

    public SimpleFlashView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SimpleFlashView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        int lightImageResource = 0;
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SimpleFlashView);
            lightImageResource = typedArray.getResourceId(R.styleable.SimpleFlashView_light_image, 0);
            mLightImageWidth = typedArray.getDimensionPixelOffset(R.styleable.SimpleFlashView_light_image_width, DEFAULT_LIGHT_IMAGE_WIDTH);
            mLightImageHeight = typedArray.getDimensionPixelOffset(R.styleable.SimpleFlashView_light_image_height, DEFAULT_LIGHT_IMAGE_HEIGHT);
            mCornerRadius = typedArray.getDimensionPixelOffset(R.styleable.SimpleFlashView_corner_radius, DEFAULT_RADIUS_DP);
            typedArray.recycle();
        }
        init(lightImageResource);
    }

    private void init(int lightImageResource) {
        //初始化光图片
        mLightBitmap = BitmapFactory.decodeResource(getResources(), lightImageResource);
        if (mLightBitmap != null) {
            mLightImageWidth = mLightImageWidth <= 0 ? mLightBitmap.getWidth() : mLightImageWidth;
            mLightImageHeight = mLightImageHeight <= 0 ? mLightBitmap.getHeight() : mLightImageHeight;
        }
        Log.d(TAG, "light bitmap: w=" + mLightBitmap.getWidth() + " h=" + mLightBitmap.getHeight()
                + " mLightImageWidth=" + mLightImageWidth + " mLightImageHeight=" + mLightImageHeight);

        //初始化画笔 设置抗锯齿和防抖动
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setDither(true);
        mPaint.setFilterBitmap(true);//加快显示速度，本设置项依赖于dither和xfermode的设置
        
        mBgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBgPaint.setDither(true);
        mBgPaint.setFilterBitmap(true);
        mBgPaint.setStyle(Paint.Style.FILL);
        mBgPaint.setColor(Color.BLUE);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (mLightBitmap != null) {
            mLeft = mStart = -mLightImageWidth;
            mTop = -(mLightImageHeight - h) / 2;
        }
        mEnd = w;
        createRoundBitmap();
    }

    private void createRoundBitmap() {
        mRoundBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(mRoundBitmap);
        //绘制圆角矩形
        canvas.drawRoundRect(new RectF(0, 0, getWidth(), getHeight()), mCornerRadius, mCornerRadius, mBgPaint);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mLightBitmap != null) {
            int sc = canvas.saveLayer(0, 0, getWidth(), getHeight(), mPaint, Canvas.ALL_SAVE_FLAG);
            mLightImageRect.set(mLeft, mTop, mLeft + mLightImageWidth, mTop + mLightImageHeight);
            canvas.drawBitmap(mLightBitmap, null, mLightImageRect, mPaint);
            mPaint.setXfermode(mPorterDuffXfermode);
            canvas.drawBitmap(mRoundBitmap, 0, 0, mPaint);
            mPaint.setXfermode(null);
            canvas.restoreToCount(sc);
        }
    }

    public void startAnimation() {
        post(new Runnable() {
            @Override
            public void run() {
                if (mAnimator == null) {
                    initAnimator();
                } else if (mAnimator.isRunning()) {
                    mAnimator.cancel();
                }
                mAnimator.start();
            }
        });
    }

    public void stopAnimation() {
        if (mAnimator != null && mAnimator.isRunning()) {
            mAnimator.cancel();
        }
        mLeft = mStart;
        postInvalidate();
    }

    private void initAnimator() {
        PropertyValuesHolder pvhLeft = PropertyValuesHolder.ofKeyframe(android.view.View.TRANSLATION_Y,
                Keyframe.ofFloat(0f, mStart),
                Keyframe.ofFloat(1f, mEnd)
        );
        //初始化动画
        mAnimator = ValueAnimator.ofPropertyValuesHolder(pvhLeft);
        mAnimator.setInterpolator(new LinearInterpolator());
        mAnimator.setDuration(ANIMATOR_DURATION);
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mLeft = ((Float) animation.getAnimatedValue()).intValue();
                postInvalidate();
            }
        });
    }
}
