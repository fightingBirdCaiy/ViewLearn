package com.caiy.view.learn.view.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * created by caiyong at 2020/5/26
 *
 *  https://blog.csdn.net/iispring/article/details/50472485
 */
public class XfermodeView extends View {

    private Paint mPaint = new Paint();
    private RectF mRectF = new RectF();

    public XfermodeView(Context context) {
        this(context, null);
    }

    public XfermodeView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public XfermodeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        testMethod1(canvas);
        testMethod2(canvas);
    }

    private void testMethod1(Canvas canvas) {
        //设置背景色
        canvas.drawARGB(255, 139, 197, 186);

        int canvasWidth = 800;
        int r = canvasWidth / 3;
        //绘制黄色的圆形
        mPaint.setColor(0x7FFFCC44);
        canvas.drawCircle(r, r, r, mPaint);
        //绘制蓝色的矩形
        mPaint.setColor(0x7F66AAFF);
        canvas.drawRect(r, r, r * 2.7f, r * 2.7f, mPaint);
    }

    private void testMethod2(Canvas canvas) {
        //设置背景色
//        canvas.drawARGB(255, 139, 197, 186);

        int canvasWidth = 800;
        int r = canvasWidth / 3;
        //绘制黄色的圆形
        mPaint.setColor(0x7FFFCC44);
        canvas.drawCircle(r, r, r, mPaint);

        //使用CLEAR作为PorterDuffXfermode绘制蓝色的矩形
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
        //绘制蓝色的矩形
        mPaint.setColor(0x7F66AAFF);

        mRectF.set(r, r, r * 2.7f, r * 2.7f);
        canvas.drawRoundRect(mRectF, 50, 50, mPaint);
//        canvas.drawRect(mRectF, mPaint);

        //最后将画笔去除Xfermode
        mPaint.setXfermode(null);
    }
}
