package com.caiy.view.learn;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.caiy.view.learn.view.CImageView;
import com.caiy.view.learn.view.custom.ScrollDrawable;
import com.caiy.view.learn.view.custom.SimpleFlashView;
import com.caiy.view.learn.view.drawable.round.RoundedBitmapDrawable;
import com.caiy.view.learn.view.drawable.round.RoundedBitmapDrawableFactory;

/**
 * created by caiyong at 2020/6/14
 */
public class SplashActivity extends Activity {

    private static final String TAG = "ScrollDrawableActivity";

    private RelativeLayout mRootView;
    private SimpleFlashView mFlashView;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initView();

        for (int i = 0; i < 10; i++) {
            Log.i(TAG, (int) (Math.random() * 2) + "");
        }
    }

    private void initView() {
        mRootView = (RelativeLayout) findViewById(R.id.rl_root);
        mRootView.setClipChildren(false);

        mFlashView = (SimpleFlashView) findViewById(R.id.flash_v);
        mFlashView.setFocusable(true);
        mFlashView.setBackgroundColor(Color.parseColor("#11FF0000"));

        mFlashView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (v == mFlashView) {
                    if (hasFocus) {
                        mFlashView.setScaleX(2F);
                        mFlashView.setScaleY(2F);
                        triggerStartAnim(mFlashView);
                    } else {
                        mFlashView.setScaleX(1F);
                        mFlashView.setScaleY(1F);
                        triggerStopAnim(mFlashView);
                    }
                }
            }
        });
    }

    public void triggerStartAnim(View view) {
        mFlashView.startAnimation();
    }

    public void triggerStopAnim(View view) {
        mFlashView.stopAnimation();
    }
}
