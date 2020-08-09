package com.caiy.view.learn.view.list;

/**
 * created by caiyong at 2020/8/8
 */
public class DataSetObservable extends Observable<DataSetObserver> {

    public void notifyChanged() {
        synchronized(mObservers) {
            for (int i = mObservers.size() - 1; i >= 0; i--) {
                mObservers.get(i).onChanged();
            }
        }
    }
}

