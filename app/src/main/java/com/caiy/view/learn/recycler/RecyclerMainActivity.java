package com.caiy.view.learn.recycler;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.caiy.view.learn.R;
import com.caiy.view.learn.view.custom.MyFocusView;

/**
 * created by caiyong at 2020/6/14
 */
public class RecyclerMainActivity extends Activity {

    private static final String TAG = "Learn/RecyclerMainActivity";

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_main);
        initView();
    }

    private void initView() {
    }
}
