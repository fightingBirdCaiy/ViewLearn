package com.caiy.view.learn.view.custom;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * created by caiyong at 2020/7/2
 */
public class MyFocusView extends LinearLayout {

    private static final String TAG = "Learn/MyFocusView";

    public MyFocusView(Context context) {
        this(context, null);
    }

    public MyFocusView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyFocusView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        Log.i(TAG, "init");
        setFocusableInTouchMode(true);
    }

    @Override
    protected void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
        Log.i(TAG, "onFocusChanged: gainFocus=" + gainFocus);
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
        String hintStr = gainFocus ? "获取焦点" : "失去焦点";
        Toast.makeText(getContext(), hintStr, Toast.LENGTH_SHORT).show();
    }
}
