package com.caiy.view.learn;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.caiy.view.learn.view.drawable.ProgressDrawable;

/**
 * created by caiyong at 2020/6/14
 */
public class ScrollActivity extends Activity {

    private static final String TAG = "Learn/ScrollActivity";

    private ImageView mPosterImageView;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll_drawable);
        initView();
    }

    private void initView() {
        mPosterImageView = (ImageView)findViewById(R.id.iv_poster);
        Drawable drawable = getResources().getDrawable(R.drawable.taohuayuan_440_608);
        Log.i(TAG, "drawable's width=" + drawable.getIntrinsicWidth() + " drawable's height=" + drawable.getIntrinsicHeight());
        mPosterImageView.setImageDrawable(drawable);
    }
}
