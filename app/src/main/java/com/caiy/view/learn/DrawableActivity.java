package com.caiy.view.learn;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.caiy.view.learn.R;
import com.caiy.view.learn.view.custom.MyFocusView;
import com.caiy.view.learn.view.drawable.ProgressDrawable;

/**
 * created by caiyong at 2020/6/14
 */
public class DrawableActivity extends Activity {

    private static final String TAG = "Learn/FocusActivity";

    private ImageView mProgressImageView;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawable);
        initView();
    }

    private void initView() {
        mProgressImageView = (ImageView)findViewById(R.id.iv_progress);
        testProgressDrawable();
    }

    private void testProgressDrawable() {
        ProgressDrawable progressDrawable = createProgressDrawable();
        progressDrawable.setRoundCorner(true);
        progressDrawable.setProgress(30);
        mProgressImageView.setImageDrawable(progressDrawable);
    }

    private ProgressDrawable createProgressDrawable() {
        ProgressDrawable drawable = new ProgressDrawable();
        drawable.setProgressColor(getResources().getColor(R.color.task_item_progress_start),
                getResources().getColor(R.color.task_item_progress_end));
        drawable.setBackgroundColor(getResources().getColor(R.color.task_item_progress_bg));
        drawable.setMax(100);
        return drawable;
    }
}
