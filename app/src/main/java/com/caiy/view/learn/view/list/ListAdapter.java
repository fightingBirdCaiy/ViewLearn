package com.caiy.view.learn.view.list;

import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * created by caiyong at 2020/8/8
 */
public abstract class ListAdapter<T> extends ListBaseAdapter{

    private List<T> mDatas = new ArrayList<>();
    private final Object mLock = new Object();

    @Override
    public int getCount() {
        return mDatas.size();
    }

    public T getItem(int position) {
        if (position >= 0 && position < mDatas.size()) {
            return mDatas.get(position);
        }
        return null;
    }

    public void addAll(List<T> datas) {
        synchronized (mLock) {
            if (datas != null) {
                mDatas.addAll(datas);
            }
        }
    }

    public void setData(List<T> datas) {
        synchronized (mLock) {
            mDatas.clear();
            if (datas != null) {
                mDatas.addAll(datas);
            }
        }
    }
}
