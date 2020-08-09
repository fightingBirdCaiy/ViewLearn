package com.caiy.view.learn.view.list;

import android.view.View;
import android.view.ViewGroup;

/**
 * created by caiyong at 2020/8/8
 */
public interface Adapter {

    void registerObserver(DataSetObserver observer);

    void unregisterObserver(DataSetObserver observer);

    int getCount();

    View getView(int position, View converView, ViewGroup parent);
}
