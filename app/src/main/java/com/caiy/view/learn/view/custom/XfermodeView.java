package com.caiy.view.learn.view.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
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
        testMethod1(canvas);
    }

    private void testMethod1(Canvas canvas) {
        //设置背景色
        canvas.drawARGB(255, 139, 197, 186);

        int canvasWidth = 800;
        int r = canvasWidth / 3;
        //绘制黄色的圆形
        mPaint.setColor(0xFFFFCC44);
        canvas.drawCircle(r, r, r, mPaint);
        //绘制蓝色的矩形
        mPaint.setColor(0xFF66AAFF);
        canvas.drawRect(r, r, r * 2.7f, r * 2.7f, mPaint);
    }
}
