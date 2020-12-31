package com.caiy.view.learn;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.caiy.view.learn.view.CImageView;
import com.caiy.view.learn.view.custom.ScrollDrawable;
import com.caiy.view.learn.view.drawable.ProgressDrawable;
import com.caiy.view.learn.view.drawable.round.RoundedBitmapDrawable;
import com.caiy.view.learn.view.drawable.round.RoundedBitmapDrawableFactory;

/**
 * created by caiyong at 2020/6/14
 */
public class ScrollDrawableActivity extends Activity {

    private static final String TAG = "ScrollDrawableActivity";

    private RelativeLayout mRootView;
    private CImageView mPosterImageView;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll_drawable);
        initView();

        for (int i = 0; i < 10; i++) {
            Log.i(TAG, (int) (Math.random() * 2) + "");
        }
    }

    private void initView() {
        mRootView = (RelativeLayout) findViewById(R.id.rl_root);
        mRootView.setClipChildren(false);

        mPosterImageView = new CImageView(this);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(300 + 10 + 10, 150);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        mPosterImageView.setPadding(10, 0, 10, 0);
        mPosterImageView.setLayoutParams(layoutParams);
        mPosterImageView.setScaleType(ImageView.ScaleType.FIT_XY);
        mPosterImageView.setFocusable(true);
        mPosterImageView.setBackgroundColor(getResources().getColor(R.color.colorBlue));

        mRootView.addView(mPosterImageView);

        Drawable bitmapDrawable = getResources().getDrawable(R.drawable.taohuayuan_440_608);
        RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), ((BitmapDrawable)bitmapDrawable).getBitmap());
        roundedBitmapDrawable.setCornerRadius(50F, false, true, true , false);
        final ScrollDrawable scrollDrawable = new ScrollDrawable(roundedBitmapDrawable, mPosterImageView.getContext());
        mPosterImageView.setImageDrawable(scrollDrawable);
        Log.i(TAG, roundedBitmapDrawable + " drawable's width=" + roundedBitmapDrawable.getIntrinsicWidth() + " drawable's height=" + roundedBitmapDrawable.getIntrinsicHeight());

        mPosterImageView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (v == mPosterImageView) {
                    if (hasFocus) {
                        mPosterImageView.setScaleX(2F);
                        mPosterImageView.setScaleY(2F);
                        triggerStartAnim(mPosterImageView);
                    } else {
                        mPosterImageView.setScaleX(1F);
                        mPosterImageView.setScaleY(1F);
                        triggerStopAnim(mPosterImageView);
                    }
                }
            }
        });
    }

    public void triggerStartAnim(View view) {
        ((ScrollDrawable)mPosterImageView.getDrawable()).start();
    }

    public void triggerStopAnim(View view) {
        ((ScrollDrawable)mPosterImageView.getDrawable()).stop();
    }
}
