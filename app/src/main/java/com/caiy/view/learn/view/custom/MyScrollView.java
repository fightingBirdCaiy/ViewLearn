package com.caiy.view.learn.view.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;


import com.caiy.view.learn.R;

/**
 * created by caiyong at 2020/6/14
 */
public class MyScrollView extends LinearLayout {

    private static final String TAG = "MyScrollView";

    public MyScrollView(Context context) {
        this(context, null);
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void draw(Canvas canvas) {
        Log.d(TAG, "draw: canvas.width="+ canvas.getWidth() + " canvas.height=" + canvas.getHeight());
        super.draw(canvas);
    }
}
