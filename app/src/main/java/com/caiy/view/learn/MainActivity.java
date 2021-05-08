package com.caiy.view.learn;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;

import com.caiy.view.learn.recycler.RecyclerMainActivity;

public class MainActivity extends Activity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        test();
    }

    private void test() {
//        routeToScrollActivity();
//        testSparseArray();
//        routeToFocusActivity();
//        routeToListActivity();
//        routeToDrawableActivity();
//        routeToScrollDrawableActivity();
//        routeToFlashActivity();
        routeToRecyclerMainActivity();
    }

    private void routeToScrollActivity() {
        startActivity(new Intent(MainActivity.this, ScrollDrawableActivity.class));
    }

    private void testSparseArray() {
        new Thread() {
            @Override
            public void run() {
                SparseArray<String> sparseArray = new SparseArray<>(16);
                sparseArray.put(400, "400str");
                sparseArray.put(200, "200str");
                sparseArray.put(600, "600str");
                sparseArray.put(100, "100str");
                for(int i=0; i<sparseArray.size(); i++) {
                    Log.d(TAG, "testSparseArray: " + i + " " + sparseArray.get(sparseArray.keyAt(i)));
                }
                Log.d(TAG, "testSparseArray: sparseArray="+ sparseArray);
//                sparseArray.delete(400);
//                Log.d(TAG, "testSparseArray: after delete 400: sparseArray="+ sparseArray);
//                sparseArray.delete(200);
//                Log.d(TAG, "testSparseArray: after delete 200: sparseArray="+ sparseArray);
//                sparseArray.delete(600);
//                Log.d(TAG, "testSparseArray: after delete 600: sparseArray="+ sparseArray);
//                sparseArray.delete(100);
//                Log.d(TAG, "testSparseArray: after delete 100: sparseArray="+ sparseArray);
//                ;
//                Log.d(TAG, "testSparseArray: sparseArray.valueAt(0)="+ sparseArray.valueAt(0));

            }
        }.start();

    }

    private void routeToFocusActivity() {
        startActivity(new Intent(MainActivity.this, FocusActivity.class));
    }

    private void routeToListActivity() {
        startActivity(new Intent(MainActivity.this, ListActivity.class));
    }

    private void routeToDrawableActivity() {
        startActivity(new Intent(MainActivity.this, DrawableActivity.class));
    }

    private void routeToScrollDrawableActivity() {
        startActivity(new Intent(MainActivity.this, ScrollDrawableActivity.class));
    }

    private void routeToFlashActivity() {
        startActivity(new Intent(MainActivity.this, SplashActivity.class));
    }

    private void routeToRecyclerMainActivity() {
        startActivity(new Intent(MainActivity.this, RecyclerMainActivity.class));
    }
}
