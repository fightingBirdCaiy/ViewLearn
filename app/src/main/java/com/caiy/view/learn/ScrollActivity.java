package com.caiy.view.learn;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.caiy.view.learn.view.custom.MyScrollView;

/**
 * created by caiyong at 2020/6/14
 */
public class ScrollActivity extends Activity {

    private TextView mLogTextView;
    private MyScrollView mScrollView;

    private Rect mTempRect = new Rect();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll);
        initView();
        test();
    }

    private void initView() {
        mLogTextView = findViewById(R.id.tv_log);
        mScrollView = findViewById(R.id.sv_1);
    }

    private void test() {
        mScrollView.setBackgroundColor(getResources().getColor(R.color.colorBlue));

        mScrollView.setScrollX(0);
        mScrollView.setScrollY(-50);
        log();
    }

    private void log() {
        getWindow().getDecorView().postDelayed(new Runnable() {
            @Override
            public void run() {
                String str = getLogInfo();
                mLogTextView.setText(str);
            }
        }, 1000);
    }

    private String getLogInfo() {
        StringBuilder builder = new StringBuilder();
        mScrollView.getDrawingRect(mTempRect);
        builder.append(" mScrollView: ").append(getViewLogInfo(mScrollView))
                .append("\n")
                .append(" childView0: ").append(getViewLogInfo(mScrollView.getChildAt(0)))
                .toString();
        return builder.toString();
    }

    private String getViewLogInfo(View view) {
        StringBuilder builder = new StringBuilder();
        view.getDrawingRect(mTempRect);
        builder.append(" left: ").append(view.getLeft())
                .append(" top: ").append(view.getTop())
                .append(" right: ").append(view.getRight())
                .append(" bottom: ").append(view.getBottom())
                .append(" scrollX: ").append(view.getScrollX())
                .append(" scrollY: ").append(view.getScrollY())
                .append(" drawingRect: ").append(mTempRect)
                .toString();
        return builder.toString();
    }
}
