package com.caiy.view.learn;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.caiy.view.learn.view.custom.MyFocusView;
import com.caiy.view.learn.view.custom.MyScrollView;

/**
 * created by caiyong at 2020/6/14
 */
public class FocusActivity extends Activity {

    private static final String TAG = "Learn/FocusActivity";

    private MyFocusView mFocusView;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_focus);
        initView();
    }

    private void initView() {
        mFocusView = (MyFocusView) findViewById(R.id.v_focus);
        Log.d(TAG, "before setClickable: mFocusView=" + mFocusView);
        mFocusView.setClickable(true);
        Log.d(TAG, "after setClickable: mFocusView=" + mFocusView);
        mFocusView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mFocusView.requestFocus();
            }
        }, 5000L);
    }
}
