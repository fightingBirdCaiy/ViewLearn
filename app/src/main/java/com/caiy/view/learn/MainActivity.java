package com.caiy.view.learn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        test();
    }

    private void test() {
        routeToScrollActivity();
    }

    private void routeToScrollActivity() {
        startActivity(new Intent(MainActivity.this, ScrollActivity.class));
    }
}
