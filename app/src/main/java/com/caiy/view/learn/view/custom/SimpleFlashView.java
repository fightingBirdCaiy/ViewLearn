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

import javax.security.auth.login.LoginException;

/**
 * created by caiyong at 2020/12/23
 */
public class SimpleFlashView extends android.view.View {
    private static final long ANIMATOR_DURATION = 3000L;
    private static final int DEFAULT_RADIUS_DP = 9;
    private static final int DEFAULT_LIGHT_IMAGE_WIDTH = 10;

    private String TAG = "SimpleFlashView@" + Integer.toHexString(hashCode());

    private Bitmap mLightBitmap;

    private Bitmap mRounderBitmap;

    private Paint mFlashPaint;

    private Paint mPaint;

    private int mLeft;

    private int mTop;

    private int mStart;

    private int mEnd;

    private ValueAnimator mAnimator;

    private PorterDuffXfermode mPorterDuffXfermode;

    private int mLightImage;

    private int mLightImageWidth;

    private int mRadius;

    private Rect mSrcRect = new Rect();
    private Rect mDstRect = new Rect();

    public SimpleFlashView(Context context) {
        this(context,null);
    }

    public SimpleFlashView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SimpleFlashView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SimpleFlashView);
            mLightImage = typedArray.getResourceId(R.styleable.SimpleFlashView_light_image, 0);
            mLightImageWidth = typedArray.getDimensionPixelOffset(R.styleable.SimpleFlashView_light_image_width, DEFAULT_LIGHT_IMAGE_WIDTH);
            mRadius = typedArray.getDimensionPixelOffset(R.styleable.SimpleFlashView_corner_radius, DEFAULT_RADIUS_DP);
            typedArray.recycle();
        }
        init();
    }

    private void init() {
        //初始化光图片
        mLightBitmap = BitmapFactory.decodeResource(getResources(), mLightImage);
        if (mLightBitmap != null) {
            mLightImageWidth = Math.min(mLightImageWidth, mLightBitmap.getWidth());
        }
        Log.d(TAG, "light bitmap: w=" + mLightBitmap.getWidth() + " h=" + mLightBitmap.getHeight() +" mLightImageWidth=" + mLightImageWidth);

        //初始化画笔 设置抗锯齿和防抖动
        mFlashPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mFlashPaint.setDither(true);
        mFlashPaint.setFilterBitmap(true);//加快显示速度，本设置项依赖于dither和xfermode的设置
        
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setDither(true);
        mPaint.setFilterBitmap(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.BLUE);

        mPorterDuffXfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_IN);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.i(TAG, "onSizeChanged: width=" + w + " height=" + h);
        if (mLightBitmap != null) {
            mLeft = mStart = -mLightImageWidth;
//            mTop = -mBitmap.getHeight() / 2;
            mTop = -(mLightBitmap.getHeight() - h) / 2;
        }
        createRounderBitmap();
        mEnd = w;
    }

    private void createRounderBitmap() {
        mRounderBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(mRounderBitmap);
        //绘制圆角矩形
        canvas.drawRoundRect(new RectF(0, 0, getWidth(), getHeight()), mRadius, mRadius, mPaint);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mLightBitmap != null) {
            int sc = canvas.saveLayer(0, 0, getWidth(), getHeight(), mFlashPaint, Canvas.ALL_SAVE_FLAG);
            int inset = (mLightBitmap.getWidth() - mLightImageWidth)/2;
            Log.i(TAG, "onDraw: width=" + canvas.getWidth() + " height=" + canvas.getHeight() + " inset=" + inset);

            mSrcRect.set(0 + inset, 0, mLightBitmap.getWidth() - inset, mLightBitmap.getHeight());
            mDstRect.set(mLeft, mTop, mLeft + mLightImageWidth, mTop + mLightBitmap.getHeight());
            canvas.drawBitmap(mLightBitmap, mSrcRect, mDstRect, mFlashPaint);
            mFlashPaint.setXfermode(mPorterDuffXfermode);
            canvas.drawBitmap(mRounderBitmap, 0, 0, mFlashPaint);
            mFlashPaint.setXfermode(null);
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
        PropertyValuesHolder pvhLeft = PropertyValuesHolder.ofKeyframe(android.view.View.TRANSLATION_X,
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
