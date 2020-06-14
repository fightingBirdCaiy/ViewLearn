package com.caiy.view.learn.view.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.caiy.view.learn.R;

/**
 * created by caiyong at 2020/6/14
 */
public class MyScrollView extends LinearLayout {

    public MyScrollView(Context context) {
        this(context, null);
    }

    public MyScrollView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyScrollView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

}
