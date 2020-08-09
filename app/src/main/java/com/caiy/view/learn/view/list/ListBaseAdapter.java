package com.caiy.view.learn.view.list;

/**
 * created by caiyong at 2020/8/8
 */
public abstract class ListBaseAdapter implements Adapter{

    private final DataSetObservable mDatasetObservable = new DataSetObservable();

    @Override
    public void registerObserver(DataSetObserver observer) {
        mDatasetObservable.registerObserver(observer);
    }

    @Override
    public void unregisterObserver(DataSetObserver observer) {
        mDatasetObservable.unregisterObserver(observer);
    }

    public void notifyDataSetChanged() {
        mDatasetObservable.notifyChanged();
    }
}
